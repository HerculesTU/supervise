<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'dbmanager_view.jsp' starting page</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,plat-ui,tipsy,autocomplete,nicevalid">
	</plattag:resources>
	<link rel="stylesheet" type="text/css" href="plug-in/webuploader-0.1.5/css/webuploader.css">
	<link rel="stylesheet" href="plug-in/Jcrop-0.9.12/css/jquery.Jcrop.css">
	<link rel="stylesheet" href="plug-in/ion.rangeSlider-2.1.6/css/ion.rangeSlider.css">
	<link rel="stylesheet" href="plug-in/ion.rangeSlider-2.1.6/css/ion.rangeSlider.skinNice.css">
	<link rel="stylesheet" href="plug-in/jquery-colpick-1.0.1/css/colpick.css">
	<style type="text/css">
	.ImgCutOption{
		clear: both;
    	overflow: hidden;
    	padding: 10px 10px 8px 10px;
    	border-bottom: 1px solid #d1d9e0;
	}
	.ImgCutOption .options {
	    font-weight: normal;
	    padding-top: 8px;
	}
	.ImgCutOption .options .button-flat{
		text-decoration: none;
	}
	.ImgCutOption .options .button-flat:hover {
    	border-radius: 4px;
    	color: #08d;
    	border: 1px solid #08d;
	}
	.imageJcropContainer{
		padding: 3px;
	}
	.nav-tabs > li > a {
	    color: #a7b1c2;
	    font-weight: 600;
	    padding: 10px 20px 10px 25px;
	}
	.nav-tabs > li > a:focus, .nav-tabs > li > a:hover {
	    background-color: #e6e6e6;
	    color: #676a6c;
	}
	.ui-layout-pane{
		border-top: 0px !important;
	}
	:focus{
		outline:#1AB394 auto 1px;
	}
	.TextBox, .warterImg {
	    background: rgba(0, 0, 0, 0) none repeat scroll 0 0;
	    border: medium none;
	    color: #000000;
	    left: 20px;
	    position: absolute;
	    text-align: left;
	    top: 20px;
  	}
  	.colorSelect{
    	width: 28px;
    	cursor:pointer;
    	height: 28px;
    	background: url(webpages/common/compui/select2.png) center;
    	background-color: #336699;
  	}
  	.textOptions td{
  		padding: 2px;
  	}
  	.webuploader-pick {
	    background: #fff none repeat scroll 0 0;
	    border: 1px solid #ccc;
	    border-radius: 4px;
	    color: #333;
	    cursor: pointer;
	    display: inline-block;
	    overflow: hidden;
	    position: relative;
	    text-align: center;
	    padding: 5px 15px;
	}
	</style>
  </head>
  
  <body>
  		<input type="hidden" value="${imageUrl}" id="resultImageUrl"/>
  		<div class="plat-directlayout " style="height: 100%;" layoutsize="&quot;west__size&quot;:230,&quot;north__size&quot;:40" >
  			<div class="ui-layout-north" style="padding-top: 5px;">
				<ul class="nav nav-tabs">
				  <li  class="active" id="nav-tabs1"><a onfocus="this.blur();" href="javascript:void(0);" onclick="changeTabs('1');"><i class="fa fa-scissors"></i>&nbsp;裁剪&缩略</a></li>
				  <li id="nav-tabs2"><a onfocus="this.blur();" href="javascript:void(0);" onclick="changeTabs('2');"><i class="fa fa-refresh"></i>&nbsp;图片旋转</a></li>
				  <li id="nav-tabs3"><a onfocus="this.blur();" href="javascript:void(0);" onclick="changeTabs('3');"><i class="fa fa-picture-o"></i>&nbsp;图片水印</a></li>
				  <li id="nav-tabs4"><a onfocus="this.blur();" href="javascript:void(0);" onclick="changeTabs('4');"><i class="fa fa-pencil"></i>&nbsp;文字水印</a></li>
				</ul>
  			</div>
  			<div class="ui-layout-west">
  				<div id="toolDiv1">
	  				<div class="ImgCutOption">
	  					<strong>裁剪：</strong>
						<div class="options" id="imgForm">
							<table width="100%" cellpadding="0" border="0" height="100%">
								<tr>
									<td>宽度 <plattag:input name="Imgwidth" id="Imgwidth" style="width:30px;height:25px;display: inline;padding: 0px;" auth_type="write" allowblank="true" placeholder="" comp_col_num="0" datarule="integer[+0]"></plattag:input>&nbsp;px</td>
									<td>高度<plattag:input name="Imgheight" id="Imgheight" style="width:30px;height:25px;display: inline;padding: 0px;" auth_type="write" allowblank="true" placeholder="" comp_col_num="0" datarule="integer[+0]"></plattag:input>&nbsp;px</td>
								</tr>
								<tr>
									<td colspan="2">
										<plattag:checkbox name="isLockWH" auth_type="write" static_values="锁定宽高:1" allowblank="true" is_horizontal="false" comp_col_num="0"></plattag:checkbox>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										预设比例:<plattag:checkbox style="width:100px;display: inline;" name="ischeck" auth_type="write" static_values="约束比例:1" allowblank="true" is_horizontal="true" comp_col_num="0"></plattag:checkbox>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<plattag:radio name="bi" auth_type="readonly" static_values="1比1:1,1比2:2,2比1:3" select_first="true" allowblank="false" is_horizontal="true" comp_col_num="0"></plattag:radio>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<plattag:radio name="bi" auth_type="readonly" static_values="4比3:4,3比4:5,16比9:6" select_first="false" allowblank="false" is_horizontal="true" comp_col_num="0"></plattag:radio>
									</td>
								</tr>
							</table>
						</div>
	  				</div>
	  				<div class="ImgCutOption">
	  					<strong>缩略：</strong>
						(
						<label id="scaleImg" style="font-weight:100"></label>
						<label id="scale" style="padding-left:5px;font-weight:100">100%</label>
						) 
						<div class="options" style="text-align: center;">
							<button type="button" onclick="resetImgRand();" class="btn btn-outline btn-primary btn-sm" ><i class="fa fa-picture-o "></i>&nbsp;实际大小</button>
						</div>
						<div class="options">
							<table width="100%" cellpadding="0" border="0" height="100%">
								<tr>
									<td style="width: 10px;cursor: pointer;"><i class="fa fa-minus-circle" onclick="minusRang();"></i></td>
									<td><input type="text" id="rangeName" name="rangeName" value="" /></td>
									<td style="width: 10px;cursor: pointer;"><i class="fa fa-plus-circle" onclick="addRang();"></i></td>
								</tr>
							</table>
							
						</div>
	  				</div>
	  				<div class="ImgCutOption">
	  					<strong>预设尺寸：</strong>
						<div class="options">
							<table width="100%" cellpadding="0" border="0" height="100%">
								<tr>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(60,60);">60x60</a></td>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(60,80);">60x80</a></td>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(80,80);">80x80</a></td>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(136,96);">136x96</a></td>
								</tr>
								<tr>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(200,100);">200x100</a></td>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(310,230);">310x230</a></td>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(360,270);">360x270</a></td>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(450,250);">450x250</a></td>
								</tr>
								<tr>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(480,270);">480x270</a></td>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(500,300);">500x300</a></td>
									<td><a class="button-flat" href="javascript:void(0);" onclick="fixImgSize(640,360);">640x360</a></td>
									<td></td>
								</tr>
							</table>
						</div>
	  				</div>
	  				<div style="padding-top: 10px;text-align: center;">
	  					<button type="button" onclick="saveCutImg();" class="btn btn-outline btn-primary btn-sm" ><i class="fa fa-floppy-o"></i>&nbsp;应用</button>
	  					<button type="button" onclick="resetCutImg();" class="btn btn-outline btn-danger btn-sm"><i class="fa fa-repeat"></i>&nbsp;取消</button>
	  				</div>
  				</div>
  				<div id="toolDiv2" style="display: none;">
  					<input type="hidden" value="0" id="rotNum"/>
  					<div class="ImgCutOption">
	  					<strong>旋转方式：</strong>
						<div class="options" style="text-align: center;">
							<button type="button" onclick="rotLeft();" class="btn btn-outline btn-primary btn-sm" ><i class="fa fa-undo"></i>&nbsp;左旋</button>
						</div>
						<div class="options" style="text-align: center;">
							<button type="button" onclick="rotRight();" class="btn btn-outline btn-primary btn-sm" ><i class="fa fa-repeat"></i>&nbsp;右旋</button>
						</div>
	  				</div>
  					<div style="padding-top: 10px;text-align: center;">
	  					<button type="button" onclick="saveRotImg();" class="btn btn-outline btn-primary btn-sm" ><i class="fa fa-floppy-o"></i>&nbsp;应用</button>
	  					<button type="button" onclick="resetRotImg();" class="btn btn-outline btn-danger btn-sm"><i class="fa fa-repeat"></i>&nbsp;取消</button>
	  				</div>
  				</div>
  				<div id="toolDiv3" style="display: none;">
  					<div class="ImgCutOption">
  						<div class="options textOptions">
			              <table style=" width:100%; table-layout:fixed;" cellspacing="0" cellpadding="0" border="0">
			                
			                <tr>
			                  <td width="70" align="right">
			                     <strong>水印图片：</strong>
			                  </td>
			                  <td>
			                     <span id="picker"><i class="fa fa-cloud-upload"></i>&nbsp;上传</span>
			                  </td>
			                </tr>  
			                <tr>
			                  <td width="70" align="right">透明度：</td>
			                  <td>
			                  	<plattag:select placeholder="" istree="false" allowblank="true" comp_col_num="0" auth_type="write" 
			                  	static_values="1:1,0.9:0.9,0.8:0.8,0.7:0.7,0.6:0.6,0.5:0.5,0.4:0.4,0.3:0.3,0.2:0.2,0.1:0.1"
			                  	id="waterimg_transportant" name="waterimg_transportant" value="1"></plattag:select>
			                  </td>
			                </tr>   
			              </table>
			            </div>
  					</div>
  					<div style="padding-top: 10px;text-align: center;">
	  					<button type="button" onclick="saveWaterImage();" class="btn btn-outline btn-primary btn-sm" ><i class="fa fa-floppy-o"></i>&nbsp;应用</button>
	  					<button type="button" onclick="cancelImage();" class="btn btn-outline btn-danger btn-sm"><i class="fa fa-repeat"></i>&nbsp;取消</button>
	  				</div>
  				</div>
  				<div id="toolDiv4" style="display: none;">
  					<div class="ImgCutOption">
	  					<strong>文字设置：</strong>
						<div class="options textOptions">
			              <table style=" width:100%; table-layout:fixed;" cellspacing="0" cellpadding="0" border="0">
			                <tr>
			                  <td width="60" align="right">字体：</td>
			                  <td>
			                  	<plattag:select placeholder="" istree="false" allowblank="true" comp_col_num="0" auth_type="write" 
			                  	static_values="宋体:宋体,雅黑:微软雅黑,黑体:黑体,Serif:Serif"
			                  	id="font_outer" name="font_outer" value="宋体"></plattag:select>
			                  </td>
			                </tr>
			                <tr>
			                  <td align="right">颜色：</td>
			                  <td height="28">
			                  		<div id="textColorBox" class="colorSelect"></div>
			                  		<input type="hidden" name="colorValue" id="colorValue" value="">
			                   </td>
			                </tr>
			                <tr>
			                  <td width="60" align="right">字号：</td>
			                  <td>
			                  	<plattag:select placeholder="" istree="false" allowblank="true" comp_col_num="0" auth_type="write" 
			                  	static_values="12px:12,14px:14,18px:18,24px:24,26px:26,28px:28,32px:32,36px:36,48px:48,54px:54,72px:72"
			                  	id="fontsize_outer" name="fontsize_outer" value="14"></plattag:select>
			                  </td>
			                </tr>
			                <tr>
			                  <td width="60" align="right">透明度：</td>
			                  <td>
			                  	<plattag:select placeholder="" istree="false" allowblank="true" comp_col_num="0" auth_type="write" 
			                  	static_values="1:1,0.9:0.9,0.8:0.8,0.7:0.7,0.6:0.6,0.5:0.5,0.4:0.4,0.3:0.3,0.2:0.2,0.1:0.1"
			                  	id="transportant_outer" name="transportant_outer" value="1"></plattag:select>
			                  </td>
			                </tr>
			                 <tr>
			                  <td colspan="2" style="padding-left: 71px;">
			                     <plattag:checkbox name="bold"  auth_type="write" static_values="加粗:1"  allowblank="false" is_horizontal="true" comp_col_num="0"></plattag:checkbox>
			                  </td>
			                </tr>
			                <tr>
			                  <td colspan="2" style="padding-left: 71px;">
			                     <plattag:checkbox name="Italic"  auth_type="write" static_values="斜体:1"  allowblank="false" is_horizontal="true" comp_col_num="0"></plattag:checkbox>
			                  </td>
			                </tr>
			                <tr>
			                  <td colspan="2" style="padding-left: 71px;">
			                     <plattag:checkbox name="underline"  auth_type="write" static_values="下划线:1"  allowblank="false" is_horizontal="true" comp_col_num="0"></plattag:checkbox>
			                  </td>
			                </tr>  
			                <%-- <tr>
			                  <td colspan="2">
			                     <strong>描边设置：</strong><plattag:checkbox name="Outline" style="width:100px;display: inline;" auth_type="write" static_values="描边:1"  allowblank="false" is_horizontal="true" comp_col_num="0"></plattag:checkbox>
			                  </td>
			                </tr>  
			                <tr>
			                  <td width="60" align="right">大小：</td>
			                  <td>
			                  	<plattag:select placeholder="" istree="false" allowblank="true" comp_col_num="0" auth_type="write" 
			                  	static_values="1px:1,2px:2,3px:3"
			                  	id="lineWeight_outer" name="lineWeight_outer" value="1"></plattag:select>
			                  </td>
			                </tr>
			                <tr>
			                  <td align="right">颜色：</td>
			                  <td height="28">
			                  		<div id="lineColorBox" class="colorSelect"></div>
			                  		<input type="hidden" name="linecolorValue" id="linecolorValue" value="">
			                   </td>
			                </tr>    --%> 
			              </table>
			            </div>
	  				</div>
  					<div style="padding-top: 10px;text-align: center;">
	  					<button type="button" onclick="saveText();" class="btn btn-outline btn-primary btn-sm" ><i class="fa fa-floppy-o"></i>&nbsp;应用</button>
	  					<button type="button" onclick="cancelText();" class="btn btn-outline btn-danger btn-sm"><i class="fa fa-repeat"></i>&nbsp;取消</button>
	  				</div>
  				</div>
  			</div>
  			<div class="ui-layout-center" style="overflow: auto;">
  				<table style="table-layout: fixed;" width="100%" cellpadding="0" border="0" height="100%">
  					<tr>
  						<td valign="middle" align="center">
  							<div class="imageJcropContainer" id="imageJcropContainer">
			  					<img id="JcropImageElement" src="${imageUrl}" >
			  				</div>
  						</td>
  					</tr>
  				</table>
  			</div>
  			<div class="ui-layout-south">
		   		<div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
                   <div class="col-sm-12 text-right">
                       <button class="btn btn-outline btn-primary btn-sm" onclick="PlatUtil.closeWindow();" type="button" ><i class="fa fa-check"></i>确定</button>
                   </div>
           		</div>
	  		</div>
  		</div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,ztree,jquery-layout,bootswitch,plat-util,tipsy,autocomplete,pinyin,nicevalid">
