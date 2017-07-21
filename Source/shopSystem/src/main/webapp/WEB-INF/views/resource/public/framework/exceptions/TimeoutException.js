
define(function() {

	return TimeoutException={
		onException : function() {
		alert('请求超时, 请检查网络设置。');
	}
	};

});
