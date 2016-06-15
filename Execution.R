desdir="/home/mclab/workspace/M.C.Function" 
setwd(desdir)
source('./MC_Function.R') 

C1="ATGGCGAGAACTAARCANAT"
C2="TYAYGAAAAATGTCKWGMRCCA"
C3="GAAGCTGGTCCTTCAACACC"
C4="ATGGCNMGNACNAARCA"
C5="AGYTGNATRTCCTTYTGCAT"
C6="CGGGATCCATGGCGAGAACTAAACAAACG"
C7="GCGTCGACCGAAAAATGTCGAGCGCCACCA"
C8="ATGGCGAGAACTAAACAAACG"
C9="TTACGAAAAATGTCGAGCGCCA"
C10="ATTGCGGTCAGCGGAGTGTTAGTC"
C11="GATTTTCCCGAGCCTCCTTAGTGG"
C12="GGTGCTCAAGGCGGGACATTCGTT"
C13="TCGGGATTGGCCACAGCGTTGAC"

time=proc.time()[3]
#######################
folder='rtest3'
#11-11 11-12
primer_f=C1
primer_r=C1
name_primer_f='tttr'
name_primer_r='rete'
#######################
local=TRUE
local_path='/home/mclab/seqtest'
#######################
source="ftp://140.109.56.5/104DATA/0524/"
username="rm208"
password="167cm"
#######################
nt_search=FALSE
#######################
##############  #######
#############   #######
############   #######
MC_Function(primer_f, primer_r, name_primer_f, name_primer_r, source, username, password, desdir,folder,local_path, local, nt_search) ###
#############   #######
##############  #######
#######################
proc.time()[3]-time

hr=t%/%3600
hrre=t%%3600
min=hrre%/%60
sec=hrre%%60
text=paste0("It tooks ", hr, " hour ", min, " minute ", sec, " second to complete." )

#######################
folder='ch11-6'
#######################
##############  #######
#############   #######
############   #######
FetchingSeq(folder)
#############   #######
##############  #######
#######################

#######################
des_folder='0524'
source="ftp://140.109.56.5/104DATA/0524/"
username="rm208"
password="167cm"
#######################
##############  #######
#############   #######
############    #######
DownloadFTP(source, username, password, folder)
#############   #######
##############  #######
#######################

#11-1 #11-2 #11-3
primer_f=C12
primer_r=C13
#11-4
primer_f=C1
primer_r=C2
#11-5, 11-6
primer_f=C1
primer_r=C5
#11-8, 11-9 11-10
primer_f=C6
primer_r=C7
#11-11 11-12
primer_f=C8
primer_r=C9






