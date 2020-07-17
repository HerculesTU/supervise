<div id="${id}" class="platpicslide" style="height: ${height}px;">
	<div class="hd" style="height: ${height}px;">
		<ul>
		    <#list dataList as data>
			<li><img src="${data.IMGSRC}" /></li>
			</#list>
		</ul>
	</div>
	<div class="bd" style="height: ${height}px;">
		<ul>
		    <#list dataList as data>
			<li style="height: ${height}px;" >
				<div class="bg"></div>
				<div class="pic" style="height: ${height}px;" >
				<a href="javascript:void(0);" onclick="${onclickfn}('${data.IMGVALUE}');">
				<img src="${data.IMGSRC}" style="height: ${height}px;" /></a>
				</div>
				<div class="title"><a href="javascript:void(0);" title="${data.IMGTITLE}">${data.IMGTITLE}</a></div>
			</li>
			</#list>
		</ul>
	</div>
</div>
