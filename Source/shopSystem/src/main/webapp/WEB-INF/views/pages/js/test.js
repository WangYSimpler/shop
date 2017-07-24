
/**
 * 封装登录方法
 * 
 * @returns
 */
function login(){

   remotePermission.login("1", "888888", function(errCode, errMsg, resultData){
	   
	   console.log('resultData :' + resultData)
        if(errCode == 0){
        	//localStorage.setItem("isLogin", true);
            alert("登录成功！");
        }
        else{
        	//localStorage.setItem("isLogin", false);
            alert("登录失败！" + errMsg);
        }
    });
}

function loginPage(){
	 window.location.href = 'login.html'
}
/**
 * 封装登出方法
 */
function logout(){
	remotePermission.logout(function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("登出成功！");
        }
        else{
            alert("登出失败！" + errMsg);
        }
    });
}
var date=[];
	date[0]='1';

function rQueryAll(){
	
    var _resultJson;
    var id = 1;
  
    remoteRetriver.resetSortFileds();
    var sortParams = remoteRetriver.addSortFiled('createDatetime,createUser',remoteRetriver.SORT_TYPE_ASC);
    sortParams = remoteRetriver.addSortFiled('userName',remoteRetriver.SORT_TYPE_DESC)
    remoteRetriver.query('HyUsersRepository', 'findAll',[],sortParams, 0,10,false,function(errCode, errMsg, resultData,totalCount,pageCount){

	console.log(resultData);
	if (errCode == 0) {
		alert("查询成功！数据：" + JSON.stringify(resultData));
	} else {
		alert("查询失败！" + errMsg);
	}
    });
    return _resultJson;
   
}

function queryUserNo(){
	
    var _resultJson;
    var id = 1;
    var params=[];
    params.push('1');
    params.push('0');
    
  
    //'["1","0"]'
    remoteRetriver.queryList('TUserRepository', 'findByUserNoAndDeleteFlag', params,false,function(errCode, errMsg, resultData,totalCount,pageCount){
    	
    	console.log(resultData);
        if(errCode == 0){
         alert("查询成功！数据：" + JSON.stringify(resultData));
       }
        else{
            alert("查询失败！" + errMsg);
        }
    });
    return _resultJson;
   
}

function rCreate(){
   
	//var todo =  {"createDatetime":1481644800000,"createUser":"admin","deleteFlag":"0","employeeId":1,"orgDeptId":1,"organizationId":1,"updateDatetime":1486915200000,"updateUser":"王勇","userId":11,"userName":"王勇","userNo":"demo","userPd":"c4ca4238a0b923820dcc509a6f75849b","userStatus":"1","userType":"3"};
	var todo =  {"delFlag":"1","password":"111111","userName":"测试","userNo":"6"};
    remoteCreate.create("TUserRepository", todo, true,function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("新建成功！");
        }
        else{
            alert("新建失败！" + errMsg);
        }
    });
}

function rUpdate(){
    var id = 61;
    var todo =  {"delFlag":"1","id":61,"password":"111111","userName":"Array","userNo":"6"};
    remoteUpdate.update("TUserRepository", id, todo, false, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("更新成功！");
        }
        else{
            alert("更新失败！" + errMsg);
        }
    });
}

function rDelete(){
    var id = 22;
    if(!confirm("将删除id为" + id + "的记录!"))
        return;
    remoteDeleter.deleter("HyUsersRepository", id,true,function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("删除成功！");
        }
        else{
            alert("删除失败！" + errMsg);
        }
    });
}

function sQueryAll(){
    cffex.service.request("toDoService", "findAllToDo", function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("所有的数据为：" + JSON.stringify(resultData));
        }
        else{
            alert("查询失败！" + errMsg);
        }
    });
}

function sCreate(){
    var todo = {"title":"测试数据标题", "content":"测试数据内容"};
    cffex.service.request("toDoService", "createToDo", todo, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("新建成功！");
        }
        else{
            alert("新建失败！" + errMsg);
        }
    });
}

function sUpdate(){
    var id = 1;
    if(!confirm("将更新id为" + id + "的记录!"))
        return;
    var todo = {"title":"测试数据标题更新", "content":"测试数据内容更新"};
    cffex.service.request("toDoService", "updateToDo", id, todo, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("更新成功！");
        }
        else{
            alert("更新失败！" + errMsg);
        }
    });
}

function sDelete(){
    var id = 1;
    if(!confirm("将删除id为" + id + "的记录!"))
        return;
    cffex.service.request("toDoService", "deleteToDo", id, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("删除成功！");
        }
        else{
            alert("删除失败！" + errMsg);
        }
    });
}
