sum_names= gsub('_F.fasta','',data_seq$names) %>%
  grep('_R',.,value=TRUE,invert=TRUE)
data_summary=matrix(nrow=length(sum_names),ncol=7)
colnames(data_summary)=c('Seq_names','F_length_raw','R_length_raw','F_length_vs','R_length_vs','Result','Seq_Length')

for (su in 1:length(sum_names)){
  index=data_seq$names %>% grep(paste(sum_names[su],'_',sep=''),.)    
  index_f= index[data_seq$names[index] %>% grep('F',.)]
  index_r= index[data_seq$names[index] %>% grep('R',.)]
  data_summary[su,1]=sum_names[su]
  if(index_f%>%isEmpty()){
    data_summary[su,2]=NA
    data_summary[su,3]=data_seq[index_r,2]
    data_summary[su,4]=NA
    data_summary[su,5]=data_seq[index_r,3]
    data_summary[su,6]='Failure'
    data_summary[su,7]=NA
  }else if(index_r%>%isEmpty()){
    data_summary[su,2]=data_seq[index_f,2]
    data_summary[su,3]=NA
    data_summary[su,4]=data_seq[index_f,3]
    data_summary[su,5]=NA
    data_summary[su,6]='Failure'
    data_summary[su,7]=NA
  }else{
    data_summary[su,2]=data_seq[index_f,2]
    data_summary[su,3]=data_seq[index_r,2]
    data_summary[su,4]=data_seq[index_f,3]
    data_summary[su,5]=data_seq[index_r,3]
    data_summary[su,6]=if(data_seq[index_f,4]&data_seq[index_r,4]){'Success'}else{'Failure'}
    if(!table_length[,V1]%>%grep(sum_names[su],.)%>%isEmpty()){
      data_summary[su,7]=table_length[table_length[,V1]%>%grep(paste0(sum_names[su],'$'),.),V2]
    }else{
      data_summary[su,7]=NA
    }
  }
}
data_summary=data_summary%>% as.data.frame()
data_summary$Result= data_summary$Result%>%as.character()
data_summary$Seq_Length= data_summary$Seq_Length%>%as.character()%>% as.numeric()
vmn=vec.mis %>% gsub('_[FR].fasta','',.)
pmn=pri.mis %>% gsub('_[FR].fasta','',.)
amn=aln.mis %>% gsub('_[FR].fasta','',.)
data_summary$Result[data_summary$Seq_names %in% vmn]='Vector not found'
data_summary$Result[data_summary$Seq_names %in% pmn]='Primer not found'
data_summary$Result[data_summary$Seq_names %in% amn]='Alignment no hits'
if(nt_search){
  data_summary$Result[data_summary$Seq_names %in% seq.mis]='Annotation not found'
}
data_summary$Result[data_summary$Seq_Length<100]='Too Short to blast'
write.csv(data_summary,paste0('../summary_',folder,'.csv'))