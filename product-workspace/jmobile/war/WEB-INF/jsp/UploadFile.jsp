<%@ include file="IncludeHead.jsp" %>

<form name="uploadFileForm"  method="post" enctype="multipart/form-data" target="_blank">
<table width=550 border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#cc0000">
<tbody>
<tr><td height="8"></td></tr>

  <tr><td>
	<table width="98%" align="center" bordercolor="#ED6703" bgcolor="#FF9900">
	  <tr>
	    <td width="33%">
	      <spring:message code="INPUT_MOBILE_TEXT"/>
	    </td>
	    <td width="67%" colspan="2">
		  <spring:bind path="uploadFileForm.mobile">
	        <input type="text" class="input" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		  <!-- 
	        <input type="text" class="input" name="<c:out value="${status.expression}"/>" value="13916939847"/>
          -->
		  </spring:bind>
	    </td>
	  </tr>
	  
	  <!-- Show the error message -->
	  <spring:bind path="uploadFileForm.mobile">
	    <c:if test="${!empty status.errorMessage}">
	    <tr>
	      <td></td>
	      <td colspan="2"> <c:out value="${status.errorMessage}"/> </td>
	    </tr>
	    </c:if>
	  </spring:bind>
	  
	  <tr>
	    <td>
	      <spring:message code="INPUT_DESC_TEXT"/>
	    </td>
	    <td colspan="2">
		  <spring:bind path="uploadFileForm.description">
	        <input type="text" class="input" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		  </spring:bind>
	    </td>
	  </tr>
	  
	  <!-- Show the error message -->
	  <spring:bind path="uploadFileForm.description">
	    <c:if test="${!empty status.errorMessage}">
	    <tr>
	      <td></td>
	      <td colspan="2"> <c:out value="${status.errorMessage}"/> </td>
	    </tr>
	    </c:if>
	  </spring:bind>
	  
	  <tr>
	    <td>
	      <spring:message code="INPUT_AUTHOR_TEXT"/>
	    </td>
	    <td colspan="2">
		  <spring:bind path="uploadFileForm.author">
	        <input type="text" class="input" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
		  </spring:bind>
	    </td>
	  </tr>
	  
	  <!-- Show the error message -->
	  <spring:bind path="uploadFileForm.author">
	    <c:if test="${!empty status.errorMessage}">
	    <tr>
	      <td></td>
	      <td colspan="2"> <c:out value="${status.errorMessage}"/> </td>
	    </tr>
	    </c:if>
	  </spring:bind>
	  
	  <tr>
	    <td>
	      <spring:message code="UPLOAD_FILE_NAME"/>
	    </td>
	    <td colspan="2">
		  <spring:bind path="uploadFileForm.uploadfile">
		    <input type="file" name="file" class="input"/>
		    <input type="hidden" name="uploadfile"/>
		  </spring:bind>
	    </td>
	  </tr>
	  
	  <!-- Show the error message -->
	  <spring:bind path="uploadFileForm.uploadfile">
	    <c:if test="${!empty status.errorMessage}">
	    <tr>
	      <td></td>
	      <td colspan="2"> <c:out value="${status.errorMessage}"/> </td>
	    </tr>
	    </c:if>
	  </spring:bind>
	  
	  <tr height="30" valign="bottom">
	    <td>
	      <input type="button" name="upload" class="input" value="<spring:message code="UPLOAD_CONFIRM"/>"
	             onclick="javascript:checkFileType('<spring:message code="UPLOAD_FILETYPE_INVALIDATE"/>')">
	    </td>
	    <td>
	      <input type="reset" name="reset" class="input" value="<spring:message code="UPLOAD_RESET"/>">
	    </td>
	    <td>
	      <input type="button" name="query" class="input" value="<spring:message code="QUERY_UPLOAD"/>"
	             onclick="javascript:queryUploadFile()">
	    </td>
	  </tr>
	  
	  <tr><td colspan="3">
	    <table>
	      <tr><td colspan="5"><spring:message code="WEEK_PAYTIMES_LIST"/></td></tr>
		  <tr>
		    <td width="10%"><spring:message code="PLACE_POSITION"/></td>
		    <td width="20%"><spring:message code="RESOURCE_UPLOADER"/></td>
		    <td width="20%"><spring:message code="WEEK_PAYTIMES"/></td>
		    <td width="20%"><spring:message code="PAY_PERCENT"/></td>
		    <td width="20%"><spring:message code="RETRIEVE_CASH"/></td>
		  </tr>
		  <c:forEach items="${uploaders}" var="item" varStatus="status">
		   <tr>
		    <td>${status.index + 1}</td>
		    <td>${item[0]}</td>
		    <td align="center">${item[1]}</td>
		    <td><fmt:formatNumber value="${0.1 * ((5 - status.index) > 0 ? 5 - status.index : 0)}" pattern="00%"/></td>
		    <c:if test="${status.index < 5}"> 
		      <td>${item[1]} * <fmt:formatNumber value="${0.1 * ((5 - status.index) > 0 ? 5 - status.index : 0)}" pattern="00%"/> = <fmt:formatNumber value="${item[1] * 0.1 * (5 - status.index)}" pattern="#,##0.00"/></td>
		    </c:if>
		    <c:if test="${status.index >= 5}"> 
		      <td><fmt:formatNumber value="${0}" pattern="#,##0.00"/></td>
		    </c:if>
		  </tr>
		  </c:forEach>
	      <tr><td colspan="5"><spring:message code="WEEK_PAYTIMES_LIST_PAY_DESC"/></td></tr>
	    </table>
	  </td></tr>
	  
	  <tr>
	    <td colspan="3">
	      <font color="#000000"><spring:message code="UPLOAD_DESCRIPTION"/></font>
	    </td>
	  </tr>
	  
	</table>
  </td></tr>

<tr><td height="8"></td></tr>
</tbody>
</table>
</form>

<script language="javascript">
function uploadFile() {
  document.all.uploadFileForm.action="uploadFile.do";
  document.all.uploadFileForm.submit();
}
function queryUploadFile() {
  document.all.uploadFileForm.action="viewUploadFiles.do";
  document.all.uploadFileForm.submit();
}
function checkFileType(supportPrompt) {
  var length = document.all.file.value.length;
  var ext = document.all.file.value.substring(length - 3, length);
  if (ext!="jpg" && ext!="JPG" && ext!="jpeg" && ext!="JPEG" && ext!="mmf" && ext!="MMF" 
      && ext!="wav" && ext!="WAV" && ext!="amr" && ext!="AMR" && ext!="mid" && ext!="MID"
      && ext!="mp3" && ext!="MP3" && ext!="imy" && ext!="IMY" && ext!="wma" && ext!="WMA"
      && ext!="bmp" && ext!="BMP" && ext!="gif" && ext!="GIF" && ext!="ico" && ext!="ICO"
      && ext!="png" && ext!="PNG") {
    alert(supportPrompt);
  } else {
    uploadFile();
  }
}
</script>

<%@ include file="IncludeTail.jsp" %>