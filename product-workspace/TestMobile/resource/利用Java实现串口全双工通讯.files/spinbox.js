function open_window(path, win_width, win_height) {
var win_width = win_width;
var win_height = win_height;
var parameters = "left=0,top=0,toolbar=no,menubar=no,scrollbars=yes,resizable=no,width=" + win_width + ",height=" + win_height;
var newWindow = window.open(path,'name',parameters);
newWindow.creator = self;
newWindow.focus();
return true;

}//open_window

function chkRateing(Rate){
	var Rtemp="empt";
	var doRadio=document.Rate.Rating;
	for(var i=0; i<doRadio.length; i++)
	{
		if(doRadio[i].checked){
			Rtemp=i;
			return true;
		}
	}
	if(Rtemp=="empt")
	{
	alert("谢谢你的支持，但是请选择，否则无法提交！");
	doRadio[0].focus();
	return false;
	}
}

function cxx_pop(url,w,h){
	var windowconfig = "status=0,scrollbars=yes,resizable=0,width=" + w + ",height=" + h;
		if (!subwin || subwin.closed)
			{
			var subwin=window.open(url, 'cxx', windowconfig);
			subwin.focus();
			}
		else subwin.focus();
	}

function redirect(select) {
var page = select.options[select.selectedIndex].value
if (page!="#") document.location.href = page
}

self.name = "oldwindow";
function checkInput(form)
{
var b = navigator.appName
var TheURL="http://www.ibm.com/software/main/search/message.html";
var selected;
selected = form.realm.value;
if ((form.q.value == "") && (selected == "software"))
{
if (b=="Netscape")
{
zview=window.open(TheURL,"view","toolbar=no,menubar=no,scrollbars=no,resizable=yes,width=315,height=348");
}
else
{
zview=window.open(TheURL,"view","toolbar=no,menubar=no,scrollbars=no,resizable=yes,width=315,height=370");
}
zview.creator=self;
}
else
{
form.submit();
}
return false;
}

function requestm(){
	var fri=document.req_form.fromindex.value;
	var toi=document.req_form.toindex.value;
	var cnt=toi-fri+1;
	var ind=this.location.href.indexOf("&");
	if(ind==-1) ind = this.location.href.length;
	var suburl=this.location.href.substring(0,ind);
	var newurl=suburl+"&start="+fri+"&count="+cnt;
	this.location.href=newurl;
}

function requestm1(){
	var fri=document.req_form1.fromindex.value;
	var toi=document.req_form1.toindex.value;
	var cnt=toi-fri+1;
	var ind=this.location.href.indexOf("&");
	if(ind==-1) ind = this.location.href.length;
	var suburl=this.location.href.substring(0,ind);
	var newurl=suburl+"&start="+fri+"&count="+cnt;
	this.location.href=newurl;
}
function requestm2(){
		var toi=document.form1.UserRestriction.value;
		var fri=document.form1.selScope.value;
	var newurl="http://210.82.67.149/cgi-bin/dWsearch.pl"+"?selScope="+fri+"&UserRestriction="+toi;
	this.location.href=newurl;
}

function getURL(){
	document.getURL.url.value = this.location.href;
}


function showWithinTime(endDateAndTime,text) { 
//var endDateAndTime="200302190000"
var aTime = new Date()
var thisYear = aTime.getYear()
var thisMonth = aTime.getMonth() + 1
var thisDate = aTime.getDate()
var thisHour = aTime.getHours()
var thisMinute = aTime.getMinutes()
var thisDateAndTime = (thisYear*100000000)+(thisMonth*1000000)+(thisDate*10000)+(thisHour*100)+thisMinute
    if (thisDateAndTime <= endDateAndTime) {
        document.write(text);
    }
}