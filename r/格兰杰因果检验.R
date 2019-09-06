# install.packages("lmtest")
library(lmtest)
# 读入数据
grangerdata <- data
grangerdata <- read.csv("grangerdata.csv")
# 进行因果检验
# Y是因变量，X为自变量,将Y放到第一列
n = 6 #自变量个数
grangerres <- as.data.frame(matrix(data = NA,nrow=n,ncol=1))
rownames(grangerres) <- colnames(grangerdata)[-1]
colnames(grangerres) <- "Pvalue"
grangerdata <-ts(grangerdata)
for(i in 1:n){
  res <- grangertest(data[,1] ~ data[,i+1],order=2,data =grangerdata)
  grangerres[i,1] <- res$`Pr(>F)`[2]
}
# baocun
write.csv(grangerres,"grangerresult.csv")
