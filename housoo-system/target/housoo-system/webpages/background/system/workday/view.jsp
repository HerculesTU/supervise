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
	<plattag:resources restype="css" loadres="jquery-ui,plat-ui">
	</plattag:resources>
	<link href='plug-in/fullcalendar-2.4.0/fullcalendar.css' rel='stylesheet' />
	<style type='text/css'>
    #calendar
    {
        width: 1100px;
        margin: 0 auto;
    }
    #div1 {   
        text-align: center;  
        font-size: 12px;  
        font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;  
        }  
     .fc-toolbar{
     	margin-bottom: 5px;
     	margin-top: 5px;
     }
     h2{
     	font-size: 20px;
     }
 	</style>
  </head>
  
  <body>
    <div class="ui-layout-center" >
        <div class="panel-Title">
            <h5 id="design_datatablepaneltitle">工作日设置 </h5>
        </div>
        <div id="div1">
            <div id='calendar'></div>
        </div>
    </div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jquery-layout,plat-util">
</plattag:resources>
<script src='plug-in/fullcalendar-2.4.0/lib/moment.min.js'></script>
<script src='plug-in/fullcalendar-2.4.0/fullcalendar.min.js'></script>
<script>

    $(document).ready(function() {
        
        $('#calendar').fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month'
            },
             monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
             monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
             dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
             dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
             today: ["今天"],
             firstDay: 1,
             titleFormat:{
            	 month: 'YYYY年MMMM'          	 
             },
             buttonText: {
                 today: '今天',
                 month: '月',
                 prev: '上一月',
                 next: '下一月'
             },
            editable: false,
            weekMode:'liquid',
            aspectRatio:'2.68',
            viewRender : function (view) {//动态把数据查出，按照月份动态查询
                var viewStart = moment(view.start).format("YYYY-MM-DD");
                var viewEnd = moment(view.end).format("YYYY-MM-DD");
                $("#calendar").fullCalendar('removeEvents');
              PlatUtil.ajaxProgress({
       			url:"system/WorkdayController.do?findData",
       			params : { 'Q_T.WORKDAY_DATE_GE': viewStart, 'Q_T.WORKDAY_DATE_LT': viewEnd },
       			callback : function(resultJson) {
       				if (resultJson.success) {
       					var resultCollection =  jQuery.parseJSON(resultJson.data);;
       					$.each(resultCollection, function (index, term) {
                               $("#calendar").fullCalendar('renderEvent', term, true);
                           });
       				} else {
       					parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
       				}
       			}
       		}); 
            //把从后台取出的数据进行封装以后在页面上以fullCalendar的方式进行显示
            },
            eventClick: function (event) {
            	var url = "system/WorkdayController.do?goForm&UI_DESIGNCODE=sysWorkDayForm&WORKDAY_ID="+event.WORKDAY_ID;
            	PlatUtil.openWindow({
            		title:"工作日设置",
            		area: ["800px","300px"],
            		content: url,
            		end:function(){
            		  if(PlatUtil.isSubmitSuccess()){
            			  var WORKDAY_SETID = PlatUtil.getData("WORKDAY_SETID");
            		      if(WORKDAY_SETID){
            		    	if(WORKDAY_SETID=="1"){
                                  event.color = "#FF0000";
                      			event.title = "休息日";
                      		}else if(WORKDAY_SETID=="2"){
                      			event.color = "#3a87ad";
                      			event.title = "工作日";
                      		}
                          	$("#calendar").fullCalendar('updateEvent', event);  
            		    	PlatUtil.removeData("WORKDAY_SETID");
            		      }
            		  }
            		}
            	});
            }
        });
        
    });

</script>

