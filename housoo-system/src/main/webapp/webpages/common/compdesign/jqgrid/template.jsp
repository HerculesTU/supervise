<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="JqGrid表格(${CONTROL_ID})" compcode="${FORMCONTROL_COMPCODE}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
   compcontrolid="${FORMCONTROL_ID}">
</#if>
<#if TABLE_TITLE?? >
<div class="panel-Title">
	<h5 id="${CONTROL_ID}_datatablepaneltitle">${TABLE_TITLE}</h5>
</div>
</#if>
<div class="titlePanel" <#if haveVisiableQuery=='-1'&&(operbtns?size==0) >style="display: none;"</#if> >
    <#if querys??&&(querys?size>0) >
	<div class="title-search form-horizontal" id="${CONTROL_ID}_search" formcontrolid="${FORMCONTROL_ID}">
		<table  <#if haveVisiableQuery=='-1' >style="display: none;"</#if> >
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
					    <#list querys as query>
                	       <#if query.QUERYCONDITION_CTRLTYPE=='1' >
                			  <input type="hidden" name="${query.QUERYCONDITION_CTRLNAME}" value="${query.QUERYCONDITION_CONTROLVALUE!''}">
                		   </#if>
						</#list>
						<input type="text" style="width: 200px; "
							onclick="PlatUtil.showOrHideSearchTable(this);"
							class="table-form-control" name="search" readonly="readonly" />
						<div class="table-filter-list"
							style="width: 420px; display: none;max-height: 280px;">
							<#list querys as query>
							    <#if query.QUERYCONDITION_CTRLTYPE!='1' >
								<div class="form-group">
								    <#if query.QUERYCONDITION_CTRLTYPE=='2' >
									<plattag:input name="${query.QUERYCONDITION_CTRLNAME}" auth_type="write"
										label_value="${query.QUERYCONDITION_LABEL}" maxlength="100" allowblank="true"
										placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='3' >
								    <plattag:select placeholder="" istree="${query.IS_TREE}" label_col_num="3" 
								      label_value="${query.QUERYCONDITION_LABEL}" style="width:100%;"
								      allowblank="true" comp_col_num="9" auth_type="write" 
								      static_values="${query.QUERYCONDITION_STATICVALUES!''}" 
		                              dyna_interface="${query.QUERYCONDITION_DYNAINTERFACE!''}" dyna_param="${query.QUERYCONDITION_DYNAPARAM!''}"
								       name="${query.QUERYCONDITION_CTRLNAME}"
								      <#if (query.MULTI_SELECT??)&&query.MULTI_SELECT=="true" >
								      multiple="multiple"
								      </#if>    
								    >
								    </plattag:select>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='4' >
								    <plattag:radio name="${query.QUERYCONDITION_CTRLNAME}" auth_type="write" 
								    label_col_num="3" label_value="${query.QUERYCONDITION_LABEL}"
								    static_values="${query.QUERYCONDITION_STATICVALUES!''}" 
		                             dyna_interface="${query.QUERYCONDITION_DYNAINTERFACE!''}" dyna_param="${query.QUERYCONDITION_DYNAPARAM!''}"
								    select_first="${query.SELECT_FIRST!''}" allowblank="true" is_horizontal="true" comp_col_num="9">
								    </plattag:radio>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='5' >
								    <plattag:checkbox name="${query.QUERYCONDITION_CTRLNAME}" auth_type="write" 
								    label_col_num="3" label_value="${query.QUERYCONDITION_LABEL}"
								    static_values="${query.QUERYCONDITION_STATICVALUES!''}" 
		                             dyna_interface="${query.QUERYCONDITION_DYNAINTERFACE!''}" dyna_param="${query.QUERYCONDITION_DYNAPARAM!''}"
								     allowblank="true" is_horizontal="true" comp_col_num="9">
								    </plattag:checkbox>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='6' >
								    <plattag:datetime istime="${(query.QUERYCONDITION_ISTIME=='1')?string('true', 'false')}" placeholder="" 
								    label_col_num="3" label_value="${query.QUERYCONDITION_LABEL}"
								    allowblank="true" comp_col_num="9" auth_type="write" format="${query.QUERYCONDITION_DATEFORMAT}" name="${query.QUERYCONDITION_CTRLNAME}">
								    </plattag:datetime>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='7' >
								    <jsp:include page="/webpages/common/plattagtpl/rangetime_tag.jsp">
									    <jsp:param name="label_col_num" value="3" />
									    <jsp:param name="format" value="${query.QUERYCONDITION_DATEFORMAT}"/>
									    <jsp:param name="label_value" value="${query.QUERYCONDITION_LABEL}" />
									    <jsp:param name="comp_col_num" value="9" />
									    <jsp:param value="auth_type" name="write"/>
									    <jsp:param name="posttimefmt" value="${query.TIME_POSTFMT!''}" />
									    <jsp:param name="istime" value="${(query.QUERYCONDITION_ISTIME=='1')?string('true', 'false')}" />
									    <jsp:param name="allowblank" value="false" />
									    <jsp:param name="start_name" value="${query.QUERYCONDITION_CTRLNAME}_GE" />
									    <jsp:param name="end_name" value="${query.QUERYCONDITION_CTRLNAME}_LE" />
									</jsp:include>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='8' >
									<plattag:number name="${query.QUERYCONDITION_CTRLNAME}" auth_type="write"
									    step="${query.QUERYCONDITION_STEP}" decimals="${query.QUERYCONDITION_DECIMALS}"
									    max="${query.QUERYCONDITION_MAX!''}" min="${query.QUERYCONDITION_MIN!''}"
										label_value="${query.QUERYCONDITION_LABEL}" allowblank="true"
										placeholder="" label_col_num="3" comp_col_num="9"></plattag:number>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='9' >
								    <plattag:winselector placeholder="" allowblank="true" 
								     label_col_num="3" maxselect="${query.MAXSELECT!''}" minselect="${query.MINSELECT!''}"
								     <#if query.IS_TREESELECTOR=='true' >
								         <#if query.CHECKCASCADEN?? >checkcascaden="${query.CHECKCASCADEN?replace(',','')}"</#if>
								         <#if query.CHECKCASCADEY?? >checkcascadey="${query.CHECKCASCADEY?replace(',','')}"</#if>
								         checktype="${query.CHECKTYPE}"
								     </#if>
								     height="${query.WIN_HEIGHT!''}" comp_col_num="9" auth_type="write" label_value="${query.QUERYCONDITION_LABEL}"
								      width="${query.WIN_WIDTH!''}" selectorurl="${query.SELECTORURL!''}" title="${query.WIN_TITLE!''}" name="${query.QUERYCONDITION_CTRLNAME}"
								     >
								      </plattag:winselector>
								    </#if>
								</div>
								</#if>
							</#list>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('${CONTROL_ID}_search');"
									class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a
									onclick="PlatUtil.tableDoSearch('${CONTROL_ID}_datatable','${CONTROL_ID}_search',true);"
									class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	</#if>
	<#if operbtns??&&(operbtns?size>0) >
	<div class="toolbar">
	    <#list operbtns as btn>
	    <#if btn.TABLEBUTTON_UPLOADED=='false'>
	      <#if (btn_index<7) >
	      <button type="button" onclick="${btn.TABLEBUTTON_FN}();" platreskey="${btn.TABLEBUTTON_RESKEY!''}" 
	        <#if btn.TABLEBUTTON_DISABLED=='-1' >disabled="disabled"</#if> 
		    <#if btn.TABLEBUTTON_BTNID?? >id="${btn.TABLEBUTTON_BTNID}"</#if> 
			class="btn btn-outline btn-${btn.TABLEBUTTON_COLOR} btn-sm">
			<i class="${btn.TABLEBUTTON_ICON}"></i>&nbsp;${btn.TABLEBUTTON_NAME}
		  </button>
		  </#if>
		  <#if (btn_index==7) >
	       <div class="btn-group" id="OTHER_OPERDIV">
                 <button data-toggle="dropdown" class="btn btn-outline btn-info btn-sm dropdown-toggle">其它操作 <span class="caret"></span>
                 </button>
                 <ul class="dropdown-menu">
                     <#list operbtns as otherbtn>
	                     <#if (otherbtn_index>=7) >
	                     <li platreskey="${otherbtn.TABLEBUTTON_RESKEY!''}"
		                     <#if otherbtn.TABLEBUTTON_BTNID?? >id="${otherbtn.TABLEBUTTON_BTNID}"</#if> 
		                 >
	                     <a onclick="${otherbtn.TABLEBUTTON_FN}();" style="cursor: pointer;">${otherbtn.TABLEBUTTON_NAME}</a>
	                     </li>
	                     </#if>
                     </#list>
                 </ul>
            </div>
		  
		  </#if>
	    </#if>
	    <#if btn.TABLEBUTTON_UPLOADED=='true'>
          <span id="${btn.TABLEBUTTON_BTNID}" impfiletypes="${btn.TABLEBUTTON_IMPFILETYPES}" 
             platreskey="${btn.TABLEBUTTON_RESKEY!''}" impfilesize="${btn.TABLEBUTTON_IMPFILESIZE}"
			 imptableid="${btn.TABLEBUTTON_TABLEID}" impurl="${btn.TABLEBUTTON_IMPURL}"
			 class="btn btn-outline btn-${btn.TABLEBUTTON_COLOR} btn-sm platImpUploadFile"><i class="${btn.TABLEBUTTON_ICON}"></i>&nbsp;${btn.TABLEBUTTON_NAME}</span>
	    </#if>
		
		</#list>
	</div>
	</#if>
