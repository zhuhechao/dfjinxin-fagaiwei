package io.dfjinxin.modules.sys.entity;

import io.dfjinxin.common.utils.KpiTypeEnum;
import lombok.Data;

/**
 * Created by GaoPh on 2020/1/14.
 */
public enum MenuHiddenEnum {
    ModelCre("/modelDesign/modelManage/createModel","/modelDesign/createModel"),
    ModelMap("/modelDesign/modelManage/modelMapping","/modelDesign/modelMapping"),
    PriceWatch("/pricewatch/pricewarning","dashboard"),
    PublicSystem("/pubilcsystem/index","link"),
    OnLine("/onlineSurveySystem/index","password"),
    AnalyReport("/analysereportconfig/reportproduceconfig","eye-open"),
    ModelAdmin("/modeladmin/datapretreatment","form"),
    DataSourceManagement("/datasourcemanagement/datasourcemanagement","nested"),
    SysConfig("/sysconfig/usersconfig","user"),
    AsyncRouter("/secondpage/index","table"),
    WarningShow("/warningshow/index","tree"),
    Analyses("/analyses/index","eye"),
    Divine("/divine/index","example");

    private String key, value;
    MenuHiddenEnum(String key,String value){
        this.key = key;
        this.value = value;
    }
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    public static MenuHiddenEnum getbyType(String key) {
        for (MenuHiddenEnum tEnum : values()) {
            if (tEnum.getKey().equals(key)) {
                return tEnum;
            }
        }
        return null;
    }
}
