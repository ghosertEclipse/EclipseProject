<!-- 页面中使用了JSTL Core taglib 和Spring lib-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- 设定页面编译时采用UTF-8编码，同时指定浏览器显示时采取UTF-8解码-->
<%@ page pageEncoding="utf-8" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>JMobile</title> 
<!-- Disable the right click -->
<script language="JavaScript">
<!-- 
if (window.Event) 
document.captureEvents(Event.MOUSEUP); 
function nocontextmenu() 
{
event.cancelBubble = true
event.returnValue = false;
return false;
}
function norightclick(e) 
{
if (window.Event) 
{
if (e.which == 2 || e.which == 3)
return false;
}
else
if (event.button == 2 || event.button == 3)
{
event.cancelBubble = true
event.returnValue = false;
return false;
}
}
document.oncontextmenu = nocontextmenu; 
document.onmousedown = norightclick; 
// -->
</script>
</head>

<style type="text/css">
<!--
body,ul,ol,li,p,h1,h2,h3,h4,h5,h6,form,fieldset,table,td,img,div{margin:0;padding:0;border:0;}
body,ul,ol,li,p,form,fieldset,table,td{font-family:"宋体";}
td{font-size:12px;color: #ffffff;}
.blacktd{font-size:12px;color: #000000;}
.orangetd{font-size:12px;color: #FF9900;}
p,li,select,input,textarea,div{font-size:12px;color:#000000;}
.f14{font-size:14px;}
a:visited, a:link{text-decoration:none;color:#ffffff;}
a:hover,a:active,a:focus{text-decoration:none;color:#ff0000;}
a.redreda:visited, a.redreda:link{text-decoration:none;color:#ce0000;}
a.redreda:hover,a.redreda:active,a.redreda:focus{text-decoration:none;color:#ff0000;}
a.redorga:visited, a.redorga:link{text-decoration:none;color:#ce0000;}
a.redorga:hover,a.redorga:active,a.redorga:focus{text-decoration:none;color:#ff9900;}
.input {background:#fffdc8; border:1px #ad3100 solid;}
-->
</style>

<body>
