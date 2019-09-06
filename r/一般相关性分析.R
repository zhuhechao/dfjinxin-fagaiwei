library(psych)
library(corrplot)
cor.data <- read.csv("./cor-data.csv") #读入数据
cordatamat <- corr.test(cor.data)
cordatamatp <- cordatamat$p #p值表
cordatamatr <- cordatamat$r #相关系数表
#保存数据
write.table(cordatamatr,"E:/cordatamatr.txt",sep = "\t")
write.table(cordatamatp,"E:/cordatamatp.txt",sep = "\t")