</div>

<div class="gridPanel">
	<table id="${CONTROL_ID}_datatable"></table>
</div>
<div id="${CONTROL_ID}_datatable_pager"></div>
<script type="text/javascript">
<#if operbtns??&&(operbtns?size>0) >
<#list operbtns as btn>
${btn.TABLEBUTTON_FNCONTENT!''}
</#list>
</#if>

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"${CONTROL_ID}_datatable",
		  searchPanelId:"${CONTROL_ID}_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=${FORMCONTROL_ID}",
		  <#if IS_PAGE=='-1' >
		  nopager:true,
		  rowNum : -1,
		  </#if>
		  <#if TABLE_HEIGHT?? >
		  height:${TABLE_HEIGHT},
		  </#if>
		  <#if ALLOW_DRAG=='1' >
		  sortable:true,
		  </#if>
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  ${JSDBCLICK_FN!''}
		  },
		  <#if GRIDCOMPLETE_FN?? >
		  gridComplete:function(){
			  ${GRIDCOMPLETE_FN!''}
	      },
		  </#if>
		  colModel: [
		      <#list columns as column>
		         {name: "${column.FIELD_NAME}",label:"${column.FIELD_COMMENT}",
		         width: ${column.FIELD_WIDTH},align:"left",
		         <#if column.FIELD_ISHIDE=='1' >hidden:true,</#if>
		         <#if column.FORMAT_FN??&&column.FORMAT_FN!='' >formatter:${column.FORMAT_FN},</#if>
		         <#if column.FIELD_DATAJOIN??&&column.FIELD_DATAJOIN=='1' >
		         cellattr: function(rowId, tv, rawObject, cm, rdata) { 
	                    return 'id=\'${column.FIELD_NAME}' + rowId + "\'"; 
	              } ,</#if>
		         <#if column.FIELD_SORT=='1' >sortable:true,index: "${column.FIELD_ORDERNAME}"</#if>
		         <#if column.FIELD_SORT=='-1' >sortable:false</#if>
		         }<#if column_index!=(columns?size-1) >,</#if>
		      </#list>
           ]
	 });
});
</script>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>