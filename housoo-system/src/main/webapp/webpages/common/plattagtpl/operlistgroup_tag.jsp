<div class="panel-Title">
	<h5>${grouptitle}
	<button type="button" onclick="${addclickfn!''}()" style="position: absolute;right: 5px;"
	class="btn btn-outline btn-primary btn-xs" platreskey="${addclickfn!''}" ><i class="fa fa-plus"></i>
	&nbsp;新增</button>
	</h5>
</div>
<div
	style="position:absolute; top:32px; left:0px; right:0px; bottom:0px;">
	<ul class="list-group plat-listgroup slimscroll-enable" onclickfn="${onclickfn!''}" id="${id!''}" 
	   dyna_interface="${dyna_interface}" editclickfn="${editclickfn}" delclickfn="${delclickfn}"
	>
		<#list groupList as group>
		   <li class="list-group-item" onclick="PlatUtil.onListGroupClick(this,'${group.VALUE}');" >
		      <#if group.VALUE!='0' >
		      <span class="listgroup-operbtn">
			  <span class="badge badge-primary" onclickfn="${editclickfn!''}"
			  platreskey="${editclickfn!''}"
			  onclick="PlatUtil.onListGroupEditClick(this,'${group.VALUE}');"
			  >编辑</span> 
			  <span class="badge badge-danger" onclickfn="${delclickfn!''}"
			  platreskey="${delclickfn!''}"
			  onclick="PlatUtil.onListGroupDelClick(this,'${group.VALUE}');"
			  >删除</span>
			  </span>
			   </#if>
			  &nbsp;${group.LABEL}
			 
		   </li>
	    </#list>
	</ul>
</div>

