package io.dfjinxin.common.utils;

/**
 * @Desc: 指标类型
 * @Author: z.h.c
 * @Date: 2019/9/10 16:18
 * @Version: 1.0
 */
public enum KpiTypeEnum {

    Prd("Prd", "生产"),
    Ccl("Ccl", "流通"),
    Pri("Pri", "价格"),
    Csp("Csp", "消费"),
    Cst("Cst", "成本"),
    Trd("Trd", "贸易"),
    Pop("Pop", "舆情"),
    Mtl("Mtl", "气象"),
    Rpt("Rpt", "报告"),
    Dsc("Dsc", "会商");


    private String val, type;

    KpiTypeEnum(String val, String type) {
        this.val = val;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getVal() {
        return val;
    }

    public static KpiTypeEnum getbyType(String type) {
        for (KpiTypeEnum tEnum : values()) {
            if (tEnum.getType().equals(type)) {
                return tEnum;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        KpiTypeEnum kk = KpiTypeEnum.getbyType("生产");
        System.out.println(kk);
    }
}
