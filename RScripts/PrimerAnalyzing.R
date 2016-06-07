primer_f2=primer_f%>%DNAString()%>%reverseComplement()%>%as.character()
primer_r2=primer_r%>%DNAString()%>%reverseComplement()%>%as.character()
primerall=c(primer_f,primer_f2,primer_r,primer_r2)

pro=c('N','R','Y','K','M','W')
sp_N=c('A','T','C','G')
sp_R=c('A','G')
sp_Y=c('T','C')
sp_K=c('T','G')
sp_M=c('A','C')
sp_W=c('A','T')
sp_list=list(sp_N,sp_R,sp_Y,sp_K,sp_M,sp_W)
names(sp_list)=pro

primer=c()
for (pri in 1:4){
  listP=list()
  for (i in 1:6){
    listP[[i]]=strsplit(primerall[pri],'')%>%unlist() %>%grep(pro[i],.)
  }
  seqn=c()
  for (i in 1:6){
    seqn[listP[[i]]]=pro[i]
  }
  seqn=seqn%>%na.omit()%>%as.character()
  grid=  expand.grid(if(!is.na(seqn[1])){sp_list[seqn[1]]%>%unlist()}else{NA},
                     if(!is.na(seqn[2])){sp_list[seqn[2]]%>%unlist()}else{NA},
                     if(!is.na(seqn[3])){sp_list[seqn[3]]%>%unlist()}else{NA},
                     if(!is.na(seqn[4])){sp_list[seqn[4]]%>%unlist()}else{NA},
                     if(!is.na(seqn[5])){sp_list[seqn[5]]%>%unlist()}else{NA},
                     if(!is.na(seqn[6])){sp_list[seqn[6]]%>%unlist()}else{NA},
                     if(!is.na(seqn[7])){sp_list[seqn[7]]%>%unlist()}else{NA})
  primer=c(primer, primerall[pri]%>% gsub('[NRYKMW]','%s',.) %>% 
             sprintf(.,grid[,1],grid[,2],grid[,3],grid[,4],grid[,5],grid[,6],grid[,7]))
}
primerraw=primer
primern=c()
for (i in 1:length(primer)){
  primern=c(primern,primer[i] %>% substr(.,1,nchar(.)-5),primer[i] %>% substr(.,6,nchar(.))) 
}
primer=primern