</plattag:resources>
<!--引入JS-->
<script type="text/javascript" src="plug-in/webuploader-0.1.5/dist/webuploader.js"></script>
<script type="text/javascript" src="plug-in/Jcrop-0.9.12/js/jquery.Jcrop.js"></script>
<script type="text/javascript" src="plug-in/ion.rangeSlider-2.1.6/js/ion.rangeSlider.min.js"></script>
<script type="text/javascript" src="plug-in/jqueryRotate-2.3.0/jQueryRotate.js"></script>
<script type="text/javascript" src="plug-in/jquery-colpick-1.0.1/js/colpick.js"></script>
<script  type="text/javascript">
var cutChange = false;
var textChange = false;
var waterChange = false;
var tipCutting = "是否应用裁剪和缩略？";
var tipPressImage = "是否应用图片水印？";
var tipPressText = "是否应用文字水印？";
var tipRotText = "是否应用图片旋转？";
var tabnum = 1;
var jcropApi;
var imgWidth; 
var imgHeight; 
var fontText = {
		height: '',
		font: '',
		size: '',
		fcolor: '',
		transparency: '',
		fbold: '',
		Weight: '',
		Italic: '',
		underline: '',
		text: '',
		top: '',
		left: '',
		fliterCon:'none',
		textshadowCon:'none'
};
var _move = false;
var imgEditState = {
		text: {
			text: '',
			height: '26',
			fontSize: '14',
			left: 20,
			top: 20,
			color: '#336699',
			fontFamily: '宋体',
			fontWeight: '',
			textdecoration: 'none',
			fontstyle: 'normal',
			opicty: '1',
			fliterCon:'none',
			textshadowCon:'none'
		}
	};
