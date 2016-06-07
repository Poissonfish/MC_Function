setwd(file.path(desdir,'seq',folder,'raw'))
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