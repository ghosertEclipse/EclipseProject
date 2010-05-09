<%@ include file="IncludeHead.jsp" %>

<table width="780" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#cc0000">
<tbody>
<tr><td height="8"></td></tr>

  <tr>
    <td>
      <table width="98%" height="26" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FF9900">
        <tr>
          <td>
            <font color="#000000"><spring:message code="DOWNLOAD_DESCRIPTION"/></font>
            <b><a class="redreda" href="javascript:;" onclick="copydocurl(docsurl_getbyid('docurlb'),'<spring:message code="COPY_PROMPT"/>')"><spring:message code="COPY_URL"/></a></b>
            <input name="docsurl" id="docurlb" type="hidden" 
            value="<c:url value="http://221.137.247.59/jmobile/listUploadFiles.do"><c:param name="uploader" value="${uploader}"/><c:param name="mpage" value="${mpage - 1}"/><c:param name="ppage" value="${ppage}"/></c:url>"/>
	        <br><br>
            &nbsp; <spring:message code="OFFICAL_DESC"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>

  <!-- show musics -->
  <c:if test="${music.totalSize > 0}">
  <tr><td height="8"></td></tr>
  <tr>
    <td><table width="98%" height="26" border="0" align="center" cellpadding="0" cellspacing="0" background="images/bg_04.gif">
          <tr>
            <td width="4">&nbsp;</td>
            <td><font size="3" color="white"><b><spring:message code="MUSIC_TITLE"/></b></font></td>
            <td width="4">&nbsp;</td>
          </tr>
        </table>
        <table width="98%" height="26" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FF9900">
            <tr>
            <td align="right">
              <b>
              <!-- total size -->
              <spring:message code="TOTAL_SIZE" arguments="${music.totalSize}"/>
              <!-- first page -->
	          <a href="<c:url value="listUploadFiles.do">
	                     <c:param name="uploader" value="${uploader}"/>
	                     <c:param name="mpage" value="${1}"/>
	                     <c:param name="ppage" value="${ppage}"/>
	                   </c:url>"><spring:message code="FIRST_PAGE"/></a>
              <!-- pre page -->
	          <c:if test="${mpage==1}">
	            <spring:message code="PRE_PAGE"/>
	          </c:if>
	          <c:if test="${mpage!=1}">
	          <a href="<c:url value="listUploadFiles.do">
	                     <c:param name="uploader" value="${uploader}"/>
	                     <c:param name="mpage" value="${mpage - 1}"/>
	                     <c:param name="ppage" value="${ppage}"/>
	                   </c:url>"><spring:message code="PRE_PAGE"/></a>
	          </c:if>
              <!-- number page -->
	          <c:forEach begin="1" varStatus="status" end="${music.totalPage}">
	              <font color="white"><b>
	                <a href="<c:url value="listUploadFiles.do">
	                  <c:param name="uploader" value="${uploader}"/>
	                  <c:param name="mpage" value="${status.index}"/>
	                  <c:param name="ppage" value="${ppage}"/>
	                </c:url>"><c:out value="${status.index}"/></a>
	              </b></font>
	          </c:forEach>
              <!-- next page -->
	          <c:if test="${mpage==music.totalPage}">
	            <spring:message code="NEXT_PAGE"/>
	          </c:if>
	          <c:if test="${mpage!=music.totalPage}">
	          <a href="<c:url value="listUploadFiles.do">
	                     <c:param name="uploader" value="${uploader}"/>
	                     <c:param name="mpage" value="${mpage + 1}"/>
	                     <c:param name="ppage" value="${ppage}"/>
	                   </c:url>"><spring:message code="NEXT_PAGE"/></a>
	          </c:if>
              <!-- last page -->
	          <a href="<c:url value="listUploadFiles.do">
	                     <c:param name="uploader" value="${uploader}"/>
	                     <c:param name="mpage" value="${music.totalPage}"/>
	                     <c:param name="ppage" value="${ppage}"/>
	                   </c:url>"><spring:message code="LAST_PAGE"/></a>
            </td>
            </b>
            <td width="8">&nbsp;&nbsp;</td>
            </tr>
        </table>
        <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr bgcolor="#FFF3DE" height="25">
                <td class="orangetd">&nbsp;</td>
                <td class="orangetd"><spring:message code="MUSIC_ID"/></td>
                <td class="orangetd"><spring:message code="MUSIC_NAME"/></td>
                <td class="orangetd"><spring:message code="MUSIC_AUTHOR"/></td>
                <td class="orangetd"><spring:message code="MUSIC_WEEKPAYTIMES"/></td>
                <td class="orangetd"><spring:message code="MUSIC_PLAY"/></td>
                <td class="orangetd">&nbsp;</td>
                <td class="orangetd"><spring:message code="MUSIC_ID"/></td>
                <td class="orangetd"><spring:message code="MUSIC_NAME"/></td>
                <td class="orangetd"><spring:message code="MUSIC_AUTHOR"/></td>
                <td class="orangetd"><spring:message code="MUSIC_WEEKPAYTIMES"/></td>
                <td class="orangetd"><spring:message code="MUSIC_PLAY"/></td>
            </tr>
            <c:forEach items="${music.items}" var="item" varStatus="status">      
                  <c:if test="${status.index % 2 == 0}">
	                ${(status.index / 2) % 2 == 0 ? '<tr bgcolor="#F7FBF7" height="25">' : '<tr bgcolor="#FFF3DE" height="25">'}
                  </c:if>
                    <td class="blacktd">&nbsp;</td>
					<td class="blacktd"><c:out value="${item.id}"/></td>
					<td class="blacktd"><a class="redorga" href="javascript:pushPopup('${item.id}')">
					                      <img src="images/ringtone.gif"/>&nbsp;<c:out value="${item.name}"/>
					                    </a>
					</td>
					<td class="blacktd"><c:out value="${item.author}"/></td>
					<td class="blacktd"><c:out value="${item.weekpaytimes}"/></td>
					<td class="blacktd">
					  <a href="javascript:pushPopup('${item.id}')">
					    <img title="<spring:message code="PLAY_MUSIC"/>" src="images/play.bmp">
					  </a>
					</td>
					<c:if test="${status.index % 2 == 0 && status.last}">
	                    <td colspan="6">&nbsp;</td>
				    </c:if>
                  <c:if test="${status.index % 2 == 1 || status.last}">
	                </tr>
                  </c:if>
            </c:forEach>
       </table>
    </td>
  </tr>
  </c:if>
  
  <!-- show pictures -->
  <c:if test="${picture.totalSize > 0}">
  <tr><td height="8"></td></tr>
  <tr>
    <td><table width="98%" height="26" border="0" align="center" cellpadding="0" cellspacing="0" background="images/bg_04.gif">
          <tr>
            <td width="4">&nbsp;</td>
            <td><font size="3" color="white"><b><spring:message code="PICTURE_TITLE"/></b></font></td>
            <td width="4">&nbsp;</td>
          </tr>
        </table>
        <table width="98%" height="26" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FF9900">
            <tr>
            <td align="right">
              <b>
              <!-- total size -->
              <spring:message code="TOTAL_SIZE" arguments="${picture.totalSize}"/>
              <!-- first page -->
	          <a href="<c:url value="listUploadFiles.do">
	                     <c:param name="uploader" value="${uploader}"/>
	                     <c:param name="mpage" value="${mpage}"/>
	                     <c:param name="ppage" value="${1}"/>
	                   </c:url>"><spring:message code="FIRST_PAGE"/></a>
              <!-- pre page -->
	          <c:if test="${ppage==1}">
	            <spring:message code="PRE_PAGE"/>
	          </c:if>
	          <c:if test="${ppage!=1}">
	          <a href="<c:url value="listUploadFiles.do">
	                     <c:param name="uploader" value="${uploader}"/>
	                     <c:param name="mpage" value="${mpage}"/>
	                     <c:param name="ppage" value="${ppage - 1}"/>
	                   </c:url>"><spring:message code="PRE_PAGE"/></a>
	          </c:if>
              <!-- number page -->
	          <c:forEach begin="1" varStatus="status" end="${picture.totalPage}">
	              <font color="white"><b>
	                <a href="<c:url value="listUploadFiles.do">
	                  <c:param name="uploader" value="${uploader}"/>
	                  <c:param name="mpage" value="${mpage}"/>
	                  <c:param name="ppage" value="${status.index}"/>
	                </c:url>"><c:out value="${status.index}"/></a>
	              </b></font>
	          </c:forEach>
              <!-- next page -->
	          <c:if test="${ppage==picture.totalPage}">
	            <spring:message code="NEXT_PAGE"/>
	          </c:if>
	          <c:if test="${ppage!=picture.totalPage}">
	          <a href="<c:url value="listUploadFiles.do">
	                     <c:param name="uploader" value="${uploader}"/>
	                     <c:param name="mpage" value="${mpage}"/>
	                     <c:param name="ppage" value="${ppage + 1}"/>
	                   </c:url>"><spring:message code="NEXT_PAGE"/></a>
	          </c:if>
              <!-- last page -->
	          <a href="<c:url value="listUploadFiles.do">
	                     <c:param name="uploader" value="${uploader}"/>
	                     <c:param name="mpage" value="${mpage}"/>
	                     <c:param name="ppage" value="${picture.totalPage}"/>
	                   </c:url>"><spring:message code="LAST_PAGE"/></a>
            </td>
            </b>
            <td width="8">&nbsp;&nbsp;</td>
            </tr>
        </table>
        <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FF9900">
            <c:forEach items="${picture.items}" var="item" varStatus="status">      
	            <c:if test="${status.index % 5 == 0}">
	              <tr>
				</c:if>					
					<td width="20%">
					<table>
					<tr><td>
					<table align="center" background="images/1876.gif" border="0" cellpadding="0" cellspacing="0" height="153" width="139">
					<tbody>
					  <tr><td height="15"></td></tr>
					  <tr><td>
					    <a href="javascript:pushPopup('${item.id}')">
					      <div align="center"><img title="<spring:message code="DOWNLOAD_PROMPT" arguments="${item.id},${item.name}"/>" src="<c:out value="${item.url}"/>" border="0" height="128" width="128"></div>
					    </a>
					  </td></tr>
					  <tr><td height="10"></td></tr>
					</tbody>
					</table>
					</td></tr>
					<tr><td align="center">
					<spring:message code="PICTURE_DESC" arguments="${item.id},${fn:substring(item.name, 0, 6)},${item.weekpaytimes}"/>
					</td></tr>
					</table>
					</td>
	            <c:if test="${status.index < 5 && status.last}">
	              <c:forEach begin="1" end="${4 - status.index}">
	                <td width="20%">&nbsp;</td>
	              </c:forEach>
				</c:if>
	            <c:if test="${status.index % 5 == 4 || status.last}">
	              </tr>
				</c:if>
            </c:forEach>
       </table>
    </td>
  </tr>
  </c:if>
  
<tr><td height="8"></td></tr>
</tbody>
</table>

<script language="javascript">
function pushPopup(id) {
    window.open("pushPopup.do?id="+encodeURI(id),"","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes, width=620,height=400");
}
function docsurl_getbyid(id) {
	itm = null;
	if (document.getElementById) {
		itm = document.getElementById(id);
	} else if (document.all)	{
		itm = document.all[id];
	} else if (document.layers) {
		itm = document.layers[id];
	}
	return itm;
}
function copydocurl(obj, copyprompt){	
	obj.select();
                js=obj.createTextRange();
                js.execCommand("Copy")
	alert(copyprompt);
}
</script>

<%@ include file="IncludeTail.jsp" %>