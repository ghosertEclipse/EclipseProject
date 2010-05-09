var md = window.showModelessDialog; /* Win 5+, WinCE 5.5+ */
var ns = document.namespaces; /* Win 5.5+, WinCE 5.5+ */
var im = document.implementation; /* Win 6, Mozilla */

var isIE = window.external && (md || ns || im);
var isIE50 = md && !ns && !im;
var isIE55 = md && ns && !im;
var isIE60 = md && ns && im;
var isOpera = window.opera;
var isKDE = navigator.vendor == 'KDE' || (document.childNodes && (!document.all || navigator.accentColorName) && !navigator.taintEnabled);

function writeStyle(s) {
 document.write('<style type="text/css">' + s + '<\/style>');
}

if (isIE) {
 writeStyle('#search { padding: 0 } #menu li { width: 100px } #footer { padding-bottom: 16px } #logo, #menu, #search, #cntnt, #footer { width: expression(document.body.clientWidth > 752 ? "752px" : "auto") } body { padding-left: expression(document.body.clientWidth > 752 ? ((document.body.clientWidth - 752) / 2) + "px" : 0) }');
}

if (isIE50) {
 writeStyle('a.entryLink, a.entryComments, a.entryTrackback { background-image: none } #search .button { margin-bottom: -1px }');
}

if (isIE55) {
 writeStyle('#leftCntnt { width: 50% } #rightCntnt { width: 100%; padding-left: 9%; margin: 0 }');
}

if (isIE50 || isIE55) {
 writeStyle('#logo { height: 143px } td, th { font-size: 76% } #blogCalendar { font-size: 100% }');
}

if (isIE60) {
 writeStyle('#logo { height: 137px } #leftCntnt { margin-right: 9% }');
}

if (isOpera) {
 writeStyle('#q { padding: 0px } #search input { padding-left: 4px } #logo { height: 137px } #leftCntnt { margin-top: 15px } #rightCntnt { margin: 0; padding-left: 10% }');
}

if (isKDE) {
 writeStyle('#rightCntnt { padding-left: 10% }');
}