$(function(){
	$("#JcropImageElement").Jcrop({
		 bgColor:"#fff",
		 bgOpacity:0.4,
		 onChange:showCoords,
		 onSelect:showCoords
	}, function() {
		  jcropApi = this;
	});
	PlatUtil.initDirectLayout();
	$("#imgForm").validator({
	 	timely:1,
	 	focusCleanup:true
	});
	$("input[name='isLockWH']").click(function(){
		if ($(this).is(":checked")) {
			$("#Imgwidth").attr("disabled", "disabled");
			$("#Imgheight").attr("disabled", "disabled");
			$("#JcropImageElement").Jcrop({allowResize:false});
		} else {
			$("#Imgwidth").removeAttr("disabled");
			$("#Imgheight").removeAttr("disabled");
			$("#JcropImageElement").Jcrop({allowResize:true});
		}
	});
	$("input[name='ischeck']").click(function(){
		if ($(this).is(":checked")) {
			$("input[name='bi']").removeAttr("disabled");
			setBiFunction();
		} else {
			$("input[name='bi']").attr("disabled", "disabled");
			$("#JcropImageElement").Jcrop({aspectRatio:0});
		}
	});
	$("input[name='bi']").click(function(){
		setBiFunction();
	});
	
	$("#Imgwidth,#Imgheight").keyup(function() {
		setSelectblock();
	});
	
	$("#rangeName").ionRangeSlider({
	    min: 1,                        
	    max: 100,    
	    from:100,  
	    to:100,
	    type: "single",                 
	    step: 1,  
	    postfix:"%",
	    grid:true,
	    onChange: function(obj){        
	      changeImgRang(obj.from);
	    }
	});
	var img = new Image();
	img.src = "${imageUrl}";
	imgWidth = img.width;
	imgHeight = img.height;
	$("#scaleImg").text(imgWidth + "×" + imgHeight);
	$("#JcropImageElement").width(imgWidth+"px");
	$("#JcropImageElement").height(imgHeight+"px");
});


