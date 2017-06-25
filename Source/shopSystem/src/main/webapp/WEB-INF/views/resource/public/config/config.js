var gofirstConfig = {
    domain: 'http://localhost:8080',
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