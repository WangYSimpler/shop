
define(function() {

	return UnhandledException={
		onException : function(msg) {
		alert('未处理错误: ' + msg);
	}
	};

});