//头部切换
function changeTabs(index){
	$(".nav-tabs").find("li").each(function(){
		$(this).removeClass("active");
	});
	 $("div[id^='toolDiv']").each(function(){
		$(this).attr("style","display:none;");
	});
	$("#nav-tabs"+index).attr("class","active");
	$("#toolDiv"+index).removeAttr("style");
	if(index=='1'){
		if(tabnum==1){
			return;
		}else{
			openConfirm(index,tabnum);
			tabnum = 1;
		}
	}else if(index=='2'){
		if(tabnum==2){
			return;
		}else{
			openConfirm(index,tabnum);
			tabnum = 2;
		}
	}else if(index=='3'){
		if(tabnum==3){
			return;
		}else{
			openConfirm(index,tabnum);
			tabnum = 3;
		}
	}else if(index=='4'){
		if(tabnum==4){
			return;
		}else{
			openConfirm(index,tabnum);
			tabnum = 4;
		}
	}
}

function openConfirm(index,tabnum){
	if(tabnum==1){
		if(cutChange){
			parent.layer.confirm(tipCutting, {
				  btn: ['应用','取消'] 
				}, function(){
					saveCutImg(index);
				}, function(){
					setTabFunction(index);
				});
		}else{
			setTabFunction(index);
		}
		
	}else if(tabnum==2){
		var rotNum = parseInt($("#rotNum").val());
		if(rotNum>0||rotNum<0){
			parent.layer.confirm(tipRotText, {
				  btn: ['应用','取消'] 
				}, function(){
					saveRotImg(index);
				}, function(){
					setTabFunction(index);
				});
		}else{
			setTabFunction(index);
		}
		
	}else if(tabnum==3){
		if(waterChange){
			parent.layer.confirm(tipPressImage, {
				  btn: ['应用','取消'] 
				}, function(){
					saveWaterImage(index);
				}, function(){
					setTabFunction(index);
				});
		}else{
			setTabFunction(index);
		}
		
	}else if(tabnum==4){
		if(textChange){
			parent.layer.confirm(tipPressText, {
				  btn: ['应用','取消'] 
				}, function(){
					saveText(index);
				}, function(){
					setTabFunction(index);
				});
		}else{
			setTabFunction(index);
		}
		
	}
}

