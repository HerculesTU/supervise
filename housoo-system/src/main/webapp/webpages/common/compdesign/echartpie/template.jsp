<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
<div class="titlePanel" <#if haveVisiableQuery=='-1' >style="display: none;"</#if> >
    <#if querys??&&(querys?size>0) >
	<div class="title-search form-horizontal" id="${FORMCONTROL_ID}search" formcontrolid="${FORMCONTROL_ID}">
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
							style="width: 300px; display: none;max-height: 280px;">
							<#list querys as query>
							    <#if query.QUERYCONDITION_CTRLTYPE!='1' >
								<div class="form-group">
								    <#if query.QUERYCONDITION_CTRLTYPE=='2' >
									<plattag:input name="${query.QUERYCONDITION_CTRLNAME}" auth_type="write"
										label_value="${query.QUERYCONDITION_LABEL}" maxlength="100" allowblank="true"
										placeholder="" label_col_num="4" comp_col_num="8"></plattag:input>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='3' >
								    <plattag:select placeholder="" istree="${query.IS_TREE}" label_col_num="4" 
								      label_value="${query.QUERYCONDITION_LABEL}" style="width:100%;"
								      allowblank="true" comp_col_num="8" auth_type="write" 
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
								    label_col_num="4" label_value="${query.QUERYCONDITION_LABEL}"
								    static_values="${query.QUERYCONDITION_STATICVALUES!''}" 
		                             dyna_interface="${query.QUERYCONDITION_DYNAINTERFACE!''}" dyna_param="${query.QUERYCONDITION_DYNAPARAM!''}"
								    select_first="${query.SELECT_FIRST!''}" allowblank="true" is_horizontal="true" comp_col_num="8">
								    </plattag:radio>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='5' >
								    <plattag:checkbox name="${query.QUERYCONDITION_CTRLNAME}" auth_type="write" 
								    label_col_num="4" label_value="${query.QUERYCONDITION_LABEL}"
								    static_values="${query.QUERYCONDITION_STATICVALUES!''}" 
		                             dyna_interface="${query.QUERYCONDITION_DYNAINTERFACE!''}" dyna_param="${query.QUERYCONDITION_DYNAPARAM!''}"
								     allowblank="true" is_horizontal="true" comp_col_num="8">
								    </plattag:checkbox>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='6' >
								    <plattag:datetime istime="${(query.QUERYCONDITION_ISTIME=='1')?string('true', 'false')}" placeholder="" 
								    label_col_num="4" label_value="${query.QUERYCONDITION_LABEL}"
								    allowblank="true" comp_col_num="8" auth_type="write" format="${query.QUERYCONDITION_DATEFORMAT}" name="${query.QUERYCONDITION_CTRLNAME}">
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
								    <#if query.QUERYCONDITION_CTRLTYPE=='8' >
									<plattag:number name="${query.QUERYCONDITION_CTRLNAME}" auth_type="write"
									    step="${query.QUERYCONDITION_STEP}" decimals="${query.QUERYCONDITION_DECIMALS}"
									    max="${query.QUERYCONDITION_MAX!''}" min="${query.QUERYCONDITION_MIN!''}"
										label_value="${query.QUERYCONDITION_LABEL}" allowblank="true"
										placeholder="" label_col_num="4" comp_col_num="8"></plattag:number>
								    </#if>
								    <#if query.QUERYCONDITION_CTRLTYPE=='9' >
								    <plattag:winselector placeholder="" allowblank="true" 
								     label_col_num="4" maxselect="${query.MAXSELECT!''}" minselect="${query.MINSELECT!''}"
								     <#if query.IS_TREESELECTOR=='true' >
								         <#if query.CHECKCASCADEN?? >checkcascaden="${query.CHECKCASCADEN?replace(',','')}"</#if>
								         <#if query.CHECKCASCADEY?? >checkcascadey="${query.CHECKCASCADEY?replace(',','')}"</#if>
								         checktype="${query.CHECKTYPE}"
								     </#if>
								     height="${query.WIN_HEIGHT!''}" comp_col_num="8" auth_type="write" label_value="${query.QUERYCONDITION_LABEL}"
								      width="${query.WIN_WIDTH!''}" selectorurl="${query.SELECTORURL!''}" title="${query.WIN_TITLE!''}" name="${query.QUERYCONDITION_CTRLNAME}"
								     >
								      </plattag:winselector>
								    </#if>
								</div>
								</#if>
							</#list>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('${FORMCONTROL_ID}search');"
									class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a
									onclick="PlatUtil.loadEchartPic('${FORMCONTROL_ID}');"
									class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	</#if>
</div>
<div style="height:${CONTROL_HEIGHT};" id="${FORMCONTROL_ID}">                    	
</div>
<script type="text/javascript">
$(function() {
	PlatUtil.loadEchartPic("${FORMCONTROL_ID}",{
		title : {
	        text: "${CHART_TITLE}",
	        <#if CHART_SUBTITLE?? >subtext: "${CHART_SUBTITLE!''}",</#if>
	        x:"center"
	    },
	    tooltip : {
	        trigger: "item",
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient: "vertical",
	        left: "left"
	    },
	    series : [
	        {
	            name: "${PLAT_COMPNAME}",
	            type: "pie",
	            radius : "55%",
	            center: ["50%", "60%"],
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }
	    ]
	});
});
</script>

<#if PLAT_DESIGNMODE?? >
</div>
</#if>
