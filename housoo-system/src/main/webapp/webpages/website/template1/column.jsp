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

<title>成功案例</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="webpages/website/template1/assets/css/amazeui.css" />
<link rel="stylesheet"
	href="webpages/website/template1/assets/css/common.min.css" />
<link rel="stylesheet"
	href="webpages/website/template1/assets/css/example.min.css" />
</head>
<body>
  <div class="layout">
    <!--===========layout-header================-->
    <div class="layout-header am-hide-sm-only">
      <!--topbar start-->
      <div class="topbar">
        <div class="container">
          <div class="am-g">
            <div class="am-u-md-3">
              <div class="topbar-left">
                <i class="am-icon-globe"></i>
                <div class="am-dropdown" data-am-dropdown>
                  <button class="am-btn am-btn-primary am-dropdown-toggle" data-am-dropdown-toggle>Language <span class="am-icon-caret-down"></span></button>
                  <ul class="am-dropdown-content">
                    <li><a href="#">English</a></li>
                    <li class="am-divider"></li>
                    <li><a href="#">Chinese</a></li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="am-u-md-9">
              <div class="topbar-right am-text-right am-fr">
                Follow us
                <i class="am-icon-facebook"></i>
                <i class="am-icon-twitter"></i>
                <i class="am-icon-google-plus"></i>
                <i class="am-icon-pinterest"></i>
                <i class="am-icon-instagram"></i>
                <i class="am-icon-linkedin"></i>
                <i class="am-icon-youtube-play"></i>
                <i class="am-icon-rss"></i>
                <a href="./login.html">登录</a>
                <a href="./register.html">注册</a>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!--topbar end-->

      <div class="header-box" data-am-sticky>
        <!--header start-->
          <div class="container">
            <div class="header">
              <div class="am-g">
                <div class="am-u-lg-2 am-u-sm-12">
                  <div class="logo">
                    <a href=""><img src="webpages/website/template1/assets/images/logo.png" alt="" /></a>
                  </div>
                </div>
                <div class="am-u-md-10">
                  <div class="header-right am-fr">
                    <div class="header-contact">
                      <div class="header_contacts--item">
  											<div class="contact_mini">
  												<i style="color:#7c6aa6" class="contact-icon am-icon-phone"></i>
  												<strong>0 (855) 233-5385</strong>
  												<span>周一~周五, 8:00 - 20:00</span>
  											</div>
  										</div>
                      <div class="header_contacts--item">
  											<div class="contact_mini">
  												<i style="color:#7c6aa6" class="contact-icon am-icon-envelope-o"></i>
  												<strong>cn@yunshipei.com</strong>
  												<span>随时欢迎您的来信！</span>
  											</div>
  										</div>
                      <div class="header_contacts--item">
  											<div class="contact_mini">
  												<i style="color:#7c6aa6" class="contact-icon am-icon-map-marker"></i>
  												<strong>天使大厦,</strong>
  												<span>海淀区海淀大街27</span>
  											</div>
  										</div>
                    </div>
                    <a href="html/contact.html" class="contact-btn">
                      <button type="button" class="am-btn am-btn-secondary am-radius">联系我们</button>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        <!--header end-->


        <!--nav start-->
        <div class="nav-contain">
          <div class="nav-inner">
            <ul class="am-nav am-nav-pills am-nav-justify">
              <li class=""><a href="../index.html">首页</a></li>
              <li>
                <a href="#">产品中心</a>
                <!-- sub-menu start-->
                <ul class="sub-menu">
									<li class="menu-item"><a href="./product1.html">产品展示1</a></li>
									<li class="menu-item"><a href="./product2.html">产品展示2</a></li>
									<li class="menu-item"><a href="./product3.html">产品展示3</a></li>
								</ul>
                <!-- sub-menu end-->
              </li>
              <li><a href="./example.html">客户案例</a></li>
              <li><a href="./solution.html">解决方案</a></li>
              <li>
                <a href="./news.html">新闻中心</a>
                <!-- sub-menu start-->
                <ul class="sub-menu">
									<li class="menu-item"><a href="./news-content.html">公司动态</a></li>
									<li class="menu-item"><a href="./404-dark.html">行业动态</a></li>
									<li class="menu-item"><a href="./404-light.html">精彩专题</a></li>
								</ul>
                <!-- sub-menu end-->
              </li>
              <li><a href="./about.html">关于我们</a></li>
              <li><a href="./join.html">加入我们</a></li>
              <li><a href="./contact.html">联系我们</a></li>
            </ul>
          </div>
        </div>
        <!--nav end-->
      </div>

    </div>
    <!--mobile header start-->
    <div class="m-header">
      <div class="am-g am-show-sm-only">
        <div class="am-u-sm-2">
          <div class="menu-bars">
            <a href="#doc-oc-demo1" data-am-offcanvas="{effect: 'push'}"><i class="am-menu-toggle-icon am-icon-bars"></i></a>
            <!-- 侧边栏内容 -->
            <nav data-am-widget="menu" class="am-menu  am-menu-offcanvas1" data-am-menu-offcanvas >
              <a href="javascript: void(0)" class="am-menu-toggle"></a>

              <div class="am-offcanvas" >
                <div class="am-offcanvas-bar">
                  <ul class="am-menu-nav am-avg-sm-1">
                    <li><a href="../index.html" class="" >首页</a></li>
                    <li class="am-parent">
                      <a href="#" class="" >产品中心</a>
                      <ul class="am-menu-sub am-collapse ">
                        <li class="">
                          <a href="./product1.html" class="" >产品展示1</a>
                        </li>
                        <li class="">
                          <a href="./product2.html" class="" >产品展示2</a>
                        </li>
                        <li class="">
                          <a href="./product3.html" class="" >产品展示3</a>
                        </li>
                      </ul>
                    </li>
                    <li class=""><a href="./example.html" class="" >客户案例</a></li>
                    <li class=""><a href="./solution.html" class="" >解决方案</a></li>
                    <li class="am-parent">
                      <a href="./news.html" class="" >新闻中心</a>
                      <ul class="am-menu-sub am-collapse  ">
                        <li class="">
                          <a href="./news-content.html" class="" >公司动态</a>
                        </li>
                        <li class="">
                          <a href="./404-dark.html" class="" >行业动态</a>
                        </li>
                        <li class="">
                          <a href="./404-light.html" class="" >精彩专题</a>
                        </li>
                      </ul>
                    </li>
                    <li class=""><a href="./about.html" class="" >关于我们</a></li>
                    <li class=""><a href="./join.html" class="" >加入我们</a></li>
                    <li class=""><a href="./contact.html" class="" >联系我们</a></li>
                    <li class="am-parent">
                      <a href="./news.html" class="nav-icon nav-icon-globe" >Language</a>
                      <ul class="am-menu-sub am-collapse  ">
                        <li>
                          <a href="#" >English</a>
                        </li>
                        <li class="">
                          <a href="#" >Chinese</a>
                        </li>
                      </ul>
                    </li>
                    <li class="nav-share-contain">
                      <div class="nav-share-links">
                        <i class="am-icon-facebook"></i>
                        <i class="am-icon-twitter"></i>
                        <i class="am-icon-google-plus"></i>
                        <i class="am-icon-pinterest"></i>
                        <i class="am-icon-instagram"></i>
                        <i class="am-icon-linkedin"></i>
                        <i class="am-icon-youtube-play"></i>
                        <i class="am-icon-rss"></i>
                      </div>
                    </li>
                    <li class=""><a href="./login.html" class="" >登录</a></li>
                    <li class=""><a href="./register.html" class="" >注册</a></li>
                  </ul>

                </div>
              </div>
            </nav>

          </div>
        </div>
        <div class="am-u-sm-5 am-u-end">
          <div class="m-logo">
            <a href=""><img src="webpages/website/template1/assets/images/logo.png" alt=""></a>
          </div>
        </div>
      </div>
      <!--mobile header end-->
    </div>




    <!--===========layout-container================-->
    <div class="layout-container">
      <div class="page-header">
        <div class="am-container">
          <h1 class="page-header-title">客户案例</h1>
        </div>
      </div>

      <div class="breadcrumb-box">
        <div class="am-container">
          <ol class="am-breadcrumb">
            <li><a href="../index.html">首页</a></li>
            <li class="am-active">客户案例</li>
          </ol>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="container">
        <div class="section--header">
					<h2 class="section--title">全球首创 自主创新</h2>
					<p class="section--description">
						Enterplorer Studio是一套面向企业级移动信息化建设的开发平台。集聚开发、测试、
						<br>打包、发布于一体的移动化开发综合平台。
					</p>
				</div>

        <div class="example-container">
          <div class="am-tabs" data-am-tabs>
            <ul class="am-tabs-nav am-nav am-nav-tabs am-g">
              <li class="am-active am-u-md-2"><a href="#tab-4-1"><i class="am-icon-map-o"></i>主要案例</a></li>
              <li class="am-u-md-2"><a href="#tab-4-2"><i class="am-icon-scribd"></i>客户案例一</a></li>
              <li class="am-u-md-2"><a href="#tab-4-3"><i class="am-icon-odnoklassniki"></i>客户案例二</a></li>
              <li class="am-u-md-2"><a href="#tab-4-4"><i class="am-icon-building-o"></i>客户案例三</a></li>
              <li class="am-u-md-2"><a href="#tab-4-5"><i class="am-icon-hand-scissors-o "></i>客户案例四</a></li>
              <li class="am-u-md-2"><a href="#tab-4-6"><i class="am-icon-camera"></i>客户案例五</a></li>
            </ul>
            <div class="am-tabs-bd am-tabs-bd-ofv">
              <div class="am-tab-panel am-active" id="tab-4-1">
                 <div class="am-g">
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                 </div>

                 <div class="am-g">
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                 </div>

                 <div class="am-g">
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                 </div>

                 <div class="am-g">
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                   <div class="am-u-md-3">
                     <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                    <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                     <span>了解更多></span>
                   </div>
                 </div>
              </div>
              <div class="am-tab-panel" id="tab-4-2">
                <div class="am-g">
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                </div>

                <div class="am-g">
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                </div>

                <div class="am-g">
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                </div>
              </div>
              <div class="am-tab-panel" id="tab-4-3">
                <div class="am-g">
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                </div>

                <div class="am-g">
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                </div>
              </div>


              <div class="am-tab-panel" id="tab-4-4">
                <div class="am-g">
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                </div>
              </div>

              <div class="am-tab-panel" id="tab-4-5">
                <div class="am-g">
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                </div>
              </div>

              <div class="am-tab-panel" id="tab-4-6">
                <div class="am-g">
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                  <div class="am-u-md-3">
                    <a href="#" style="background-image: url('webpages/website/template1/assets/images/example/example1.jpg');" class="example-item-bg"></a>
                   <img src="webpages/website/template1/assets/images/example/logo_hx_active.png" alt="">
                    <span>了解更多></span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>





    <!--===========layout-footer================-->
    <div class="layout-footer">
      <div class="footer">
        <div style="background-color:#383d61" class="footer--bg"></div>
        <div class="footer--inner">
          <div class="container">
            <div class="footer_main">
              <div class="am-g">
                <div class="am-u-md-3 ">
                  <div class="footer_main--column">
                    <strong class="footer_main--column_title">关于我们</strong>
                    <div class="footer_about">
												<p class="footer_about--text">
													云适配(AllMobilize Inc.) 是全球领先的HTML5企业移动化解决方案供应商，由前微软美国总部IE浏览器核心研发团队成员及移动互联网行业专家在美国西雅图创立.
												</p>
												<p class="footer_about--text">
                          云适配跨屏云也成功应用于超过30万家企业网站，包括微软、联想等世界500强企业
												</p>
											</div>
                  </div>
                </div>

                <div class="am-u-md-3 ">
                  <div class="footer_main--column">
										<strong class="footer_main--column_title">产品中心</strong>
										<ul class="footer_navigation">
											<li class="footer_navigation--item"><a href="#" class="footer_navigation--link">Enterplorer 企业浏览器</a></li>
											<li class="footer_navigation--item"><a href="#" class="footer_navigation--link">Xcloud 网站跨屏云</a></li>
											<li class="footer_navigation--item"><a href="#" class="footer_navigation--link">Amaze UI 前端开发框架</a></li>
											<li class="footer_navigation--item"><a href="#" class="footer_navigation--link">Amaze UI 前端开发框架</a></li>
											<li class="footer_navigation--item"><a href="#" class="footer_navigation--link">Amaze UI 前端开发框架</a></li>
										</ul>
									</div>
                </div>

                <div class="am-u-md-3 ">
                  <div class="footer_main--column">
										<strong class="footer_main--column_title">技术支持</strong>
										<ul class="footer_navigation">
											<li class="footer_navigation--item"><a href="#" class="footer_navigation--link">企业移动信息化白皮书</a></li>
											<li class="footer_navigation--item"><a href="#" class="footer_navigation--link">企业移动信息化白皮书</a></li>
											<li class="footer_navigation--item"><a href="#" class="footer_navigation--link">企业移动信息化白皮书</a></li>
											<li class="footer_navigation--item"><a href="#" class="footer_navigation--link">企业移动信息化白皮书</a></li>
											<li class="footer_navigation--item"><a href="#" class="footer_navigation--link">企业移动信息化白皮书</a></li>
										</ul>
									</div>
                </div>

                <div class="am-u-md-3 ">
                  <div class="footer_main--column">
										<strong class="footer_main--column_title">联系详情</strong>
										<ul class="footer_contact_info">
											<li class="footer_contact_info--item"><i class="am-icon-phone"></i><span>服务专线：400 069 0309</span></li>
											<li class="footer_contact_info--item"><i class="am-icon-envelope-o"></i><span>yunshipei.com</span></li>
											<li class="footer_contact_info--item"><i class="am-icon-map-marker"></i><span>北京市海淀区海淀大街27号天使大厦（原亿景大厦）三层</span></li>
											<li class="footer_contact_info--item"><i class="am-icon-clock-o"></i><span>Monday - Friday, 9am - 6 pm; </span></li>
										</ul>
									</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
	<script src="webpages/website/template1/assets/js/jquery-2.1.0.js"></script>
	<script src="webpages/website/template1/assets/js/amazeui.js"></script>
	<script src="webpages/website/template1/assets/js/common.js"></script>
</body>

</html>