function setTabFunction(index){
	if(index=='1'){
		resetRotImg();
		resetText();
		cancelImage();
		$("#JcropImageElement").Jcrop({
			 bgColor:"#fff",
			 bgOpacity:0.4,
			 onChange:showCoords,
			 onSelect:showCoords
		}, function() {
			  jcropApi = this;
		});
		imgWidth = $("#JcropImageElement").width();
 		imgHeight = $("#JcropImageElement").height();
 		$("#scaleImg").text(imgWidth + "×" + imgHeight);
 		$("#scale").text("100%");
 		var slider = $("#rangeName").data("ionRangeSlider");
 		slider.update({
 		    from: 100,
 		    to:100
 		});
 		$("#Imgwidth").val("");
 		$("#Imgheight").val("");
	}else if(index=='2'){
		resetText();
		cancelImage();
		jcropApi.destroy();
	}else if(index=='3'){
		resetText();
		resetRotImg();
		resetUploadWaterImg();
		setUploadWaterImg();
		jcropApi.destroy();
	}else if(index=='4'){
		jcropApi.destroy();
		resetRotImg();
		cancelImage();
		$("#JcropImageElement").css("cursor", "text");
		docClick();
	}
}
//显示宽高
function showCoords(c){
    $("#Imgwidth").val(c.w);
    $("#Imgheight").val(c.h);
    cutChange = true;
}
//设置比例
function setBiFunction(){
	if($("input[name='ischeck']").is(":checked")){
		var val=$("input:radio[name='bi']:checked").val();
		var bi = 0;
		if(val==1){
			bi = 1/1;
		}else if(val==2){
			bi = 1/2;
		}else if(val==3){
			bi = 2/1;
		}else if(val==4){
			bi = 4/3;
		}else if(val==5){
			bi = 3/4;
		}else if(val==6){
			bi = 16/9;
		}
		$("#JcropImageElement").Jcrop({aspectRatio:val});
	}
}
//设置选框大小
function setSelectblock(){
	var selectW = $("#Imgwidth").val();
	var selectH = $("#Imgheight").val();
	var imgW = $("#JcropImageElement").width();
	var imgH = $("#JcropImageElement").height();
	var scaledObject = jcropApi.tellScaled();
	var newX = 0;
	var newY = 0;
	if(scaledObject.x+selectW>imgW){
		newX = imgW;
		$("#Imgwidth").val(parseInt(imgW));
	}else{
		newX = scaledObject.x+selectW;
	}
	if(scaledObject.y+selectH>imgH){
		newY = imgH;
		$("#Imgheight").val(parseInt(imgH));
	}else{
		newY = scaledObject.y+selectH;
	}
	jcropApi.setSelect([scaledObject.x,scaledObject.y,newX,newY]);
}
//增加缩放
function addRang(){
	var rangeVal = $("#rangeName").val();
	var slider = $("#rangeName").data("ionRangeSlider");
	if(parseInt(rangeVal)<100){
		slider.update({
		    from: parseInt(rangeVal)+1,
		    to:parseInt(rangeVal)+1
		});
		changeImgRang(parseInt(rangeVal)+1);
	}
}
//减小缩放
function minusRang(){
	var rangeVal = $("#rangeName").val();
	var slider = $("#rangeName").data("ionRangeSlider");
	if(parseInt(rangeVal)>1){
		slider.update({
		    from: parseInt(rangeVal)-1,
		    to:parseInt(rangeVal)-1
		});
		changeImgRang(parseInt(rangeVal)-1);
	}
}
//改变缩放
function changeImgRang(percent){
	var v = (Math.round(percent)) / 100;
	$("#JcropImageElement").attr("style","width:"+Math.round(imgWidth * v )+"px");
	var imgW = $("#JcropImageElement").width();
	var imgH =  $("#JcropImageElement").height();
	refreshJcrop();
	$("#scaleImg").text($("#JcropImageElement").width() + "×" + $("#JcropImageElement").height());
	$("#scale").text(percent+"%");
}
//重置缩放
function resetImgRand(){
	$("#JcropImageElement").width(imgWidth);
	$("#JcropImageElement").height(imgHeight);
	$("#scaleImg").text(imgWidth + "×" + imgHeight);
	$("#scale").text("100%");
	var slider = $("#rangeName").data("ionRangeSlider");
	slider.update({
	    from: 100,
	    to:100
	});
	refreshJcrop();
}
//预设尺寸
function fixImgSize(fixW,fixH){
	if(fixW>imgWidth || fixH > imgHeight){
		return;//如果图片现有尺寸就小于缩放目标尺寸则不处理
	}
	var nRatio=fixW/fixH;
	var imgRatio=imgWidth/imgHeight;
	var v,x=0,y=0;
	if(nRatio>imgRatio){
		$("#JcropImageElement").attr("style","width:"+fixW+ "px");
		$("#JcropImageElement").attr("style","height:"+Math.round(fixH/imgRatio)+ "px");
		v=fixW/imgWidth;
	}else{
		$("#JcropImageElement").attr("style","height:"+fixH+ "px");
		$("#JcropImageElement").attr("style","width:"+Math.round(fixH*imgRatio)+ "px");
		v=fixH/imgHeight;
	}
	refreshJcrop();
	$("#scaleImg").text($("#JcropImageElement").width() + "×" + $("#JcropImageElement").height());
	$("#scale").text(Math.round(v*100)+"%");
}
//重新绘画裁剪框
function refreshJcrop(){
	var tellSelect = jcropApi.tellSelect();
	jcropApi.destroy();
	$("#JcropImageElement").Jcrop({
		 bgColor:"#fff",
		 bgOpacity:0.4,
		 onChange:showCoords,
		 onSelect:showCoords
	}, function() {
		  jcropApi = this;
	});
	jcropApi.setSelect([tellSelect.x, tellSelect.y, tellSelect.x2, tellSelect.y2]);
	jcropApi.setOptions({
		maxSize: [$("#JcropImageElement").width(),$("#JcropImageElement").height()]
	});
}
//裁剪重置
function resetCutImg(){
	$("[name='Imgwidth']").val(0);
	$("[name='Imgheight']").val(0);
	 resetImgRand();
	 cutChange = false;
}
//保存裁剪
function saveCutImg(index){
	$("#imgForm").trigger("validate"); 
	$("#imgForm").isValid(function(valid){
		if(valid){
			var imgW = $("#JcropImageElement").width();
			var imgH =$("#JcropImageElement").height();
			var imgUrl = $("#resultImageUrl").val(); 
			var scaledObject = jcropApi.tellScaled();
			if(scaledObject.w<=0||scaledObject.h<=0){
				parent.layer.alert("请选择裁剪区域!",{icon: 2,resize:false});
				return ;
			}
			PlatUtil.ajaxProgress({
				   url:"appmodel/CommonUIController.do?saveCutImg",
				   params:{
					   "imgUrl":imgUrl,
					   "imgW":imgW,
					   "imgH":imgH,
					   "scaledX":scaledObject.x,
					   "scaledY":scaledObject.y,
					   "scaledW":scaledObject.w,
					   "scaledH":scaledObject.h,
				   },
				   callback:function(resultJson){
						if(resultJson.success){
			    	   	 	$("#JcropImageElement").attr("src",resultJson.path);
							$("#JcropImageElement").width(scaledObject.w);
			    	   	 	$("#JcropImageElement").height(scaledObject.h);
			    	   	 	$("#scaleImg").text($("#JcropImageElement").width() + "×" + $("#JcropImageElement").height());
			    	   	 	imgWidth = scaledObject.w;
			    	 		imgHeight = scaledObject.h;
			    	   	 	jcropApi.setImage(resultJson.path);
			    	   	 	$("#resultImageUrl").val(resultJson.path);
			    	   	 	PlatUtil.setData("resultImageUrl",resultJson.path); 
			    	   	    PlatUtil.setData("dbfilepath",resultJson.dbfilepath); 
			    	   	 	cutChange = false;
			    	   	 	parent.layer.msg("裁剪成功!", {icon: 1});
			    	   	 	if(index!=null){
			    	   	 		setTabFunction(index);
			    	   	 	}
						}else{
							parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
						}
				   }
				});
		}
	});
	
}
//左旋
function rotLeft(){
	var rotNum = $("#rotNum").val();
	if(parseInt(rotNum)>-270){
		$("#JcropImageElement").rotate({angle:parseInt(rotNum)-90});
		$("#rotNum").val(parseInt(rotNum)-90);
	}else{
		$("#JcropImageElement").rotate(0);
		$("#rotNum").val(0);
	}
	
}
//右旋转
function rotRight(){
	var rotNum = $("#rotNum").val();
	if(parseInt(rotNum)<270){
		$("#JcropImageElement").rotate({angle:parseInt(rotNum)+90});
		$("#rotNum").val(parseInt(rotNum)+90);
	}else{
		$("#JcropImageElement").rotate(0);
		$("#rotNum").val(0);
	}
}
//重置
function resetRotImg(){
	$("#rotNum").val(0);
	$("#JcropImageElement").rotate(0);
}
//保存旋转
function saveRotImg(index){
	var rotNum = $("#rotNum").val();
	var imgUrl = $("#resultImageUrl").val(); 
	if(parseInt(rotNum)==0){
		parent.layer.msg("旋转保存成功!", {icon: 1});
	}else{
		PlatUtil.ajaxProgress({
			   url:"appmodel/CommonUIController.do?saveRotImg",
			   params:{
				   "imgUrl":imgUrl,
				   "rotNum":rotNum
			   },
			   callback:function(resultJson){
					if(resultJson.success){
		    	   	 	$("#JcropImageElement").attr("src",resultJson.path);
		    	   	 	var neww = $("#JcropImageElement").width();
		    	   	 	var newh = $("#JcropImageElement").height();
		    	   	 	if(parseInt(rotNum)==-270||parseInt(rotNum)==-90||parseInt(rotNum)==270||parseInt(rotNum)==90){
		    	   	 		var w = $("#JcropImageElement").width(); 
		    	   	 		var h = $("#JcropImageElement").height();
		    	   	 		neww = h;
		    	   	 		newh = w;
		    	   	 	}
		    	   	 	$("#JcropImageElement").attr("style","width:"+neww+"px;height:"+newh+"px;");
		    	   	 	$("#resultImageUrl").val(resultJson.path);
		    	   	 	PlatUtil.setData("resultImageUrl",resultJson.path); 
		    	   	 	$("#rotNum").val(0)
		    	   	 	parent.layer.msg("旋转保存成功!", {icon: 1});
		    	   	 	if(index!=null){
		    	   	 		setTabFunction(index);
		    	   	 	}
					}else{
						parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
					}
			   }
			});
	}
}
function docClick() {
	var clickImg = 0;
	$(document).click(function(e) {
		var evt = e ? e : (window.event ? window.event : null);
		var node = evt.srcElement ? evt.srcElement : evt.target;
		if ($(node).attr("id") == "JcropImageElement") {
			if (clickImg == 0) {
				_move = false;
				var left = e.pageX - parseInt($("#JcropImageElement").offset().left);
				var top = e.pageY - parseInt($("#JcropImageElement").offset().top);
				fontText.top = top;
				fontText.left = left;
				addText();
				clickImg = 1;
				move();
			} else if (clickImg == 1) {
				if ( !! $(".TextBox").length) {
					if ($(".TextBox").text().trim() == "") {
						$("#imageJcropContainer div").remove(".TextBox");
						$("#JcropImageElement").css("cursor", "text");
						docClick();
						return;
					}
				}
				$(".TextBox").attr("contentEditable", "false");
				$(".TextBox").css("cursor", "move");
				$(".TextBox").blur();
			}
		} else {
			if ($(node).attr("class") != "TextBox") {
				if ( !! $(".TextBox").length) {
					if ($(".TextBox").text().trim() == "") {
						$("#imageJcropContainer div").remove(".TextBox");
						$("#JcropImageElement").css("cursor", "text");
						docClick();
						return;
					}
				}
				$(".TextBox").attr("contentEditable", "false");
				$(".TextBox").css("cursor", "move");
				$(".TextBox").blur();
			}
		}
	});
}
function addText() {
	var newImgW = $("#JcropImageElement").width();
	setOptions();
	var imgBox = $("#imageJcropContainer");
	if (imgBox) {
		if (!$(".TextBox").length) {
			imgBox.css({
				"position": "relative",
				"width": newImgW,
				"margin": "0 auto"
			});
			imgBox.append("<div class='TextBox' id='TextBox'>&nbsp;</div>");
			$(".TextBox").attr("contentEditable", "true");
			$(".TextBox").css("cursor", "text");
			$(".TextBox").focus();
			$(".TextBox").keyup(function() {
				fontText.text = $(this).text();
				setOptions();
				imgEditState.text.text = fontText.text;
			});
		}
		$("#imageJcropContainer .TextBox").css({
			"color": fontText.fcolor,
			"font-size": fontText.size + "px",
			"font-style": fontText.Italic,
			"font-weight": fontText.fbold,
			"opacity": fontText.transparency,
			"text-decoration": fontText.underline,
			"font-family": fontText.font,
			"top": fontText.top + "px",
			"left": fontText.left + "px"
		});
		$("#JcropImageElement").css("cursor", "default");
	}
}
function setOptions() {
	fontText.font = $("#font_outer").val();
	var c = $("#colorValue").val();
	if (c != "" && c != null) {
		if (c.indexOf(":") > 0) {
			c = c.substring(c.indexOf(":") + 1, c.length - 1).trim();
		}
	} else {
		$("#color").attr("value", "#336699");
		c="#336699";
	}
	fontText.fcolor = c;
	fontText.transparency = $("#transportant_outer").val();
	fontText.size = $("#fontsize_outer").val();
	if ($(".TextBox")) {
		fontText.height = $(".TextBox").height();
	}
	if ($("[name='bold']").prop("checked")) {
		fontText.fbold = "bold";
	} else {
		fontText.fbold = "normal";
	}
	if ($("[name='Italic']").prop('checked')) {
		fontText.Italic = "italic";
	} else {
		fontText.Italic = "normal";
	}
	if ($("[name='underline']").prop('checked')) {
		fontText.underline = "underline";
	} else {
		fontText.underline = "none";
	}
	
	/* if ($("[name='Outline']").prop('checked')) {
		var textborder;
		if($("#lineWeight_outer").val() != "" && $("#lineWeight_outer").val()!= null){
			textborder=$("#lineWeight_outer").val();
		}else{
			textborder=0;
		}
		var textbdcolor= $("#linecolorValue").val();
		if (textbdcolor != "" && textbdcolor != null) {
			if (textbdcolor.indexOf(":") > 0) {
				textbdcolor = textbdcolor.substring(textbdcolor.indexOf(":") + 1, textbdcolor.length - 1).trim();
			}
		} else {
			$("#linecolorValue").attr("value", "#336699");
			textbdcolor="#336699";
		}	
		//fontText.textshadowCon=textbdcolor+" 0px 0 "+textborder+"px,"+textbdcolor+" 0 0 "+textborder+"px,"+textbdcolor+" 0 0 " +textborder+"px,"+textbdcolor+" 0 0 "+textborder+"px";
		fontText.textshadowCon =  "0px 0px "+textborder+"px "+textbdcolor;
		fontText.fliterCon="shadow(Color="+textbdcolor+", Strength="+textborder+")";
		if(textborder < 1){
			fontText.textshadowCon="none";
			fontText.fliterCon="";
		}
	} else {
		fontText.textshadowCon="none";
		fontText.fliterCon="";
	} */
	var opictyvalue="alpha(opacity=" + fontText.transparency*100 +")";
	fontText.fliterCon = opictyvalue +" "+fontText.fliterCon ; 
	fontText.height = $(".TextBox").height();
	fontText.text = $(".TextBox").text();
	imgEditState.text.color = fontText.fcolor;
	imgEditState.text.height = fontText.height;
	imgEditState.text.fontFamily = fontText.font;
	imgEditState.text.fontSize = fontText.size;
	imgEditState.text.fontstyle = fontText.Italic;
	imgEditState.text.fontWeight = fontText.fbold;
	imgEditState.text.opicty = fontText.transparency;
	imgEditState.text.textdecoration = fontText.underline;
	imgEditState.text.text = fontText.text;
	imgEditState.text.top = fontText.top;
	imgEditState.text.left = fontText.left;
	imgEditState.text.height = fontText.height;
	imgEditState.text.fliterCon=fontText.fliterCon;
	imgEditState.text.textshadowCon=fontText.textshadowCon;
	var imgBox = $("#imageJcropContainer");
	if (imgBox) {
		$("#imageJcropContainer .TextBox").css({
			"color": fontText.fcolor,
			"font-size": fontText.size + "px",
			"font-style": fontText.Italic,
			"font-weight": fontText.fbold,
			"opacity": fontText.transparency,
			"text-decoration": fontText.underline,
			"font-family": fontText.font,
			"top": fontText.top + "px",
			"left": fontText.left + "px",
			//"filter": fontText.fliterCon,
			"text-shadow": fontText.textshadowCon
		});
		textChange = true;
	}
}
function move() {
	var _x, _y;
	$(".TextBox").click(function() {}).mousedown(function(e) {
		if ($(".TextBox").attr("contentEditable") == "false") {
			_move = true;
			e.preventDefault();
		}
		_x = e.pageX - parseInt($(".TextBox").css("left"));
		_y = e.pageY - parseInt($(".TextBox").css("top"));
	});
	$(".TextBox").dblclick(function() {
		textChange = true;
		_move = false;
		$(".TextBox").attr("contentEditable", "true");
		$(".TextBox").css("cursor", "text");
		$(".TextBox").focus();
	});
	$(".TextBox").mousemove(function(e) {
		if (_move) {
			e.preventDefault();
			var x = e.pageX - _x;
			var y = e.pageY - _y;
			var leftwidth = $("#JcropImageElement").width() - $(".TextBox").width();
			var topwidth = $("#JcropImageElement").height() - $(".TextBox").height();
			if (x < 0) {
				x = 0;
			} else if (x > leftwidth) {
				x = leftwidth;
			}
			if (y < 0) {
				y = 0;
			} else if (y > topwidth) {
				y = topwidth;
			}
			$(".TextBox").css({
				top: y,
				left: x
			});
			fontText.left = x;
			fontText.top = y;
			imgEditState.text.top = y;
			imgEditState.text.left = x;
		}
	}).mouseup(function() {
		_move = false;
	});
}


