//设置cookie
function setCookie(cname, cvalue, expireHours) {
	var SetTime = new Date(); //设置过期时间
	SetTime.setTime(SetTime.getTime() + (expireHours * 60 * 60 * 1000)); //设置过期时间
	var expires = "expires=" + SetTime.toGMTString(); //设置过期时间
	document.cookie = cname + "=" + cvalue + "; " + expires; //创建一个cookie
}
//获取cookie
function getCookie(cname) {
	if (document.cookie.length > 0) {
		c_start = document.cookie.indexOf(cname + "=")
		if (c_start != -1) {
			c_start = c_start + cname.length + 1
			c_end = document.cookie.indexOf(";", c_start)
			if (c_end == -1)
				c_end = document.cookie.length
			return unescape(document.cookie.substring(c_start, c_end))
		}
	}
	return ""
}