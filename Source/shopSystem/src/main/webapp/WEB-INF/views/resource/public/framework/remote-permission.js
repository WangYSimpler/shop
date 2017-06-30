/**
 * @Author JiangR 2017/4/18.
 */

/**
 * 用户登录权限登录
 */
var remotePermission = {

    /**
     * 登入功能
     * @param userName
     * @param password
     * @param callback
     */
    login: function (userNo, password, callback) {
        var url = '/login';
        ////转换成jsonObject 根据实际情况 王勇20170418
        var params = {
            "userNo": userNo,
            "password": password
        };
        baseRequest.sendPost(url, params, false, callback);
    },
    
    /**
     * 登出功能
     * @param callback
     */
    logout: function (callback) {
        var url = '/logout';
        baseRequest.sendDelete(url, '', false, callback);
    },
    /**
     * 获取请求
     * @param type
     * @param callback
     */
    getPermissions: function (type, callback) {
        var url = '/user/profile';
        var params = {type: type};
        baseRequest.sendGet(url, params, this.async, callback);
    }

}