<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="可编辑表格" compcode="${FORMCONTROL_COMPCODE}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
   compcontrolid="${FORMCONTROL_ID}">
</#if>
<div class="titlePanel" 
   <#if !(operbtns??&&(operbtns?size>0)) >style="display: none;"</#if>
>
    <#if querys??&&(querys?size>0) >
	<div class="title-search form-horizontal" id="${CONTROL_ID}_search">
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
								    <plattag:select placeholder="" istree="false" label_col_num="3" 
								      label_value="${query.QUERYCONDITION_LABEL}" style="width:100%;"
								      allowblank="true" comp_col_num="9" auth_type="write"
								      static_values="${query.QUERYCONDITION_STATICVALUES!''}" 
		                              dyna_interface="${query.QUERYCONDITION_DYNAINTERFACE!''}" dyna_param="${query.QUERYCONDITION_DYNAPARAM!''}"
								       name="${query.QUERYCONDITION_CTRLNAME}">
								    </plattag:select>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='4' >
								    <plattag:radio name="${query.QUERYCONDITION_CTRLNAME}" auth_type="write" 
								    label_col_num="3" label_value="${query.QUERYCONDITION_LABEL}"
								    static_values="${query.QUERYCONDITION_STATICVALUES!''}" 
		                             dyna_interface="${query.QUERYCONDITION_DYNAINTERFACE!''}" dyna_param="${query.QUERYCONDITION_DYNAPARAM!''}"
								    select_first="false" allowblank="true" is_horizontal="true" comp_col_num="9">
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
									    <jsp:param name="istime" value="${(query.QUERYCONDITION_ISTIME=='1')?string('true', 'false')}" />
									    <jsp:param name="allowblank" value="false" />
									    <jsp:param name="start_name" value="${query.QUERYCONDITION_CTRLNAME}_GE" />
									    <jsp:param name="end_name" value="${query.QUERYCONDITION_CTRLNAME}_LE" />
									</jsp:include>
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
		<button type="button" onclick="${btn.TABLEBUTTON_FN}();" id="${btn.TABLEBUTTON_RESKEY!''}"
			class="btn btn-outline btn-${btn.TABLEBUTTON_COLOR} btn-sm">
			<i class="${btn.TABLEBUTTON_ICON}"></i>&nbsp;${btn.TABLEBUTTON_NAME}
		</button>
		</#list>
	</div>
	</#if>
</div>

<div class="gridPanel">
	<plattag:edittable dyna_interface="${JAVA_INTERCODE}"
		tr_tplpath="background/genui/${DESIGN_CODE}_sub/${CONTROL_ID}" 
		<#if ALLOW_DRAG=='1' >dragable="true"</#if>
		id="${CONTROL_ID}" searchpanel_id="${CONTROL_ID}_search"
		col_style="${COL_STYLE}">
	</plattag:edittable>
</div>

<script type="text/javascript">
<#if operbtns??&&(operbtns?size>0) >
<#list operbtns as btn>
${btn.TABLEBUTTON_FNCONTENT}
</#list>
</#if>

<#list tableCols as col>
<#if col.TABLECOL_COMPTYPE=='button' >
${col.TABLECOL_BTNFNCONTENT}
</#if>
</#list>
</script>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>