<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal" id="OrderForm">
    <div class="titlePanel">
        <div class="title-search form-horizontal" id="order_search">
              <input type="hidden" name="FORMCONTROL_ID" value="${formControl.FORMCONTROL_ID}">
              <input type="hidden" name="DESIGN_ID" value="${formControl.FORMCONTROL_DESIGN_ID}">
        </div>
        <div class="toolbar">
			<button type="button" onclick="addOrder();"
				class="btn btn-outline btn-primary btn-sm">
				<i class="fa fa-plus"></i>&nbsp;新增
			</button>
			<button type="button"
				onclick="PlatUtil.removeEditTableRows('orderTable');"
				class="btn btn-outline btn-danger btn-sm">
				<i class="fa fa-trash-o"></i>&nbsp;删除
			</button>
		</div>
    </div>
    <div class="gridPanel">
        <plattag:edittable dyna_interface="commonUIService.findDatatableOrders" 
          tr_tplpath="common/compdesign/jqgrid/ordertable_tr" id="orderTable"
          col_style="[60%,排序字段选择],[40%,排序方式]" dragable="true"
          searchpanel_id="order_search"
        >
        </plattag:edittable>
    </div>
</form>

<script type="text/javascript">

function addOrder(){
	var DESIGN_ID = $("#order_search input[name='DESIGN_ID']").val();
	PlatUtil.addEditTableRow({
		tableId:"orderTable",
		DESIGN_ID:DESIGN_ID
	});
}

</script>