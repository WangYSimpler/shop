var remoteDeleter = {
    deleter: function (repositoryName, id, async, callback) {
        var url = '/repository/' + repositoryName + '/' + id;
        baseRequest.sendDelete(url, null, async, callback);
    }
};



