package io.dfjinxin.common.utils.json;

/**
 * Created by SongCQ on 2017/7/26.
 */
public class PageField {
    private int field_id;

    private int page_id;

    private int job_id;

    private int user_id;

    private String field_name;

    private int field_datatype;

    private int parent_field_id;

    private int combine_field_value;

    private int field_locate_id;

    private PageFieldLocate pageFieldLocate;

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public int getField_datatype() {
        return field_datatype;
    }

    public void setField_datatype(int field_datatype) {
        this.field_datatype = field_datatype;
    }

    public int getParent_field_id() {
        return parent_field_id;
    }

    public void setParent_field_id(int parent_field_id) {
        this.parent_field_id = parent_field_id;
    }

    public int getCombine_field_value() {
        return combine_field_value;
    }

    public void setCombine_field_value(int combine_field_value) {
        this.combine_field_value = combine_field_value;
    }

    public int getField_locate_id() {
        return field_locate_id;
    }

    public void setField_locate_id(int field_locate_id) {
        this.field_locate_id = field_locate_id;
    }

    public PageFieldLocate getPageFieldLocate() {
        return pageFieldLocate;
    }

    public void setPageFieldLocate(PageFieldLocate pageFieldLocate) {
        this.pageFieldLocate = pageFieldLocate;
    }
}
