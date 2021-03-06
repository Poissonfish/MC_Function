#INSTALLATION
library(annotate) 
library(Biostrings) #DNAStringSet Object
library(rBLAST)
library(rMSA)
library(devtools)
library(magrittr)
library(seqinr)
library(ape) #read.dna, write.fasta
library(data.table)
library(lubridate)
library(RCurl)
library(magrittr)
library(R.utils)
library(downloader)
library(ggplot2)
library(gridExtra)
library(plyr)
library(taxize)
library(rentrez)

MC_Function=function(primer_f, primer_r, name_primer_f, name_primer_r, source, username, password, desdir,folder,local_path, local, nt_search){
  packageStartupMessage("Creating Folders...", appendLF = FALSE)
  setwd(desdir)
  if (!file.exists(file.path(desdir,'seq',folder,'raw'))){
    dir.create(file.path(desdir,'seq',folder,'raw'),recursive = TRUE)
  }
  if (!file.exists(file.path(desdir,'seq',folder,'fasta'))){
    dir.create(file.path(desdir,'seq',folder,'fasta'),recursive = TRUE)
  }
  if (!file.exists(file.path(desdir,'seq',folder,'fasta.aln'))){
    dir.create(file.path(desdir,'seq',folder,'fasta.aln'),recursive = TRUE)
  }
  if(local){system(paste('cp -r',file.path(local_path,'.'),file.path(desdir,'seq',folder,'raw')))}
  packageStartupMessage(" Done!")

  packageStartupMessage("Primer Analyzing...", appendLF = FALSE)
  primer_f2=primer_f%>%DNAString()%>%reverseComplement()%>%as.character()
  primer_r2=primer_r%>%DNAString()%>%reverseComplement()%>%as.character()
  primerall=c(primer_f,primer_f2,primer_r,primer_r2)
  
  pro=c('N','R','Y','K','M','W')
  sp_N=c('A','T','C','G')
  sp_R=c('A','G')
  sp_Y=c('T','C')
  sp_K=c('T','G')
  sp_M=c('A','C')
  sp_W=c('A','T')
  sp_list=list(sp_N,sp_R,sp_Y,sp_K,sp_M,sp_W)
  names(sp_list)=pro
  
  primer=c()
  for (pri in 1:4){
    listP=list()
    for (i in 1:6){
      listP[[i]]=strsplit(primerall[pri],'')%>%unlist() %>%grep(pro[i],.)
    }
    seqn=c()
    for (i in 1:6){
      seqn[listP[[i]]]=pro[i]
    }
    seqn=seqn%>%na.omit()%>%as.character()
    grid=  expand.grid(if(!is.na(seqn[1])){sp_list[seqn[1]]%>%unlist()}else{NA},
                       if(!is.na(seqn[2])){sp_list[seqn[2]]%>%unlist()}else{NA},
                       if(!is.na(seqn[3])){sp_list[seqn[3]]%>%unlist()}else{NA},
                       if(!is.na(seqn[4])){sp_list[seqn[4]]%>%unlist()}else{NA},
                       if(!is.na(seqn[5])){sp_list[seqn[5]]%>%unlist()}else{NA},
                       if(!is.na(seqn[6])){sp_list[seqn[6]]%>%unlist()}else{NA},
                       if(!is.na(seqn[7])){sp_list[seqn[7]]%>%unlist()}else{NA})
    primer=c(primer, primerall[pri]%>% gsub('[NRYKMW]','%s',.) %>% 
               sprintf(.,grid[,1],grid[,2],grid[,3],grid[,4],grid[,5],grid[,6],grid[,7]))
  }
  primerraw=primer
  primern=c()
  for (i in 1:length(primer)){
    primern=c(primern,primer[i] %>% substr(.,1,nchar(.)-5),primer[i] %>% substr(.,6,nchar(.))) 
  }
  primer=primern
  packageStartupMessage(" Done!")
  
  setwd(file.path(desdir,'seq',folder,'raw'))
  if(!local){
    packageStartupMessage("File Downloading...", appendLF = FALSE)
    filenames= getURL(source,userpwd=paste0(username,':',password),
                      verbose=TRUE,ftp.use.epsv=TRUE, dirlistonly = TRUE) %>%
               strsplit("[\\\\]|[^[:print:]]",fixed = FALSE) %>%
               unlist() %>% (function(x){x[grep('seq',x)]})
    filepath= sprintf(paste0('ftp://',
                            paste0(username,':',password),'@',
                            (source%>%gsub('ftp://','',.)),'%s'),filenames)
    
    
    
    for (i in 1:(length(filenames))){
      download.file(filepath[i],
                    file.path(getwd(),filenames[i]),method="libcurl")
    }
    packageStartupMessage(" Done!") 
  }
  
  packageStartupMessage("Renaming...", appendLF = FALSE)  
  names=list.files()
  new_names=names
  new_names[grep(name_primer_r,names)]= names[grep(name_primer_r,names)] %>% 
                                        gsub("^[0-9][0-9]\\.","",.) %>%
                                        gsub(paste0('(',name_primer_r,')'),"_R",.)
  new_names[grep(name_primer_f,names)]= names[grep(name_primer_f,names)] %>% 
                                        gsub("^[0-9][0-9]\\.","",.) %>%
                                        gsub(paste0('(',name_primer_f,')'),"_F",.)
   
  new_names_split= new_names %>% strsplit('_') %>% unlist() 
  SN= new_names_split[seq(1,length(new_names_split),2)]  
  FR= new_names_split[seq(2,length(new_names_split),2)]  
  nchr= SN %>%  nchar() %>% (function(x){c(min(x),max(x))})
  
  if (nchr[1]!=nchr[2]){
    s_index= SN%>%nchar() == nchr[1]
    l_index= SN%>%nchar() == nchr[2]
    
    new_names[l_index]=paste0(SN[l_index] %>% substr(1,2),'-',
                              SN[l_index] %>% substr(3,nchr[2]-2),'-',
                              SN[l_index] %>% substr(nchr[2]-1,nchr[2]),'_', FR[l_index])
    new_names[s_index]=paste0(SN[s_index] %>% substr(1,2),'-',
                              SN[s_index] %>% substr(3,nchr[1]-1),'-',
                              SN[s_index] %>% substr(nchr[1],nchr[1]),'_', FR[s_index])
    new_names=new_names%>%gsub('.seq','.fasta',.)
    
    for (j in 1:length(names)){
      text=readLines(names[j])
      for (i in length(text):1){
        text[i+1]=text[i]
      }
      text[1]=paste0(">",new_names[j])
      writeLines(text,file.path('../fasta',new_names[j]%>%gsub('seq','fasta',.)))
    }
  }else{
    new_names=paste0(SN %>% substr(1,2),'-',
                     SN %>% substr(3,nchr[2]-1),'-',
                     SN %>% substr(nchr[2],nchr[2]),'_',FR)
    new_names=new_names%>%gsub('.seq','.fasta',.)
    for (j in 1:length(names)){
      text=readLines(names[j])
      for (i in length(text):1){
        text[i+1]=text[i]
      }
      text[1]=paste0(">",new_names[j])
      writeLines(text,file.path('../fasta',new_names[j]%>%gsub('seq','fasta',.)))
    }
  }
  packageStartupMessage(" Done!")
  
  packageStartupMessage("Vector Screening...", appendLF = FALSE)
  setwd('../fasta')
  db_vec= blast(db="../../../db/UniVec") 
  seq=readDNAStringSet(new_names)
  data_seq=seq@ranges %>% data.frame()
  index_r=new_names%>%grep('R',.)
  seq[index_r]=seq[index_r]%>% reverseComplement()
  
  l.vec=array(dim=c(2,2,length(new_names)),
              dimnames = list(c(1:2),c('Start.pt','End.pt'),new_names))
  vec.mis=c()
  for (k in 1: length(new_names)){
    num=predict(db_vec,seq[k])%>% (function(x){x[x$Mismatches<10,]})
    if(nrow(num)!=0){
      qs= num$Q.start
      qe= num$Q.end
      d3= data.frame(qs=qs, qe=qe)
      d3=d3[order(d3$qs),]
      
      vec=matrix(ncol=2,nrow=2)
      vec[1,1]=d3[1,1]
      vec[1,2]=d3[1,2]
      for (i in 1:(nrow(d3)-1)){
        if(d3[i+1,1]<=vec[1,2]){
          if(d3[i+1,2]>vec[1,2]){
            vec[1,2]=d3[i+1,2]
          }}
        else if(vec[2,1]%>%is.na()){
          vec[2,1]=d3[i+1,1]
          vec[2,2]=d3[i+1,2]
        }else if(d3[i+1,2]>vec[2,2]){
          vec[2,2]=d3[i+1,2]
        }
      }
      l.vec[,,k]=vec
    }else{
      vec.mis=c(vec.mis, new_names[k])
    }
  }
  packageStartupMessage(" Done!")
  
  packageStartupMessage("Candidate Sequences Evaluating...", appendLF = FALSE)
  l.seq=array(dim=c(3,2,length(new_names)),
              dimnames = list(c(1:3),c('Start.pt','End.pt'),new_names))
  for (k in 1: length(new_names)){
    if(l.vec[2,1,k]%>%is.na()){
      l.seq[1,,k]=c(1,l.vec[1,1,k]-1)
      l.seq[2,,k]=c(l.vec[1,2,k]+1,data_seq$width[k])
    }else{
      if(l.vec[1,1,k]==1){
        l.seq[1,,k]=c(1,1)
      }else{
        l.seq[1,,k]=c(1,l.vec[1,1,k]-1)
      }
      l.seq[2,,k]=c(l.vec[1,2,k]+1,l.vec[2,1,k]-1)
      if(l.vec[2,2,k]!=data_seq$width[k]){
        l.seq[3,,k]=c(l.vec[2,2,k]+1,data_seq$width[k])  
      }
    }
  }

  seq.pure=seq
  pri.mis=c()
  for (k in 1: length(new_names)){
    if(!new_names[k]%in%vec.mis){
      s.seq=seq[k]%>%unlist()
      c=matrix(ncol=3,nrow=length(primer))
      if(l.seq[3,1,k]%>%is.na()){
        for (i in 1:2){
          for (j in 1:length(primer)){
            c[j,i]=s.seq[l.seq[i,1,k]:l.seq[i,2,k]]%>%as.character()%>%grepl(primer[j],.)
          }
        }
      }else{
        for (i in 1:3){
          for (j in 1:length(primer)){
            c[j,i]=s.seq[l.seq[i,1,k]:l.seq[i,2,k]]%>%as.character()%>%grepl(primer[j],.)
          }
        }
      }
      if(!(grep('TRUE',c)/length(primer))%>%isEmpty()){
        seq.pure[k]=seq[k]%>%
                    unlist()%>%
                    (function(x){x[l.seq[(grep('TRUE',c)/length(primer)) %>% ceiling()%>%mean(),1,k]:
                    l.seq[(grep('TRUE',c)/length(primer)) %>% ceiling()%>%mean(),2,k]]})%>%
                    as("DNAStringSet")
      }else{
        pri.mis=c(pri.mis, new_names[k])
      }
    }
  }
  for (i in 1:length(seq.pure)){
    write.fasta(seq.pure[i]%>%as.DNAbin(), names(seq.pure[i])%>%gsub('.seq','',.), names(seq.pure[i])%>%gsub('.seq','.fasta',.))
  }
  packageStartupMessage(" Done!")
  
  packageStartupMessage("Sequences Alignment...", appendLF = FALSE)
  data_seq=cbind(seq@ranges%>%data.frame()%>%(function(x){cbind(x$names, x$width)}),
                 seq.pure@ranges%>%data.frame()%>%(function(x){x$width})) %>% data.frame()
  colnames(data_seq)=c('names','original','selected')
  data_seq$original=data_seq$original %>% as.character()%>% as.numeric()
  data_seq$selected=data_seq$selected %>% as.character()%>% as.numeric()
  data_seq=data.frame(data_seq,success=(data_seq$original!=data_seq$selected))
  
  seq_names=data_seq[data_seq$success==TRUE,]$names %>%
            gsub('_[FR].fasta','',.)%>%
            (function(x){x[!duplicated(x)]})
  table_length=data.table()
  aln.mis=c()
  for (sn in 1:length(seq_names)){
    setwd(file.path(desdir,'seq',folder,'fasta'))
    set=seq.pure[grep(paste0(seq_names[sn],'_'),new_names)]
    if(length(set)==2){
      set=DNAStringSet(c(set[names(set)%>%grep('_F',.)],set[names(set)%>%grep('_R',.)]))
      aln.r=system(paste('blastn','-subject',set[2]%>%names,'-query',set[1]%>%names),intern=TRUE)
      if(!aln.r %>% grep('Score',.) %>% isEmpty()){
        aln.r2=aln.r[(aln.r %>% grep('Score',.)%>%min()):
                    ((aln.r %>% grep('Lambda',.)%>%min())-4)]
        aln.in=aln.r2 %>% grep('Score',.)
        aln=matrix(nrow=length(aln.in), ncol=2)
        for (i in 1:length(aln.in)){
          aln[i,1]=aln.in[i]
          if (i==length(aln.in)){
            aln[i,2]=length(aln.r2)
          }else{
            aln[i,2]=aln.in[i+1]-3 
          }
        }
        aln.len=aln.r2%>% grep('Identities',.)
        aln.bigind= aln.r2[aln.len] %>% 
          strsplit(' ')%>% unlist() %>% 
          (function(x){x[seq(4,length(x),9)]}) %>%
          strsplit('/')%>% unlist() %>%
          (function(x){x[seq(2,length(x),2)]}) %>%
          (function(x){x==max(x)})
        aln.truind=aln[aln.bigind,]
        aln.t=aln.r2[aln.truind[1]:aln.truind[2]]
        aln.q=aln.t%>% grep('Query',.,value=TRUE)%>%gsub('[A-Za-z]*','',.)%>% 
          strsplit(' ')%>%unlist()%>%gsub('-','',.)%>%as.numeric()%>%na.omit()
        aln.s=aln.t%>% grep('Sbjct',.,value=TRUE)%>%gsub('[A-Za-z]*','',.)%>% 
          strsplit(' ')%>%unlist()%>%gsub('-','',.)%>%as.numeric()%>%na.omit()
        
        if(mean(aln.q)>mean(aln.s)){
          seq.aln=paste0(set[1]%>%as.character()%>% substr(1,mean(c(max(aln.q),min(aln.q)))),
                         set[2]%>%as.character()%>% substr(mean(c(max(aln.s),min(aln.s)))+1,nchar(.))) %>% 
            DNAStringSet()
        }else if((names(set) %in% pri.mis | names(set)%in% vec.mis)%>% any()){
          seq.aln=set[!(names(set) %in% pri.mis | names(set)%in% vec.mis)]%>%
            as.character()%>% DNAStringSet()
        }else{
          seq.aln=paste0(set[2]%>%as.character()%>% substr(1,mean(c(max(aln.s),min(aln.s)))),
                         set[1]%>%as.character()%>% substr(mean(c(max(aln.q),min(aln.q)))+1,nchar(.))) %>% 
            DNAStringSet()
        }
        setwd(file.path(desdir,'seq',folder,'fasta.aln'))
        seq.aln%>% as.DNAbin()%>%write.fasta(names=seq_names[sn],file.out=paste(seq_names[sn],'.fasta',sep=''))
        table_length=rbind(table_length,data.table(seq_names[sn],seq.aln@ranges@width))
      }else{
        aln.mis= c(aln.mis,names(set))
      }
    }
  } 
  seq.aln= readDNAStringSet(list.files())
  packageStartupMessage(" Done!")
######################################

######################################
  if (nt_search){
    packageStartupMessage(paste0("Fetching sequence information...\nIt may take at least ",
                                 length(seq.aln)*3,
                                 " mins to complete."), appendLF = FALSE)
    seq.mis=c()
    db_nt <- blast(db="../../../db/nt") 
    report=data.frame()
    options(download.file.method = "wininet")
    for (i in 1: (length(seq.aln)/3)){
      if (nchar(seq.aln[i])>100){
        cl <- predict(db_nt, seq.aln[i]) 
        if(!cl[1]%>%isEmpty()){
          if (nrow(cl)<10){
            x=cl[order(-cl$Bits),] %>%
            (function(x){x[1:nrow(cl),2]})
          }else{
            x=cl[order(-cl$Bits),] %>%
            (function(x){x[1:10,2]})
          }
          x2=x %>%
             as.character() %>%
             strsplit('\\|') %>%
             unlist()
          y=x2[seq(4,length(x2),4)]%>%
            genbank2uid()%>%
            ncbi_get_taxon_summary()
          z=x2[seq(2,length(x2),4)]%>%
            entrez_summary(db='Nucleotide',id=.)%>%
            extract_from_esummary('title')
          if (nrow(cl)<10){
            report=rbind(report,
                         data.frame(Seq.Names=cl[1:nrow(cl),c(1)],
                                    description=z,y[,c(2,3)],
                                    cl[1:nrow(cl),c(3,4,5,6,7,8,9,10)]))
          }else{
            report=rbind(report,
                         data.frame(Seq.Names=cl[1:10,c(1)],
                                    description=z,y[,c(2,3)],
                                    cl[1:10,c(3,4,5,6,7,8,9,10)]))
          }
        }else{
          seq.mis=c(seq.mis, seq.aln[i]%>%names())
        }
      }
    }
    Sys.sleep(60)
    for (i in ((length(seq.aln)/3)+1):((length(seq.aln)/3)*2)){
      if (nchar(seq.aln[i])>100){
        cl <- predict(db_nt, seq.aln[i]) 
        if(!cl[1]%>%isEmpty()){
          if (nrow(cl)<10){
            x=cl[order(-cl$Bits),] %>%
              (function(x){x[1:nrow(cl),2]})
          }else{
            x=cl[order(-cl$Bits),] %>%
              (function(x){x[1:10,2]})
          }
          x2=x %>%
            as.character() %>%
            strsplit('\\|') %>%
            unlist()
          y=x2[seq(4,length(x2),4)]%>%
            genbank2uid()%>%
            ncbi_get_taxon_summary()
          z=x2[seq(2,length(x2),4)]%>%
            entrez_summary(db='Nucleotide',id=.)%>%
            extract_from_esummary('title')
          if (nrow(cl)<10){
            report=rbind(report,
                         data.frame(Seq.Names=cl[1:nrow(cl),c(1)],
                                    description=z,y[,c(2,3)],
                                    cl[1:nrow(cl),c(3,4,5,6,7,8,9,10)]))
          }else{
            report=rbind(report,
                         data.frame(Seq.Names=cl[1:10,c(1)],
                                    description=z,y[,c(2,3)],
                                    cl[1:10,c(3,4,5,6,7,8,9,10)]))
          }
        }else{
          seq.mis=c(seq.mis, seq.aln[i]%>%names())
        }
      }
    }
    Sys.sleep(60)
    for (i in ((length(seq.aln)/3)*2+1): length(seq.aln)){
      if (nchar(seq.aln[i])>100){
        cl <- predict(db_nt, seq.aln[i]) 
        if(!cl[1]%>%isEmpty()){
          if (nrow(cl)<10){
            x=cl[order(-cl$Bits),] %>%
              (function(x){x[1:nrow(cl),2]})
          }else{
            x=cl[order(-cl$Bits),] %>%
              (function(x){x[1:10,2]})
          }
          x2=x %>%
            as.character() %>%
            strsplit('\\|') %>%
            unlist()
          y=x2[seq(4,length(x2),4)]%>%
            genbank2uid()%>%
            ncbi_get_taxon_summary()
          z=x2[seq(2,length(x2),4)]%>%
            entrez_summary(db='Nucleotide',id=.)%>%
            extract_from_esummary('title')
          if (nrow(cl)<10){
            report=rbind(report,
                         data.frame(Seq.Names=cl[1:nrow(cl),c(1)],
                                    description=z,y[,c(2,3)],
                                    cl[1:nrow(cl),c(3,4,5,6,7,8,9,10)]))
          }else{
            report=rbind(report,
                         data.frame(Seq.Names=cl[1:10,c(1)],
                                    description=z,y[,c(2,3)],
                                    cl[1:10,c(3,4,5,6,7,8,9,10)]))
          }
        }else{
          seq.mis=c(seq.mis, seq.aln[i]%>%names())
        }
      }
    }
    packageStartupMessage(" Done!")
    write.csv(report,paste0('../Annotation_',folder,'.csv'),row.names = FALSE)
  }
  
  packageStartupMessage("Exporting Summary Information...", appendLF = FALSE)
  sum_names= gsub('_F.fasta','',data_seq$names) %>%
             grep('_R',.,value=TRUE,invert=TRUE)
  data_summary=matrix(nrow=length(sum_names),ncol=7)
  colnames(data_summary)=c('Seq_names','F_length_raw','R_length_raw','F_length_vs','R_length_vs','Result','Seq_Length')
  
  for (su in 1:length(sum_names)){
    index=data_seq$names %>% grep(paste(sum_names[su],'_',sep=''),.)    
    index_f= index[data_seq$names[index] %>% grep('F',.)]
    index_r= index[data_seq$names[index] %>% grep('R',.)]
    data_summary[su,1]=sum_names[su]
    if(index_f%>%isEmpty()){
      data_summary[su,2]=NA
      data_summary[su,3]=data_seq[index_r,2]
      data_summary[su,4]=NA
      data_summary[su,5]=data_seq[index_r,3]
      data_summary[su,6]='Failure'
      data_summary[su,7]=NA
    }else if(index_r%>%isEmpty()){
      data_summary[su,2]=data_seq[index_f,2]
      data_summary[su,3]=NA
      data_summary[su,4]=data_seq[index_f,3]
      data_summary[su,5]=NA
      data_summary[su,6]='Failure'
      data_summary[su,7]=NA
    }else{
      data_summary[su,2]=data_seq[index_f,2]
      data_summary[su,3]=data_seq[index_r,2]
      data_summary[su,4]=data_seq[index_f,3]
      data_summary[su,5]=data_seq[index_r,3]
      data_summary[su,6]=if(data_seq[index_f,4]&data_seq[index_r,4]){'Success'}else{'Failure'}
      if(!table_length[,V1]%>%grep(sum_names[su],.)%>%isEmpty()){
        data_summary[su,7]=table_length[table_length[,V1]%>%grep(paste0(sum_names[su],'$'),.),V2]
      }else{
        data_summary[su,7]=NA
      }
    }
  }
  data_summary=data_summary%>% as.data.frame()
  data_summary$Result= data_summary$Result%>%as.character()
  data_summary$Seq_Length= data_summary$Seq_Length%>%as.character()%>% as.numeric()
  vmn=vec.mis %>% gsub('_[FR].fasta','',.)
  pmn=pri.mis %>% gsub('_[FR].fasta','',.)
  amn=aln.mis %>% gsub('_[FR].fasta','',.)
  data_summary$Result[data_summary$Seq_names %in% vmn]='Vector not found'
  data_summary$Result[data_summary$Seq_names %in% pmn]='Primer not found'
  data_summary$Result[data_summary$Seq_names %in% amn]='Alignment no hits'
  if(nt_search){
    data_summary$Result[data_summary$Seq_names %in% seq.mis]='Annotation not found'
  }
  data_summary$Result[data_summary$Seq_Length<100]='Too Short to blast'
  write.csv(data_summary,paste0('../summary_',folder,'.csv'))
  packageStartupMessage(paste0('Done! Program End! \n\n\nFiles Location: ',
                               file.path(desdir,'seq',folder)))
}

