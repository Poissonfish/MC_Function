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
write.csv(report,paste0('../Annotation_',folder,'.csv'),row.names = FALSE)