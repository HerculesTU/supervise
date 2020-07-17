<#list groupList as group>
   <li class="list-group-item" onclick="PlatUtil.onListGroupClick(this,'${group.VALUE}');" >
      <#if group.VALUE!='0' >
	  <span class="badge badge-primary" onclickfn="${editclickfn!''}"
	  onclick="PlatUtil.onListGroupEditClick(this,'${group.VALUE}');"
	  >编辑</span> 
	  <span class="badge badge-danger" onclickfn="${delclickfn!''}"
	  onclick="PlatUtil.onListGroupDelClick(this,'${group.VALUE}');"
	  >删除</span>
	   </#if>
	  &nbsp;${group.LABEL}
	 
   </li>
   </#list>

