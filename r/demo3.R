fun <-  function(file) {
  library(psych)
  library(corrplot)
  cor.data <- read.csv(file) #读入数据
  cordatamat <- corr.test(cor.data)
  cordatamatp <- cordatamat$p #p值表
  cordatamatr <- cordatamat$r #相关系数表
  
  pfile = paste(file, "_p.txt", sep = "")
  rfile = paste(file, "_r.txt", sep = "")
  write.table(cordatamatr, rfile, sep = "\t")
  write.table(cordatamatp, pfile, sep = "\t")
  return (list(pfile, rfile))
}

