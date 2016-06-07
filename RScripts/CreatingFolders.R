setwd(desdir)
if (!file.exists(file.path(desdir,'seq',folder,'raw'))){
  dir.create(file.path(desdir,'seq',folder,'raw'),recursive = TRUE)
}
if (!file.exists(file.path(desdir,'seq',folder,'fasta'))){
  dir.create(file.path(desdir,'seq',folder,'fasta'),recursive = TRUE)
}
if (!file.exists(file.path(desdir,'seq',folder,'fasta.aln'))){
  dir.create(file.path(desdir,'seq',folder,'fasta.aln'),recursive = TRUE)
}
if(local){system(paste('cp -r',file.path(local_path,'.'),file.path(desdir,'seq',folder,'raw')))}