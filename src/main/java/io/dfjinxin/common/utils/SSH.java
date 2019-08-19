package io.dfjinxin.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.dfjinxin.config.SystemParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * ssh执行命令工具
 *
 */
public class SSH {
    private String userName;
    private String password;
    private String host;
    private int port;

    private static Logger logger = LoggerFactory.getLogger(SSH.class);

    public SSH(String host, String userName, String password, int port) {
        this.host = host;
        this.userName = userName;
        this.password = password;
        this.port = port;
    }

    private Session getSession() {
        JSch jsch = new JSch();
        Session session = null;

        try {
            session = jsch.getSession(userName, host, port);
            session.setPassword(password);

            Properties props = new Properties();
            props.put("StrictHostKeyChecking", "no");
            session.setConfig(props);
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }

    public R exec(String[] cmds) {
        StringBuffer cmdBuffer = new StringBuffer();

        for (int i = 0; i < cmds.length; i++) {
            if (i != 3) {
                cmdBuffer.append(" ");
            }
            cmdBuffer.append(cmds[i]);
        }
        return exec(cmdBuffer.toString());
    }

    public R exec(String cmd) {
        Session session = getSession();
        ChannelExec channelExec = null;
        InputStream in = null;
        try {
            channelExec = (ChannelExec)session.openChannel("exec");
            channelExec.setCommand(cmd);
            channelExec.setInputStream(null);
            channelExec.setErrStream(System.err); // 获取执行错误的信息

            in = channelExec.getInputStream();
            channelExec.connect();
            byte[] b = new byte[1024];
            int size = -1;
            while ((size = in.read()) > -1) {
                System.out.print(new String(b, 0, size)); // 打印执行命令的所返回的信息
            }
            String out = IOUtils.toString(in, "UTF-8");
            return R.ok(out);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error(-1, e.getMessage());
        } finally {
            // 关闭流
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return R.error(-1, e.getMessage());
                }
            }
            // 关闭连接
            channelExec.disconnect();
            session.disconnect();
        }

    }

    /**
     * 执行远程脚本
     * @param cmd
     * @param systemParams
     * @return
     */
    public static R execScript(String cmd, SystemParams systemParams){
        String mode = systemParams.getShExecMode();
        boolean isLocal = "local".equals(mode);
        logger.info("ssh:Run of Runnable:" + cmd);
        R res = null;
        if(isLocal)
            res = execScriptLocal(cmd);
        else
            res = execScriptRemote(cmd, systemParams);
        logger.info("ssh:result:"+res.get("code")+","+res.get("msg"));
        return res;
    }

    public static R execScriptRemote(String cmd, SystemParams systemParams){
        String host = systemParams.getSshHost();
        String user = systemParams.getSshUser();
        String pass = systemParams.getSshPwd();
        Integer port = systemParams.getSshPort();
        if(port == null)
            port = 22;

        SSH ssh = new SSH(host, user, pass, port);
        R res = ssh.exec(cmd);
        return res;
    }
    public static R execScriptLocal(String cmd){
        String line ;
        String errline = null;
        InputStream in = null;
        R res = null;
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            //读取标准输出流
            BufferedReader bf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((line=bf.readLine())!=null){
                System.out.println(line);
            }
            //读取标准错误流
            /*BufferedReader brError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            while ((errline = brError.readLine())!=null){
                System.out.println(errline);
            }*/
            //判断进程是否停止
            int rs = proc.waitFor();
            if(rs !=0){
                return  R.error().put("msg","脚本执行错误！");
            }
            int r = proc.exitValue();  //接收执行完毕的返回值
            logger.info("ssh: Shell return code:"+r+" "+cmd);
            proc.destroy();  //销毁子进程
            proc = null;
            res = R.ok("启动成功").put("returnCode", r);
        } catch (Exception ex) {
            ex.printStackTrace();
            res = R.error(ex.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    res = R.error("脚本无法关闭,"+ioe.getMessage());
                }
            }
        }
        return res;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("usage:\n\t" + SSH.class.getName()
                    + " <host> <usename> <password> <cmds>");
            System.exit(1);
        }

        // 用以保存命令（可能是一串很长的命令）
        StringBuffer cmdBuffer = new StringBuffer();
        for (int i = 3; i < args.length; i++) {
            if (i != 3) {
                cmdBuffer.append(" ");
            }
            cmdBuffer.append(args[i]);
        }

        SSH ssh = new SSH(args[0], args[1], args[2], 22);
        R res = ssh.exec(cmdBuffer.toString());
        String code = res.get("code").toString();
        System.exit(code.equals("0") ? 0 : 1);
    }
}