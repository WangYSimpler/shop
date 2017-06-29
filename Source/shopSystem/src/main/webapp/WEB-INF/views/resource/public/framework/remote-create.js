var remoteCreate = {
    create: function (repositoryName, data, async, callback) {
        var url = '/repository/' + repositoryName;
        baseRequest.sendPut(url, data, async, callback);
    }
}