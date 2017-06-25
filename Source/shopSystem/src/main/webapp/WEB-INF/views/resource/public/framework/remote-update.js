var RemoteUpdater = {
    /**
     * 发起请求
     * @param {number} id 要更新的id
     * @param {Object} data 要更新的数据
     * @param {function} callback 结构回调函数
     * */
    update: function (repositoryName, id, data, async, callback) {
        var url = '/repository/' + repositoryName + '/' + id;
        baseRequest.sendPost(url, data, async, callback);
    },
    /**
     * 发起请求
     * @param {number} id 要更新的id
     * @param {Object} data 要更新的部分的数据
     * @param {function} callback 结构回调函数
     * */
    partUpdate: function (repositoryName, id, data, async, callback) {
        var url = '/repository/partUpdate/' + repositoryName + '/' + id;
        var request = new baseRequest(url,async);
        baseRequest.sendPost(url, data, async, callback);
    }
}

