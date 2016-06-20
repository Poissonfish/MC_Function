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
setwd(file.path(desdir,'seq',folder,'fasta.aln'))
seq.aln= readDNAStringSet(list.files())