function resetText(){
	if ( !! $("#imageJcropContainer > .TextBox").length) {
		$("#imageJcropContainer div").remove(".TextBox")
	}
	$("#JcropImageElement").css("cursor", "text");
	textChange = false;
	$(document).click(function(e) {});
}
function cancelText() {
	if ( !! $("#imageJcropContainer > .TextBox").length) {
			$("#imageJcropContainer div").remove(".TextBox")
	}
	$("#JcropImageElement").css("cursor", "text");
	docClick();
}

$(document).ready(function() {
	  $("#font_outer").select2();
	  $("#fontsize_outer").select2();
	  $("#transportant_outer").select2();
	  //$("#lineWeight_outer").select2();
	  $("#waterimg_transportant").select2();
	  $("#textColorBox").colpick({color:"#336699",submitText:"确定",onSubmit:function(HSB,HEX,RGB){
		  $("#textColorBox").attr("style","background-color:#"+HEX);
		  $("#colorValue").val("#"+HEX);
		  if($(".TextBox")){
			  imgEditState.text.color = "#"+HEX;
			  $(".TextBox").css("color","#"+HEX);
		  }
		  $("#textColorBox").colpickHide();
	  }});
	  /* $("#lineColorBox").colpick({color:"#336699",submitText:"确定",onSubmit:function(HSB,HEX,RGB){
		  $("#lineColorBox").attr("style","background-color:#"+HEX);
		  $("#linecolorValue").val("#"+HEX);
		  $("#lineColorBox").colpickHide();
		  setTextShadow();
	  }}); */
	  $("#font_outer").change(function(){
		  if($(".TextBox")){
			  imgEditState.text.fontFamily = $(this).val();
			  $(".TextBox").css("font-family",$(this).val());
		  }
	  })
	  $("#fontsize_outer").change(function(){
		  if($(".TextBox")){
			  imgEditState.text.fontSize = $(this).val();
			  $(".TextBox").css("font-size",$(this).val()+"px");
		  }
	  })
	  $("#transportant_outer").change(function(){
		  if($(".TextBox")){
			  imgEditState.text.opicty = $(this).val();
			  $(".TextBox").css("opacity",$(this).val());
		  }
	  });
	 /*  $("[name='lineWeight_outer']").change(function(){
		  setTextShadow();
	  }); */
	 /*  $("[name='Outline']").click(function(){
		  setTextShadow();
	  }); */
	  $("[name='bold']").click(function(){
		  if ($("[name='bold']").prop('checked')){
			  imgEditState.text.fontWeight = "bold";
			  $(".TextBox").css("font-weight","bold");
		  }else{
			  imgEditState.text.fontWeight = "normal";
			  $(".TextBox").css("font-weight","normal");
		  }
	  });
	  $("[name='Italic']").click(function(){
		  if ($("[name='Italic']").prop('checked')){
			  imgEditState.text.fontstyle = "italic";
			  $(".TextBox").css("font-style","italic");
		  }else{
			  imgEditState.text.fontstyle = "normal";
			  $(".TextBox").css("font-style","normal");
		  }
	  });
	  $("[name='underline']").click(function(){
		  if ($("[name='underline']").prop('checked')){
			  imgEditState.text.textdecoration = "underline";
			  $(".TextBox").css("text-decoration","underline");
		  }else{
			  imgEditState.text.textdecoration = "none";
			  $(".TextBox").css("text-decoration","none");
		  }
	  });
	  
	  $("#waterimg_transportant").change(function(){
		  if($(".warterImg")){
			  imgEditState.text.opicty = $(this).val();
			  $(".warterImg").css("opacity",$(this).val());
		  }
	  });
	  
});

