package io.dfjinxin.modules.price.service;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import io.dfjinxin.common.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;

/**
 * Created by yzn00 on 2019/10/22.
 */
public class SSHConnect {
    protected static Logger logger = LoggerFactory.getLogger(SSHConnect.class);

    private static final int TIMEOUT = 2000;

    private static final int RECONNECT_TIMES = 20;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    private String host;


    private String pass;


    private int port;

    private int sleepTime = 100;

    private int waitTime = 500;

    public SSHConnect(String ip, String userName, String pass, int port) {
        this.host = ip;
        this.userName = userName;
        this.pass = pass;
        this.port = port;
    }

    public SSHConnect() {

    }

    public String executePython(String cmd) {

        String retStr = null;
        try {
            Session session = openConnection();
            InputStream inputStream = executeCommand(cmd, session);
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            int i = 0;
            while (i < 8 && (line = in.readLine()) != null) {//服务器环境是返回8次空本地是+1
                if (line.length() == 0) {
                    logger.debug("exe numbers " + i++);
                    continue;
                }
                if (StringUtils.trim(line).length() > 0 && StringUtils.trim(line).substring(StringUtils.trim(line).length() - 1).equals("#"))
                    break;
                logger.debug("call result the result:" + line);
                stringBuffer.append(line);
            }
            in.close();
            retStr = stringBuffer.toString();

        } catch (Exception e) {

        } finally {
            return retStr;
        }
    }

    public Session openConnection() throws Exception {

        Connection connection = connect(host, port, TIMEOUT);
        if (!connection.authenticateWithPassword(userName, pass)) {
            throw new Exception("用户名密码错误");
        }
        logger.info("登陆成功!");
        Session session = connection.openSession();
        session.requestPTY("");
        session.startShell();
        return session;
    }

    private Connection connect(String address, int port, long timeOut) {

        Connection conn = new Connection(address, port);
        int connectTimes = 1;
        long waitTime = System.currentTimeMillis() + timeOut;
        do {
            try {
                conn.connect();
                break;
            } catch (IOException e) {
                logger.error("ssh连接到主机时出错", e);
                connectTimes++;
            }
        } while (System.currentTimeMillis() < waitTime
                && RECONNECT_TIMES <= connectTimes);
        return conn;
    }

    public InputStream executeCommand(String command, Session session)
            throws Exception {

        if (command.equals("")) {
            logger.info("执行空指令");
            return null;
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(session.getStdin(),
                    "UTF-8"));
            out.println(command);
            out.flush();
            return handleBufferStr(session);
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 处理接收的缓存数据
     */
    protected InputStream handleBufferStr(Session session) throws Exception {
        ByteArrayOutputStream sb = new ByteArrayOutputStream();
        ByteArrayOutputStream ersb = new ByteArrayOutputStream();
        InputStream in = null;
        try {
            if (sleepTime == 0) {
                return null;
            }
            Thread.sleep(sleepTime);
            in = receiveMsg(sb, ersb, session);
            return in;
        } finally {
            if (null != in) {
                in.close();
            }
        }
    }

    /**
     * 接收shell返回信息
     *
     * @param stdNormal
     * @param stdError
     * @return
     * @throws Exception
     */
    protected InputStream receiveMsg(ByteArrayOutputStream stdNormal,
                                     ByteArrayOutputStream stdError, Session session) throws Exception {
        InputStream stdout = null;
        try {
            stdout = session.getStdout();
            // InputStream stderr = session.getStderr();
            int conditions = session.waitForCondition(
                    ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA
                            | ChannelCondition.EOF, waitTime);

            if ((conditions & ChannelCondition.TIMEOUT) != 0) {
                logger.error("获取session数据超时");
                throw new IOException("获取打印数据超时");
            }
            if ((conditions & ChannelCondition.EOF) != 0) {
                logger.error("无数据可读");
            }

            while (stdout.available() > 0) {
                return stdout;
                /*
                 * byte[] buffer = new byte[8192]; int len =
                 * stdout.read(buffer); if (len > 0) { // this check is somewhat
                 * paranoid if (log.isDebugEnable()) { //
                 * log.debug("Receive msg :" + buffer); }
                 * stdNormal.write(buffer, 0, len); }
                 */
            }

            /*
             * while (stderr.available() > 0) { byte[] buffer = new byte[8192];
             * int len = stderr.read(buffer); if (len > 0) {// this check is
             * somewhat paranoid if (log.isDebugEnable()) { //
             * log.debug("Receive error msg :" + buffer); }
             * stdError.write(buffer, 0, len); } }
             */
        } finally {
            if (null != stdout) {
                stdout.close();
            }
        }
        return null;
    }

    /**
     * @Desc: 调用远程python方法
     * @Param: [file, params]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/10/28 14:18
     */
    public static String callPyProc(String pyFileName,
                                    String jsonStr,
                                    String host,
                                    String userName,
                                    String pass,
                                    int port) {
        logger.debug("调用文件:{},开始---", pyFileName);
        logger.debug("请求参数:{}", jsonStr);
        SSHConnect sshConnect = new SSHConnect(host, userName, pass, port);
        String commandPy = "python3 " + pyFileName + " " + jsonStr;
        String res = sshConnect.executePython(commandPy);
        logger.debug("调用文件:{},结束---", pyFileName);
        int index = res.lastIndexOf("{");
        res = res.substring(index);
        logger.debug("python返回结果:{}", res);
        return res;
    }

}
