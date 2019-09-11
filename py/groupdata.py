import pandas as pd
import numpy as np
import os
import matplotlib.pyplot as plt 

data = pd.read_csv(os.path.dirname(__file__) + '/tempdata.csv',header=None,parse_dates=True)#,index_col='日期'
data.columns = ['0','商品', '指标', '环节', '地区', '取值', '日期']

data_group = data.groupby(['日期','指标','地区','商品'])['取值'].mean().unstack().unstack().unstack()#.shape#['1'].value_counts()

data_group.to_csv(os.path.dirname(__file__) + '/data_group.csv')

#
print(data_group[29][['上海','北京']])