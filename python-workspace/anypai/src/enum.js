/* Remember to use single quote like 'string' as string only. */
/* And use this style of comment sign only. */
/* Otherwise, it will fail to work in enum.ahk */
function main()
{
	var content = document.getElementById('content');
	var divs = content.getElementsByTagName('div');
	var last4Msg = new Array();
	for (var i = divs.length - 1; i >= 0; i--) {
		value = divs[i].getAttribute('SendID');
		if (value) {
    		value = divs[i].getAttribute('id');
			if (value == 'MsgElement') {
				last4Msg.push(divs[i]);
			}
		}
		if (last4Msg.length == 4) break;
	}
	if (last4Msg.length == 0) return 'false';
	
	for (msg in last4Msg) {
		alert(last4Msg[msg].getAttribute('SendID'));
	}
	
	return 'true';
}

function parseMsg(msgElement)
{
	msgHead = msgElement.getElementById('MsgHead');
	msgContent = msgElement.getElementById('MsgContent');
	
	senderName = msgHead.getElementById('SenderName').innerHTML;
	time = msgHead.getElementById('MsgTime').innerHTML;
	
	innerMsgs = msgContent.getElementsByTagName('div');
	count = innerMsgs.length;
	content = ''
	for (msg in innerMsgs) {
		content = content + innerMsgs[msg].firstChild.innerHTML;
	}
	
	return {senderName: senderName, time: time, count: count, content: content}
}
