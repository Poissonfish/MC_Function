setwd(file.path(desdir,'seq',folder,'raw'))
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
                            SN[l_index] %>% substr(3,4),'-',
                            SN[l_index] %>% substr(nchr[2]-1,nchr[2]),'_', FR[l_index])
  new_names[s_index]=paste0(SN[s_index] %>% substr(1,2),'-',
                            SN[s_index] %>% substr(3,4),'-',
                            SN[s_index] %>% substr(5,nchr[1]),'_', FR[s_index])
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
                   SN %>% substr(3,4),'-',
                   SN %>% substr(5,nchr[2]),'_',FR)
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