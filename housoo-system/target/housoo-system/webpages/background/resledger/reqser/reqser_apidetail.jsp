<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<div class="row">
<div class="col-sm-12">
	<div class="wrapper wrapper-content animated fadeInUp">
		<div class="ibox">
			<div class="ibox-content">
				<div class="row">
					<div class="col-sm-12">
						<dl class="dl-horizontal">
						    <dt>服务名称:</dt>
							<dd>${reqser.REQSER_NAME}</dd>
							<dt>状态:</dt>
							<dd>
							    <c:if test="${reqser.REQSER_STATUS=='1'}">
							      <span class="label label-primary">启用</span>
							    </c:if>
								<c:if test="${reqser.REQSER_STATUS=='-1'}">
							      <span class="label label-danger">停用</span>
							    </c:if>
							</dd>
							<dt>服务编码：</dt>
							<dd>${reqser.REQSER_CODE}</dd>
							<dt>调用地址：</dt>
							<dd>${reqser.REQSER_URL}</dd>
						</dl>
					</div>
				</div>
                 <div class="hr-line-dashed"></div>
                      <div class="row">
                          <div class="col-sm-12">
                              <div class="panel blank-panel">
                                  <div class="panel-heading">
                                       <dt>查询参数说明：</dt>
                                  </div>
                                  <div class="hr-line-dashed"></div>
                                  <div class="panel-body">
                                          <div class="well">
                                          <c:forEach items="${resqueryList}" var="resquery" varStatus="varStatus">
                        ${varStatus.index+1}.${resquery.RESQUERY_NAME}
                        <c:if test="${resquery.RESQUERY_NOTNULL=='1'}">
                        <font color="red"><b>【不能为空】</b></font>
                        </c:if>
                        <c:if test="${resquery.RESQUERY_NOTNULL=='-1'}">
                        <font color="green"><b>【可以为空】</b></font>
                        </c:if>
                        :${resquery.RESQUERY_DESC}<br>
                       					 </c:forEach>
                                          </div>
                                  </div>

                              </div>
                          </div>
                      </div>
                      
                      <div class="row">
                          <div class="col-sm-12">
                              <div class="panel blank-panel">
                                  <div class="panel-heading">
                                       <dt>返回参数说明：</dt>
                                  </div>
                                  <div class="hr-line-dashed"></div>
                                  <div class="panel-body">
                                       <table  class="table table-bordered table-hover" >
										<thead>
											<tr class="active">
												<th style="width:30%;">字段名</th>
										<th style="width:70%;">字段描述</th>
									</tr>
								</thead>
								<tbody>
								    <c:forEach items="${resourcesList}" var="resources">
								    <tr>
								        <td>${resources.FIELD_NAME}</td>
								        <td>${resources.FIELD_COMMENT}</td>
								    </tr>
								    </c:forEach>
								</tbody>
							</table>   
                                </div>

                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="panel blank-panel">
                                <div class="panel-heading">
                                     <dt>返回编码说明：</dt>
                                </div>
                                <div class="hr-line-dashed"></div>
                                <div class="panel-body">
                                        <div class="well">
                                        000:访问成功!<br>
                                        001:缺失必要参数!<br>
                                        002:未查询到对应服务!<br>
                                        003:服务停止,无法获取数据！<br>
                                        004:请求方IP地址无访问权限!<br>
                                        999:其它未知异常!<br>
                                        </div>
                                </div>

                            </div>
                        </div>
                    </div>
                    
                </div>
            </div>
        </div>
    </div>
</div>
