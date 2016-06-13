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

setwd("~/workspace")

tryCatch({
  blast(db="./db/nt") %>%capture.output()
},error=function(e){
  error=print(e)
})


db_nt%>%as.character()
t=blast(db="~/workspace/M.C.Function/db/nt") %>% capture.output()
t[2]%>% strsplit(': ')%>%(function(x){x[[1]][2]})
t[3]%>% strsplit(': ')%>%(function(x){x[[1]][2]})

t2=t[4]%>% gsub('\\t','',.) %>%strsplit('; ')
t2[[1]][1]
t2[[1]][2]
t[6]%>% gsub('\\t','',.)%>%strsplit('Long')%>%(function(x){x[[1]][1]})%>%gsub('Date: ','',.)
