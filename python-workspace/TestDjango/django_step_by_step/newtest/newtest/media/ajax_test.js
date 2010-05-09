function submit(){
    var form = $("form");
    var d = doSimpleXMLHttpRequest('/ajax/input/', form);
    d.addCallbacks(onSuccess, onFail);
}
onSuccess = function (data){
    var output = $("output");
    output.innerHTML = data.responseText;
    showElement(output);
}
onFail = function (data){
    alert(data);
}
function init() {
    var btn = $("submit");
    btn.onclick = submit;
    var output = $("output");
    hideElement(output);
}

function callJson(){
    var d = loadJSONDoc('/ajax/json/');
    d.addCallbacks(onSuccessJson, onFail);
}
row_display = function (row) {
    return TR(null, map(partial(TD, null), row));
}
onSuccessJson = function (data){
    var output = $("output");
    table = TABLE({border:"1"}, THEAD(null, row_display(data.head)), 
        TBODY(null, map(row_display, data.body)));
    replaceChildNodes(output, table);
    showElement(output);
}
function init() {
    var btn = $("submit");
    btn.onclick = submit;
    var output = $("output");
    hideElement(output);
    var btn = $("json");
    btn.onclick = callJson;
}

addLoadEvent(init);
