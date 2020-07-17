/**
 * 定义前端工具类
 */
var PlatUtil = {
	/**
	 * 全局成功操作消息
	 */
    SUCCESS_MSG:"操作成功!",
    /**
     * 全局失败操作消息
     */
    FAIL_MSG:"系统出错,操作失败!请联系系统管理员!",
    /**
     * 向导当前步骤
     */
    WIZARD_CUR_STEP:null,
    /**
     * codemirror editor对象
     */
    PLAT_CODEMIRROREDITOR:null,
    /**
     * 定义弹出选择器的配置KEY
     */
    WIN_SELECTOR_CONFIG:"WIN_SELECTOR_CONFIG",
    /**
     * 定义弹出选择器的选择记录
     */
    WIN_SELECTOR_RECORDS:"WIN_SELECTOR_RECORDS",
    /**
     * 表单字段值数组
     */
    FORM_FIELD_VALUE_ARRAY:null,
    /**
     * websocket对象
     */
    PLAT_WEBSOCKET:null,
    
    /**
     *初始化websocket对象
     */
    initWebSocket:function(config){
        //判断当前浏览器是否支持WebSocket
        if('WebSocket' in window){
            PlatUtil.PLAT_WEBSOCKET = new WebSocket(config.url);
        }
        else{
            alert("当前浏览器并不支持WebSocket!");
        }
        //连接发生错误的回调方法
        PlatUtil.PLAT_WEBSOCKET.onerror = function(){
        	alert("websocket连接发生错误!");
        };
        //连接成功建立的回调方法
        PlatUtil.PLAT_WEBSOCKET.onopen = function(event){
        	if(config.onopen!=null){
   			   config.onopen.call(this);
   			}
        	PlatUtil.PLAT_WEBSOCKET.send("{clientId:'"+config.clientId+"',msgType:'1'}");
        };
        //接收到消息的回调方法
        PlatUtil.PLAT_WEBSOCKET.onmessage = function(event){
        	if(config.onmessage!=null){
			   config.onmessage.call(this,event.data);
			}
        };
        //连接关闭的回调方法
        PlatUtil.PLAT_WEBSOCKET.onclose = function(){
        	if(config.onclose!=null){
 			   config.onclose.call(this);
 			}
        };
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function(){
        	if(config.onclose!=null){
  			   config.onbeforeunload.call(this);
  			 }
      	     websocket.send("{clientId:'"+config.clientId+"',msgType:'2'}");
        	 PlatUtil.PLAT_WEBSOCKET.close();
        };
        return PlatUtil.PLAT_WEBSOCKET;
    },
    /**
     * 获取UUID
     */
    getUUID:function(){
    	var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);

        }
        s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";
        var uuid = s.join("");
        return uuid;
    },
	/**
	 * 设置前端的数据对象
	 * @param key
	 * @param value
	 */
	setData:function(key,value){
		if(window.top.PlatData==undefined){
			window.top.PlatData = {};
		}
		window.top.PlatData[key] = value;
	},	
	/**
	 * 获取前端的数据对象
	 * @param key
	 * @returns
	 */
	getData:function(key){
		if(window.top.PlatData==undefined){
			window.top.PlatData = {};
		}
		return window.top.PlatData[key] ;
	},
	/**
	 * 移除前端的数据对象
	 * @param key
	 */
	removeData:function(key){
		if(window.top.PlatData==undefined){
			window.top.PlatData = {};
		}
		delete window.top.PlatData[key];
	},
	/**
	 * 显示流程办理弹出框
	 */
	showFlowHandleWindow:function(jbpmFlowInfo,winTitle){
		PlatUtil.ajaxProgress({
			url : "workflow/ExecutionController.do?cacheFlowInfo",
			params : jbpmFlowInfo,
			callback : function(resultJson) {
				var flowToken = resultJson.flowToken;
				PlatUtil.openWindow({
					title:winTitle,
					area: ["1000px","500px"],
					content: "workflow/ExecutionController.do?goFlowHandleWindow&flowToken="+flowToken,
					end:function(){
					  if(PlatUtil.isSubmitSuccess()){
						  PlatUtil.setData("submitSuccess",true);
						  PlatUtil.closeWindow();
					  }
					}
				});
			}
		});
	},
	/**
	 * 获取jbpm流程信息对象
	 */
	getJbpmFlowInfo:function(){
		var JbpmFlowInfoJson = $("#JbpmFlowInfoJson").val();
		var jbpmFlowInfo = $.parseJSON(JbpmFlowInfoJson);
		return jbpmFlowInfo;
	},
	/**
	 * 获取下一提交的办理人JSON
	 */
	getFlowNextAssignJson:function(){
		var flowAssignList = [];
		$("[name^='nextNodeKey']").each(function(index){
            //获取元素的类型
		    var tagName = $(this).get(0).tagName;
            var nextNodeKeyName = $(this).attr("name");
            var nextNodeKey = "";
            var nextNodeName = "";
            var nextAssignerIds = "";
            var nextIsOrderTask  = "";
            var nextAssignerNature = "";
            var flowAssign = {};
            if(tagName=="INPUT"){
            	nextNodeKey = $(this).val();
            	nextNodeName = $("input[name='nextNodeName_"+index+"']").val();
            	nextAssignerIds = $("input[name='nextNodeAssignerIds_"+index+"']").val();
            	nextIsOrderTask =  $(this).attr("isordertask");
            	nextAssignerNature = $(this).attr("handlernature");
            }else if(tagName=="SELECT"){
            	nextNodeKey = PlatUtil.getSelectAttValue(nextNodeKeyName,"value");
            	nextNodeName = PlatUtil.getSelectAttValue(nextNodeKeyName,"label");
            	nextAssignerIds = $("input[name='nextNodeAssignerIds_"+index+"']").val();
            	nextIsOrderTask = PlatUtil.getSelectAttValue(nextNodeKeyName,"isordertask");
            	nextAssignerNature = PlatUtil.getSelectAttValue(nextNodeKeyName,"handlernature");
            }
            flowAssign.nextNodeKey = nextNodeKey;
            flowAssign.nextNodeName = nextNodeName;
            flowAssign.nextAssignerIds = nextAssignerIds;
            flowAssign.nextIsOrderTask = nextIsOrderTask;
            flowAssign.nextAssignerNature = nextAssignerNature;
            flowAssignList.push(flowAssign);
		});
		var flowAssignJson = JSON.stringify(flowAssignList);
		return flowAssignJson;
	},
	/**
	 * 暂存流程信息
	 */
	tempSaveJbpmFlow:function(jbpmFlowInfo,callback){
		jbpmFlowInfo.jbpmIsTempSave = "true";
		PlatUtil.ajaxProgress({
			url:"workflow/ExecutionController.do?exeFlow",
			params : jbpmFlowInfo,
			callback : function(resultJson) {
				if (resultJson.OPER_SUCCESS) {
					var JbpmFlowInfoJson = resultJson.JbpmFlowInfoJson;
					$("#JbpmFlowInfoJson").val(JbpmFlowInfoJson);
					parent.layer.msg(resultJson.OPER_MSG, {icon: 1});
					jbpmFlowInfo = PlatUtil.getJbpmFlowInfo();
					var jbpmMainTablePkName = jbpmFlowInfo.jbpmMainTablePkName;
					var jbpmMainTableRecordId = jbpmFlowInfo.jbpmMainTableRecordId;
					$("input[name='"+jbpmMainTablePkName+"']").val(jbpmMainTableRecordId);
					if(callback!=null){
						callback.call(this,resultJson);
     			    }
				} else {
					parent.layer.alert(resultJson.OPER_MSG,{icon: 2,resize:false});
					if(callback!=null){
						callback.call(this,resultJson);
     			    }
				}
			}
		});
	},
	/**
     * 判断一个数组中是否包含某个值 
     */
    isContain:function(array,targetValue){
    	var isExist = false;
    	for(var index in array){
    		var value = array[index];
    		if(value==targetValue){
    			isExist = true;
    			break;
    		}    		
    	}
    	return isExist;
    },
	/**
	 * 保存附加配置信息
	 */
	saveUIAttachConfig:function(formData){
		var result = false;
		var FORMCONTROL_ID = $("#FormControlForm input[name='FORMCONTROL_ID']").val();
		var FORMCONTROL_COMPCODE = $("#FormControlForm input[name='FORMCONTROL_COMPCODE']").val();
		formData.FORMCONTROL_ID = FORMCONTROL_ID;
		formData.FORMCONTROL_COMPCODE = FORMCONTROL_COMPCODE;
		PlatUtil.ajaxProgress({
			url:"appmodel/FormControlController.do?saveAttachConfig",
			async:"-1",
			params :formData,
			callback : function(resultJson) {
				if(resultJson.success){
					result = true;
				}
			}
		});
		return result;
	},
	/**
	 * 保存基本配置信息并且跳转到附加配置界面
	 */
	saveUIBaseConfigAndGoAttach:function(formData){
		var result = false;
		if(PlatUtil.isFormValid("#BaseConfigForm")){
			var FORMCONTROL_ID = $("#FormControlForm input[name='FORMCONTROL_ID']").val();
			var FORMCONTROL_COMPCODE = $("#FormControlForm input[name='FORMCONTROL_COMPCODE']").val();
			formData.FORMCONTROL_ID = FORMCONTROL_ID;
			formData.FORMCONTROL_COMPCODE = FORMCONTROL_COMPCODE;
			PlatUtil.ajaxProgress({
				url:"appmodel/FormControlController.do?saveBaseConfig",
				async:"-1",
				params :formData,
				callback : function(resultJson) {
					if(resultJson.success){
						if(resultJson.JS_NAME&&resultJson.JS_NAME!=""){
							$("#ATTACH_CONFIG_SPAN").attr("nextstep_fnname",resultJson.JS_NAME);
						}
						formData.VIEW_TYPE = "attach";
						PlatUtil.ajaxProgress({
							url:"appmodel/FormControlController.do?getNextConfigView",
							async:"-1",
							params :formData,
							callback : function(resultHtml) {
								if(resultHtml&&resultHtml!=""){
									$("#ATTACH_CONFIG_DIV").html(resultHtml);
								}
								result = true;
							}
						});
					}
				}
			});
			
		}
		return result;
	},
	/**
	 * 全屏
	 */
	fullscreen:function(obj){
		if (!$(obj).attr('fullscreen')) {
            $(obj).attr('fullscreen', 'true');
            PlatTab.requestFullScreen();
        } else {
            $(obj).removeAttr('fullscreen');
            PlatTab.exitFullscreen();
        }
	},
	/**
	 * 合并JqGrid的数据单元格
	 * @param gridId 表格ID
	 * @param colName 列的名称
	 */
	mergeJqridDataCell:function(gridId,colName){
		//得到显示到界面的id集合 
        var mya = $("#" + gridId + "").getDataIDs(); 
        //当前显示多少条 
        var length = mya.length; 
        for (var i = 0; i < length; i++) { 
            //从上到下获取一条信息 
            var before = $("#"+gridId+"").jqGrid('getRowData', mya[i]); 
            //定义合并行数 
            var rowSpanTaxCount = 1; 
            for (j = i + 1; j <= length; j++) { 
                //和上边的信息对比 如果值一样就合并行数+1 然后设置rowspan 让当前单元格隐藏 
                var end = $("#" + gridId + "").jqGrid('getRowData', mya[j]); 
                if (before[colName] == end[colName]) { 
                    rowSpanTaxCount++; 
                    $("#"+gridId+"").setCell(mya[j], colName, '', { display: 'none' }); 
                } else { 
                    rowSpanTaxCount = 1; 
                    break; 
                } 
                $("#" + colName + "" + mya[i] + "").attr("rowspan", rowSpanTaxCount); 
            } 
        } 
	},
	/**
	 * bootstrap的tab默认点击事件
	 */
	onBootstrapTabClick:function(tabContainerId,tabId,tabClickFnName,resizeHeight){
		if(!(resizeHeight&&resizeHeight=="-1")){
			var firstLiTabId = $("#"+tabContainerId+" ul li").eq(0).attr("subtabid");
			if(tabId!=firstLiTabId){
				var length = $("#"+tabId).find(".ui-layout-center").length;
				if(length!=0){
					$("#"+tabId).find(".ui-layout-center").attr("style",$("#"+firstLiTabId).find(".ui-layout-center").attr("style"));
					if($("#"+tabId).find(".ui-jqgrid").length>0){
						$("#"+tabId).find(".ui-layout-center").css("position","");
					}
					var northHeight = $("#"+tabId).find(".ui-layout-north").height();
					var southHeight = $("#"+tabId).find(".ui-layout-south").height();
					var eastHeight = $("#"+tabId).find(".ui-layout-east").height();
					var westHeight = $("#"+tabId).find(".ui-layout-west").height();
					var subtractHeight = 0;
					if(northHeight==null||northHeight==undefined){
						$("#"+tabId).find(".ui-layout-center").css("top","0px");
					}else{
						subtractHeight += northHeight;
						$("#"+tabId).find(".ui-layout-center").css("top",northHeight+"px");
					}
					if(southHeight==null||southHeight==undefined){
						$("#"+tabId).find(".ui-layout-center").css("bottom","0px");
					}else{
						subtractHeight += southHeight;
						$("#"+tabId).find(".ui-layout-center").css("bottom",southHeight+"px");
					}
					if(eastHeight==null||eastHeight==undefined){
						$("#"+tabId).find(".ui-layout-center").css("right","0px");
					}else{
						subtractHeight += eastHeight;
						$("#"+tabId).find(".ui-layout-center").css("right",eastHeight+"px");
					}
					if(westHeight==null||westHeight==undefined){
						$("#"+tabId).find(".ui-layout-center").css("left","0px");
					}else{
						subtractHeight += westHeight;
						$("#"+tabId).find(".ui-layout-center").css("left",westHeight+"px");
					}
					var resultCenterHeight = $("#"+firstLiTabId).height()-subtractHeight;
					$("#"+tabId).find(".ui-layout-center").css("height",resultCenterHeight+"px");
					var centerWidth = $("#"+tabId).find(".ui-layout-center").css("width");
					if(centerWidth=="0px"||centerWidth=="2px"){
						var curTab = $("#"+tabContainerId+" ul li[class='active']").attr("subtabid");
						$("#"+tabId).find(".ui-layout-center").css("width",$("#"+curTab).width());
					}
					var jqgridDiv = $("#"+tabId).find(".ui-jqgrid").attr("id");
					if(jqgridDiv){
						var jqgridTableId = jqgridDiv.substring(jqgridDiv.indexOf("gbox_")+5,jqgridDiv.length);
						var width = $("#"+tabId).find(".ui-layout-center").width();
						$("#"+jqgridTableId).setGridWidth(width-2,false);
						$("#"+jqgridTableId).setGridHeight("auto");
						var firstTableHeight = $("#"+firstLiTabId).find(".ui-jqgrid-bdiv").height();
						if(firstTableHeight){
							$("#"+jqgridTableId).setGridHeight(firstTableHeight+"px");
						}
					}
				}
			}
		}
		
		if($("#"+tabContainerId+" #"+tabId).find("#myDiagram").length>0){
			$("#"+tabId).find(".ui-layout-center").css("height","100%");
			var jsonValue = $("#FLOWDEF_JSON").val();
			myDiagram.model = go.Model.fromJson(jsonValue);
		}
		
		if(tabClickFnName){
			eval(tabClickFnName).call(this,tabId);
		}
	},
	/**
	 * 切换bootstrap的tab面板索引
	 */
	changeBootstrapTabIndex:function(tabContainerId,tabId){
		$("#"+tabContainerId+" a[href=\"#"+tabId+"\"]").tab("show");
	},
	/**
	 * 验证bootstrap的tab是否有效
	 */
	validBootstrapTab:function(tabContainerId){
		var bootstrapTabs = $("#"+tabContainerId+" .tab-content").children(".tab-pane");
		var validResult = true;
		$.each(bootstrapTabs,function(index,obj){
			var tabId = $(obj).attr("id");
			var formObj = $(obj).find("form[id]");
			var formId = formObj.attr("id");
			if(formId){
				var valid = PlatUtil.isFormValid("#"+formId);
				if(!valid){
					$("#"+tabContainerId+" a[href=\"#"+tabId+"\"]").tab("show");
					validResult = false;
					return false;
				}
			}
		});
		return validResult;
	},
	/**
	 * 加载后台中央主要内容
	 */
	loadMainFrame : function() {
		$("#mainContent").height($(document.body).height() - 133);
		$(window).resize(function(e) {
			$("#mainContent").height($(document.body).height() - 133);
		});
	},
	//皮肤主题
    theme: {
        type: "1",
        setType: function () {
            switch (top.PlatUtil.theme.type) {
                case "1"://皮肤1
                    $('body').addClass('uiDefault');
                    break;
                case "4"://皮肤2
                    $('body').addClass('uiPretty');
                    break;
            }
        }
    },
    /**
     * 全局遮罩加载进度条
     * EXP:显示的例子PlatUtil.loading({ isShow: true, text: "正在安全退出..." });
     * 隐藏的例子:PlatUtil.loading({ isShow: false});
     * @param ops
     */
    loading: function (ops) {//加载动画显示与否
        var ajaxbg = top.$("#loading_background,#loading_manage");
        if (ops.isShow) {
            ajaxbg.show();
        } else {
            if (top.$("#loading_manage").attr('istableloading') == undefined) {
                ajaxbg.hide();
                top.$(".ajax-loader").remove();
            }else{
            	ajaxbg.hide();
            }
        }
        if (!!ops.text) {
            top.$("#loading_manage").html(ops.text);
        } else {
            top.$("#loading_manage").html("正在拼了命为您加载…");
        }
        top.$("#loading_manage").css("left", (top.$('body').width() - top.$("#loading_manage").width()) / 2 - 54);
        top.$("#loading_manage").css("top", (top.$('body').height() - top.$("#loading_manage").height()) / 2);
    },
	/**
	 * 初始化tab和菜单
	 */
	initTabAndMenu:function (opt) {
		//绑定子菜单
		$("[data-submenu]").submenupicker();
        PlatUtil.theme.type = opt.themeType;
        window.setTimeout(function () {
            $('#ajax-loader').fadeOut();
        }, 50);
    },
    /**
	 * 去除字符串的前后空格
	 * @param {} str
	 * @return {}
	 */
	trim:function(str){
		str = str.replace(/(^\s*)|(\s*$)/g, "");
		str = str.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
		return str;
	},
	/**
	 * 加载echart图形报表
	 */
	loadEchartPic:function(domId,config){
		var myChart = echarts.init(document.getElementById(domId));
		var searchPanelId = domId+"search";
		var queryParams = null;
		if($("#"+searchPanelId).length!=0){
			queryParams =  PlatUtil.getQueryParams(searchPanelId);
			if(!config){
				var searchTable = $("#"+searchPanelId+" input[name='search'][readonly='readonly']");
				PlatUtil.showOrHideSearchTable(searchTable);
			}
		}
		PlatUtil.ajaxProgress({
			url:"appmodel/CommonUIController.do?echartdata&FORMCONTROL_ID="+domId,
			params : queryParams,
			callback : function(data) {
				if (data.success) {
					// 指定图表的配置项和数据
					/*var dataOption = {
						legend: {
					        data: data.legendData
					    },
					    series : [
					        {
					            data:data.seriesDataList
					        }
					    ]	
					};*/
					var dataOption = data;
					if(config){
						var option = PlatUtil.mergeObject(config,dataOption)
						// 使用刚指定的配置项和数据显示图表。
						myChart.setOption(option);
					}else{
						myChart.setOption(dataOption);
					}
					
				} 
			}
		});
	},
	/**
	 * 进行表格的搜索
	 * @param tableId
	 * @param searchpanelId
	 */
	tableDoSearch:function(tableId,searchpanelId,showOrHideSearchTable){
		var searchTable = $("#"+searchpanelId+" input[name='search'][readonly='readonly']");
		if(showOrHideSearchTable){
			PlatUtil.showOrHideSearchTable(searchTable);
		}
		var queryParams =  PlatUtil.getQueryParams(searchpanelId);
		$("#"+tableId).setGridParam({postData:queryParams,page:1}).trigger("reloadGrid"); 
	},
	/**
	 * 重置表格的查询条件
	 * @param {} searchToolBarId
	 */
	tableSearchReset:function(searchpanelId){
		//$("#"+searchpanelId+" form")[0].reset();
		$("#"+searchpanelId+" input[type!='hidden'][type!='radio'][type!='checkbox']").each(function(){
			var name = $(this).attr("name");
			$(this).val("");
		});
		$("#"+searchpanelId+" input[type='radio']").each(function(){
			var name = $(this).attr("name");
			PlatUtil.setCheckRadio(name,"");
		});
		$("#"+searchpanelId+" input[type='checkbox']").each(function(){
			var name = $(this).attr("name");
			$("input[name='"+name+"'][type='checkbox']").prop("checked",false);
		});
		$("#"+searchpanelId+" input[type='hidden'][queryreset='true']").each(function(){
			$(this).val("");
		});
		PlatUtil.changeSelect2Val("#"+searchpanelId+" select","");
	},
    /**
	 * 获取查询面板上的查询条件
	 */
	getQueryParams:function(elementId){
		var queryParams = {};
		queryParams = PlatUtil.getFormEleData(elementId,null,true);
		return queryParams;
	},
	/**
	 * 全选或者反选可编辑表格
	 */
	selectAllEditTable:function(checkobj,tableId){
		var isChecked = $(checkobj).is(":checked");
		if(isChecked){
			$("#"+tableId).find(".edittable_checkbox").prop("checked",true);
		}else{
			$("#"+tableId).find(".edittable_checkbox").prop("checked",false);
		}
	},
	/**
	 * 移除可编辑表格的所选记录
	 */
	removeEditTableRows:function(tableId){
		var selectDatas = PlatUtil.getEditTableOperMulRecord(tableId);
		if(selectDatas){
			var layerIndex = parent.layer.confirm("您确定要删除所选记录吗?", {
			    resize :false
			}, function(){
				$.each(selectDatas,function(index,obj){
					var trId = tableId+"_"+obj.edittable_checkbox;
					$("#"+trId).remove();
				});
				var tbodyId = tableId+"Tbody";
				$("#"+tbodyId+" tr[id]").each(function(index){ 
				    var td = $(this).children().eq(0);
				    td.text(index+1);
				});
				$("#"+tableId+" input[name='AllSelectCheckBox']").prop("checked",false);
				parent.layer.close(layerIndex);
			}, function(){
				
			});
		}
	},
	/**
	 * 获取可编辑表格所有的数据JSON字符串
	 * var recordJson = PlatUtil.getEditTableAllRecordJson("可编辑表格ID");
	 */
	getEditTableAllRecordJson:function(tableId){
		var allDatas = PlatUtil.getEditTableAllRecord(tableId);
		if(allDatas.length>0){
			return JSON.stringify(allDatas);
		}else{
			return "";
		}
	},
	/**
	 * 获取可编辑表格所有的数据
	 * var recordArray = PlatUtil.getEditTableAllRecord("可编辑表格ID");
	 */
	getEditTableAllRecord:function(tableId){
		var tbodyId = tableId+"Tbody";
		var allDatas = [];
		$("#"+tbodyId+" tr[id]").each(function(){ 
		    var trId = $(this).attr("id");
		    var rowData = PlatUtil.getFormEleData(trId,true);
		    var newRowData = {};
		    for(var fieldName in rowData){
		    	var newFieldName = null;
		    	if(fieldName!="edittable_checkbox"){
		    		newFieldName = fieldName.substring(0,fieldName.lastIndexOf("_"));
		    	}else{
		    		newFieldName = fieldName;
		    	}
		    	if(newFieldName!="edittable_checkbox"){
		    		newRowData[newFieldName] = rowData[fieldName];
		    	}
		    }
		    allDatas.push(newRowData);
		});
		return allDatas;
	},
	/**
	 * 获取可编辑表格所勾选的多条数据
	 * var checkMulRecord = PlatUtil.getEditTableOperMulRecord("可编辑表格Id");
	 */
	getEditTableOperMulRecord:function(tableId){
		var tbodyId = tableId+"Tbody";
		var selectData = [];
		var length = $("#"+tbodyId+" input[name='edittable_checkbox']:checkbox:checked").length;
		if(length==0){
			parent.layer.alert("请选择需要被操作的记录!",{resize:false});
			return null;
		}else{
			$("#"+tbodyId+" input[name='edittable_checkbox']:checkbox:checked").each(function(){ 
			    var checkValue = $(this).val();
			    var trId = tableId+"_"+checkValue;
			    var rowData = PlatUtil.getFormEleData(trId,true);
			    var newRowData = {};
			    for(var fieldName in rowData){
			    	var newFieldName = null;
			    	if(fieldName!="edittable_checkbox"){
			    		newFieldName = fieldName.substring(0,fieldName.lastIndexOf("_"));
			    	}else{
			    		newFieldName = fieldName;
			    	}
			    	newRowData[newFieldName] = rowData[fieldName];
			    }
			    selectData.push(newRowData);
			});
			$("#associalTable input[name='AllSelectCheckBox']").prop("checked",false);
			return selectData;
		}
	},
	/**
	 * 获取表格所选择的多条数据
	 */
	getTableOperMulRecord:function(tableId){
		var selectData = [];
		var selectIds = $("#"+tableId).jqGrid("getGridParam","selarrrow");
		if(selectIds&&selectIds.length>0){
			selectIds = selectIds.sort();
			for(var i=0;i<selectIds.length;i++){
				selectData.push($("#"+tableId).jqGrid("getRowData",selectIds[i]));
			}
		}
		if(selectData.length==0){
			parent.layer.alert("请选择需要被操作的记录!",{icon: 7,resize:false});
			return null;
		}else{
			return selectData;
		}
	},
	/**
	 * 操作表格多条记录 
	 */
	operMulRecordForTable:function(config){
		var selectDatas = PlatUtil.getTableOperMulRecord(config.tableId);
		if(selectDatas){
			var selectLength = selectDatas.length;
			var tipMsg = config.tipMsg?config.tipMsg:"您确定要删除所选择的【"+selectLength+"】条记录吗?";
			var colName = config.selectColName;
			var selectColValues = "";
			$.each(selectDatas,function(index,obj){
				if(index>0){
					selectColValues+=",";
				}
				selectColValues+= eval("obj."+colName);
			});
			parent.layer.confirm(tipMsg, {
				icon: 7,
			    resize :false
			}, function(){
				PlatUtil.ajaxProgress({
					url:config.url,
					params : {
						selectColValues:selectColValues
					},
					callback : function(resultJson) {
						if (resultJson.success) {
							if(resultJson.msg){
								parent.layer.alert(resultJson.msg,{icon: 1,resize:false});
							}else{
								parent.layer.alert(PlatUtil.SUCCESS_MSG,{icon: 1,resize:false});
							}
							if(config.callback!=null){
		     					config.callback.call(this,resultJson);
		     			   }
						} else {
							if(resultJson.msg){
								parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
							}else{
								parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
							}
							
						}
					}
				});
			}, function(){
				
			});	
		}
		
	},
	/**
	 * 获取表格单条操作数据内容
	 * @param tableId
	 */
	getTableOperSingleRecord:function(tableId){
		var selectData = [];
		var selectIds = $("#"+tableId).jqGrid("getGridParam","selarrrow");
		if(selectIds.length>0){
			selectIds = selectIds.sort();
			for(var i=0;i<selectIds.length;i++){
				selectData.push($("#"+tableId).jqGrid("getRowData",selectIds[i]));
			}
		}
		if(selectData.length==0){
			parent.layer.alert("请选择需要被操作的记录!",{icon:7,resize:false});
			return null
		}else if(selectData.length>1){
			//parent.layer.alert("只能选择一条记录进行操作!",{icon:7,resize:false});
			return selectData[0];
		}else{
			return selectData[0];
		}
		
	},
	/**
	 * 显示或者隐藏查询表格
	 * @param obj
	 */
	showOrHideSearchTable:function(obj){
		if ($(obj).next(".table-filter-list").is(":hidden")) {
            $(obj).css('border-bottom-color', '#fff');
            $(obj).next(".table-filter-list").slideDown(10);
            $(obj).addClass("active")
        } else {
            $(obj).css('border-bottom-color', '#ccc');
            $(obj).next(".table-filter-list").slideUp(10);
            $(obj).removeClass("active")
        }
	},
	/**
	 * 改变单选框的值
	 */
	setCheckRadio:function(controlName,value){
		$("input[name='"+controlName+"'][type='radio'][value='"+value+"']").prop("checked",true);
	},
	/**
	 * 获取单选框的值
	 * @param {} controlName:控件名称
	 * @param {} tagName:属性名称
	 * 
	 * var TEMPLATE_CONTYPE = PlatUtil.getCheckRadioTagValue("TEMPLATE_CONTYPE","VALUE");
	 */
	getCheckRadioTagValue:function(controlName,tagName){
		var radios = $("input[name='"+controlName+"'][type='radio']");
		var tagValue = "";
		tagName = tagName.toLowerCase();
		$.each(radios,function(index,obj) { 
		      var isChecked = $(this).is(':checked');
	  	      if(isChecked){
	  	      	 tagValue = $(this).attr(tagName);
	  	      }
        }); 
        return tagValue;
	},
	/**
	 * 设置复选框的值
	 * @param controlName
	 * @param values
	 */
	setCheckBoxValues:function(controlName,values){
		var valueArray=  values.split(",");
		$.each(valueArray,function(index,objvalue){
			 $("input[name='"+controlName+"'][type='checkbox'][value='"+objvalue+"']").prop("checked",true);
		});
		
	},
	/**
	 * 获取复选框的值
	 * var checkboxValue = PlatUtil.getCheckBoxValues("你的复选框名称")
	 */
    getCheckBoxValues:function(checkBoxName,parentSelector,attrName){
    	var checkValues = "";
    	var selectorRule = "";
    	if(parentSelector){
    		selectorRule = parentSelector+" input[name='"+checkBoxName+"'][type='checkbox']";
    	}else{
    		selectorRule = "input[name='"+checkBoxName+"'][type='checkbox']";
    	}
    	$(selectorRule).each(function(){
	          var checked= $(this).is(':checked');
	          if(checked){
	        	  if(attrName){
	        		  attrName = attrName.toLowerCase();
	        		  checkValues+=($(this).attr(attrName)+","); 
	        	  }else{
	        		  checkValues+=($(this).val()+","); 
	        	  }
	          }
	    });
	    if(checkValues!=""){
	    	checkValues = checkValues.substring(0,checkValues.lastIndexOf(","));
	    }
	    return checkValues;
    },
    /**
     * 判断是否提交成功
     */
    isSubmitSuccess:function(){
    	var submitSuccess = PlatUtil.getData("submitSuccess");
    	if(submitSuccess){
    		PlatUtil.removeData("submitSuccess");
    	}
    	return submitSuccess;
    },
	/**
	 * 关闭窗口
	 */
	closeWindow:function(){
		//先得到当前iframe层的索引
		var index = parent.layer.getFrameIndex(window.name); 
		parent.layer.close(index); //再执行关闭   
	},
	/**
	 * 发起ajax加载请求
	 * @param config
	 */
	ajaxProgress:function(config){
		var showProgress = (config.showProgress&&config.showProgress=="-1")?false:true;
		var progressMsg = config.progressMsg?config.progressMsg:"提交请求中";
		var msgIndex = null;
		if(showProgress){
			if(parent.layer){
				msgIndex = parent.layer.msg(progressMsg, {
					  icon: 16,
					  shade: 0.01,
                    time: config.timeout ? config.timeout : 600000
				 });
			}else{
				msgIndex = layer.msg(progressMsg, {
					  icon: 16,
					  shade: 0.01,
                    time: config.timeout ? config.timeout : 600000
				 });
			}
		}
		var async = true;
		if(config.async&&config.async=="-1"){
			async = false;
		}
		$.ajax({
           type: "POST",
           url: config.url,
           async:async,
           //超时时间设置，单位毫秒
            timeout: config.timeout ? config.timeout : 600000,
           cache: false,
           data:config.params, 
           success: function (responseJsonObj,status) {
        	   //将对象转换成JSON字符串  JSON.stringify
        	   //将JSON字符串转换成对象  $.parseJSON(Obj);
        	   if(msgIndex){
        		   parent.layer.close(msgIndex);
        	   }
        	   if(responseJsonObj&&responseJsonObj!=null){
        		   if(config.callback!=null){
     					config.callback.call(this,responseJsonObj);
     			   }
        	   }
           },
           complete : function(XMLHttpRequest,status){
        	   if(msgIndex){
        		   parent.layer.close(msgIndex);
        	   }
               if(status=="timeout"){
           	      alert("请求超时,请重新提交!");
               }else if(status=="error"){
                  alert("服务器出错，请联系管理员!");
               }
	        }
       });
	},
	/**
	 * 获取表单字段修改JSON字符串
	 */
	getFormFieldValueModifyArrayJSON:function(){
		var formFieldOriginArray = [];
		$("form[id]").each(function(index,obj){
			var formId = $(obj).attr("id");
			var formOrignDataList = PlatUtil.getFormOrignData(formId,true);
			formFieldOriginArray = PlatUtil.mergeObject(formFieldOriginArray,formOrignDataList);
		});
		formFieldOriginArray =  PlatUtil.mergeObject(formFieldOriginArray,PlatUtil.FORM_FIELD_VALUE_ARRAY);
		return JSON.stringify(formFieldOriginArray);
	},
	/**
	 * 初始化表单原始值对象
	 */
	initFormSourceDatas:function(){
		var formFieldOriginArray = [];
		$("form[id]").each(function(index,obj){
			var formId = $(obj).attr("id");
			var formOrignDataList = PlatUtil.getFormOrignData(formId);
			formFieldOriginArray = PlatUtil.mergeObject(formFieldOriginArray,formOrignDataList);
		});
		PlatUtil.FORM_FIELD_VALUE_ARRAY = formFieldOriginArray;
	},
	/**
	 * 获取表单的原始值
	 * @param elementId
	 */
	getFormOrignData:function(elementId,isNewValue){
		var formDataArray = [];
		var selectRules = "*[name][unsubmit!='true']";
		var fieldNameArray = [];
	    $("#"+elementId).find(selectRules).each(function(){
	    	  var fieldData = {};
	    	  var FIELD_CN = null;
	    	  var FIELD_EN = null;
	    	  var FIELD_OLDVALUE = null;
	          var inputName= $(this).attr("name");
	          var inputValue = $(this).val();
	          //获取元素的类型
			  var fieldType = $(this).attr("type");
			  var tagName = $(this).get(0).tagName;
			  if(fieldType=="radio"){
			  	  var isChecked = $(this).is(':checked');
			  	  if(isChecked){
			  		 FIELD_EN = inputName;
			  		 //获取单选框的所在的DIV
			  		 var radioDiv = $("#RADIO_"+FIELD_EN+"_DIV");
			  		 if(radioDiv){
			  			 var label_value = radioDiv.attr("label_value");
			  			 if(label_value&&label_value!=""){
			  				FIELD_CN = label_value;
			  			 }
			  		 }
			  		 var checkLabel = PlatUtil.getCheckRadioTagValue(inputName,"LABEL");
			  		 FIELD_OLDVALUE = checkLabel;
			  	  }
			  }else if(fieldType=="checkbox"){
			  	  var inputValues = PlatUtil.getCheckBoxValues(inputName,"#"+elementId,"LABEL");
			  	  var checkBoxDiv = $("#CHECKBOX_"+inputName+"_DIV");
		  		  if(checkBoxDiv){
		  			 var label_value = checkBoxDiv.attr("label_value");
		  			 if(label_value&&label_value!=""){
		  				FIELD_CN = label_value;
		  			 }
		  		  }
			  	  FIELD_EN = inputName;
		  		  FIELD_OLDVALUE = inputValues;
			  }else if(tagName=="SELECT"){
				  if($(this).attr("multiselect")){
					  var multiValues =  inputValue;
					  var selectValues = "";
					  if(multiValues){
						  $.each(multiValues,function(index,objvalue){
							  if(index>0){
								  selectValues+=",";
							  }
							  selectValues+=objvalue;
						  });
					  }
					  FIELD_EN = inputName;
			  		  FIELD_OLDVALUE = selectValues;
				  }else{
					  FIELD_EN = inputName;
			  		  FIELD_OLDVALUE = PlatUtil.getSelectAttValue(inputName,"label");
				  }
				  var label_value = $(this).attr("label_value");
	  			  if(label_value&&label_value!=""){
	  				FIELD_CN = PlatUtil.trim(label_value);
	  			  }
			  }else if(fieldType=="hidden"){
				  var isewebeditor = $(this).attr("isewebeditor");
				  if(isewebeditor){
					  /*var controlId = $(this).attr("id");
					  var htmlCode = document.getElementById(controlId+"_EWEB").contentWindow.getHTML();
					  $("#"+controlId).val(htmlCode);
					  FIELD_EN = inputName;
			  		  FIELD_OLDVALUE = htmlCode;*/
				  }else{
					  FIELD_EN = inputName;
			  		  FIELD_OLDVALUE = PlatUtil.trim(inputValue);
				  }
				  var label_value = $(this).attr("label_value");
	  			  if(label_value&&label_value!=""){
	  				FIELD_CN = label_value;
	  			  }
			  }else{
				  var label_value = $(this).attr("label_value");
	  			  if(label_value&&label_value!=""){
	  				FIELD_CN = label_value;
	  			  }
				  FIELD_EN = inputName;
		  		  FIELD_OLDVALUE = PlatUtil.trim(inputValue);
			  }
			  if(FIELD_EN){
				  if(!PlatUtil.isContain(fieldNameArray,FIELD_EN)){
					  fieldData.FIELD_CN = FIELD_CN;
					  fieldData.FIELD_EN = FIELD_EN;
					  if(isNewValue){
						  fieldData.FIELD_NEWVALUE = FIELD_OLDVALUE;
					  }else{
						  fieldData.FIELD_OLDVALUE = FIELD_OLDVALUE;
					  }
					  formDataArray.push(fieldData);
					  fieldNameArray.push(FIELD_EN);
				  }
			  }
	          
	    });
	    return formDataArray;
	},
	/**
	 * 设置表单值
	 * @param elementId:元素ID
	 * @param formdata:数据源
	 */
	setFormEleData:function(elementId,formData){
		$("#"+elementId).find("*[name]").each(function(){
	          var inputName= $(this).attr("name");
	          var inputValue = $(this).val();
	          //获取元素的类型
			  var fieldType = $(this).attr("type");
			  var tagName = $(this).get(0).tagName;
			  if(fieldType=="radio"){
			  	  if(formData[inputName]){
			  		PlatUtil.setCheckRadio(inputName,formData[inputName]);
			  	  }
			  }else if(fieldType=="checkbox"){
				  if(formData[inputName]){
			  		PlatUtil.setCheckBoxValues(inputName,formData[inputName]);
			  	  }
			  }else if(tagName=="SELECT"){
				  if(formData[inputName]){
			  		PlatUtil.changeSelect2Val("[name='"+inputName+"']",formData[inputName]);
			  	  }
			  }else if(fieldType=="hidden"){
				  if(formData[inputName]){
					  $(this).val(formData[inputName]);
			  	  }
			  }else{
				  if(formData[inputName]){
					  $(this).val(formData[inputName]);
			  	  }
			  }
	          
	    });
	},
	/**
	 * 根据传入的元素id获取子孙节点元素值
	 * @param {} elementId
	 */
	getFormEleData:function(elementId,allSubmit,isSearch){
		var formData = {};
		var selectRules = "*[name][unsubmit!='true']";
		if(allSubmit){
			selectRules = "*[name]";
		}
	    $("#"+elementId).find(selectRules).each(function(){
	          var inputName= $(this).attr("name");
	          var inputValue = $(this).val();
	          //获取元素的类型
			  var fieldType = $(this).attr("type");
			  var tagName = $(this).get(0).tagName;
			  if(fieldType=="radio"){
			  	  var isChecked = $(this).is(':checked');
			  	  if(isChecked){
			  	  	 formData[inputName] = inputValue;
			  	  }
			  }else if(fieldType=="checkbox"){
			  	  var inputValues = PlatUtil.getCheckBoxValues(inputName,"#"+elementId);
			  	  formData[inputName] = inputValues;
			  }else if(tagName=="SELECT"){
				  if($(this).attr("multiselect")){
					  var multiValues =  inputValue;
					  var selectValues = "";
					  if(multiValues){
						  $.each(multiValues,function(index,objvalue){
							  if(index>0){
								  selectValues+=",";
							  }
							  selectValues+=objvalue;
						  });
					  }
					  formData[inputName] = selectValues;
				  }else{
					  formData[inputName] = inputValue;
				  }
			  }else if(fieldType=="hidden"){
				  var isewebeditor = $(this).attr("isewebeditor");
				  if(isewebeditor){
					  var controlId = $(this).attr("id");
					  var htmlCode = document.getElementById(controlId+"_EWEB").contentWindow.getHTML();
					  $("#"+controlId).val(htmlCode);
					  formData[inputName] = htmlCode;
				  }else{
					  formData[inputName] = PlatUtil.trim(inputValue);
				  }
			  }else{
				  if(isSearch){
					  var className = $(this).attr("class");
					  //获取提交属性 
					  var posttimefmt = $(this).attr("posttimefmt");
					  if(className=="wicon form-control"&&(inputName.indexOf("GE")!=-1||inputName.indexOf("LE")!=-1)){
						  var length = inputValue.length;
						  if(length>0){
							  var geAppend = "";
							  var leAppend = "";
							  if(length>1&&length<=12){
								  geAppend=" 00:00:00";
								  leAppend=" 23:59:59";
							  }else if(length>12&&length<=17){
								  geAppend=":00";
								  leAppend=":59";
							  }
							  if(posttimefmt&&posttimefmt=="1"){
								  geAppend = "";
								  leAppend = "";
							  }else if(posttimefmt&&posttimefmt=="2"){
								  geAppend=" 00:00:00";
								  leAppend=" 23:59:59";
							  }
							  if(inputName.indexOf("GE")!=-1){
									formData[inputName] = inputValue+geAppend;
							  }
							  if(inputName.indexOf("LE")!=-1){
									formData[inputName] = inputValue+leAppend;
							  }
						  }else{
							  formData[inputName] = "";
						  }
					  }else{
						  formData[inputName] = PlatUtil.trim(inputValue);
					  }
				  }else{
					  formData[inputName] = PlatUtil.trim(inputValue);
				  }
			  }
	          
	    });
	    return formData;
	},
	/**
	 * 合并对象
	 * @param {} sourceObj:源对象
	 * @param {} targetObj:目标对象
	 */
	mergeObject:function(targetObj,sourceObj){
    	return $.extend(true, targetObj, sourceObj);
	},
	/**
	 * 获取树形勾选数据
	 */
	getTreeCheckedValue:function(treeId){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.getCheckedNodes(true);
		var checkRecords = [];
		var checkIds = "";
		var checkNames = "";
		if (nodes != null && nodes.length > 0) {
			for (var i = 0; i < nodes.length; i++) {
				if (nodes[i].id != "0") {
					checkIds += nodes[i].id + ",";
					checkNames += nodes[i].name + ",";
					checkRecords.push(nodes[i].id);
				}
			}
			if (checkRecords.length >= 1) {
				checkIds = checkIds.substring(0, checkIds.length - 1);
				checkNames = checkNames.substring(0, checkNames.length - 1);
			}
		} 
		var checkValue = {
			checkIds:checkIds,	
			checkNames:checkNames
		};
		return checkValue;
	},
	/**
	 * 获取树形节点的高亮样式
	 */
	getTreeNodeHighLightFont:function(treeId, treeNode){
		return (!!treeNode.highlight) ? {color:"#E74C3C", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};  
	},
	/**
	 * 默认树形右键点击事件
	 */
	onZtreeRightClick:function(event, treeId, treeNode){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		treeObj.selectNode(treeNode);
		PlatUtil.showZtreeRightMenu(event.clientX, event.clientY);
	},
	/**
	 * 隐藏ztree的右键菜单
	 */
	hideZtreeRightMenu:function(){
		var ztreeRightMenu = $("#ztreeRightMenu");
		if (ztreeRightMenu){
			ztreeRightMenu.css({"visibility": "hidden"})
		};
		$("body").unbind("mousedown", PlatUtil.onZtreeBodyMouseDown);
	},
	/**
	 * 显示ztree树形的右键菜单
	 */
	showZtreeRightMenu:function(x,y){
		$("#ztreeRightMenu ul").show();
		$("#ztreeRightMenu").css({"top":y+"px", "left":x+"px","z-index":"999","position":"fixed", "visibility":"visible"});
		$("body").bind("mousedown", PlatUtil.onZtreeBodyMouseDown);
	},
	/**
	 * 树形body点击事件
	 */
	onZtreeBodyMouseDown:function(event){
		if (!(event.target.id == "ztreeRightMenu" || $(event.target).parents("#ztreeRightMenu").length>0)) {
			$("#ztreeRightMenu").css({"visibility" : "hidden"});
		}
	},
	/**
	 * 展开或者折叠ztree
	 */
	openFolderZtree:function(ztreeId,state){
		var treeObj = $.fn.zTree.getZTreeObj(ztreeId); 
		treeObj.expandAll(state);
	},
	/**
	 * 展开或者折叠小于某个等级的ztree节点
	 */
	expandZtreeNodeByLessLevel:function(treeId,levelIndex,expandFlag){
		var treeObj = $.fn.zTree.getZTreeObj(treeId); 
		var treeNodes = treeObj.transformToArray(treeObj.getNodes());
		for (var i = 0; i < treeNodes.length; i++) {
			if(treeNodes[i].level<=levelIndex){
				treeObj.expandNode(treeNodes[i], expandFlag, false, false);
			}
		}
	},
	/**
	 * ztree节点拖放事件的实现
	 */
	onZtreeNodeDrop:function(event, treeId, treeNodes, targetNode, moveType, isCopy){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var tableName = treeObj.setting.async.otherParam.tableName;
		if(moveType!=null){
			if(targetNode){
				PlatUtil.ajaxProgress({
					   url:"common/baseController.do?updateTreeSn",
					   params:{
						   "dragTreeNodeId":treeNodes[0].id,
						   "dragTreeNodeNewLevel":treeNodes[0].level,
						   "targetNodeId":targetNode.id,
						   "moveType":moveType,
						   "targetNodeLevel":targetNode.level,
						   "tableName":tableName
					   },
					   callback:function(resultJson){
							if(resultJson.success){
								/* $.fn.zTree.getZTreeObj("dicTypeTree").reAsyncChildNodes(null, "refresh"); */
				    	   	    parent.layer.msg("成功完成拖拽排序!", {icon: 1});
							}else{
								parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
								$.fn.zTree.getZTreeObj(treeId).reAsyncChildNodes(null, "refresh");
							}
					   	    
					   }
					});
			}else{
				$.fn.zTree.getZTreeObj(treeId).reAsyncChildNodes(null, "refresh");
			}
			
		}
	},
	/**
	 * 更新树形节点
	 */
	updateZtreeNodes:function(treeId,highlight,resultNodeList){
		var treeObj = $.fn.zTree.getZTreeObj(treeId); 
	    //是否有查询条件
	    if(highlight==false){
	    	//折叠全部的节点
		    treeObj.expandAll(false);
		    //默认展开第二级的节点
		    PlatUtil.expandZtreeNodeByLessLevel(treeId,0,true);
	    }
	    var treeNodes = treeObj.transformToArray(treeObj.getNodes());
		for (var i = 0; i < treeNodes.length; i++) {
			treeNodes[i].highlight = false;
			treeObj.updateNode(treeNodes[i]);
			treeObj.showNode(treeNodes[i]);
			if(highlight==true){
				//treeObj.hideNode(treeNodes[i]);
			}
		}
		if(resultNodeList!=null&&highlight==true){
			for (var i = 0; i < resultNodeList.length; i++) {
				resultNodeList[i].highlight = true;
				treeObj.updateNode(resultNodeList[i]);
				treeObj.showNode(resultNodeList[i]);
				//高亮显示节点的父节点的父节点....直到根节点，并展示
				var parentNode = resultNodeList[i].getParentNode();
				var parentNodes = PlatUtil.getZtreeAllParentNodes(treeId, parentNode);
				treeObj.expandNode(parentNodes, true, false, true);
				treeObj.expandNode(parentNode, true, false, true);
				treeObj.expandNode(resultNodeList[i], true, false, true);
			}
		}
	},
	/**
	 * 获取Ztree所有父亲节点
	 */
	getZtreeAllParentNodes:function(treeId, node){
		if (node != null) {
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			treeObj.showNode(node);
			var parentNode = node.getParentNode();
			return PlatUtil.getZtreeAllParentNodes(treeId, parentNode);
		} else {
			return node;
		}
	},
	/**
	 * 查询ztree的数据
	 */
	searchZtree:function(treeId){
		var value = $("input[name='"+treeId+"QueryParam']").val();
		var nodeList = new Array();
		var highlight = false;
		if(value != ""){  
			value = PlatUtil.trim(value);
	        var treeObj = $.fn.zTree.getZTreeObj(treeId);  
	        nodeList = treeObj.getNodesByParamFuzzy("name", value,null);   
	        highlight = true;
	    }  
		PlatUtil.updateZtreeNodes(treeId,highlight,nodeList); 
	},
	/**
	 * 删除ztree节点数据
	 */
	deleteZtreeNode:function(config){
		PlatUtil.hideZtreeRightMenu();
		var selectTreeNode = $.fn.zTree.getZTreeObj(config.treeId).getSelectedNodes()[0];
		var id = selectTreeNode.id;
		if(id=="0"){
			parent.layer.alert("根节点不能进行删除!",{icon: 2,resize:false});
		}else{
			parent.layer.confirm("注意!删除该节点数据,将会连带删除该子孙节点数据,您确定删除吗?", {
			    resize :false
			}, function(){
				PlatUtil.ajaxProgress({
					url:config.url,
					params:{
						treeNodeId:id
					},
					callback : function(resultJson) {
						if (resultJson.success) {
							if(resultJson.msg){
								var index  = parent.layer.alert(resultJson.msg,{icon: 1,resize:false},function(){
									$.fn.zTree.getZTreeObj(config.treeId).reAsyncChildNodes(null, "refresh");
									parent.layer.close(index);
								});
							}else{
								var index  = parent.layer.alert(PlatUtil.SUCCESS_MSG,{icon: 1,resize:false},function(){
									$.fn.zTree.getZTreeObj(config.treeId).reAsyncChildNodes(null, "refresh");
									parent.layer.close(index);
								});
							}
							 if(config.callback!=null){
		     					config.callback.call(this,resultJson);
		     			    } 
						} else {
							if(resultJson.msg){
								parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
							}else{
								parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
							}
						}
					}
				});
			}, function(){
				
			});	
		}
	},
	/**
	 * 绑定自动补全组件
	 * 自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入autoServerUrl)
	 */
	bindAutoComplete:function(config){
		//获取补全类型
		var autoCompleteType = config.autoCompleteType;
		var url = config.url;
		var compName = config.compName;
		var compConfig = {
		     'itemHeight': 20,//行高
		     'maxItems': 5,//显示几条
		     'maxHeight':200,//最大高度
		     'width': 'auto',
		     'listStyle': 'custom',
	         'createItemHandler': function(index, data){
	             var div = "<div>"+data.label+"</div>"
	             return div;
	         },
	         'onerror': function(msg){alert(msg);}	
		};
		if(autoCompleteType=="1"){
			PlatUtil.ajaxProgress({
				url:url,
				showProgress:false,
				params : config.params,
				callback : function(resultJson) {
					compConfig.data = resultJson;
					compConfig.matchHandler = function(keyword, data){
			         	  var regex = RegExp("("+keyword.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1")+")", 'ig');
			              data.label = data.label.replace(regex, "<span style='font-weight:bold;color:red;'>$1</span>");
			              var py = false;
			              var reg= /^[a-zA-Z0-9]+$/;
			              if (reg.test(keyword)){
			            	  var keypy = keyword.toUpperCase();
			            	  var valuepy = pinyin.getCamelChars(data.value);
			            	  if(valuepy.indexOf(keypy) > -1){
			            		  py = true; 
			            	  }
			              }
			              return data.value.indexOf(keyword) > -1||py;  
			        };
					$("[name='"+compName+"']").AutoComplete(compConfig).AutoComplete('show');  
				}
			});
		}else{
			compConfig.data = url;
			compConfig.ajaxDataType = "json";
			compConfig.ajaxType = "POST";
			compConfig.ajaxParams = config.params;
			compConfig.matchHandler = function(keyword, data){
				var regex = RegExp("("+keyword.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1")+")", 'ig');
	            data.label = data.label.replace(regex, "<span style='font-weight:bold;color:red;'>$1</span>");
	            return true;  // 匹配规则: 以输入框中的值开头且大小写敏感
	        };
	        $("[name='"+compName+"']").AutoComplete(compConfig).AutoComplete('show');  
		}
		
	},
	/**
	 * 刷新ztree树形面板
	 */
	refreshZtreePanel:function(zTree){
		var treeId = zTree.setting.treeId;
        PlatUtil.bindAutoComplete({
			url:zTree.setting.autoServerUrl,
			autoCompleteType:zTree.setting.autoCompleteType,
			params:zTree.setting.async.otherParam,
			compName:treeId+"QueryParam"
		});
        zTree.reAsyncChildNodes(null, "refresh");
	},
	/**
	 * 初始化ztree
	 */
	initZtree:function(config){
		var initSetting = {
			check : {
				enable : false
			},
			edit : {
				enable : true,
				showRemoveBtn : false,
				showRenameBtn : false
			},
			view : {
				selectedMulti : false,
				fontCss: PlatUtil.getTreeNodeHighLightFont 
			},
			async : {
				enable : true
			}
		};
		var treeSetting = PlatUtil.mergeObject(initSetting,config);
		//自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		//如果需要自定义服务端url,请传入autoServerUrl
		if(treeSetting.autoCompleteType){
			var url = treeSetting.autoServerUrl?treeSetting.autoServerUrl:"common/baseController.do?autoComplete";
			PlatUtil.bindAutoComplete({
				url:url,
				autoCompleteType:treeSetting.autoCompleteType,
				params:treeSetting.async.otherParam,
				compName:config.treeId+"QueryParam"
			});
		}
		
		$("[name='"+config.treeId+"TreeOpenFoldSwitch']").bootstrapSwitch({
			  onSwitchChange: function(event,state) {
				  PlatUtil.openFolderZtree(config.treeId,state);
			  }
	    });
		//初始化树形面板提示
		$(".treepaneltip").tipsy({gravity: 'nw',fallback:"操作提示<br/>右键进行数据操作,树支持拖拽排序",html: true,opacity: 1});
		var dataNodes = config.dataNodes;
		if(dataNodes){
			return $.fn.zTree.init($("#"+config.treeId), treeSetting,dataNodes);
		}else{
			return $.fn.zTree.init($("#"+config.treeId), treeSetting);
		}
		
	},
	/**
	 * 获取对象的长度
	 */
	getObjLength:function(srcObj){
		var objLength = 0;
    	for(var index in srcObj){
    		objLength++;
    	}
    	return objLength;
	},
	/**
	 * 进行可编辑表格的搜索
	 * @param tableId
	 * @param searchpanelId
	 */
	editTableDoSearch:function(tableId,searchpanelId,showOrHideSearchTable){
		var searchTable = $("input[name='search'][readonly='readonly']");
		if(showOrHideSearchTable){
			PlatUtil.showOrHideSearchTable(searchTable);
		}
	    PlatUtil.loadEditTable({
	    	searchPanelId:searchpanelId,
	    	tableId:tableId
	    });
	},
	/**
	 * 添加可编辑表格的行
	 */
	addEditTableRow:function(config){
		var tableId = config.tableId;
		var tbodyId = tableId+"Tbody";
		var TR_INDEX = $("#"+tbodyId).children("tr").length+1;
		var tr_tplpath = $("#"+tableId).attr("tr_tplpath");
		config.TR_INDEX = TR_INDEX;
		config.tr_tplpath = tr_tplpath;
		PlatUtil.ajaxProgress({
			url : "common/baseController/loadEditTableRow.do",
			params :config,
			callback : function(resultHtml) {
				var trId = $(resultHtml).attr("id");
				$("#"+tableId+" input[name='AllSelectCheckBox']").prop("checked",false);
				$("#"+tbodyId).append(resultHtml);
				//var trId = tableId+"_"+TR_INDEX;
				PlatUtil.initUIComp("#"+trId);
			}
		});
	},
	/**
	 * 重新加载可编辑表格
	 */
	loadEditTable:function(config){
		var searchPanelId = config.searchPanelId;
		var tableId = config.tableId;
		var tbodyId = tableId+"Tbody";
		var tr_tplpath = $("#"+tableId).attr("tr_tplpath");
		var params = config;
		var queryParams = null;
		if(searchPanelId){
	        queryParams =  PlatUtil.getQueryParams(searchPanelId);
	        params = PlatUtil.mergeObject(params,queryParams)
		}
		params.tr_tplpath = tr_tplpath;
		PlatUtil.ajaxProgress({
			url : "common/baseController/loadEditTableRow.do",
			params:params,
			callback : function(resultHtml) {
				$("#"+tbodyId).html(resultHtml);
				PlatUtil.initUIComp("#"+tbodyId);
			}
		});
	},
	/**
	 * 获取选择的网格项目数组列表
	 */
	getSelectedGridItem:function(gridItemId){
		var selectedGridItems =$("#"+gridItemId).find(".card-box");
		var selectItemList = [];
		$(selectedGridItems).each(function(index,obj){
			var itemList = $(obj).find("p[itemkey]");
			var selectItem = {};
			$(itemList).each(function(pindex,pobj){
				var itemKey = $(pobj).attr("itemkey");
				var itemValue = $(pobj).attr("title");
				selectItem[itemKey] = itemValue;
			});
			selectItemList.push(selectItem);
		});
		return selectItemList;
	},
	/**
	 * 批量选中网格项目
	 * @param treeId
	 * @param checkIds
	 * @param isCheckBox
	 * @param gridItemId
	 */
	selectedGridItems:function(treeId,treeNode,isCheckBox,gridItemId){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
        var nodes = treeObj.getNodesByFilter(function(node){
           return true;
        },false,treeNode);    
        var checkIds = "";
        nodes.push(treeNode);
        for(var index in nodes){
          var childNode = nodes[index];
          if(index>0){
            checkIds+=",";
          }
          checkIds+=childNode.id;
        }
		if(!isCheckBox){
			treeObj.checkNode(treeNode, !treeNode.checked, true); 
		}
		if(treeNode.checked){
			var userDivLength = $("#"+gridItemId).find("#"+treeNode.id).length;
	        if(userDivLength==0){
	        	PlatUtil.loadGridItemTable({
	    		    gridItemId:gridItemId,
	    			selectedRecordIds:checkIds
	    	    });
			}
		}else{
			var checkIdArray = checkIds.split(",");
		    $.each(checkIdArray,function(index,value){
		    	$("#"+value).remove();
		    });
			
		}
	},
	/**
	 * 选中网格项目
	 */
	selectedGridItem:function(treeId, treeNode,isCheckBox,gridItemId){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		if(!isCheckBox){
			treeObj.checkNode(treeNode, !treeNode.checked, true); 
		}
		if(treeNode.checked){
			var userDivLength = $("#"+gridItemId).find("#"+treeNode.id).length;
	        if(userDivLength==0){
	        	PlatUtil.loadGridItemTable({
	    		    gridItemId:gridItemId,
	    			selectedRecordIds:treeNode.id
	    	    });
			}
		}else{
			$("#"+treeNode.id).remove();
		}
	},
	/**
	 * 搜索网格表格
	 */
	searchGridItem:function(gridItemId){
		var searchValue = $("input[name='"+gridItemId+"searchItem']").val();
		if(searchValue){
			searchValue = PlatUtil.trim(searchValue);
			$("#"+gridItemId).find(".card-box").each(function(){
				var dataRecord = $(this).attr("data-record");
				if(dataRecord.indexOf(searchValue) > -1 ){
				    $(this).css("display","");
				}else{
					$(this).css("display","none");
				}
			});
		}else{
			$("#"+gridItemId).find(".card-box").each(function(){
			    $(this).css("display","");
			});
		}
	},
	/**
	 * 加载网格表格
	 */
	loadGridItemTable:function(config){
		var gridItemId = config.gridItemId;
		var selectObj = $("#"+gridItemId);
		var params = {};
		$.each(selectObj.get(0).attributes, function(i, obj){
			params[obj.name] = obj.value;
		}); 
		params = PlatUtil.mergeObject(params,config);
		PlatUtil.ajaxProgress({
			url : "appmodel/CommonUIController/loadGridItem.do",
			params:params,
			callback : function(resultHtml) {
				$("#"+gridItemId).append(resultHtml);
			}
		});
	},
	/**
	 * 获取已选表格的已选择的记录集合
	 * @param tableId
	 */
	getSelectedTableRecord:function(tableId){
		var checkRecords = {};
		var checkIds = "";
		var checkNames = "";
		var length = 0;
		$("#"+tableId+" input[value]:checkbox").each(function(index){ 
		    var checkId = $(this).val();
		    var checkName = $(this).attr("label");
		    if(index>0){
		    	checkIds+=",";
		    	checkNames+=",";
		    }
		    checkIds+=checkId;
		    checkNames+=checkName;
		    length++;
		});
		checkRecords.checkIds = checkIds;
		checkRecords.checkNames = checkNames;
		checkRecords.length = length;
		return checkRecords;
	},
	/**
	 * 加载已选表格
	 */
	loadSelectedTable:function(config){
		var tableid = config.tableid;
		var selectObj = $("#"+tableid);
		var params = {};
		$.each(selectObj.get(0).attributes, function(i, obj){
			params[obj.name] = obj.value;
		}); 
		var currentrecordIds = "";
		$("#"+tableid+" input[value]:checkbox").each(function(index){ 
		    var checkValue = $(this).val();
		    if(index>0){
		    	currentrecordIds+=",";
		    }
		    currentrecordIds+=checkValue;
		});
		params = PlatUtil.mergeObject(params,config);
		params.currentrecordIds = currentrecordIds;
		PlatUtil.ajaxProgress({
			url : "appmodel/CommonUIController/loadSelectedTable.do",
			params:params,
			callback : function(resultHtml) {
				$("#"+tableid+"_tbody").append(resultHtml);
				var selectedCount = $("#"+tableid+" input[value]:checkbox").length;
				$("#"+tableid+"_count").text(selectedCount);
			}
		});
	},
	/**
	 * 删除JqGrid树形表格节点数据
	 */
	deleteJqTreeGridNode:function(config){
		var treeNodeId = config.treeNodeId;
    	if(treeNodeId=="0"){
    		parent.layer.alert("根节点禁止删除!",{icon: 2,resize:false});
    	}else{
    		parent.layer.confirm("注意!删除该节点数据,将会连带删除该子孙节点数据,您确定删除吗?", {
			    resize :false
			}, function(){
				PlatUtil.ajaxProgress({
					url:config.url,
					params:{
						treeNodeId:treeNodeId
					},
					callback : function(resultJson) {
						if (resultJson.success) {
							if(resultJson.msg){
								parent.layer.alert(resultJson.msg,{icon: 1,resize:false});
							}else{
								parent.layer.alert(PlatUtil.SUCCESS_MSG,{icon: 1,resize:false});
							}
							$("#"+config.tableId).trigger("reloadGrid"); 
							if(config.callback!=null){
		     					config.callback.call(this,resultJson);
		     			    } 
						} else {
							if(resultJson.msg){
								parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
							}else{
								parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
							}
						}
					}
				});
			}, function(){
				
			});	
    	}
	},
	/**
	 * 初始化JqGrid的树形表格
	 */
	initJqTreeGrid:function(config){
		var searchPanelId = config.searchPanelId;
		var tableId = config.tableId;
		var queryParams = null;
		if(searchPanelId){
	        queryParams =  PlatUtil.getQueryParams(searchPanelId);
		}
		var initConfig = {
			datatype: "json",
            mtype : "post",
            autowidth: true,
            height: $(window).height()-62,
            treeGrid: true,
            postData:queryParams,
            treeGridModel: "adjacency",
            ExpandColClick:true,
            rowNum: "all",
            rownumbers: true
		};
		var jqGridConfig = PlatUtil.mergeObject(initConfig,config);
		var oldGridComplete = jqGridConfig.gridComplete;
		jqGridConfig.gridComplete = function(){
			var length = $("#jqtreeContextmenu").find("a[fnname]").length;
        	if(length&&length!=0){
        		$("tr.jqgrow","#"+tableId).contextmenu({
			        target: "#jqtreeContextmenu",
			        onItem: function(context,e) {
			          var name = $(e.target).attr("name");
			          var fnname = $(e.target).attr("fnname");
			          var id = $(context).attr("id");
	            	  var rowData = $("#"+tableId).jqGrid("getRowData",id);
	            	  if(fnname){
	            		  eval(fnname).call(this,rowData);
	            	  }
			        }
		        }); 
        	}
            if(oldGridComplete){
            	oldGridComplete.call();
            }
            
		};
		$("#"+tableId).jqGrid(jqGridConfig);
	},
	/**
	 * 初始化jqgrid配置
	 */
	initJqGrid:function(config){
		var searchPanelId = config.searchPanelId;
		var tableId = config.tableId;
		var queryParams = null;
		var pager = "#"+tableId+"_pager";
		var pageHeight = 25;
		if(searchPanelId){
	        queryParams =  PlatUtil.getQueryParams(searchPanelId);
		}
		if(config.nopager){
			pager = null;
			pageHeight = 0;
		}
		var grandpaClass = $("#"+tableId).parent().parent().attr("class");
		var parentHeight = 0;
		if(grandpaClass=="platdesigncomp"){
			parentHeight = $("#"+tableId).parent().parent().parent().height();
			pageHeight+=30;
		}else{
			parentHeight = $("#"+tableId).parent().parent().height();
		}
		var searchPanelHeight = $("#"+tableId).parent().prev().height();
		var titlePanelHeight = 0;
		if($("#"+tableId+"paneltitle").length>0){
			titlePanelHeight = $("#"+tableId+"paneltitle").parent().height();
		}
		var rightCopyFootHeight = 0;
		//查找Iframe的子孙元素
		var iframeLength = $(window.parent.document).find("[class='Plat_iframe'][style!='display: none;']").contents().find("#"+tableId).length;
		if(iframeLength>0){
			rightCopyFootHeight=parent.$("#rightCopyFoot").height()+6;
		}
		var jqgridhdivHeight = 0;
		if(rightCopyFootHeight==0){
			jqgridhdivHeight = 30;
		}
		//报表标题高度
		var reportTitleHeight = 0;
		if($(".jqgrid-ui-report").length>0){
			reportTitleHeight = $(".jqgrid-ui-report").height()+41;
		}
		if(config.groupHeaders){
			reportTitleHeight += 30;
		}
		if(config.footerrow){
			reportTitleHeight += 28;
		}
		var minusHeight = searchPanelHeight+titlePanelHeight+pageHeight+rightCopyFootHeight+jqgridhdivHeight+reportTitleHeight;
		var initConfig = {
            datatype: "json",
            mtype : "post",
            loadui:"block",
            postData:queryParams,
            autowidth: true,
	        rowNum : 30,
	        rowList : [ 30,50,100,200],
	        pager : pager,
	        height: parentHeight - minusHeight,
            //定义是否要显示总记录数
            viewrecords: true,
            //定义是否显示行号
            rownumbers: true,
            //是否存在复选框
            multiselect: true,//复选框 
            //是否只能选择一个
            multiboxonly:false//是否只选择一个	
		};
		var jqGridConfig = PlatUtil.mergeObject(initConfig,config);
		var oldGridComplete = jqGridConfig.gridComplete;
		jqGridConfig.gridComplete = function(){
			var rownum = $("#"+tableId+" [role='row']:last [role='gridcell']:first").text();
            if(rownum.length>3){
            	var s = $("#"+tableId+"_rn").width()-30;
        		 $("#"+tableId+"_rn").css("width",(rownum.length*10)+"px");
	             $("#"+tableId+" .jqgfirstrow").find("[role='gridcell']:first").css("width",(rownum.length*10)+"px");
	             var ed = rownum.length -3;
	             var newWidth = $("#"+tableId+" .jqgfirstrow").find("[role='gridcell']:last").width()+s-ed*10;
	             $("#"+tableId+" .jqgfirstrow").find("[role='gridcell']:last").css("width",newWidth+"px");
	             $("[aria-labelledby='gbox_"+tableId+"']").find("[role='columnheader']:last").css("width",newWidth+"px");
            }else{
            	$("#"+tableId+"_rn").css("width","30px");
	             $("#"+tableId+" .jqgfirstrow").find("[role='gridcell']:first").css("width","30px");
            }
            if(oldGridComplete){
            	oldGridComplete.call();
            }
            
		};
		$("#"+tableId).jqGrid(jqGridConfig);
		//让表格能够拖拽排序
		if(config.sortable){
			$("#"+tableId).jqGrid("sortableRows");
		}
		
	},
	/**
	 * 格式化下拉树的图片
	 */
	formatTreeSelectState:function(state){
		if (!state.id) {
			return state.text; 
		}
		//获取是否叶子节点
		var isleaf = $(state.element).attr("isleaf");
		var $state = null;
		if(isleaf&&isleaf=="true"){
			var span = "<span><img src=\"plug-in/platform-1.0/images/file_01.png\" style=\"height:18px;width:18px;\" />";
			span+=(state.text+"</span>");
			$state = $(span);
		}else{
			var span = "<span><img src=\"plug-in/platform-1.0/images/folder_01.png\" style=\"height:15px;width:18px;\" />";
			span+=(state.text+"</span>");
			$state = $(span);
		}
		return $state;
	},
	/**
	 * 打开弹出框
	 */
	openWindow:function(config){
		var initSet = {
			type: 2,
			resize:false
		};
	    var openConfig = PlatUtil.mergeObject(initSet,config);
	    if(openConfig.params){
	    	var objLength = PlatUtil.getObjLength(openConfig.params);
	    	if(objLength!=0){
	    		PlatUtil.ajaxProgress({
					url:"common/baseController.do?cacheParams",
					showProgress:false,
					params : openConfig.params,
					callback : function(resultJson) {
						parent.layer.open(openConfig);
					}
				});
	    	}else{
	    		parent.layer.open(openConfig);
	    	}
	    }else{
	    	parent.layer.open(openConfig);
	    }
	    
	},
	/**
	 * 对控件进行隐藏操作
	 */
	hiddenUIComp:function(config){
		var tagName = config.tagName;
		var compName = config.compName;
		var authValue= config.authValue;
		var targetField = config.targetField;
		$("#"+tagName+"_"+compName+"_DIV").css("display","none");
		$("#"+tagName+"_"+compName+"_LABEL").css("display","none");
		var validconfig = {
		};
		validconfig[compName] =null;
		$("form").validator("setField",validconfig);
		targetField.attr("auth_type",authValue);
	},
	/**
	 * 对控件进行写操作
	 */
	writeUIComp:function(config){
		var tagName = config.tagName;
		var compName = config.compName;
		var authValue= config.authValue;
		var targetField = config.targetField;
		var datarule = config.datarule;
		$("#"+tagName+"_"+compName+"_DIV").css("display","");
		$("#"+tagName+"_"+compName+"_LABEL").css("display","");
		targetField.removeAttr("disabled");
		targetField.removeAttr("readonly");
		targetField.attr("data-rule",datarule);
		targetField.attr("auth_type",authValue);
		if(tagName=="RADIO"||tagName=="CHECKBOX"){
			if(datarule.indexOf("checked;")!=-1){
				var lableId = tagName+"_"+compName+"_LABEL";
				$("#"+lableId+" font[class='redDot']").css("display","");
			}
		}else{
            if(datarule&&datarule.indexOf("required;")!=-1){
				var lableId = tagName+"_"+compName+"_LABEL";
				$("#"+lableId+" font[class='redDot']").css("display","");
			}
		}
		$('form').validator("setField",compName,datarule);
	},
	/**
	 * 对控件进行只读操作
	 */
	readonlyUIComp:function(config){
		var tagName = config.tagName;
		var compName = config.compName;
		var authValue= config.authValue;
		var targetField = config.targetField;
		$("#"+tagName+"_"+compName+"_DIV").css("display","");
		$("#"+tagName+"_"+compName+"_LABEL").css("display","");
		if(tagName=="SELECT"||tagName=="RADIO"||tagName=="CHECKBOX"){
			targetField.attr("disabled","disabled");
		}else{
			targetField.attr("readonly","readonly");
			var validconfig = {
			};
			validconfig[compName] =null;
			$("form").validator("setField",validconfig);
		}
		targetField.attr("auth_type",authValue);
	},
	/**
	 * 根据选择器判断表单是否验证通过
	 */
	isFormValid:function(selector){
		/*$(selector).trigger("validate"); 
		var instance = $(selector).validator().data("validator");
		return instance.isFormValid();*/
		var idValid = false;
		$(selector).trigger("validate"); 
		$(selector).isValid(function(valid){
			if(valid){
				idValid = true;
			}
		});
		return idValid;
	},
	/**
	 * 修改权限
	 * 修改一个控件的权限值
	 * authValue:权限值(write,hidden,readonly)
	 * allowblank:是否允许为空(1:允许,-1:不允许)
	 * 
	 */
    changeUICompAuth:function(authValue,compName,allowblank){
    	var targetFields = $("[name='"+compName+"']");
    	targetFields.each(function(index,obj){
			var targetField = $(this);
			var type = targetField.attr("type");
			var tagName = targetField.get(0).tagName;
			if(type&&type=="radio"){
				tagName = "RADIO";
			}else if(type&&type=="checkbox"){
				tagName = "CHECKBOX";
			}
			var datarule = targetField.attr("datarule");
			var auth_type = targetField.attr("auth_type");
			if(authValue=="hidden"){
				PlatUtil.hiddenUIComp({
					targetField:targetField,
					tagName:tagName,
					compName:compName,
					auth_type:auth_type
				});
			}else if(authValue=="write"){
				if(allowblank&&allowblank=="-1"){
					if(tagName=="RADIO"||tagName=="CHECKBOX"){
						datarule+="checked;"
					}else{
						datarule+="required;"
					}
				}
				PlatUtil.writeUIComp({
					targetField:targetField,
					tagName:tagName,
					compName:compName,
					auth_type:auth_type,
					datarule:datarule
				});
			}else{
				PlatUtil.readonlyUIComp({
					targetField:targetField,
					tagName:tagName,
					compName:compName,
					auth_type:auth_type
				});
			}
			
    	});
    },
    /**
	 * 重新加载复选框
	 * EXP:
	 * PlatUtil.reloadCheckBox("TABLE_NAME",{
			dyna_param:"1"
	   });
	 */
	reloadCheckBox:function(name,config){
		var checkboxObj = $("#CHECKBOX_"+name+"_DIV");
		var params = {};
		$.each(checkboxObj.get(0).attributes, function(i, obj){
			params[obj.name] = obj.value;
		}); 
		params = PlatUtil.mergeObject(params,config);
		PlatUtil.ajaxProgress({
			url : "common/baseController/reloadCheckbox.do",
			params :params,
			callback : function(resultJson) {
				checkboxObj.html(resultJson.resultHtml);
			}
		});
	},
    /**
	 * 重新加载单选框
	 * EXP:
	 * PlatUtil.reloadRadio("TABLE_NAME",{
			dyna_param:"1"
	   });
	 */
	reloadRadio:function(name,config){
		var radioObj = $("#RADIO_"+name+"_DIV");
		var params = {};
		$.each(radioObj.get(0).attributes, function(i, obj){
			params[obj.name] = obj.value;
		}); 
		params = PlatUtil.mergeObject(params,config);
		PlatUtil.ajaxProgress({
			url : "common/baseController/reloadRadio.do",
			params :params,
			callback : function(resultJson) {
				radioObj.html(resultJson.resultHtml);
			}
		});
	},
	/**
	 * 重新加载可操作列表
	 */
	reloadOperGroupList:function(id,config){
		var selectObj = $("#"+id);
		var params = {};
		$.each(selectObj.get(0).attributes, function(i, obj){
			params[obj.name] = obj.value;
		}); 
		params = PlatUtil.mergeObject(params,config);
		PlatUtil.ajaxProgress({
			url : "common/baseController/reloadOperListGroup.do",
			params :params,
			callback : function(resultJson) {
				selectObj.html(resultJson.resultHtml);
			}
		});
	},
	/**
	 * 重新加载下拉框
	 * EXP:
	 * PlatUtil.reloadSelect("TABLE_NAME",{
			dyna_param:"1"
	   });
	 */
	reloadSelect:function(name,config){
		var selectObj = $("select[name='"+name+"']");
		var params = {};
		$.each(selectObj.get(0).attributes, function(i, obj){
			params[obj.name] = obj.value;
		}); 
		params = PlatUtil.mergeObject(params,config);
		PlatUtil.ajaxProgress({
			url : "common/baseController/reloadSelect.do",
			params :params,
			callback : function(resultJson) {
				selectObj.html(resultJson.resultHtml);
			}
		});
	},
	/**
	 * 获取下拉框的属性值
	 * selectName:下拉框的name
	 * attrName:属性的名称
	 * var selectValue = PlatUtil.getSelectAttValue("你的下拉框名称","属性名称,例如value");
	 */
	getSelectAttValue:function(selectName,attrName){
		attrName = attrName.toLowerCase();
		var attrValue = $("select[name='"+selectName+"']").find("option:selected").attr(attrName);
		if(attrValue){
			attrValue = PlatUtil.trim(attrValue);
		}
		return attrValue;
	},
	/**
	 * 初始化下拉框控件
	 */
	initSelect2:function(parentSelector){
		//初始化下拉框控件
		var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" select[class*='select2']";
		}else{
			selectorRule = "select[class*='select2']";
		}
		$(selectorRule).each(function(index,obj){
			var placeholder = $(this).attr("placeholder");
			var max_select_num = $(this).attr("max_select_num");
			var istree = $(this).attr("istree");
			var multiple = $(this).attr("multiple");
			var name = $(this).attr("name");
			var selectConfig = {
				placeholder:placeholder,
				allowClear: true,
				language: "zh-CN"
			};
			if(max_select_num){
				selectConfig.maximumSelectionLength = max_select_num;
			}
			if(istree=="true"){
				selectConfig.templateResult = PlatUtil.formatTreeSelectState;
			}
			$("select[name='"+name+"']").select2(selectConfig);
		});
	},
	/**
	 * 初始化日期控件
	 */
	initDateTime:function(parentSelector){
		var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" input[class*='wicon']";
		}else{
			selectorRule = "input[class*='wicon']";
		}
		//初始化日期控件
		$(selectorRule).each(function(index,compobj){
			var dateObj = $(this);
			var datarule = dateObj.attr("datarule");
			var name = dateObj.attr("name");
			var istime = dateObj.attr("istime");
			var choosefun = dateObj.attr("choosefun");
			var start_name = dateObj.attr("start_name");
			var end_name = dateObj.attr("end_name");
			var setting = {
			    skinCell:"jedateblue",	
			    isTime:istime=="true"?true:false,
			    festival:false,
			    minDate:dateObj.attr("min_date"),
			    maxDate:dateObj.attr("max_date"),
			    format:dateObj.attr("format"),
			    choosefun: function(elem,datas){
			    	if(datarule){
		    		   $(dateObj).trigger("validate"); 
			    	}
			    	/*if(start_name){
			    		PlatUtil[start_name].maxDate = datas; 
			    	}
			    	if(end_name){
			    		PlatUtil[end_name].minDate = datas; 
			    	}*/
			    	if(choosefun!=null){
			    		eval(choosefun).call(this,elem,datas);
			    	}
			    },
			    okfun:function(elem, datas) {
			    	if(datarule){
		    		   $(dateObj).trigger("validate"); 
			    	}
			    	/*if(start_name){
			    		PlatUtil[start_name].maxDate = datas; 
			    	}
			    	if(end_name){
			    		PlatUtil[end_name].minDate = datas; 
			    	}*/
			    	if(choosefun!=null){
			    		eval(choosefun).call(this,elem,datas);
			    	}
			    }
			};
			if(start_name||end_name){
				PlatUtil[name] = setting;
				$.jeDate(compobj,PlatUtil[name]);
			}else{
				$.jeDate(compobj,setting);
			}
			
		});
	},
	/**
	 * 初始化可编辑表格
	 */
	initEditTable:function(parentSelector){
		var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" table[class*='platedittable']";
		}else{
			selectorRule = "table[class*='platedittable']";
		}
		//初始化可编辑表格
		$(selectorRule).each(function(index,compobj){
			var searchPanelId = $(this).attr("searchpanel_id");
			var tableId = $(this).attr("id");
			var dataInterface = $(this).attr("dyna_interface");
			if(dataInterface&&dataInterface!=""){
				PlatUtil.loadEditTable({
					searchPanelId:searchPanelId,
					tableId:tableId,
					dataInterface:dataInterface
				});
			}
			
			var dragable = $(this).attr("dragable");
			if(dragable&&dragable=="true"){
				$("#"+tableId+" tbody").sortable({
				    cursor: "move",
				    opacity: 0.8,
				    revert: true
				});
			}
		});
	},
	/**
	 * 向导往上一步
	 * 需要调用的函数名称
	 */
	wizardPre:function(fnName){
		var isPre = false;
		if(fnName){
			var result = fnName.call(this);
			if(result&&result!=false){
				isPre = true;
			}
		}else{
			isPre = true;
		}
		if(isPre){
			if(PlatUtil.WIZARD_CUR_STEP==1){
				   return;
		    }
		    $(".steps").children("li").each(function(){
				  $(this).attr("class",""); 
			});
		    if($("#wizard-showdiv").length!=0){
		    	$("#wizard-showdiv").children("div").each(function(){
					  $(this).attr("style","display: none;"); 
			    });
		    }else{
		    	$("[class='platdesigncomp'][compcode='platwizard']").children("[platcomid]").each(function(){
					  $(this).attr("style","display: none;"); 
			    });
		    }
		    PlatUtil.WIZARD_CUR_STEP = PlatUtil.WIZARD_CUR_STEP - 1;
		    $("#WIZARD_OVERBTN").attr("disabled","disabled");
		    for(var i=0;i<PlatUtil.WIZARD_CUR_STEP-1;i++){
			   $(".steps").children("li:eq("+i+")").attr("class","complete");
		    }
		    $(".steps").children("li:eq("+(PlatUtil.WIZARD_CUR_STEP-1)+")").attr("class","active");
		    if($("#wizard-showdiv").length!=0){
		    	$("#wizard-showdiv").children("div:eq("+(PlatUtil.WIZARD_CUR_STEP-1)+")").attr("style","");
		    }else{
		    	$("[class='platdesigncomp'][compcode='platwizard']").children("[platcomid]").
		    	eq((PlatUtil.WIZARD_CUR_STEP-1)).attr("style","");
		    }
		    
		    var layoutLength = $("#wizard-showdiv").children("div:eq("+(PlatUtil.WIZARD_CUR_STEP-1)+")")
		       .children(".ui-layout-container").length;
		    if(layoutLength!=0){
		    	$("#wizard-showdiv").children("div:eq("+(PlatUtil.WIZARD_CUR_STEP-1)+")")
			       .children(".ui-layout-container").parent().height("100%");
		    }else{
		    	$("[class='platdesigncomp'][compcode='platwizard']").children("[platcomid]").
		    	eq((PlatUtil.WIZARD_CUR_STEP-1)).find(".ui-layout-container").parent().height("100%");
		    }
		}
		
	},
	/**
	 * 向导往下一步
	 * 需要调用的函数名称
	 */
	wizardNext:function(fnName){
		var isNext = false;
		var curStep = PlatUtil.WIZARD_CUR_STEP;
		var nextFnName = $(".plat-wizard span[class='step']").eq(curStep-1).attr("nextstep_fnname");
		if(nextFnName){
			var result = eval(nextFnName).call(this);
			if(result&&result!=false){
				isNext = true;
			}
		}else{
			isNext = true;
		}
		if(isNext){
			if(PlatUtil.WIZARD_CUR_STEP==$(".plat-wizard span[class='step']").length){
				   return;
			}
		    $(".steps").children("li").each(function(){
			  $(this).attr("class",""); 
		    });
		    if($("#wizard-showdiv").length!=0){
		    	$("#wizard-showdiv").children("div").each(function(){
					  $(this).attr("style","display: none;"); 
			    });
		    }else{
		    	$("[class='platdesigncomp'][compcode='platwizard']").children("[platcomid]").each(function(){
					  $(this).attr("style","display: none;"); 
			    });
		    }
		  
		    PlatUtil.WIZARD_CUR_STEP = PlatUtil.WIZARD_CUR_STEP + 1;
		    if(PlatUtil.WIZARD_CUR_STEP==$(".plat-wizard span[class='step']").length){
		    	$("#WIZARD_OVERBTN").removeAttr("disabled");
		    }
		    /*if(PlatUtil.WIZARD_CUR_STEP==$(".plat-wizard span[class='step']").length){
		    	$("#WIZARD_NEXTBTN").attr("disabled","disabled");
			}*/
		    for(var i=0;i<PlatUtil.WIZARD_CUR_STEP-1;i++){
			   $(".steps").children("li:eq("+i+")").attr("class","complete");
		    }
		    $(".steps").children("li:eq("+(PlatUtil.WIZARD_CUR_STEP-1)+")").attr("class","active");
		    if($("#wizard-showdiv").length!=0){
		    	$("#wizard-showdiv").children("div:eq("+(PlatUtil.WIZARD_CUR_STEP-1)+")").attr("style","");
		    }else{
		    	$("[class='platdesigncomp'][compcode='platwizard']").children("[platcomid]").
		    	eq((PlatUtil.WIZARD_CUR_STEP-1)).attr("style","");
		    }
		    
		    var layoutLength = $("#wizard-showdiv").children("div:eq("+(PlatUtil.WIZARD_CUR_STEP-1)+")")
		       .children(".ui-layout-container").length;
		    if(layoutLength!=0){
		    	$("#wizard-showdiv").children("div:eq("+(PlatUtil.WIZARD_CUR_STEP-1)+")")
			       .find(".ui-layout-container").parent().height("100%");
		    }else{
		    	$("[class='platdesigncomp'][compcode='platwizard']").children("[platcomid]").
		    	eq((PlatUtil.WIZARD_CUR_STEP-1)).find(".ui-layout-container").parent().height("100%");
		    }
		}
		
	},
	/**
	 * 初始化向导界面
	 */
	initWizard:function(parentSelector){
		var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" .plat-wizard";
		}else{
			selectorRule = ".plat-wizard";
		}
		if($(selectorRule).length>0){
			PlatUtil.WIZARD_CUR_STEP = 1;
			//$("#WIZARD_PREBTN").attr("disabled","disabled");
		}
	},
	/**
	 * 初始化滚动条
	 */
	initSlimScroll:function(parentSelector){
		var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" .slimscroll-enable";
		}else{
			selectorRule = ".slimscroll-enable";
		}
		if($(selectorRule).length>0){
			$(selectorRule).slimScroll({
			    width: "auto", //可滚动区域宽度
			    height:"auto" //可滚动区域高度
			});
		}
	},
	/**
	 * 初始化方向布局
	 */
	initDirectLayout:function(parentSelector){
		var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" .plat-directlayout";
		}else{
			selectorRule = ".plat-directlayout";
		}
		$(selectorRule).each(function(){
			var targetField = $(this);
			var configJson = "{\"resizable\":false,\"resizerTip\":\"可调整大小\",\"closable\":false"
			var layoutsize = targetField.attr("layoutsize");
			if(layoutsize){
				configJson+=",";
				configJson+=layoutsize;
			}
			configJson+="}";
			var config = $.parseJSON(configJson);
		    $(this).layout(config);
		});
	},
	/**
	 * 初始化fancybox
	 * 
	 */
	initFancyBox:function(parentSelector){
		var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" .fancybox";
		}else{
			selectorRule = ".fancybox";
		}
		if($(selectorRule).length!=0){
			$(selectorRule).fancybox({openEffect:"none",closeEffect:"none"});
		}
	},
	/**
	 * 初始化表单验证
	 */
	initFormValid:function(parentSelector,config){
		var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" .form";
		}else{
			selectorRule = "form";
		}
		if($(selectorRule).length!=0){
			var initConfig = {
				timely:1,
			 	ignore: ':hidden'	
			};
			var tabLength = $(".tabs-container").length;
			if(tabLength>0){
				initConfig = {
					timely:1,
				 	ignore:null	
				};
			}
			if(config){
				initConfig = PlatUtil.mergeObject(initConfig,config);
			}
			$(selectorRule).validator(initConfig);  
		}
	},
	/**
	 * 初始化codemirror
	 */
    initCodeMirror:function(parentSelector){
    	var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" textarea[codemirror='codemirror']";
		}else{
			selectorRule = "textarea[codemirror='codemirror']";
		}
		if($(selectorRule).length!=0){
			$(selectorRule).each(function(index,obj){
				var textarea = $(obj);
				var id = textarea.attr("id");
				var mirrorwidth = textarea.attr("mirrorwidth");
				var mirrorheight = textarea.attr("mirrorheight");
				if(id){
					PlatUtil.PLAT_CODEMIRROREDITOR = CodeMirror.fromTextArea(document.getElementById(id), {
						  mode: "javascript",
						  theme:"blackboard",
						  autoCloseTags: true,
						  autoCloseBrackets: true,
						  styleActiveLine: true,
						  lineNumbers: true,
						  lineWrapping: true,
						  matchBrackets: true,
						  extraKeys: {
					        "F11": function(cm) {
					          cm.setOption("fullScreen", !cm.getOption("fullScreen"));
					        },
					        "Esc": function(cm) {
					          if (cm.getOption("fullScreen")) cm.setOption("fullScreen", false);
					        },
					        "Alt-F": "findPersistent"
					      }
					});
					if(mirrorwidth){
						PlatUtil.PLAT_CODEMIRROREDITOR.setSize(mirrorwidth,mirrorheight);
					}
					
				}
			});
		}
    },
    /**
     * 初始化单个图片上传控件
     * 
     */
    initSingleImageUploader:function(parentSelector){
    	var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" .platSingleImgUploadDiv";
		}else{
			selectorRule = ".platSingleImgUploadDiv";
		}
		if($(selectorRule).length!=0){
			$(selectorRule).each(function(index,obj){
				var pickerId = $(obj).find(".platSingleImgUploadPicker").attr("id");
				var filesinglesizelimit = $(obj).attr("filesinglesizelimit");
				var uploadrootfolder = $(obj).attr("uploadrootfolder");
				var singleSizeLimit = new Number(filesinglesizelimit);
				PlatUtil.createSingleImgWebUploader({
					pickerId:pickerId,
					pick: {
				    	id:"#"+pickerId,
				    	multiple:false,
				    },
				    //单个文件最大允许大小单位是字节
				    fileSingleSizeLimit:singleSizeLimit,
				    formData:{
				    	"uploadRootFolder":uploadrootfolder
				    }
				});
			});
		}
    },
    /**
     * 初始化bootStrap的TAB控件的高度
     */
    initBootStrapTabHeight:function(parentSelector){
    	var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" .tab-content";
		}else{
			selectorRule = ".tab-content";
		}
		if($(selectorRule).length!=0){
			$(selectorRule).each(function(index,obj){
				var parentHeight = $(obj).parent().height();
				var ulObj = $(obj).parent().children().eq(0);
				var ulObjHeight = ulObj.height();
				var childrenTab = $(obj).children();
				$.each(childrenTab,function(index,obj){
					var tabObj = $(obj);
					var oldStyle = tabObj.attr("style");
					if(oldStyle=="100%;"){
						tabObj.height(parentHeight-ulObjHeight);
					}
				});
			});
		}
    },
    /**
     * 初始化多文件列表上传控件
     * 
     */
    initMultiFileUploader:function(parentSelector){
    	var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" .platListFileUploadDiv";
		}else{
			selectorRule = ".platListFileUploadDiv";
		}
		if($(selectorRule).length!=0){
			$(selectorRule).each(function(index,obj){
				var pickerId = $(obj).find("span[id]").attr("id");
				var filesinglesizelimit = $(obj).attr("filesinglesizelimit");
				var uploadrootfolder = $(obj).attr("uploadrootfolder");
				var extensions = $(obj).attr("extensions");
				var singleSizeLimit = new Number(filesinglesizelimit);
				PlatUtil.createMultiFileWebUploader({
					pickerId:pickerId,
					pick: {
				    	id:"#"+pickerId,
				    	multiple:true
				    },
				    accept:{
				        title: "Files",
				        extensions:extensions
				    },
				    //单个文件最大允许大小单位是字节
				    fileSingleSizeLimit:singleSizeLimit,
				    formData:{
				    	"uploadRootFolder":uploadrootfolder
				    }
				});
			});
		}
    },
    /**
     * 初始化后台登录用户的权限
     */
    initBackLoginUserRights:function(){
    	var backLoginUser = PlatUtil.getBackPlatLoginUser();
    	if(backLoginUser){
    		$("[platreskey]").each(function(index,obj){
        		var platreskey = $(obj).attr("platreskey");
        		if(platreskey){
        			var RESCODESET = backLoginUser.RESCODESET;
        			var isContain = PlatUtil.isContain(RESCODESET,platreskey);
        			if(!isContain){
        			   $(obj).css("display","none");
        			}
        		}
        	});
    		var otherLength = $("#OTHER_OPERDIV").length;
    		if(otherLength!=0){
    			var li = $("#OTHER_OPERDIV").find("ul li[platreskey]");
    			var isVisible = false;
    			$.each(li,function(index,obj){
    				var result = $(obj).css("display");
    				if(result!="none"){
    					isVisible = true;
    				}
    			});
    			if(!isVisible){
    				$("#OTHER_OPERDIV").css("display","none");
    			}
    		}
    	}
    },
    /**
     * 初始化导入文件按钮
     */
    initImpFileButton:function(parentSelector){
    	var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" .platImpUploadFile";
		}else{
			selectorRule = ".platImpUploadFile";
		}
		if($(selectorRule).length!=0){
			$(selectorRule).each(function(index,obj){
				var impfiletypes = $(obj).attr("impfiletypes");
				var impfilesize = $(obj).attr("impfilesize");
				var imptableid = $(obj).attr("imptableid");
				var impurl = $(obj).attr("impurl");
				var pickerId = $(obj).attr("id");
				var fileLimit = new Number(impfilesize);
				var GUID = WebUploader.Base.guid();
				var initConfig = {
					pickerId:pickerId,
					pick: {
				    	id:"#"+pickerId,
				    	multiple:false,
				    },
				    //单个文件最大允许大小单位是字节
				    fileSingleSizeLimit:fileLimit,
					// swf文件路径
				    swf: "plug-in/webuploader-0.1.5/dist/Uploader.swf",
				    // 文件接收服务端。
				    server: "system/FileAttachController/upload.do",
				    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
				    accept:{
				        title: "Files",
				        extensions: impfiletypes
				    },
				    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
				    resize: false,
				    auto:true,
				    prepareNextFile:true,
				    chunked:true,
				    //默认20M进行分块
				    chunkSize:20971520,
				    //允许并发上传的文件个数
				    threads: 1,
				    duplicate:true,
				    formData:{
				    	guid:GUID,
				    	"uploadRootFolder":"impfiles"
				    }
				};
				WebUploader.create(initConfig).on("uploadSuccess", function( file,response) {
					var dbfilepath = response.dbfilepath;
					PlatUtil.ajaxProgress({
						url:impurl,
						params : {
							dbfilepath:dbfilepath
						},
						callback : function(resultJson) {
							if (resultJson.success) {
								parent.layer.msg("导入成功!", {icon: 1});
								$("#"+imptableid).trigger("reloadGrid"); 
								parent.layer.close(initConfig.progressWinIndex);
							} else {
								parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
							}
						}
					});
				}).on("error",function(errortype){
					PlatUtil.webuploadErrorEvent(errortype,initConfig);
				}).on("startUpload",function(){
					PlatUtil.webuploadStartUploadEvent(initConfig);
				});
			});
		}
    },
    /**
     * 初始化数字输入框控件
     */
    initNumberField:function(parentSelector){
    	var selectorRule = "";
		if(parentSelector){
			selectorRule = parentSelector+" .platnumber";
		}else{
			selectorRule = ".platnumber";
		}
		if($(selectorRule).length!=0){
			$(selectorRule).each(function(index,obj){
				var min = $(obj).attr("min");
				var max = $(obj).attr("max");
				var step = $(obj).attr("step");
				var decimals = $(obj).attr("decimals");
				var prefix = $(obj).attr("prefix");
				var postfix = $(obj).attr("postfix");
				var auth_type = $(obj).attr("auth_type");
				if(auth_type!="readonly"){
					var config = {
						boostat: 5,
				        maxboostedstep: 10
					};
					if(min){
						config.min = new Number(min);
					}
					if(max){
						config.max = new Number(max);
					}
					if(step){
						config.step = new Number(step);
					}
					if(decimals){
						config.decimals = new Number(decimals);
					}
					if(prefix){
						config.prefix = prefix;
					}
					if(postfix){
						config.postfix = postfix;
					}
					$(obj).TouchSpin(config);
				}
				
			});
		}
    },
	/**
	 * 初始化UI组件
	 * 是否基于父亲选择情况下再选择
	 */
	initUIComp:function(parentSelector,config){
		//初始化布局控件
		PlatUtil.initDirectLayout(parentSelector);
		//初始化下拉框控件
		PlatUtil.initSelect2(parentSelector);
		//初始化日期时间控件
		PlatUtil.initDateTime(parentSelector);
		//初始化可编辑表格
	    PlatUtil.initEditTable(parentSelector);
		//初始化向导界面
		PlatUtil.initWizard(parentSelector);
		//初始化滚动条
		PlatUtil.initSlimScroll(parentSelector);
		//初始化fancybox
		PlatUtil.initFancyBox(parentSelector);
		//初始化codemirror
		PlatUtil.initCodeMirror(parentSelector);
		//初始化表单验证
		PlatUtil.initFormValid(parentSelector,config);
		//初始化单图片上传控件
		PlatUtil.initSingleImageUploader(parentSelector);
		//初始化BootStrap的tab控件
		PlatUtil.initBootStrapTabHeight(parentSelector);
		//初始化后台登录用户的权限
		PlatUtil.initBackLoginUserRights();
		//初始化导入按钮
		PlatUtil.initImpFileButton();
		//初始化数字输入框
		PlatUtil.initNumberField(parentSelector);
		//初始化表单字段原始值对象
		PlatUtil.initFormSourceDatas();
	},
	/**
	 * 改变select2下拉框的值
	 */
	changeSelect2Val:function(selector,newVal){
		$(selector).val(newVal);
		$(selector).trigger("change"); 
	},
	/**
	 * 字符串首字母大写
	 * str:源字符串
	 * otherLower:是否将其它字符串小写(true,false)
	 */
	firstLetterUpperCase:function(str,otherLower){
		if(otherLower){
			return str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase();
		}else{
			return str.substring(0,1).toUpperCase()+str.substring(1);
		}
	},
	/**
	 * 字符串首字母小写
	 * 
	 */
	firstLetterLowerCase:function(str){
		return str.substring(0,1).toLowerCase()+str.substring(1);
	},
	/**
	 * 列表组点击事件
	 */
	onListGroupClick:function(listobj,listValue){
		var onclickfn = $(listobj).parent().attr("onclickfn");
		$(listobj).parent().children("li").removeClass("plat-listgroupon");
		$(listobj).addClass("plat-listgroupon");
		if(onclickfn!=null){
			eval(onclickfn).call(this,listValue);
		}
	},
	/**
	 * 列表组删除事件
	 */
	onListGroupDelClick:function(spanobj,listvalue){
		var layerIndex = parent.layer.confirm("您确定要删除该记录吗?", {
		    resize :false
		}, function(){
			var onclickfn = $(spanobj).attr("onclickfn");
			if(onclickfn!=null){
				eval(onclickfn).call(this,listvalue);
			}
			parent.layer.close(layerIndex);
		}, function(){
			
		});	
		var e = window.event || arguments.callee.caller.arguments[0];
		// 如果提供了事件对象，则这是一个非IE浏览器
	    if ( e && e.stopPropagation ) {
	        // 因此它支持W3C的stopPropagation()方法 
	        e.stopPropagation();
	    } else { 
	        // 否则，我们需要使用IE的方式来取消事件冒泡
	        window.event.cancelBubble = true;
	    }
		return;
	},
	/**
	 * 列表组编辑事件
	 */
	onListGroupEditClick:function(spanobj,listvalue){
		var onclickfn = $(spanobj).attr("onclickfn");
		if(onclickfn!=null){
			eval(onclickfn).call(this,listvalue);
		}
		var e = window.event || arguments.callee.caller.arguments[0];
		// 如果提供了事件对象，则这是一个非IE浏览器
	    if ( e && e.stopPropagation ) {
	        // 因此它支持W3C的stopPropagation()方法 
	        e.stopPropagation();
	    } else { 
	        // 否则，我们需要使用IE的方式来取消事件冒泡
	        window.event.cancelBubble = true;
	    }
		return;
	},
	/**
	 * 显示弹出框选择器
	 */
	showWinSelector:function(combobj){
		var labelName = $(combobj).attr("name");
		var valueName = labelName.substring(0,labelName.lastIndexOf("_LABELS"));
		var needCheckIds = $("input[name='"+valueName+"']").val();
		var maxselect = new Number($(combobj).attr("maxselect"));
		var minselect = new Number($(combobj).attr("minselect"));
		var checktype = $(combobj).attr("checktype");
		var checkcascadey = $(combobj).attr("checkcascadey");
		var checkcascaden = $(combobj).attr("checkcascaden");
		var selectorurl = $(combobj).attr("selectorurl");
		var width = $(combobj).attr("width");
		var height = $(combobj).attr("height");
		var title = $(combobj).attr("title");
		var datarule = $(combobj).attr("datarule");
		var callbackfn = $(combobj).attr("callbackfn");
		PlatUtil.setData(PlatUtil.WIN_SELECTOR_CONFIG,{
			//选择的类型,单选还是多选框
			checktype:checktype,
			//0标识不控制选择的条数
			maxselect:maxselect,
			//最少需要选择的条数
			minselect:minselect,
			//选中时的级联方式
			checkCascadeY:checkcascadey,
			//取消选中后的级联方式
			checkCascadeN:checkcascaden,
			needCheckIds:needCheckIds
		});
		PlatUtil.openWindow({
			title:title,
			area: [width,height],
			content: selectorurl,
			end:function(){
				var selectedRecords = PlatUtil.getData(PlatUtil.WIN_SELECTOR_RECORDS);
				if(selectedRecords&&selectedRecords.selectSuccess){
					$("input[name='"+valueName+"']").val(selectedRecords.checkIds);
					$(combobj).val(selectedRecords.checkNames);
					if(datarule){
		    		   $(combobj).trigger("validate"); 
			    	}
					if(callbackfn){
						eval(callbackfn).call(this,selectedRecords.checkIds,selectedRecords.checkNames);
					}
					PlatUtil.removeData(PlatUtil.WIN_SELECTOR_RECORDS);
				}
			}
		});
	},
	/**
	 * 重新加载在线设计
	 */
	reloadOnlineDesign:function(){
		var oldUrl = window.top.location.href;
		if(oldUrl.indexOf("&timestamp=")==-1){
			oldUrl+="&timestamp="+new Date().getTime();
		}else{
			oldUrl = oldUrl.substring(0,oldUrl.indexOf("&timestamp="));
			oldUrl+="&timestamp="+new Date().getTime();
		}
		window.top.location.href = oldUrl;
	},
	/**
	 * 设计控件添加子控件实现
	 */
	addDesignCmp:function(parentId,parentCompId){
		var DESIGN_ID = $("#ONLINE_DESIGNID").val();
		var url = "appmodel/FormControlController.do?goForm&DESIGN_ID="+DESIGN_ID;
		var PARENT_ID = parentId;
		var PARENT_COMPID = parentCompId;
		url+="&PARENT_ID="+PARENT_ID+"&PARENT_COMPID="+PARENT_COMPID;
		PlatUtil.openWindow({
			title:"新增子组件配置",
			area: ["100%","100%"],
			content: url,
			end:function(){
				PlatUtil.reloadOnlineDesign();
			}
		});
	},
	/**
	 * 设计控件编辑子控件实现
	 */
	editDesignCmp:function(compcontrolid,platcompname){
		var url = "appmodel/FormControlController.do?goForm";
		var FORMCONTROL_ID = compcontrolid;
		var platcompname = platcompname;
		url+="&FORMCONTROL_ID="+FORMCONTROL_ID;
		PlatUtil.openWindow({
			title:"编辑"+platcompname+"配置",
			area: ["100%","100%"],
			content: url,
			end:function(){
				PlatUtil.reloadOnlineDesign();
			}
		});
	},
	/**
	 * 设计控件删除子控件实现
	 */
	delDesignCmp:function(compcontrolid,childctrolIds){
		var url = "appmodel/FormControlController.do?delRecord";
		var formControlId = compcontrolid;
		parent.layer.confirm("注意!删除该控件数据,将会连带删除该子孙控件数据,您确定删除吗?", {
		    resize :false
		}, function(){
			PlatUtil.ajaxProgress({
				url:url,
				params:{
					treeNodeId:formControlId,
					childctrolIds:childctrolIds
				},
				callback : function(resultJson) {
					if (resultJson.success) {
						parent.layer.alert(PlatUtil.SUCCESS_MSG,{icon: 1,resize:false});
						PlatUtil.reloadOnlineDesign();
					} else {
						parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
					}
				}
			});
		}, function(){
			
		});	
	},
	/**
	 * 设计界面实现鼠标移入移出事件
	 */
	designMouseEvent:function(){
		$(".platdesigncomp").mouseenter(function(){
		    var pE = $(this).closest(".plat-designborder");
		    if(pE!=undefined){
			  pE.removeClass("plat-designborder");
			  if (pE.children(".plat-designtoolbar").length) {
				  pE.children(".plat-designtoolbar").remove();
			  }
		    }
		    $(this).addClass("plat-designborder");
		    if (!$(this).children(".plat-designtoolbar").length) {
		    	var divHtml = PlatUtil.addDesignToolbar($(this).attr("platcompname"),$(this).attr("uibtnsrights"));
				$(this).append(divHtml);
			}
	   });
	   $(".platdesigncomp").mouseleave(function(){
		   $(this).removeClass("plat-designborder");
		   if ($(this).children(".plat-designtoolbar").length) {
				$(this).children(".plat-designtoolbar").remove();
		   }
		   var pE = $(this).parent().closest(".platdesigncomp");
		   if(pE!=undefined){
				pE.addClass("plat-designborder");
				if (!pE.children(".plat-designtoolbar").length) {
					if(pE.attr("uibtnsrights")){
						var divHtml = PlatUtil.addDesignToolbar(pE.attr("platcompname"),pE.attr("uibtnsrights"));
						pE.append(divHtml);
					}
				}
		    }
		});
	},
	/**
	 * 为控件添加设计工具栏
	 */
	addDesignToolbar:function(platcompname,uibtnsrights){
		var toolbarHtml = "<div class=\"plat-designtoolbar\"><b>"+platcompname+"</b>";
		if(uibtnsrights.indexOf("add")!=-1){
			toolbarHtml+="<button type=\"button\" onclick=\"PlatUtil.addDesignCmp(this);\" class=\"btn btn-outline btn-primary btn-xs\" >新增</button>"
		}
		if(uibtnsrights.indexOf("edit")!=-1){
			toolbarHtml+="<button type=\"button\" onclick=\"PlatUtil.editDesignCmp(this);\" class=\"btn btn-outline btn-info btn-xs\" >编辑</button>"
		}
		if(uibtnsrights.indexOf("del")!=-1){
			toolbarHtml+="<button type=\"button\" onclick=\"PlatUtil.delDesignCmp(this);\" class=\"btn btn-outline btn-danger btn-xs\" >删除</button>"
		}
		toolbarHtml+="</div>";
		return toolbarHtml;
	},
	/**
	 * 初始化设计工具栏
	 */
	initDesignBoolbar:function(){
		$(".platdesigncomp").each(function(){
		    $(this).addClass("plat-designborder");
		    if (!$(this).children(".plat-designtoolbar").length) {
		    	var divHtml = PlatUtil.addDesignToolbar($(this).attr("platcompname"),$(this).attr("uibtnsrights"));
				$(this).append(divHtml);
			}
	   });
	},
	/**
	 * 隐藏ztree单选/复选框
	 */
	hiddenZtreeCheckBox:function(treeId,treeNode){
		if(treeNode!=undefined){
			var tid = treeNode.tId;
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			if(!$("#"+tid+"_check").is(".ztreehiddenUI")){
				$("#"+tid+"_check").addClass("ztreehiddenUI");
				treeObj.setChkDisabled(treeNode, true); 
			}
		}
	},
	/**
	 * 退出后台管理系统
	 */
	exitBackgroudSystem:function(){
		window.top.location.href = __ctxPath+"/security/LoginController/backLogoff.do";
	},
	/**
	 * 格式化文件大小
	 */
	formatFileSize:function(sourceFileSize){
		var sizeNames = [' Bytes', ' KB', ' MB', ' GB', ' TB', ' PB', ' EB', ' ZB', ' YB'];
		var i = Math.floor(Math.log(sourceFileSize)/Math.log(1024));
		var p = (i > 1) ? 2 : 0;
		var size = (sourceFileSize/Math.pow(1024, Math.floor(i))).toFixed(p) + sizeNames[i];
		return size;
	},
	/**
	 * 根据pickerId打开图片裁剪窗口
	 */
	openPicCutWindow:function(pickerId){
		var src = $("#"+pickerId).find("img").attr("data-imgurl");
		var url = "appmodel/CommonUIController.do?piccutView&imageUrl="+src;
		PlatUtil.openWindow({
			title:"图片处理",
			area: ["800px","600px"],
			content:url,
			end:function(){
				if(PlatUtil.getData("resultImageUrl")){
					$("#"+pickerId).find("img").attr("data-imgurl",PlatUtil.getData("resultImageUrl"));
					$("#"+pickerId).find("img").attr("src",PlatUtil.getData("resultImageUrl"));
					$("#"+pickerId).find("img").attr("dbfilepath",PlatUtil.getData("dbfilepath"));
					$("#"+pickerId).parent().attr("href",PlatUtil.getData("resultImageUrl"));
					PlatUtil.removeData("resultImageUrl");
					PlatUtil.removeData("dbfilepath");
				}
			}
		});
	},
	/**
	 * 删除单个图片上传的文件
	 */
	deleteSingleUploadImg:function(pickerId){
		var layerIndex = parent.layer.confirm("您确定删除该图片吗?", {
			icon: 7,
		    resize :false
		}, function(){
		    var defaultimgpath = $("#"+pickerId).find("img").attr("defaultimgpath");
			$("#"+pickerId).find("img").attr("src",defaultimgpath);
			$("#"+pickerId).find("img").attr("dbfilepath",defaultimgpath);
			$("#"+pickerId).find("img").removeAttr("originalfilename");
			$("#"+pickerId+"_imgUploadToolButton").html("");
			$("#"+pickerId).parent().attr("href",defaultimgpath);
			parent.layer.close(layerIndex);
		}, function(){
			
		});	
		
	},
	/**
	 * 删除多文件上传的文件
	 */
	deleteMultiFileUpload:function(fileId){
		var layerIndex = parent.layer.confirm("您确定删除该文件吗?", {
			icon: 7,
		    resize :false
		}, function(){
			var parentList = $("#"+fileId).parent();
			$("#"+fileId).remove(); 
			if(parentList.children().length==0){
				parentList.css("display",""); 
			 }
			parent.layer.close(layerIndex);
		}, function(){
			
		});	
	},
	/**
	 * 下载文件
	 */
	downloadFile:function(dbfilepath,fileName,pickerId){
		var url = __ctxPath+"/system/FileAttachController/download.do?dbfilepath="+dbfilepath+"&fileName="+fileName;
		var fileList = $("#"+pickerId+"_filelist");
		var fileserverurl = fileList.attr("fileserverurl");
		if(fileserverurl&&fileserverurl!=""){
			url = fileserverurl+"system/FileAttachController/download.do?dbfilepath="+dbfilepath+"&fileName="+fileName;
		}
		window.location.href=url;
	},
	/**
	 * 根据文件ID下载文件
	 * @param fileId
	 */
	downloadByFileId:function(fileId,fileserverurl){
		var url = __ctxPath+"/system/FileAttachController/download.do?fileId="+fileId;
		if(fileserverurl&&fileserverurl!=""){
			url = fileserverurl+"system/FileAttachController/download.do?fileId="+fileId;
		}
		window.location.href=url;
	},
	/**
	 * 创建多文件上传控件对象
	 */
	createMultiFileWebUploader:function(config){
		var pickerId = config.pickerId;
		if($("#"+pickerId).find(".webuploader-pick").length > 0){
			//PlatUtil.uploader.refresh();
		}else{
			var GUID = WebUploader.Base.guid();
			var fileList = $("#"+pickerId+"_filelist");
			var serverurl = __ctxPath+"/system/FileAttachController/upload.do";
			var fileserverurl = fileList.attr("fileserverurl");
			if(fileserverurl&&fileserverurl!=""){
				var uploadRootFolder = config.formData.uploadRootFolder;
				serverurl = fileserverurl+"servlet/CrossFileUpLoad?guid="+GUID+"&uploadRootFolder="+uploadRootFolder;
			}
			var fileupurl = fileList.attr("fileupurl");
			if(fileupurl&&fileupurl!=""){
				serverurl = __ctxPath+"/"+fileupurl;
			}
			var initConfig = {
				// swf文件路径
			    swf: "plug-in/webuploader-0.1.5/dist/Uploader.swf",
			    // 文件接收服务端。
			    server: serverurl,
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    accept:{
			        title: "Files"
			    },
			    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
			    resize: false,
			    auto:true,
			    prepareNextFile:true,
			    chunked:true,
			    //默认20M进行分块
			    chunkSize:20971520,
			    //允许并发上传的文件个数
			    threads: 1,
			    duplicate:true,
			    formData:{
			    	guid:GUID
			    }
			};
			initConfig = PlatUtil.mergeObject(initConfig,config);
			WebUploader.create(initConfig).on("uploadSuccess", function( file,response) {
				var dbfilepath = response.dbfilepath;
				$("#"+pickerId).parent().attr("href",response.getfileurl);
				$("#"+file.id).find('span.state').text('恭喜您，上传成功');
			    $("#"+file.id).find('.uploadify-progress').fadeOut();
			    $("#"+file.id).find('span.stateIcon').attr("class","stateIcon");
			    $("#"+file.id).find('span.stateIcon').addClass("succeed");
			    $("#"+file.id).find('span.stateIcon').attr("title","成功");
			    $("#"+file.id).find('span.stateIcon').html('<i class="fa fa-check-circle"></i>');
			    $("#"+file.id).find("span[class='fileName']").attr("dbfilepath",dbfilepath);
			    $("#"+file.id).find("span[class='fileName']").attr("originalfilename",file.name);
			    var operBtnHtml = "<a title=\"删除\" onclick=\"PlatUtil.deleteMultiFileUpload('"+file.id+"');\" href=\"javascript:void(0);\" ><i class=\"fa fa-trash-o\"></i></a>";
			    operBtnHtml += "<a title=\"下载\" onclick=\"PlatUtil.downloadFile('"+dbfilepath+"','"+file.name+"','"+pickerId+"');\" href=\"javascript:void(0);\" ><i class=\"fa fa-download\"></i></a>";
			    $("#"+file.id).find("div.cancel").html(operBtnHtml);
				parent.layer.close(initConfig.progressWinIndex);
			}).on("error",function(errortype){
				PlatUtil.webuploadErrorEvent(errortype,initConfig);
			}).on("startUpload",function(){
				PlatUtil.webuploadStartUploadEvent(initConfig);
			}).on("fileQueued",function(file){
				fileList.css("display","block");
				fileList.append( '<div id="' + file.id + '" class="uploadify-queue-item">' +
			    	'<span class="stateIcon" title=""></span>'+
			    	'<div class="cancel"></div>'+
			        '<span class="fileName">' + file.name+'('+WebUploader.Base.formatSize(file.size)+') </span>' +
			        '<span class="state">等待上传...</span>' +
			    	'<div class="fileImage"><i class="fa fa-file-archive-o" ></i></div>'+
			    '</div>' );
			}).on("uploadProgress",function(file, percentage){
				var $li = $("#"+file.id ),$percent = $li.find('.uploadify-progress-bar');
			    // 避免重复创建
			    if ( !$percent.length ) {
			        $('<div class="uploadify-progress">' +
			          '<div class="uploadify-progress-bar" style="width: 0%;"></div>' +
			          '</div>' ).appendTo( $li );
			    }
			    $li.find('span.state').text('上传中-'+percentage * 100 + '%');
			    $li.find('.uploadify-progress-bar').css( 'width', percentage * 100 + '%' );
			}).on("uploadError",function(file){
				$( '#'+file.id ).find('span.state').text('上传出错');
			    $( '#'+file.id ).find('span.stateIcon').attr("class","stateIcon");
			    $( '#'+file.id ).find('span.stateIcon').addClass("error");
			    $( '#'+file.id ).find('span.stateIcon').attr("title","错误");
			    $( '#'+file.id ).find('span.stateIcon').html('<i class="fa fa-exclamation-circle"></i>');
			});
		}
		
	},
	/**
	 * 获取单图片上传控件的值
	 */
	getSingleImgWebUploaderValue:function(bindId){
		var originalfilename = $("#"+bindId).find("img").attr("originalfilename");
		if(originalfilename){
			var dbfilepath = $("#"+bindId).find("img").attr("dbfilepath");
			var valueObj = {
				dbfilepath:dbfilepath,
				originalfilename:originalfilename
			};
			var values = [];
			values.push(valueObj);
			return JSON.stringify(values);
		}else{
			return "";
		}
	},
	/**
	 * 获取多文件列表上传值
	 */
	getMultiFileWebUploadValue:function(bindId){
		var fileList = $("#"+bindId+"_filelist").children();
		if(fileList.length>0){
			var values = [];
			var fileserverurl = $("#"+bindId+"_filelist").attr("fileserverurl");
			var fileuploadserver = "1";
			if(fileserverurl&&fileserverurl!=""){
				fileuploadserver = "2";
			}
			$.each(fileList,function(index,obj){
				var originalfilename = $(obj).find("span[class='fileName']").attr("originalfilename");
				var dbfilepath = $(obj).find("span[class='fileName']").attr("dbfilepath");
				var valueObj = {
					dbfilepath:dbfilepath,
					originalfilename:originalfilename,
					fileuploadserver:fileuploadserver
				};
				values.push(valueObj);
			});
			return JSON.stringify(values);
		}else{
			return "";
		}
	},
	/**
	 * 获取多图片上传控件的值
	 * @param bindId
	 */
	getMultiImageWebUploadValue:function(bindId){
		var fileList = $("#"+bindId+"_filelist").children();
		if(fileList.length>0){
			var values = [];
			var fileserverurl = $("#"+bindId+"_filelist").attr("fileserverurl");
			var maxuploadcount = $("#"+bindId+"_filelist").attr("maxuploadcount");
			var maxCount = new Number(maxuploadcount);
			if(fileList.length>maxCount){
				parent.layer.alert("最多只允许上传"+maxCount+"个文件",{icon: 7,resize:false});
				return "-1";
			}
			var fileuploadserver = "1";
			if(fileserverurl&&fileserverurl!=""){
				fileuploadserver = "2";
			}
			$.each(fileList,function(index,obj){
				var originalfilename = $(obj).attr("originalfilename");
				var dbfilepath = $(obj).attr("dbfilepath");
				var valueObj = {
					dbfilepath:dbfilepath,
					originalfilename:originalfilename,
					fileuploadserver:fileuploadserver
				};
				values.push(valueObj);
			});
			return JSON.stringify(values);
		}else{
			return "";
		}
	},
	/**
	 * 更新多图片上传控件文件数量
	 * @param bindId
	 */
    updateMulImageFileCount:function(bindId){
    	var fileList = $("#"+bindId+"_filelist").children();
    	var fileLength = fileList.length;
    	$("#"+bindId+"_filecountdiv").html("已经上传"+fileLength+"个文件");
    },
	/**
	 * 初始化图片控件
	 * @param bindId
	 */
	initMulImageEvent:function(bindId){
		PlatUtil.initFancyBox();
		$("#"+bindId+"_filelist").find("span[class='cancel']").on("click", function(){ 
			var liObj = $(this).parent().parent();
			var layerIndex = parent.layer.confirm("您确定删除该文件吗?", {
				icon: 7,
			    resize :false
			}, function(){
				liObj.remove();
				parent.layer.close(layerIndex);
				PlatUtil.updateMulImageFileCount(bindId);
			}, function(){
				
			});	
		}); 
		$(".filelist li").on('mouseenter', function() {
			$(this).find(".file-panel").stop().animate({
				height : 30
			});
		});
		$(".filelist li").on('mouseleave', function() {
			$(this).find(".file-panel").stop().animate({
				height : 0
			});
		});
	},
	/**
     * 初始化多图片上传控件
     * @param bindId
     */
	initMulImageUploader : function(bindId) {
    	var filesinglesizelimit = $("#"+bindId+"_wrapper").attr("filesinglesizelimit");
		var uploadrootfolder = $("#"+bindId+"_wrapper").attr("uploadrootfolder");
		var singleSizeLimit = new Number(filesinglesizelimit);
		PlatUtil.initMulImageEvent(bindId);
		// 添加的文件总大小
		if (!WebUploader.Uploader.support()) {
			alert('Web Uploader 不支持您的浏览器！');
			return;
		}
		// WebUploader实例
		var uploader = null;
		var GUID = WebUploader.Base.guid();
		var layerIndex = -1;
		// 实例化
		var initConfig = {
			pick : {
				id : '#'+bindId+'_filePicker',
				label : '点击选择图片'
			},
			formData : {
				uid : GUID,
				"uploadRootFolder":uploadrootfolder
			},
			accept : {
				title : "Images",
				extensions : "jpg,jpeg,png",
				mimeTypes : "image/*"
			},
			swf : '../../dist/Uploader.swf',
			chunked : false,
			auto:true,
			chunkSize : 512 * 1024,
			server : "system/FileAttachController/upload.do",
			disableGlobalDnd : true,
			duplicate:true,
			fileNumLimit : 20,
			fileSizeLimit : 200 * 1024 * 1024,
			fileSingleSizeLimit:singleSizeLimit
		};
		uploader = WebUploader.create(initConfig);
		// 添加“添加文件”的按钮，
		uploader.addButton({
			id : '#'+bindId+'_filePicker2',
			label : '添加图片'
		});
		uploader.on('ready', function() {
			window.uploader = uploader;
		});
		uploader.on("uploadSuccess", function( file,response) {
			var filelist  = $("#"+bindId+"_filelist");
			var dbfilepath = response.dbfilepath;
			var getfileurl=  response.getfileurl;
			var originalfilename = file.name;
			var fileid = file.id;
			var html = '<li id="'+fileid+'" dbfilepath="'+dbfilepath;
			html+='" getfileurl="'+getfileurl+'" originalfilename="'+originalfilename+'" >';
			html+='<p class="title">'+originalfilename+'</p><p class="imgWrap">';
			html+='<a href="'+getfileurl+'" class="fancybox" >';
			html+='<img src="'+getfileurl+'"></a></p><p class="progress"><span></span></p>';
			html+='<div class="file-panel" style="height:0px;"><span class="cancel"></span>';
			html+='</div><span class="success"></span></li>';
			filelist.append(html);
			parent.layer.close(initConfig.progressWinIndex);
			PlatUtil.initMulImageEvent(bindId);
			PlatUtil.updateMulImageFileCount(bindId);
		});
		
		uploader.on("startUpload",function(){
			PlatUtil.webuploadStartUploadEvent(initConfig);
		})
		uploader.onError = function(code) {
			var config = {
				accept:{
					extensions:"JPG,JPEG,PNG"
				},
				fileSingleSizeLimit:singleSizeLimit
			};
			initConfig = PlatUtil.mergeObject(initConfig,config);
			PlatUtil.webuploadErrorEvent(code,initConfig);
		};
		PlatUtil.updateMulImageFileCount(bindId);
	},
	/**
	 * 创建单个图片上传控件对象
	 * 
	 */
	createSingleImgWebUploader:function(config){
		var GUID = WebUploader.Base.guid();
		var initConfig = {
			// swf文件路径
		    swf: "plug-in/webuploader-0.1.5/dist/Uploader.swf",
		    // 文件接收服务端。
		    server: "system/FileAttachController/upload.do",
		    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
		    accept:{
		        title: "Images",
		        extensions: "gif,jpg,jpeg,bmp,png",
		        mimeTypes: "image/*"
		    },
		    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
		    resize: false,
		    auto:true,
		    prepareNextFile:true,
		    chunked:true,
		    //默认20M进行分块
		    chunkSize:20971520,
		    //允许并发上传的文件个数
		    threads: 1,
		    duplicate:true,
		    formData:{
		    	guid:GUID
		    }
		};
		initConfig = PlatUtil.mergeObject(initConfig,config);
		var pickerId = initConfig.pickerId;
		WebUploader.create(initConfig).on("uploadSuccess", function( file,response) {
			$("#"+pickerId).find("img").attr("src",response.getfileurl+"?times="+new Date().getTime()+"&t="+Math.random());
			$("#"+pickerId).find("img").attr("data-imgurl",response.getfileurl);
			$("#"+pickerId).find("img").attr("dbfilepath",response.dbfilepath);
			$("#"+pickerId).find("img").attr("originalfilename",response.originalfilename);
			$("#"+pickerId).parent().attr("href",response.getfileurl);
			var buttonhtml = "<i class=\"fa fa-crop\" onclick=\"PlatUtil.openPicCutWindow('"+pickerId+"');\" title=\"裁剪\"></i>";
			buttonhtml += "<i class=\"fa fa-trash\" onclick=\"PlatUtil.deleteSingleUploadImg('"+pickerId+"');\" title=\"删除\"></i>";
			$("#"+pickerId+"_imgUploadToolButton").html(buttonhtml);
			parent.layer.close(initConfig.progressWinIndex);
		}).on("error",function(errortype){
			PlatUtil.webuploadErrorEvent(errortype,initConfig);
		}).on("startUpload",function(){
			PlatUtil.webuploadStartUploadEvent(initConfig);
		}).addButton({
		    id: "#"+pickerId+"_imgUploadBtnContainer",
		    innerHTML: '<i class="fa fa-pencil"></i>',
		    multiple:false
		});
	},
	/**
	 * 百度上传控件开始上传事件实现
	 */
	webuploadStartUploadEvent:function(initConfig){
		if(!initConfig.stopupload){
			initConfig.progressWinIndex = parent.layer.msg("正在上传文件中...", {
				  icon: 16,
				  shade:0.01,
				  time:60000
			 });
		}
	},
	/**
	 * 百度上传控件错误事件实现
	 */
	webuploadErrorEvent:function(errortype,initConfig){
		if(errortype=="Q_TYPE_DENIED"){
			parent.layer.alert("上传文件类型不符合要求,只能上传后缀名为["+initConfig.accept.extensions+"]的文件"
					,{icon: 2,resize:false});
		}else if(errortype=="F_EXCEED_SIZE"){
			var fileLimit = PlatUtil.formatFileSize(initConfig.fileSingleSizeLimit);
			parent.layer.alert("文件大小超出限制,最大允许上传大小为"+fileLimit,{icon: 2,resize:false});
		}
		initConfig.stopupload = true;
	},
	/**
	 * 获取后台登录用户的信息
	 */
	getBackPlatLoginUser:function(){
		var userinfo = parent.$("#__BACK_PLAT_USER_JSON").val();
		if(!userinfo){
			userinfo = $("#__BACK_PLAT_USER_JSON").val();
		}
		if(!userinfo){
			userinfo = top.parent.$("#__BACK_PLAT_USER_JSON").val();
		}
		if(userinfo){
			var backLoginUser =  $.parseJSON(userinfo);
			return backLoginUser;
		}else{
			var userInfo = null;
			PlatUtil.ajaxProgress({
				async:"-1",
				url:"system/SysUserController.do?getUserSessionInfo",
				params : {},
				callback : function(resultJson) {
					var backLoginUserJson = resultJson.backLoginUserJson;
					userInfo = $.parseJSON(backLoginUserJson);
				}
			});
			return userInfo;
		}
	},
	/**
	 * 初始化平台UI框架
	 */
	initPlatFrame:function(){
	    //初始化中央tab控件
		PlatTab.init();
		//加载中央主要内容
		PlatUtil.loadMainFrame();
		//初始化tab和菜单
		PlatUtil.initTabAndMenu({
            "themeType": "4"
        });
		// 初始化菜单点击事件
		$('.menuiframe').unbind();
		$('.menuiframe').on('click',PlatTab.addTab);
	},
	/**
	 * 显示文章信息
	 */
	showArticleInfo:function(ARTICLE_SIGN,ARTICLE_EXTERNAL,ARTICLE_EXTERNALURL){
		if(ARTICLE_EXTERNAL=="1"){
			window.open(ARTICLE_EXTERNALURL,"_blank");
		}else{
			window.open("cms/ArticleController.do?view&ARTICLE_SIGN="+ARTICLE_SIGN,"_blank");
		}
	},
	/**
	 * 显示流程办理窗口
	 */
	showExecutionWindow:function(jbpmExeId,jbpmFlowSubject,jbpmIsQuery,jbpmOperingTaskId,callback){
		var url = "workflow/ExecutionController.do?goHandle&jbpmExeId="+jbpmExeId;
		url+=("&jbpmIsQuery="+jbpmIsQuery+"&jbpmOperingTaskId="+jbpmOperingTaskId);
		top.PlatUtil.openWindow({
			title:jbpmFlowSubject,
			area: ["100%","100%"],
			content: url,
			end:function(){
			  if(PlatUtil.isSubmitSuccess()){
				  if(callback){
					  callback.call(this);
				  }
			  }
			}
		});
	},
	/**
	 * 格式化流程实例状态
	 */
	formatExeStatus:function(cellvalue, options, rowObject){
		//rowObject对象代表这行的数据对象
	    if(cellvalue=="0"){
	       return "<span class=\"label label-warning\">草稿</span>";
	    }else if(cellvalue=="1"){
	       return "<span class=\"label label-primary\">正在办理</span>";      
	    }else if(cellvalue=="2"){
	       return "<span class=\"label label-info\">已办结(正常结束)</span>";      
	    }else if(cellvalue=="3"){
	       return "<span class=\"label label-primary\">已办结(审核通过)</span>";      
	    }else if(cellvalue=="4"){
	       return "<span class=\"label label-danger\">已办结(审核不通过)</span>";      
	    }else if(cellvalue=="5"){
	       return "<span class=\"label label-danger\">已办结(强制终止)</span>";      
	    }
	},
	/**
	 * 格式化流程实例剩余天数
	 */
	formatExeLeftDays:function(cellvalue, options, rowObject){
		//rowObject对象代表这行的数据对象
	   var EXECUTION_LEFTDAYS = rowObject.EXECUTION_LEFTDAYS;
	   if(EXECUTION_LEFTDAYS){
	    var LIMITTYPE = rowObject.LIMITTYPE;
	    var html ="";
	    if(EXECUTION_LEFTDAYS>=0){
	      html = "<span class=\"label label-primary\">剩余"+EXECUTION_LEFTDAYS;
	    }else{
	      html = "<span class=\"label label-danger\">超期"+Math.abs(EXECUTION_LEFTDAYS);
	    }
	    if(LIMITTYPE=="1"){
	      html+="个工作日";
	    }else{
	      html+="个自然日";
	    }
	    html+="</span>";
	    return html;
	  }else{
	    return "<span class=\"label label-info\">无截止期限</span>";   
	  }
	},
	/**
	 * 格式化流程任务剩余天数
	 */
	formatTaskLeftDays:function(cellvalue, options, rowObject){
		// rowObject对象代表这行的数据对象
	    var TASK_LEFTDAYS = rowObject.TASK_LEFTDAYS;
	    var TASK_LIMITTYPE = rowObject.TASK_LIMITTYPE;
		if(TASK_LEFTDAYS){
		    var html ="";
		    if(TASK_LEFTDAYS>=0){
		      html = "<span class=\"label label-primary\">剩余"+TASK_LEFTDAYS;
		    }else{
		      html = "<span class=\"label label-danger\">超期"+Math.abs(TASK_LEFTDAYS);
		    }
		    if(TASK_LIMITTYPE=="1"){
		      html+="个工作日";
		    }else{
		      html+="个自然日";
		    }
		    html+="</span>";
		    return html;
	    }else{
	        return "<span class=\"label label-info\">无截止期限</span>";      
	    }
	},
	/**
	 * 格式化流程任务状态
	 */
	formatTaskStatus:function(cellvalue, options, rowObject){
		//rowObject对象代表这行的数据对象
	    if(cellvalue=="-1"){
	       return "<span class=\"label label-warning\">挂起</span>";
	    }else if(cellvalue=="1"){
	       return "<span class=\"label label-primary\">正在办理</span>";      
	    }else if(cellvalue=="2"){
	       return "<span class=\"label label-blue\">已办理</span>";      
	    }else if(cellvalue=="3"){
	       return "<span class=\"label label-danger\">退回</span>";      
	    }else if(cellvalue=="4"){
	       return "<span class=\"label label-violet\">转发</span>";      
	    }else if(cellvalue=="5"){
	       return "<span class=\"label label-info\">结束流程</span>";      
	    }else if(cellvalue=="-2"){
	       return "<span class=\"label label-warning\">等待</span>";      
	    }else if(cellvalue=="6"){
	       return "<span class=\"label label-danger\">审核不通过</span>";      
	    }
	},
	/**
	 * 通用导出excel的方法
	 */
	exportExcel:function(config){
		var jqtableId = config.jqtableId;
		var excelFileName = config.excelFileName;
		var searchPanelId = jqtableId+"_search";
		var formcontrolid = $("#"+searchPanelId).attr("formcontrolid");
		var tempForm = document.createElement("form"); 
	    document.body.appendChild(tempForm); 
	    var queryParams =  PlatUtil.getQueryParams(searchPanelId);
	    for(var index in queryParams){
		    // 创建一个输入 
		    var input = document.createElement("input"); 
		    // 设置相应参数 
		    input.type = "hidden"; 
		    input.name = index; 
		    input.value = queryParams[index]; 
		    // 将该输入框插入到 form 中 
		    tempForm.appendChild(input); 
	     }
	     var fileNameInput = document.createElement("input"); 
	     // 设置相应参数 
	     fileNameInput.type = "hidden"; 
	     fileNameInput.name = "excelFileName"; 
	     fileNameInput.value = excelFileName; 
	     // 将该输入框插入到 form 中 
	     tempForm.appendChild(fileNameInput); 
	     var formControlIdInput = document.createElement("input"); 
	     // 设置相应参数 
	     formControlIdInput.type = "hidden"; 
	     formControlIdInput.name = "formControlId"; 
	     formControlIdInput.value = formcontrolid; 
	     tempForm.appendChild(formControlIdInput); 
		 tempForm.method = "POST"; 
		 // form 提交路径 
		 tempForm.action = "appmodel/CommonUIController.do?exportExcel"; 
		 // 对该 form 执行提交 
		 tempForm.submit(); 
		 // 删除该 form 
		 document.body.removeChild(tempForm); 
	},
	/**
	 * 打印网页
	 * @param elementId
	 */
	printHtml:function(elementId){
		var headstr = "<html><head><title></title></head><body>";
	    var footstr = "</body>";
	    var newstr = document.all.item(elementId).innerHTML;
	    var oldstr = document.body.innerHTML;
	    document.body.innerHTML = headstr+newstr+footstr;
	    window.print(); 
	    document.body.innerHTML = oldstr;
	    return false;
	},
	/**
	 * 设置平台的超时动作
	 */
	setSessionTimeOut:function(){
		$(document).ajaxComplete(function(event, xhr, settings) {
			if (xhr.getResponseHeader("backplatsessionstatus") == "timeout") {
				alert("会话过期，请重新登录!");
			    window.top.location.href = __ctxPath+"/security/LoginController/goBackLogin.do";
			}else if(xhr.getResponseHeader("backplatsessionstatus") == "forcelogout"){
				alert("当前用户被管理员强制下线!");
			    window.top.location.href = __ctxPath+"/security/LoginController/goBackLogin.do";
			}else if(xhr.getResponseHeader("backplatsessionstatus") == "sameuserlogout"){
				alert("检测到相同用户在异地登录,当前用户被强制下线!");
			    window.top.location.href = __ctxPath+"/security/LoginController/goBackLogin.do";
			} 
		});
	},
	/**
	 * 显示日志详细信息
	 * @param jqgridId
	 */
	showSysLogDetail : function(jqgridId) {
		var rowData = PlatUtil.getTableOperSingleRecord(jqgridId);
		if (rowData) {
			var DETAILABLE = rowData.DETAILABLE;
			if (DETAILABLE == "-1") {
				parent.layer.alert("所选择记录无详情信息!", {
					icon : 2,
					resize : false
				});
				return;
			}
			var LOG_ID = rowData.LOG_ID;
			var url = "system/SysLogController.do?goDetail&UI_DESIGNCODE=设计界面编码";
			var title = "新增系统日志信息";
			if (LOG_ID) {
				url += ("&LOG_ID=" + LOG_ID);
				title = "查看日志详情";
			}
			PlatUtil.openWindow({
				title : title,
				area : [ "90%", "90%" ],
				content : url,
				end : function() {
					
				}
			});
		}
	},
	/**
	 * 对字符串进行sha256加密算法加密
	 * @param sourceValue
	 */
	getSha256Encode:function(sourceValue){
		return CryptoJS.SHA256(sourceValue)+"";
	},
    /**
	 * 对字符串进行md5加密算法加密
     * @param sourceValue
     * @returns {string}
     */
	getMD5Encode:function(sourceValue){
		return md5(sourceValue);
	},
	/**
	 * 加载分页
	 * @param config
	 */
	loadPage:function(config,getPostParamsFn){
		var initConfig = {
			"page":1,
			"rows":10	
		};
		var pageId = config.pageId;
		var contentId = config.contentId;
		var params = PlatUtil.mergeObject(initConfig,config);
		if(getPostParamsFn!=null){
			var postParams = getPostParamsFn.call(this);
			params = PlatUtil.mergeObject(params,postParams);
	    }
		PlatUtil.ajaxProgress({
			url: "common/baseController/loadPage.do",
			params : params,
			callback : function(resultJson) {
				 var htmlObj = $(resultJson);
				 var PAGE_TOTALCOUNT= htmlObj.find("input[id='PAGE_TOTALCOUNT']").val();
				 laypage({
				    cont: $('#'+pageId),
				    skip: true, //是否开启跳页
				    pages: PAGE_TOTALCOUNT,
				    skin: '#1E9FFF',
				    curr: params.page || 1,
				    groups: 5, //连续显示分页数
				    jump: function(obj,first){
				    	$('#'+pageId).find(".laypage_skip").val(obj.curr);
				    	$('#'+pageId).find(".laypage_skip").attr("max",PAGE_TOTALCOUNT);
				    	if(!first){
				    		config.page  = obj.curr;
				    		PlatUtil.loadPage(config,getPostParamsFn);
				         }
				    }
				 });
				 $("#"+contentId).html("");
				 $("#"+contentId).html(resultJson);
			}
		});
	}
};

$(function(){
	 PlatUtil.setSessionTimeOut();
	 
});

