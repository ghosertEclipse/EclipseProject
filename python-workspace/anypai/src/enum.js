/* Remember to use single quote like 'string' as string only. */
/* And use this style of comment sign only. */
/* Otherwise, it will fail to work in enum.ahk */
function main()
{
	var content = document.getElementById('content');
	var divs = content.getElementsByTagName('div');
	var last4Msg = new Array();
	for (var i = divs.length - 1; i >= 0; i--) {
		var value = divs[i].getAttribute('SendID');
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
		var msgObj = parseMsg(last4Msg[msg]);
		alert('sendName', msgObj.sendName);
		alert('time', msgObj.time);
		alert('count', msgObj.count);
		alert('content', msgObj.content);
	}
	
	return 'true';
}

function parseMsg(msgElement)
{
	var msgHead = null;
	var msgContent = null;
	for (index in msgElement.childNodes) {
		var childNode = msgElement.childNodes[index];
		if (childNode.id == 'MsgHead') {
			msgHead = childNode;
		} else if (childNode.id == 'MsgContent') {
			msgContent = childNode;
		}
	}
	
	var senderName = null;
	var time = null;
	for (index in msgHead.childNodes) {
		var childNode = msgHead.childNodes[index];
		if (childNode.id == 'SenderName') {
			senderName = childNode.innerHTML;
		} else if (childNode.id == 'MsgHeadRight') {
			time = childNode.firstChild.innerHTML
		}
	}
	
	var innerMsgs = msgContent.childNodes;
	var count = innerMsgs.length;
	var content = '';
	for (index in innerMsgs) {
		alert(innerMsgs[index]);
		content = content + innerMsgs[index].firstChild.innerHTML;
	}
	
	return new Msg(senderName, time, count, content);
}

function Msg(senderName, time, count, content) {
	this.senderName = senderName;
	this.time = time;
	this.count = count;
	this.content = content;
}

