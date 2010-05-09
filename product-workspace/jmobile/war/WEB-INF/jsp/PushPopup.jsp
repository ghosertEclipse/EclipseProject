<%@ include file="IncludeHead.jsp" %>

<table width="500" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#cc0000">
<tbody>
<tr><td height="8"></td></tr>

  <tr><td>
      <table width="96%" height="26" border="0" align="center" cellpadding="0" cellspacing="0" background="images/bg_04.gif">
          <tr>
            <td width="4">&nbsp;</td>
            <td><font size="3" color="white"><b><spring:message code="DOWNLOAD_TITLE"/></b></font></td>
            <td width="4">&nbsp;</td>
          </tr>
      </table>
      <table width="96%" height="26" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FF9900">
		  <tr align="center">
          <!-- ringtone or picture and mediaplayer -->
		  <td>
		    <table><tr><td>
				<table align="center" background="images/1876.gif" border="0" cellpadding="0" cellspacing="0" height="153" width="139">
				  <tr><td height="15"></td></tr>
				  <tr><td>
			      <c:if test="${item.itemType == '1'}">
			        <div align="center"><img src="images/trylisten.gif" border="0" height="128" width="128"></div>
			      </c:if>
			      <c:if test="${item.itemType == '0'}">
			        <div align="center"><img src="<c:out value="${item.url}"/>" border="0" height="128" width="128"></div>
			      </c:if>
				  </td></tr>
				  <tr><td height="10"></td></tr>
				</table>
			</td></tr>
		    <tr><td>
		      <c:if test="${item.itemType == '1'}">
				<embed src="${item.url}" showstatusbar="true" width="140" height="20" showcontrols="false"
				       loop="false" hidden="false" autostart="true"/>
		      </c:if>
			</td></tr></table>
		  </td>
          <!-- description -->
		  <td>
		    <spring:message code="PUSH_DESC" arguments="${item.id},${item.name}"/>
		  </td>
		  </tr>
      </table>
  </td></tr>
  
<tr><td height="8"></td></tr>
</tbody>
</table>

<%@ include file="IncludeTail.jsp" %>