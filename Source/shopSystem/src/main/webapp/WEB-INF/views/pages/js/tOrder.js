//var testData = eval("([{\"No\":\"序号\",\"name\":\"姓名\",\"gender\":\"性别\",\"age\":\"年龄\"},{\"No\":\"1\",\"name\":\"小五毛\",\"gender\":\"男\",\"age\":\"22\"},{\"No\":\"2\",\"name\":\"中五毛\",\"gender\":\"女\",\"age\":\"18\"},{\"No\":\"3\",\"name\":\"大五毛\",\"gender\":\"男\",\"age\":\"20\"}])");

function queryTOrderAll(){
	
    var _resultJson;
    
    var params=[];
    params.push('2017062700001');
    
   /* remoteRetriver.resetSortFileds();
    var sortParams = remoteRetriver.addSortFiled('createDatetime,createUser',remoteRetriver.SORT_TYPE_ASC);
    sortParams = remoteRetriver.addSortFiled('userName',remoteRetriver.SORT_TYPE_DESC)
    */
    remoteRetriver.query('TOrderRepository', 'findAll','',null, 0,10,false,function(errCode, errMsg, resultData,totalCount,pageCount){

	console.log(resultData);
	if (errCode == 0) {
		alert("查询成功！数据：" + JSON.stringify(resultData));
	} else {
		alert("查询失败！" + errMsg);
	}
    });
    return _resultJson;
   
}

window.onload = function() {
	
	var testData = queryTOrderAll();
	createTable("data", testData, true, true, true);
}

