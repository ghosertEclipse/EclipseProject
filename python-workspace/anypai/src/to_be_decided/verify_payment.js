/* Remember to use single quote like 'string' as string only. */
/* And use this style of comment sign only. */
/* Otherwise, it will fail to work in enum.ahk */
/* Pass in true or false based on whether current window is monitoring. */
/* Pass in my wangwang id like: ghosert */
/* Return, 'boolean, boolean' first boolean means whether to keep opening, second boolean means whether to buy. */
function main(isMonitoringWindow, myWangwangId)
{
	var content = document.getElementsByTagName('html');
	return content.innerHTML;
}

