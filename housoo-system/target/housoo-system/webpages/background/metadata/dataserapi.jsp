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
							<dd>${dataSer.DATASER_NAME}</dd>
							<dt>状态:</dt>
							<dd>
							    <c:if test="${dataSer.DATASER_STATUS=='1'}">
							      <span class="label label-primary">启用</span>
							    </c:if>
								<c:if test="${dataSer.DATASER_STATUS=='-1'}">
							      <span class="label label-danger">停用</span>
							    </c:if>
							</dd>
							<dt>服务编码：</dt>
							<dd>${dataSer.DATASER_CODE}</dd>
							<dt>调用地址：</dt>
							<dd>${dataSer.REQSER_URL}</dd>
						</dl>
					</div>
				</div>
                 <div class="hr-line-dashed"></div>
                      <div class="row">
                          <div class="col-sm-12">
                              <div class="panel blank-panel">
                                  <div class="panel-heading">
                                       <dt>输入参数说明：</dt>
                                  </div>
                                  <div class="hr-line-dashed"></div>
                                  <div class="panel-body">
                                       <table  class="table table-bordered table-hover" >
										<thead>
											<tr class="active">
											    <th style="width:20%;">参数中文名</th>	
												<th style="width:20%;">参数英文名</th>	
												<th style="width:10%;">允许为空</th>
												<th style="width:10%;">最大长度</th>
												<th style="width:10%;">验证规则</th>
												<th style="width:30%;">参数说明</th>
											</tr>
										</thead>
										<tbody>
										    <c:forEach items="${resqueryList}" var="field">
										    <tr>
										        <td>${field.QUERY_CN}</td>
										        <td>${field.QUERY_EN}</td>
										        <c:if test="${field.QUERY_NULLABLE=='1'}">
										           <td>允许</td>
										        </c:if>
										        <c:if test="${field.QUERY_NULLABLE=='-1'}">
										           <td>不允许</td>
										        </c:if>
										        <td>${field.QUERY_LENGTH}</td>
										        <td>${field.DIC_NAME}</td>
										        <td>${field.QUERY_DESC}</td>
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
								    <c:forEach items="${returnFieldList}" var="field">
								    <tr>
								        <td>${field.FIELD_NAME}</td>
								        <td>${field.FIELD_COMMENT}</td>
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
                                        005:输入参数不合法!<br>
                                        006:请求方授权码无效!<br>
                                        007:请求服务未被授权!<br>
                                        008:超出当日调用次数!<br>
                                        999:其它异常!<br>
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
