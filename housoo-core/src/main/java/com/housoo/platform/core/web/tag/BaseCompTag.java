package com.housoo.platform.core.web.tag;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * 描述 组件基础标签属性
 *
 * @author housoo
 * @created 2017年2月5日 下午3:32:31
 */
public class BaseCompTag extends TagSupport {
    /**
     * 下拉框ID
     */
    protected String id;
    /**
     * 下拉框名称
     */
    protected String name;
    /**
     * 下拉框的选中值
     */
    protected String value;
    /**
     * 标签所占列数量
     */
    protected String label_col_num;
    /**
     * 控件所在列数量
     */
    protected String comp_col_num;
    /**
     * 是否允许为空(true ,false)
     */
    protected String allowblank;
    /**
     * 输入提示
     */
    protected String placeholder;
    /**
     * 标签值
     */
    protected String label_value;
    /**
     * 下拉静态数据源，格式如下
     * 男:1,女:2
     */
    protected String static_values;
    /**
     * 下拉动态数据源,格式为java接口代码
     * 接口声明的格式如下
     * public List<Map<String,Object>> methodName(String dynaname);
     * 传入的值如下
     * interfaceName.methodName
     */
    protected String dyna_interface;
    /**
     * 下拉动态数据源的动态参数
     */
    protected String dyna_param;
    /**
     * 样式
     */
    protected String style;
    /**
     * 附加属性值,例如设置下拉框的其它属性，而自定义标签中并没有定义的，
     * 格式如下:属性和属性之间必须使用空格分割
     * attach_props="draggable='true' autofocus='autofocus' "
     */
    protected String attach_props;
    /**
     * 权限类型,可选值(readonly,hidden,write)
     */
    protected String auth_type;


    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the label_col_num
     */
    public String getLabel_col_num() {
        return label_col_num;
    }

    /**
     * @param label_col_num the label_col_num to set
     */
    public void setLabel_col_num(String label_col_num) {
        this.label_col_num = label_col_num;
    }

    /**
     * @return the comp_col_num
     */
    public String getComp_col_num() {
        return comp_col_num;
    }

    /**
     * @param comp_col_num the comp_col_num to set
     */
    public void setComp_col_num(String comp_col_num) {
        this.comp_col_num = comp_col_num;
    }

    /**
     * @return the allowblank
     */
    public String getAllowblank() {
        return allowblank;
    }

    /**
     * @param allowblank the allowblank to set
     */
    public void setAllowblank(String allowblank) {
        this.allowblank = allowblank;
    }

    /**
     * @return the placeholder
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * @param placeholder the placeholder to set
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * @return the label_value
     */
    public String getLabel_value() {
        return label_value;
    }

    /**
     * @param label_value the label_value to set
     */
    public void setLabel_value(String label_value) {
        this.label_value = label_value;
    }

    /**
     * @return the static_values
     */
    public String getStatic_values() {
        return static_values;
    }

    /**
     * @param static_values the static_values to set
     */
    public void setStatic_values(String static_values) {
        this.static_values = static_values;
    }

    /**
     * @return the dyna_interface
     */
    public String getDyna_interface() {
        return dyna_interface;
    }

    /**
     * @param dyna_interface the dyna_interface to set
     */
    public void setDyna_interface(String dyna_interface) {
        this.dyna_interface = dyna_interface;
    }

    /**
     * @return the dyna_param
     */
    public String getDyna_param() {
        return dyna_param;
    }

    /**
     * @param dyna_param the dyna_param to set
     */
    public void setDyna_param(String dyna_param) {
        this.dyna_param = dyna_param;
    }

    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return the attach_props
     */
    public String getAttach_props() {
        return attach_props;
    }

    /**
     * @param attach_props the attach_props to set
     */
    public void setAttach_props(String attach_props) {
        this.attach_props = attach_props;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }


    /**
     * @return the auth_type
     */
    public String getAuth_type() {
        return auth_type;
    }

    /**
     * @param auth_type the auth_type to set
     */
    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }
}
