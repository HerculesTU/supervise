<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="树形面板" compcode="${FORMCONTROL_COMPCODE}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
   compcontrolid="${FORMCONTROL_ID}">
</#if>
<plattag:treepanel  id="${CONTROL_ID}" 
<#if (DRAGABLE??)&&(DRAGABLE=="-1") >opertip="false"</#if>
<#if PANEL_TITLE?? >panel_title="${PANEL_TITLE}"</#if>
<#if rightmenus?? >
right_menu="${rightmenus}"
</#if>
 >
</plattag:treepanel>
<script type="text/javascript">
<#if operbtns??&&(operbtns?size>0) >
<#list operbtns as btn>
${btn.TABLEBUTTON_FNCONTENT}
</#list>
</#if>  
  ${TREE_CLICKFN!''}
  $(function(){
	  <#if (CHECKSTYLE??)&&(CHECKSTYLE!="0") >
	  var ${CONTROL_ID}check = {
		enable : true,
		chkStyle:"${(CHECKSTYLE=="1")?string('checkbox','radio')}",
		<#if CHECKSTYLE=="2" >
		radioType:"all",
		</#if>
		chkboxType :{
			"Y" :"${CHECKEDCAS!''}",
			"N" :"${UNCHECKEDCAS!''}"
		}
	  };
	  </#if>
	  PlatUtil.initZtree({
		  treeId:"${CONTROL_ID}",
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
		  autoCompleteType:"1",
		  <#if (CHECKSTYLE??)&&(CHECKSTYLE!="0") >
		  check: ${CONTROL_ID}check,
		  </#if>
		  <#if (DRAGABLE??)&&(DRAGABLE=="-1") >
		  edit : {
				enable : false,
				showRemoveBtn : false,
				showRenameBtn : false
		  },
		  </#if>
		  callback: {
			  onRightClick: PlatUtil.onZtreeRightClick,
			  onClick:${TREE_CLICKFNNAME!''},
			  onDrop: PlatUtil.onZtreeNodeDrop
		  },
		  async : {
			url:"common/baseController.do?tree",
			otherParam : {
				//树形表名称
				"tableName" : "${TREE_TABLENAME}",
				//键和值的列名称
				"idAndNameCol" : "${ID_ANDNAME}",
				//查询其它部门列名称
				"targetCols" : "${TARGET_COLS}",
				//最先读取的根节点的值
				"loadRootId" : "${LOAD_ROOTID}",
				//需要回填的值
				"needCheckIds":"",
				//是否显示树形标题
				"isShowTreeTitle" : "${IS_SHOWROOT}",
				<#list querys as query>
			    <#if query.QUERYCONDITION_CTRLTYPE=='1' >
			    "${query.QUERYCONDITION_CTRLNAME}":"${query.QUERYCONDITION_CONTROLVALUE!''}", 
			    </#if>
				</#list>
				//根据节点名称
				"treeTitle":"${ROOT_NAME!''}"
			}
		  }
	  });
	  
});
</script>

<#if PLAT_DESIGNMODE?? >
</div>
</#if>