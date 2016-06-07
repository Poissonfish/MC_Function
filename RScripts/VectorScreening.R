setwd('../fasta')
db_vec= blast(db="../../../db/UniVec") 
seq=readDNAStringSet(new_names)
data_seq=seq@ranges %>% data.frame()
index_r=new_names%>%grep('R',.)
seq[index_r]=seq[index_r]%>% reverseComplement()

l.vec=array(dim=c(2,2,length(new_names)),
            dimnames = list(c(1:2),c('Start.pt','End.pt'),new_names))
vec.mis=c()
for (k in 1: length(new_names)){
  num=predict(db_vec,seq[k])%>% (function(x){x[x$Mismatches<10,]})
  if(nrow(num)!=0){
    qs= num$Q.start
    qe= num$Q.end
    d3= data.frame(qs=qs, qe=qe)
    d3=d3[order(d3$qs),]
    
    vec=matrix(ncol=2,nrow=2)
    vec[1,1]=d3[1,1]
    vec[1,2]=d3[1,2]
    for (i in 1:(nrow(d3)-1)){
      if(d3[i+1,1]<=vec[1,2]){
        if(d3[i+1,2]>vec[1,2]){
          vec[1,2]=d3[i+1,2]
        }}
      else if(vec[2,1]%>%is.na()){
        vec[2,1]=d3[i+1,1]
        vec[2,2]=d3[i+1,2]
      }else if(d3[i+1,2]>vec[2,2]){
        vec[2,2]=d3[i+1,2]
      }
    }
    l.vec[,,k]=vec
  }else{
    vec.mis=c(vec.mis, new_names[k])
  }
}