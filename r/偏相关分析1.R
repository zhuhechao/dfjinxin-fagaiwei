# 加载包
library(ppcor)
library(corrplot)
# 读入数据
pcordata=read.csv("pcor.data.csv")
# 计算
pcordatamat <- pcor(pcordata)
# 显著性P值
pcordatamatp <- cordatamat$p.value
# 偏相关系数矩阵
pcordatamatr <- cordatamat$estimate
# 保存结果
write.table(pcordatamatr,"pcordatamatr.txt",sep = "\t")
write.table(pcordatamatp,"pcordatamatp.txt",sep = "\t")