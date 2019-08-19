package io.dfjinxin.modules.sys.entity;

import lombok.Data;

/**
 * 太极CA认证后跳转到本系统所带参数
 */
@Data
public class GovCaUserForm {
    private String bjcaServerCert;
    private String bjcaTicket;
    private String bjcaTicketType;
    private String bjcaTargetUrl;
}
