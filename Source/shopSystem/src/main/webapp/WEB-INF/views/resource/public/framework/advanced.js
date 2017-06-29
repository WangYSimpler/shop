function logon(){
    cffex.permission.login("1", "1", "", function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("登录成功！");
        }
        else{
            alert("登录失败！" + errMsg);
        }
    });
}

function logout(){
    cffex.permission.logout(function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("登出成功！");
        }
        else{
            alert("登出失败！" + errMsg);
        }
    });
}

function rQueryById(){
    var id = 1;
    cffex.repository.query("advancedToDoRepository", "findById", null, id, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("id为" + id + "的数据：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function rQueryByTitle(){
    var title = "title";
    cffex.repository.query("advancedToDoRepository", "findByTitle", null, title, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("查询的数据：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function rQueryByCreator(){
    var creatorId = 1;
    cffex.repository.query("advancedToDoRepository", "findByCreatorId", null, creatorId, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("查询的数据：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function rQueryByTitleLike(){
    var keyword = encodeURI("ti%");//需要对“%”进行转码
    cffex.repository.query("advancedToDoRepository", "findByTitleLike", null, keyword, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("查询的数据：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function rQueryByTitleContaining(){
    var keyword = "ti";
    cffex.repository.query("advancedToDoRepository", "findByTitleContaining", null, keyword, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("查询的数据：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function rQueryByCreatorName(){
    var creatorName = "cffex";
    cffex.repository.query("advancedToDoRepository", "findToDosByCreatorName", null, creatorName, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("查询的数据：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function rQueryAllByPage(){
    var options = {"pageIndex":0, "pageSize":3};
    cffex.repository.query("advancedToDoRepository", "findAll", options, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("第" + options.pageIndex + "页的数据为：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function rQueryByCreatorNameAndPage(){
    var creatorId = 1;
    var options = {"pageIndex":0, "pageSize":3};
    cffex.repository.query("advancedToDoRepository", "findByCreatorId", options, creatorId, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("第" + options.pageIndex + "页的数据为：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function sQueryAllByPage(){
    var pageNum = 0;
    var pageSize = 3;
    cffex.service.request("advancedToDoService", "findAllByPageNum", pageNum, pageSize, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("第" + pageNum + "页的数据为：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function sQueryByTitle(){
    var title = "title";
    var pageNum = 0;
    var pageSize = 3;
    cffex.service.request("advancedToDoService", "findByTitle", title, pageNum, pageSize, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("第" + pageNum + "页的数据为：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function rPartUpdate(){
    var id = 1;
    if(!confirm("将更新id为" + id + "的记录!"))
        return;
    var data = {"content":"测试数据内容部分更新"};
    //只更新content字段的值
    cffex.repository.update("advancedToDoRepository", id, data, true, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("更新成功！");
        }
        else{
            alert("更新失败！" + errMsg);
        }
    });
}

function sessionUse1(){
    cffex.service.request("advancedToDoService", "getSessionContent", function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("调用成功，userId为：" + resultData);
        }
        else{
            alert("调用失败！" + errMsg);
        }
    });
}

function sessionUse2(){
    cffex.service.request("advancedToDoService", "getUserIdInSession", function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("调用成功，userId为：" + resultData);
        }
        else{
            alert("调用失败！" + errMsg);
        }
    });
}

function sessionUse3(){
    cffex.service.request("advancedToDoService", "getUserIdInSession", "Hello", function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("调用成功，userId为：" + resultData);
        }
        else{
            alert("调用失败！" + errMsg);
        }
    });
}

function getConfigProperties(){
    cffex.service.request("advancedToDoService", "getConfigProperties", function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("调用成功，数据为：" + resultData);
        }
        else{
            alert("调用失败！" + errMsg);
        }
    });
}

function uploadFile(){
    cffex.file.upload("advancedToDoService", "uploadFile", "fileId", function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("上传成功！");
        }
        else{
            alert("上传失败！" + errMsg);
        }
    });
}

function downloadFile(){
    cffex.file.download("advancedToDoService", "downloadFile", function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("下载成功！");
        }
        else{
            alert("下载失败！" + errMsg);
        }
    });
}

function callForbidMethod1(){
    cffex.service.request("advancedToDoService", "toBeForbided1", function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("调用成功!" + resultData);
        }
        else{
            alert("调用失败！" + errMsg);
        }
    });
}

function callForbidMethod2(){
    cffex.service.request("advancedToDoService", "toBeForbided2", function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("调用成功!" + resultData);
        }
        else{
            alert("调用失败！" + errMsg);
        }
    });
}