DownloadFTP=function(source, username, password, des_folder){
  filenames= getURL(source,userpwd=paste0(username,':',password),
                    verbose=TRUE,ftp.use.epsv=TRUE, dirlistonly = TRUE) %>%
             strsplit("[\\\\]|[^[:print:]]",fixed = FALSE) %>%
             unlist() %>% (function(x){x[grep('seq',x)]})
  filepath=sprintf(paste0('ftp://',
                         paste0(username,':',password),'@',
                         (source%>%gsub('ftp://','',.)),'%s'),filenames)
  if (!file.exists(file.path(desdir,'Download',des_folder))){
    dir.create(file.path(desdir,'Download',des_folder),recursive = TRUE)
  }
  for (i in 1:(length(filenames))){
    download.file(filepath[i],
                  file.path(desdir,'Download',des_folder,filenames[i]),method="libcurl")
  }
}

FetchingSeq=function(folder){
  setwd(filePath(desdir,'seq',folder,'fasta.aln'))
  seq.aln= readDNAStringSet(list.files())
  db_nt <- blast(db="../../../db/nt") 
  report=data.frame()
  options(download.file.method = "wininet")
  packageStartupMessage(paste0("Fetching sequence information...\nIt may take at least ",
                               length(seq.aln)*3,
                               " mins to complete."), appendLF = FALSE)
  for (i in 1: length(seq.aln)){
    if (nchar(seq.aln[i])>100){
      cl <- predict(db_nt, seq.aln[i]) 
      if(!cl[1]%>%isEmpty()){
        if (nrow(cl)<10){
          x=cl[order(-cl$Bits),] %>%
            (function(x){x[1:nrow(cl),2]})
        }else{
          x=cl[order(-cl$Bits),] %>%
            (function(x){x[1:10,2]})
        }
        x2=x %>%
          as.character() %>%
          strsplit('\\|') %>%
          unlist()
        y=x2[seq(4,length(x2),4)]%>%
          genbank2uid()%>%
          ncbi_get_taxon_summary()
        z=x2[seq(2,length(x2),4)]%>%
          entrez_summary(db='Nucleotide',id=.)%>%
          extract_from_esummary('title')
        if (nrow(cl)<10){
          report=rbind(report,
                       data.frame(Seq.Names=cl[1:nrow(cl),c(1)],
                                  description=z,y[,c(2,3)],
                                  cl[1:nrow(cl),c(3,4,5,6,7,8,9,10)]))
        }else{
          report=rbind(report,
                       data.frame(Seq.Names=cl[1:10,c(1)],
                                  description=z,y[,c(2,3)],
                                  cl[1:10,c(3,4,5,6,7,8,9,10)]))
        }
      }
    }
  }
}
