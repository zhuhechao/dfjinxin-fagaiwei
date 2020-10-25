**安装R语言**
- 到 https://www.r-project.org/ 下载R语言安装包
- 进入 R console (linux 在终端输入R， windows可打开R console)然后执行 
~ install.packages("Rserve")
~ library(Rserve)
~ Rserve()
- 然后就可以在 java 中调用R语言了，java test： TestR 有3个例子
- install.packages 安装项目依赖  lavaan semPlot OpenMx GGally corrplot, 然后 library
 

**项目结构** 
```
dfjinxin-fast
├─db  项目SQL语句
│
├─common 公共模块
│  ├─aspect 系统日志
│  ├─exception 异常处理
│  ├─validator 后台校验
│  └─xss XSS过滤
│ 
├─config 配置信息
│ 
├─modules 功能模块
│  ├─app API接口模块(APP调用)
│  ├─job 定时任务模块
│  ├─oss 文件服务模块
│  └─sys 权限模块
│ 
├─dfjinxinApplication 项目启动类
│  
├──resources 
│  ├─mapper SQL对应的XML文件
│  └─static 静态资源

```
<br> 


**技术选型：** 
- 核心框架：Spring Boot 2.1
- 安全框架：Apache Shiro 1.4
- 视图框架：Spring MVC 5.0
- 持久层框架：MyBatis 3.3
- 定时器：Quartz 2.3
- 数据库连接池：Druid 1.0
- 日志管理：SLF4J 1.7、Log4j
- 页面交互：Vue2.x 
<br> 


 **后端部署**
- 通过git下载源码
- idea、eclipse需安装lombok插件，不然会提示找不到entity的get set方法
- 创建数据库dfjinxin_fast，数据库编码为UTF-8
- 执行db/mysql.sql文件，初始化数据
- 修改application-dev.yml，更新MySQL账号和密码
- Eclipse、IDEA运行dfjinxinApplication.java，则可启动项目
- Swagger路径：http://localhost:8080/fagaiwei/swagger-ui.html

- 测试环境：打包test，生成war，然后上传到172.20.10.13 /home/dfjx/apache-tomcat-fagaiwei_api/webapps/
然后到 /home/dfjx/apache-tomcat-fagaiwei_api/bin 执行 shutdown.sh 在执行 start.sh
接着登录119.3.247.102 打开地址访问 http://172.20.10.13:8081/fagaiwei_api/swagger-ui.html
<br> 

 **前端部署**
 - 本项目是前后端分离的，还需要部署前端，才能运行起来
 - 前端部署完毕，就可以访问项目了，账号：admin，密码：admin
 
<br> 

 **数据返回规范类io.dfjinxin.common.adviceCommonResponseDataAdvice**
- 规范后台返回数据格式，返回数据必须是io.dfjinxin.common.utils.R，如果不按R格式返回，强制把返回的数据赋值R.ok().put(data, 返回数据类)返回，
 这样规范数据格式，统一序列化，前台页面js也可以定义全局方法判断返回的code除了正常状态0以外的状态如果为异常、未登录等状态，可以统一做弹窗、跳转
- 如有特殊情况不能返回R，可以在返回的方法、或者类（如果类中的所有类都不能返回R）中添加定义的annotation（io.dfjinxin.common.annotation.IgnoreResponseAdvice）

<br> 

 **新增将项目打成war包配置文件**
- 执行命令
- mvn clean package -Dmaven.test.skip=true -f pom4war.xml