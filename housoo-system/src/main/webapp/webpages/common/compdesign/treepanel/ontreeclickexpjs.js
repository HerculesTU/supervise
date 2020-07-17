//这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id=="0"){
			$("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val("");
			$("#dictionary_paneltitle").text("字典信息列表");
		}else{
			$("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val(treeNode.DICTYPE_CODE);
			$("#dictionary_paneltitle").text(treeNode.name+"-->字典信息列表");
		}
		//刷新右侧列表
		PlatUtil.tableDoSearch("dictionary_datatable","dictionary_search");
	}
}  