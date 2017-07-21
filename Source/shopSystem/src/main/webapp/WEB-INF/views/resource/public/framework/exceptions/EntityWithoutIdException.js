define(function () {

    return EntityWithoutIdException = {
        onException: function () {
            alert('entity定义错误，没有主键Id');
        }
    };
});
