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

setwd("~/workspace/")

x=tryCatch({
  blast(db="./db/nt") %>%capture.output()
},error=function(e){
  e
})
x
%>%as.character()

db_nt%>%as.character()
t=blast(db="~/workspace/M.C.Function/db/nt") %>% capture.output()
t[2]%>% strsplit(': ')%>%(function(x){x[[1]][2]})
t[3]%>% strsplit(': ')%>%(function(x){x[[1]][2]})

t2=t[4]%>% gsub('\\t','',.) %>%strsplit('; ')
t2[[1]][1]
t2[[1]][2]
t[6]%>% gsub('\\t','',.)%>%strsplit('Long')%>%(function(x){x[[1]][1]})%>%gsub('Date: ','',.)


aka=function(x){print(x); Sys.sleep(5)}
aka(555)



for (i in 1:99999){ x=i*i}




