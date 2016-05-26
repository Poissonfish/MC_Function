desdir="/home/mclab/R/git/M.C.Lab" 
setwd(desdir)
source('./MC_Function.R') 

time=proc.time()[3]
#######################
folder='test'
#11-11
primer_f="ATGGCGAGAACTAARCANAT"
primer_r="AGYTGNATRTCCTTYTGCAT"
name_primer_f='R'
name_primer_r='F'
#######################
local=TRUE
local_path='/home/mclab/Desktop/test'
#######################
source="ftp://140.109.56.5/104DATA/0520/"
username="rm208"
password="167cm"
#######################
nt_search=TRUE
#######################
##############  #######
#############   #######
############   #######
MC_Function(primer_f, primer_r, name_primer_f, name_primer_r, source, username, password, desdir,folder,local_path, local, nt_search) ###
#############   #######
##############  #######
#######################
proc.time()[3]-time




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
des_folder='0519'
source="ftp://140.109.56.5/104DATA/0509/"
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



#11-6, 11-5
primer_f="ATGGCGAGAACTAARCANAT"
primer_r="AGYTGNATRTCCTTYTGCAT"
#11-4
primer_f="ATGGCGAGAACTAARCANAT"
primer_r="TYAYGAAAAATGTCKWGMRCCA"
#11-11
primer_f="GGTGCTCAAGGCGGGACATTCGTT"
primer_r="TCGGGATTGGCCACAGCGTTGAC"

#11-8, 11-9 11-10
primer_f="CGGGATCCATGGCGAGAACTAAACAAACG"
primer_r="GCGTCGACCGAAAAATGTCGAGCGCCACCA"

#11-10
primer_f="GGTGCTCAAGGCGGGACATTCGTT"
primer_r="TCGGGATTGGCCACAGCGTTGAC"

