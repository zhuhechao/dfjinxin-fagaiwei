////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.dfjinxin.common.utils;
//
//import com.bjca.security.SecurityEngineDeal;
//import com.bjca.sso.bean.UserTicket;
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.Hashtable;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import io.dfjinxin.modules.sys.entity.SysUserEntity;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.w3c.dom.Text;
//
//public class GovCaTicketManager {
//    private UserTicket userticket = null;
//    private String beginDate = null;
//    private String endDate = null;
//    private Hashtable roles = null;
//    private SecurityEngineDeal safeengine = null;
//
//    public GovCaTicketManager() {
//        this.userticket = new UserTicket();
//        this.roles = new Hashtable();
//    }
//
//    public static void main(String[] args) throws UnsupportedEncodingException {
//        GovCaTicketManager govCaTicketManager = new GovCaTicketManager();
//
//        String ticket = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" +
//                "<uams-ticket>" +
//                "<valid-date>" +
//                "<BeginTime>20190708112952</BeginTime>" +
//                "<EndTime>20190708115952</EndTime>" +
//                "</valid-date>" +
//                "<base-info>" +
//                "<UserID>871b412eab6ea3e7b21cb2dfa4b64c58</UserID>" +
//                "<UserType>0</UserType>" +
//                "<CredenceType>CREDENCE_00002</CredenceType>" +
//                "<DepartCode>110000C005039</DepartCode>" +
//                "<SystemCode>gydsj_sjqx</SystemCode>" +
//                "</base-info>" +
//                "<role-infos><role-info>" +
//                "<RoleName>经信局工业大数据_数据清洗子系统角色</RoleName>" +
//                "<RoleCode>gydsj_sjqx</RoleCode>" +
//                "</role-info>" +
//                "</role-infos>" +
//                "<append-info><userLoginName>bd_zyzx</userLoginName>" +
//                "<userLoginPwd>bd_zyzx</userLoginPwd><departName>信息资源中心</departName>" +
//                "<userName>市信息资源管理中心</userName></append-info></uams-ticket>";
//
//
//        UserTicket result = govCaTicketManager.getTicket(ticket
//                , "2", null);
//
//        System.out.println(result);
//
//    }
//
//    public UserTicket getTicket(String ticket, String mode, String certification) {
//        String viewticket = null;
//        if (mode == null) {
//            return null;
//        } else if (!mode.equals("0") && !mode.equals("1") && !mode.equals("2")) {
//            return null;
//        } else if (ticket != null && !ticket.equals("")) {
//            if (mode.equals("0")) {
//                viewticket = this.decrptyTicket(ticket, certification);
//                viewticket = this.truphTicket(viewticket, certification);
//            }
//
//            if (mode.equals("1")) {
//                viewticket = this.truphTicket(ticket, certification);
//            }
//
//            if (mode.equals("2")) {
//                viewticket = ticket;
//            }
//
//            if (!viewticket.startsWith("<?xml version=\"1.0\" encoding=\"GB2312\"?>")) {
//                return null;
//            } else {
//                this.parseTicket(viewticket);
//                return this.userticket;
//            }
//        } else {
//            return null;
//        }
//    }
//
//    private String decrptyTicket(String ticket, String certification) {
//        Object var3 = null;
//
//        try {
//            System.out.println("ticket=" + this.safeengine.GetServerCertificate());
//            System.out.println("safe mi ticket=" + ticket);
//            String i = this.safeengine.EnvelopedData_Decrypt(ticket);
//            System.out.println("i = " + i);
//            if (i != null) {
//                return i;
//            }
//        } catch (Exception var5) {
//            var5.printStackTrace();
//        }
//
//        return null;
//    }
//
//    private String truphTicket(String ticket, String certification) {
//        int sFlag = ticket.indexOf("<Signature>");
//        int eFlag = ticket.indexOf("</Signature>");
//        String newticket = null;
//        String signTicket = null;
//        if (sFlag != -1 && eFlag != -1) {
//            int lens = "<Signature>".length();
//            int len = "</Signature>".length();
//            signTicket = ticket.substring(sFlag + lens, eFlag);
//            newticket = ticket.substring(0, sFlag) + ticket.substring(eFlag + len);
//            if (this.safeengine == null || certification == null) {
//                return null;
//            }
//
//            try {
//                String i = this.safeengine.SignedDataByP7_Verify(newticket, signTicket);
//                if (i != null) {
//                    return newticket;
//                }
//
//                return null;
//            } catch (Exception var10) {
//                var10.printStackTrace();
//            }
//        }
//
//        return null;
//    }
//
//    private void parseTicket(String ticket) {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
//        try {
//            InputStream in = new ByteArrayInputStream(ticket.getBytes("gbk"));
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(in);
//            Element ticketElement = doc.getDocumentElement();
//            NodeList ticketNode = ticketElement.getChildNodes();
//
//            for(int i = 0; i < ticketNode.getLength(); ++i) {
//                Node item = ticketNode.item(i);
//                if (item.getNodeType() == 1) {
//                    Element time = (Element)item;
//                    NodeList firstTimes = time.getElementsByTagName("BeginTime");
//                    if (firstTimes.getLength() > 0) {
//                        this.beginDate = getElementText((Element)firstTimes.item(0));
//                    }
//
//                    NodeList endTimes = time.getElementsByTagName("EndTime");
//                    if (endTimes.getLength() > 0) {
//                        this.endDate = getElementText((Element)endTimes.item(0));
//                    }
//
//                    NodeList userIDs = time.getElementsByTagName("UserID");
//                    if (userIDs.getLength() > 0) {
//                        this.userticket.setUserUniqueID(getElementText((Element)userIDs.item(0)));
//                    }
//
//                    NodeList userTypes = time.getElementsByTagName("UserType");
//                    if (userTypes.getLength() > 0) {
//                        this.userticket.setUserType(getElementText((Element)userTypes.item(0)));
//                    }
//
//                    NodeList credenceTypes = time.getElementsByTagName("CredenceType");
//                    if (credenceTypes.getLength() > 0) {
//                        this.userticket.setUserCredenceType(getElementText((Element)credenceTypes.item(0)));
//                    }
//
//                    NodeList departCodes = time.getElementsByTagName("DepartCode");
//                    if (departCodes.getLength() > 0) {
//                        this.userticket.setUserDepartCode(getElementText((Element)departCodes.item(0)));
//                    }
//
//                    NodeList systemCodes = time.getElementsByTagName("SystemCode");
//                    if (systemCodes.getLength() > 0) {
//                        this.userticket.setUserSystemCode(getElementText((Element)systemCodes.item(0)));
//                    }
//
//                    NodeList appendInfo_userNames = time.getElementsByTagName("userName");
//                    if (appendInfo_userNames.getLength() > 0) {
//                        this.userticket.setUserName(getElementText((Element)appendInfo_userNames.item(0)));
//                    }
//
//                    NodeList appendInfo_departNames = time.getElementsByTagName("departName");
//                    if (appendInfo_userNames.getLength() > 0) {
//                        this.userticket.setUserDepartName(getElementText((Element)appendInfo_departNames.item(0)));
//                    }
//
//                    NodeList appendInfo_userCertTypes = time.getElementsByTagName("userCertType");
//                    if (appendInfo_userCertTypes.getLength() > 0) {
//                        this.userticket.setUserCertType(getElementText((Element)appendInfo_userCertTypes.item(0)));
//                    }
//
//                    String roleCode = null;
//                    String roleName = null;
//                    NodeList roleInfos = time.getElementsByTagName("role-info");
//
//                    for(int j = 0; j < roleInfos.getLength(); ++j) {
//                        Node roleI = roleInfos.item(j);
//                        if (roleI.getNodeType() == 1) {
//                            Element role = (Element)roleI;
//                            NodeList roleNames = role.getElementsByTagName("RoleName");
//                            if (roleNames.getLength() > 0) {
//                                roleName = getElementText((Element)roleNames.item(0));
//                            }
//
//                            NodeList roleCodes = role.getElementsByTagName("RoleCode");
//                            if (roleCodes.getLength() > 0) {
//                                roleCode = getElementText((Element)roleCodes.item(0));
//                            }
//
//                            if (roleCode != null && roleName != null) {
//                                this.roles.put(roleCode, roleName);
//                            }
//                        }
//                    }
//
//                    if (this.roles.size() > 0) {
//                        this.userticket.setUserRoles(this.roles);
//                    }
//
//                    String signature = time.getNodeName();
//                    if (signature.equals("Signature")) {
//                        String var3 = getElementText(time);
//                    }
//                }
//            }
//        } catch (Exception var30) {
//            var30.printStackTrace();
//        }
//
//    }
//
//    private static String getElementText(Element elem) {
//        StringBuffer buff = new StringBuffer();
//        NodeList list = elem.getChildNodes();
//
//        for(int i = 0; i < list.getLength(); ++i) {
//            Node item = list.item(i);
//            if (item instanceof Text) {
//                Text charItem = (Text)item;
//                buff.append(charItem.getData());
//            }
//        }
//
//        return buff.toString();
//    }
//}
