package io.dfjinxin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "system-params")
public class SystemParams {

	private String sshHost;	//后台服务器地址
	private Integer sshPort;	//后台服务器端口
	private String sshUser;	//后台服务器用户
	private String sshPwd;	//后台服务器密码
	private String shCheck;	//探查执行脚本
	private String shCleanse;	//清洗执行脚本
	private String shEtl;	//发布作业脚本
	private String shEtlNow;	//立即执行作业脚本
	private String shTm;	//发布数据脱敏作业脚本
	private String shCleanseMode;	//清洗模式
	private String shExecMode;	//SHELL执行方式
	private String shRefresh;	//查询不到数据表时先执行同步命令

	private String sqlDialect; //SQL执行方言
}
