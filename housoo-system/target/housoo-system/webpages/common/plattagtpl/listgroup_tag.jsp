<div class="panel-Title">
	<h5>${grouptitle}</h5>
</div>
<div
	style="position:absolute; top:32px; left:0px; right:0px; bottom:0px;">
	<ul class="list-group plat-listgroup slimscroll-enable" onclickfn="${onclickfn!''}" >
		<#list groupList as group>
		   <li class="list-group-item" onclick="PlatUtil.onListGroupClick(this,'${group.VALUE}');" >
		      <#if group.GROUP_NUM?? >
			  <span class="badge ${group.NUM_COLOR?default("badge-primary")}"
			  >${group.GROUP_NUM}</span> 
			  </#if>
			  <#if group.GROUP_FONT?? >
			  <i class="${group.GROUP_FONT}"></i>
			  </#if>
			  &nbsp;${group.LABEL}
		   </li>
	    </#list>
	</ul>
</div>
