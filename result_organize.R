library(magrittr)
library(data.table)
setwd('~/seq_result')
filenames= list.files()
data=data.frame()

for (i in 1:length(filenames)){
  data=rbind(data,
             read.csv(filenames[i]))
}
data=data[!duplicated(data[,1]),]

des=data$description[!duplicated(data$description)] %>% as.character()
index=matrix(ncol=length(des), nrow=nrow(data))
des=des%>% gsub('\\(','\\\\(',.) %>% gsub('\\)','\\\\)',.)

for (i in 1:length(des)){
  lg=grep(des[i],data$description)
  if (length(lg)!=0){
    index[1:length(lg),i]=lg 
  }
}
summary= data.table(Description=NA, Seq.Names=NA)
for (i in 1:length(des)){
 summary= list(summary, data.frame(des[i],
                                    data[index[,i],]%>% na.omit() %>% 
                                    (function(x){x$Seq.Names}) %>% as.character() %>%
                                     paste(collapse=', '))) %>% rbindlist()
}
summary= summary%>% na.omit()
summary$Description= summary$Description %>% gsub('\\\\','',.)
summary
write.csv(summary,'summary_des.csv')
