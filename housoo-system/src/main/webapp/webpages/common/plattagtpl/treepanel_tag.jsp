<#if panel_title?? >
<div class="panel-Title">
	<h5>
		${panel_title}
		<#if (!opertip??)||(opertip=="true") >
		<i class="fa fa-exclamation-circle treepaneltip"
			style="position: absolute;right: 10px;color:green;"></i>
		</#if>
	</h5>
</div>
</#if>
<div class="tree-search">
	<div class="input-group">
		<input type="text" class="form-control" name="${id}QueryParam"
			maxlength="100" placeholder="请输入查询条件"> <span
			class="input-group-btn">
			<button type="button" class="btn btn-primary"
				onclick="PlatUtil.searchZtree('${id}');">搜索</button>
		</span>
	</div>
</div>
<div class="tree-div" style="top:${panel_title?default("36px")}">
	<ul id="${id}" class="ztree">
</div>
<#if rightmenus?? && (rightmenus?size > 0) >
<div id="ztreeRightMenu">
	<ul class="dropdown-menu">
	    <#list rightmenus as menu>
		<li onclick="${menu.fnName}();" platreskey="${menu.platreskey!''}"><a><i class="fa ${menu.iconName}"></i>&nbsp;${menu.menuName}</a></li>
		</#list>
	</ul>
</div>
</#if>
<div class="tree-bottom-button">
	<table>
		<tr>
			<td><input type="checkbox" name="${id}TreeOpenFoldSwitch"
				data-on-color="success" data-off-color="success"
				data-label-width="0" data-size="small" data-on-text="展开"
				data-off-text="折叠"></td>
		</tr>
	</table>
</div>
