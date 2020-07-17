<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="dashboard-stat blue col-sm-2" style="margin-right:4.1%;">
	<div class="visual"><i class="fa fa-comments-o"></i></div>
	<div class="details">
           <div class="number"> ${dbnum} </div>
           <div class="desc"> 待办任务数量 </div>
       </div>
</div>

<div class="dashboard-stat red col-sm-2" style="margin-right:4.1%;">
		<div class="visual"><i class="fa fa-area-chart"></i></div>
		<div class="details">
            <div class="number"> ${Cpu}</div>
            <div class="desc"> CPU占有率 </div>
        </div>       
</div>
<div class="dashboard-stat green col-sm-2" style="margin-right:4.1%;">
	<div class="visual"><i class="fa fa-pie-chart"></i></div>
	<div class="details">
           <div class="number"> ${Memery}</div>
           <div class="desc"> 内存占有率 </div>
       </div>
</div>
<div class="dashboard-stat purple col-sm-2" style="margin-right:4.1%;">
	<div class="visual"><i class="fa fa-pie-chart"></i></div>
	<div class="details">
           <div class="number"> ${onlineUserNum}</div>
           <div class="desc"> 在线用户总数 </div>
       </div>
</div>
<%--<div class="dashboard-stat lightblue col-sm-2">
	<div class="visual"><i class="fa fa-television"></i></div>
	<div class="details">
           <div class="number"> ${videoClickNum} </div>
           <div class="desc"> 视频点击总数 </div>
       </div>
</div>--%>



