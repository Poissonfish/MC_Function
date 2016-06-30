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