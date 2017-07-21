
define(function() {

	return PermissionDeniedException={
		onException : function() {
		alert('无权限访问');
	}
	};

});
