define(function () {

    return NotLogonException = {
        onException: function () {
            alert('未登录');
        }
    }
});
