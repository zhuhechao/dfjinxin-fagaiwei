# 去除季节性因素
# 设置环境路径
SPARK_HOME = "D:/software/spark/spark-2.4.3-bin-hadoop2.7"
if (nchar(Sys.getenv("SPARK_HOME")) < 1) {
    Sys.setenv(SPARK_HOME = "D:/software/spark/spark-2.4.3-bin-hadoop2.7")
}
# 加载包
library(SparkR, lib.loc = c(file.path(Sys.getenv("SPARK_HOME"), "R", "lib")))
# 计算周期函数
period <- function(starttime,endtime,ndata){
    ntime = endtime - starttime
    ntime <- as.numeric(ntime)
    t = ntime / ndata
    frequeny = 365 %/% t
    return(frequeny)
}
# 去除季节性因素函数，将结果保存到大数据平台
write_remove_seasonal_price <- function(commid){
    SPARK_HOME = "D:/software/spark/spark-2.4.3-bin-hadoop2.7"
    if (nchar(Sys.getenv("SPARK_HOME")) < 1) {
        Sys.setenv(SPARK_HOME = "D:/software/spark/spark-2.4.3-bin-hadoop2.7")
    }
    # 加载包
    library(SparkR, lib.loc = c(file.path(Sys.getenv("SPARK_HOME"), "R", "lib")))
    sparkR.session(master = "local[*]", sparkConfig = list(spark.driver.memory = "2g"),enableHiveSupport = TRUE)
    sql = paste("select * from lbl_use.souzhu_all where index_id = ",commid,sep='')
    data <- sql(sql)
    data <- as.data.frame(data)
    frequency = period(data$datetime[1],data$datetime[nrow(data)],nrow(data))
    price <- ts(data$index_val,frequency=frequency)
    price_components <- decompose(price)
    price1 <- price-price_components$seasonal
    price1 <- as.numeric(price1)
    data$index_val <- price1
    df <- createDataFrame(data)
    createOrReplaceTempView(df, "df")
    sql =paste("INSERT OVERWRITE TABLE lbl_use.souzhu_price1 SELECT * FROM lbl_use.souzhu_price1 where index_id != ",commid,sep = "")
    sql(sql)
    sql("INSERT into TABLE lbl_use.souzhu_price1 SELECT * FROM df")
    sparkR.session.stop()
    sparkR.stop()
}
args <- commandArgs(trailingOnly = TRUE)
comid <- args[1]
write_remove_seasonal_price(comid )

fun3 <-  function(b) {
    a= 1
    b= a +1
    return(b)

}
