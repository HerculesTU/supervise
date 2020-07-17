<div class="panel-Title">
	<h5 id="${id}_paneltitle">${paneltitle}
	</h5>
</div>
<div class="card-search">
	<div class="input-group">
		<input class="form-control" name="${id}searchItem" maxlength="100"
			placeholder="请输入查询条件" type="text"> <span
			class="input-group-btn">
			<button class="btn btn-primary" type="button"
				onclick="PlatUtil.searchGridItem('${id}');">搜索</button>
		</span>
	</div>
</div>
<div class="gridItemSelect" id="${id}" itemconf="${itemconf}" iconfont="${iconfont}"
	dyna_interface="${dyna_interface}" itemtplpath="${itemtplpath}">
</div>
<script type="text/javascript">
function removeGridItem(itemDiv){
	$(itemDiv).remove();
}

$(function(){
	 //获取初始化配置
	 var selectedRecordIds = "${selectedrecordids!''}";
     var selectorConfig = PlatUtil.getData(PlatUtil.WIN_SELECTOR_CONFIG); 
	 if(selectorConfig&&selectorConfig.needCheckIds){
		  selectedRecordIds = selectorConfig.needCheckIds;
	 }
	 PlatUtil.loadGridItemTable({
		gridItemId:"${id}",
		selectedRecordIds:selectedRecordIds
	});
	 <#if (dragable??)&&(dragable=="true") >
	  $("#${id}").sortable({
		    cursor: "move",
		    opacity: 0.8,
		    revert: true,
		    handle:".card-box-img"
		    
	 });
	  </#if>
});
</script>