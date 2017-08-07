var gofirstConfig = {
    domain: 'http://localhost:9911',
    project: 'shopSystem',
    timeout: 40000,
    errorHandlers: {
        timeoutHandler: function(){
            alert('自定义错误处理：超时');
        },
        unhandledErrorHandler: function(msg){
            alert('自定义未处理错误处理：' + msg);
        }
    }
};

/**
 *  部署地址
 */
/*var gofirstConfig = {
	    domain: 'http://192.168.1.202:9999',
	    project: 'shopSystem',
	    timeout: 40000,
	    errorHandlers: {
	        timeoutHandler: function(){
	            alert('自定义错误处理：超时');
	        },
	        unhandledErrorHandler: function(msg){
	            alert('自定义未处理错误处理：' + msg);
	        }
	    }
	};*/