function setTextShadow(){
	if ($("[name='Outline']").prop('checked')){
		if($(".TextBox")){
			var c = $("#linecolorValue").val();
			var s = $("#lineWeight_outer").val();
			if(c==undefined||c==""){
				c = "#336699"
			}
			var ts = "0px 0px "+s+"px "+c;
			$(".TextBox").css("text-shadow",ts);
			imgEditState.text.textshadowCon = ts;
		  }
	}else{
		if($(".TextBox")){
			  imgEditState.text.textshadowCon = "none";
			  $(".TextBox").css("text-shadow","none");
		  }
	}
}

function saveText(index){
	if($(".TextBox").text().trim()!=""&&$(".TextBox").text().trim()!=undefined){
		imgEditState.text.text = $(".TextBox").text().trim();
		var imgUrl = $("#resultImageUrl").val(); 
		var param = imgEditState.text;
		param.imgUrl = imgUrl;
		PlatUtil.ajaxProgress({
			   url:"appmodel/CommonUIController.do?saveTextImg",
			   params:param,
			   callback:function(resultJson){
					if(resultJson.success){
		    	   	 	$("#JcropImageElement").attr("src",resultJson.path);
		    	   	 	$("#resultImageUrl").val(resultJson.path);
		    	   	 	PlatUtil.setData("resultImageUrl",resultJson.path); 
		    	   	 	textChange = false;
		    	   	 	parent.layer.msg("水印文字成功!", {icon: 1});
		    	   	 	if(index!=null){
		    	   	 		setTabFunction(index);
		    	   	 	}
					}else{
						parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
					}
			   }
			});
	}else{
		parent.layer.alert("请输入水印文字!",{icon: 2,resize:false});
	}
}
var GUID = WebUploader.Base.guid();
var uploader; 

