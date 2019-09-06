# 加载包
library(lavaan)
library(semPlot)
library(OpenMx)
library(GGally)
# 读入数据
cauadata=read.csv("caua.data.csv")
# 构建模型公式，Y为因变量，X为自变量
model1 = 'Y ~ X1 +	X2 +	X3 + X4 +	X5 + X6'
# 构建路径分析
fit = cfa(model1, data = cauadata)
# 路径系数和显著性P值保存在cauamat里面
n = ncol(cauadata)-1 #自变量个数
cauamat <- parameterEstimates(fit, ci = TRUE, boot.ci.type = "norm",standardized = TRUE, rsquare = TRUE)
cauamat <- cauamat[1:n,c(1,2,3,7,11)]
# baocun
write.csv(cauamat,"cauamat.csv")





