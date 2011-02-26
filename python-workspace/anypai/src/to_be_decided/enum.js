/* Remember to use single quote like 'string' as string only. */
/* And use this style of comment sign only. */
/* Otherwise, it will fail to work in enum.ahk */
/* Pass in true or false based on whether current window is monitoring. */
/* Pass in my wangwang id like: ghosert */
/* Return, 'boolean, boolean' first boolean means whether to keep opening, second boolean means whether to buy. */
function main(isMonitoringWindow, myWangwangId)
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
	
	for (var msg in last4Msg) {
		var msgObj = parseMsg(last4Msg[msg]);
		last4Msg[msg] = msgObj;
		/* alert('senderName: ' + msgObj.senderName); */
		/* alert('time: ' + msgObj.time); */
		/* alert('count: ' + msgObj.count); */
		/* alert('content:' + msgObj.content); */
	}
	
	var regPai = new RegExp('拍好了，已付款，捷易通ID：' + myWangwangId + ' 请加款，稍候统一确认，发好评，谢谢。');
	var regQuery = /有货吗？/;
	
	if (isMonitoringWindow) {
		var msgObj = last4Msg.shift();
		if (!msgObj) return 'false, false';
		if (msgObj.content.match(regQuery) && msgObj.senderName == myWangwangId) {
			var nowTime = new Date();
			/* if more than 20 mins, close the window, otherwise keep opening. */
			if ((nowTime - msgObj.time)/1000/60 > 20) {
				return 'false, false';
			} else {
				return 'true, false';
			}
		} else {
			var msgObj2 = last4Msg.shift();
			if (!msgObj2) return 'false, false';
    		if (msgObj2.content.match(regQuery) && msgObj2.senderName == myWangwangId) {
				if (msgObj.senderName != myWangwangId) {
    				if (msgObj.content.match(/没|无|缺/) && !msgObj.content.match(/没短信|没有短信|没截图|没有截图/)) {
    					return 'false, false';
    				} else if (msgObj.content.match(/有|OK|ok|Ok|成交|自动加|好的|拍下/)) {
						if (msgObj.content.match(/>http.*?</)) {
							if (msgObj.content.match(/http:\/\/item\.taobao\.com\/item\.htm\?id=/)) {
								return 'false, true';
							} else {
								return 'false, false';
							}
						} else {
    						return 'false, true';
						}
					} else {
						return 'false, false';
					}
				}
			}
		}
	} else {
		var size = last4Msg.length;
		if (size == 0) return 'true, false';
		for (var index = 0; index < size; index++) {
    		if (last4Msg[index].content.match(regPai) || last4Msg[index].content.match(regQuery)) {
				size = index + 1;
			}
		}
		var lastMsg = last4Msg[size - 1];
		if (lastMsg.senderName != myWangwangId) return 'true, false';
		if (lastMsg.content.match(regPai) || lastMsg.content.match(regQuery)) {
			for (var index = 0; index < size - 1; index++) {
				if (last4Msg[index].senderName == myWangwangId) {
					return 'true, false';
				}
			}
			if (lastMsg.content.match(regQuery) && size == 1) return 'true, false';
			return 'false, false';
		}
	}
	
	return 'false, false';
}

function parseMsg(msgElement)
{
	var msgHead = null;
	var msgContent = null;
	for (var index in msgElement.childNodes) {
		var childNode = msgElement.childNodes[index];
		if (childNode.id == 'MsgHead') {
			msgHead = childNode;
		} else if (childNode.id == 'MsgContent') {
			msgContent = childNode;
		}
	}
	
	var senderName = null;
	var time = null;
	var regtest = /AM|PM/;
	for (var index in msgHead.childNodes) {
		var childNode = msgHead.childNodes[index];
		if (childNode.id == 'SenderName') {
			senderName = childNode.innerHTML;
		} else if (childNode.id == 'MsgHeadRight') {
			var timeString = childNode.firstChild.innerHTML;
			if (regtest.test(timeString)) { /* for the time format: '(6:13:29 AM):' */
				var matched = timeString.match(/(\d+):(\d+):(\d+)/);
				time = new Date();
				/* matched[0] is the whole matched string */
				if (/PM/.test(timeString)) {
					matched[1] = parseInt(matched[1]) + 12;
				}
				/* Don't change year, month, day in this case. */
				time.setHours(matched[1]);
				time.setMinutes(matched[2]);
				time.setSeconds(matched[3]);
			} else { /* for the time format: '(2011-02-22 16:13:24):' */
				var matched = timeString.match(/(\d+)-(\d+)-(\d+)\s(\d+):(\d+):(\d+)/);
				time = new Date();
				/* matched[0] is the whole matched string */
				time.setFullYear(matched[1]);
				time.setMonth(matched[2] - 1);
				time.setDate(matched[3]);
				time.setHours(matched[4]);
				time.setMinutes(matched[5]);
				time.setSeconds(matched[6]);
			}
		}
	}
	
	/* HTMLElement.childNodes contain all the text nodes and element nodes, use node.nodeType = 1(ElEMENT_NODE) or 3(Text_NODE) to distingish.*/
	/* Unlike document has three methods on document.getElementXXX() method, HTMLElement contains only one element getter which is node.getElementsByTagName() */
	var innerMsgs = msgContent.childNodes;
	var count = innerMsgs.length;
	var content = '';
	for (var index in innerMsgs) {
		if (innerMsgs[index].nodeType == 1) { /* if node type Node.Element = 1, Node.Text_NODE = 3. */
    		/* content = content + innerMsgs[index].getElementsByTagName('font')[0].innerText + ' '; */
    		content = content + innerMsgs[index].innerHTML + ' ';
		}
	}
	return new Msg(senderName, time, count, content);
}

/* senderName: e.g. likecider, time: Date type, count: message count number, content: message content string. */
function Msg(senderName, time, count, content) {
	this.senderName = senderName;
	this.time = time;
	this.count = count;
	this.content = content;
}

