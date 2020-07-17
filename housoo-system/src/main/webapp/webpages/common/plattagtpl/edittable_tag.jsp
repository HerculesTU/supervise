<table id="${id}" class="table table-bordered table-hover platedittable" dyna_interface="${dyna_interface}"
   tr_tplpath="${tr_tplpath}" 
   <#if style?? >style="${style}"</#if>  
   <#if dragable?? >dragable="${dragable}"</#if>
   <#if searchpanel_id?? >searchpanel_id="${searchpanel_id}"</#if>
>
	<thead>
		<tr class="active">
			<th style="width: 30px;"></th>
			<th style="width: 10px;"><input type="checkbox"
				onchange="PlatUtil.selectAllEditTable(this,'${id}');"
				name="AllSelectCheckBox"></th>
			<#list cols as col>
			<th style="width: ${col.width};">${col.name}</th>
			</#list>
		</tr>
	</thead>
	<tbody id="${id}Tbody">
	</tbody>
</table>
