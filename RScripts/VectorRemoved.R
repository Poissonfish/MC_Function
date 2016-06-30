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
for (k in 1: length(new_names)){
  if(!new_names[k]%in%vec.mis){
    seq.pure[k]=  seq[k]%>%
                  unlist()%>%
                  (function(x){x[
                    (l.seq[((l.seq[,2,k]-l.seq[,1,k])%>%na.omit())==((l.seq[,2,k]-l.seq[,1,k])%>%na.omit()%>%max()),1,k]%>%na.omit()):
                    (l.seq[((l.seq[,2,k]-l.seq[,1,k])%>%na.omit())==((l.seq[,2,k]-l.seq[,1,k])%>%na.omit()%>%max()),2,k]%>%na.omit())]
                    }) %>%
                  as("DNAStringSet")
  }
}
for (i in 1:length(seq.pure)){
  write.fasta(seq.pure[i]%>%as.DNAbin(), names(seq.pure[i])%>%gsub('.seq','',.), names(seq.pure[i])%>%gsub('.seq','.fasta',.))
}