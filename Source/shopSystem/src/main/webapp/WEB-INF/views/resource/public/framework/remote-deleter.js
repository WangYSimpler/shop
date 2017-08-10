var remoteDeleter = {
    deleter: function (repositoryName, id, async, callback) {
        var url = '/repository/' + repositoryName + '/' + id;
        baseRequest.sendDelete(url, null, async, callback);
    },
    /**
     * 发起请求
     * @param {number} id 要更新的id
     * @param {Object} data 要更新的数据
     * @param {function} callback 结构回调函数
     * */
    logicDeleter: function (repositoryName, id, data, async, callback) {
        var url = '/repository/' + repositoryName + '/' + id;
        baseRequest.sendPost(url, data, async, callback);
    }
};



