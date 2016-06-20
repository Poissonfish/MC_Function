setwd(desdir)
db_nt <- blast(db= filePath(desdir,'db','nt') ) 
setwd(local_path)
seq.aln= readDNAStringSet(list.files())

if(con==-1){
  i2=1
  report=data.frame()
}else if(con==1){
  i2=itemp
}



options(download.file.method = "wininet")
for (i in i2: length(seq.aln)){
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
      report=rbind(report,
                   data.frame(Seq.Names=seq.aln[i]%>%names(), 
                              description=NA, name=NA,
                              rank=NA, Perc.Ident=NA,
                              Alignment.Length=NA, Mismatches=NA,
                              Gap.Openings=NA, Q.start=NA, Q.end=NA, S.start=NA, S.end=NA)
                   )
    }
  }
  write.csv(report,paste0('./Annotation.csv'),row.names = FALSE)
}