function setUploadWaterImg(){
	uploader = WebUploader.create({
	    // swf文件路径
	    swf: 'plug-in/webuploader-0.1.5/dist/Uploader.swf',
	    // 文件接收服务端。
	    server: '<%=basePath%>system/FileAttachController/upload.do',
	    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	    pick: {
	    	id:'#picker',
	    	multiple:true,
	    },
	    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
	    resize: false,
	    auto:true,
	    prepareNextFile:true,
	    chunked:true,
	    threads: 1,
	    duplicate:true,
	    accept:{
	        title: 'Images',
	        extensions: 'gif,jpg,jpeg,bmp,png',
	        mimeTypes: 'image/*'
	    },
	    formData:{
	    	guid:GUID,
	    	uploadRootFolder:"imagewater"
	    }
	});
	//上传成功
	uploader.on( 'uploadSuccess', function( file,response) {
		var imgBox = $("#imageJcropContainer");
		if (imgBox) {
			var newImgW = $("#JcropImageElement").width();
			var newImgH = $("#JcropImageElement").height();
			var a = new Image(); 
			a.src = response.getfileurl;
			a.onload = function(){
				var aWidth = a.width;
				var aHeight = a.height;
				if(aWidth>newImgW||aHeight>newImgH){
					parent.layer.alert("水印图片过大!",{icon: 2,resize:false});
					return;
				}
				imgBox.css({
					"position": "relative",
					"width": newImgW,
					"margin": "0 auto"
				});
				if (!$("#waterimagelist").length) {
					imgBox.append("<div id='waterimagelist' class='warterImg'></div>");
					$("#waterimagelist").css("cursor", "move");
				}
				$("#waterimagelist").html('<img class="_warterImg" src="'+response.getfileurl+'"/>');
				waterChange = true;
				moveWaterImg();
			}
		}
		
	});
}
function resetUploadWaterImg(){
	try{
		uploader.destroy();
	}catch (e) {
		
	}
}
function moveWaterImg() {
	var _x, _y;
	var moveW = false;
	$(".warterImg").mousedown(function(e) {
		moveW = true;
		curimgW = $(this).width();
		curimgH = $(this).height();
		waterImgSelf=$(this);
		_x = e.pageX - parseInt($(this).css("left"));
		_y = e.pageY - parseInt($(this).css("top"));
		e.preventDefault();
	});
	$(".warterImg").mousemove(function(e) {
		if (moveW) {
			e.preventDefault();
			var x = e.pageX - _x;
			var y = e.pageY - _y;
			var leftwidth = $("#JcropImageElement").width() - curimgW;
			var topwidth = $("#JcropImageElement").height() - curimgH;
			if (x < 0) {
				x = 0;
			} else if (x > leftwidth) {
				x = leftwidth;
			}
			if (y < 0) {
				y = 0;
			} else if (y > topwidth) {
				y = topwidth;
			}
			$(waterImgSelf).css({
				top: y,
				left: x
			});
		}
	}).mouseup(function() {
		moveW = false;
	});
}

function cancelImage() {
	if ( !! $("#imageJcropContainer > .warterImg").length) {
			$("#imageJcropContainer div").remove(".warterImg");
	}
	waterChange = false;
}

function saveWaterImage(index){
	if ( !! $("#imageJcropContainer > .warterImg").length) {
		var  waterImgUrl = $("._warterImg").attr("src"); 
		var imgUrl = $("#resultImageUrl").val(); 
		var waterimg_transportant = $("[name='waterimg_transportant']").val();
		var x = $(".warterImg").css("left").replace("px","");
		var y = $(".warterImg").css("top").replace("px","");
		PlatUtil.ajaxProgress({
			   url:"appmodel/CommonUIController.do?saveWaterImage",
			   params:{
				   imgUrl:imgUrl,
				   waterImgUrl:waterImgUrl,
				   alpha:waterimg_transportant,
				   x:x,
				   y:y
			   },
			   callback:function(resultJson){
					if(resultJson.success){
		    	   	 	$("#JcropImageElement").attr("src",resultJson.path);
		    	   	 	$("#resultImageUrl").val(resultJson.path);
		    	   	 	PlatUtil.setData("resultImageUrl",resultJson.path); 
		    	   	 	waterChange = false;
		    	   	 	parent.layer.msg("水印图片成功!", {icon: 1});
		    	   	 	if(index!=null){
		    	   	 		setTabFunction(index);
		    	   	 	}
					}else{
						parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
					}
			   }
			});
	}else{
		parent.layer.alert("请上传水印图片!",{icon: 2,resize:false});
	}
}
</script>
