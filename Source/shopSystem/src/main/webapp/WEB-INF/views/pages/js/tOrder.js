//var testData = eval("([{\"No\":\"序号\",\"name\":\"姓名\",\"gender\":\"性别\",\"age\":\"年龄\"},{\"No\":\"1\",\"name\":\"小五毛\",\"gender\":\"男\",\"age\":\"22\"},{\"No\":\"2\",\"name\":\"中五毛\",\"gender\":\"女\",\"age\":\"18\"},{\"No\":\"3\",\"name\":\"大五毛\",\"gender\":\"男\",\"age\":\"20\"}])");
//[{"buyAddress":"买家地址","buyDate":"交易日期","flag":"0","freeNum":"赠送","id":"1","orderAmount":228,"orderName":"201706270001烟台竹叶6瓶","orderNo":"201706270001","orderNum":6,"orderObject":"竹叶","orderPrice":38,"orderStatus":"1"}];
 var headers = [{"buyDate":"交易日期","orderNo":"订单编号","orderName":"订单名称","orderObject":"产品类型","orderPrice":"产品价格","orderNum":"下单数量","orderAmount":"总价","freeNum":"赠送数量","orderStatus":"订单状态","buyAddress":"买家下单地址","flag":"0", "delete":"删除"}];

function queryTOrderAll(){
    var params=[];
    params.push('201706270001');
    
    remoteRetriver.resetSortFileds();
    var sortParams = remoteRetriver.addSortFiled('createDatetime,createUser',remoteRetriver.SORT_TYPE_ASC);
    sortParams = remoteRetriver.addSortFiled('userName',remoteRetriver.SORT_TYPE_DESC)
    remoteRetriver.query('TOrderRepository', 'findAll','',null, 0,10,false,function(errCode, errMsg, resultData,totalCount,pageCount){
    var datas = JSON.parse(resultData)
 
	if (errCode == 0) {
		 creatTable(document.body, headers, datas);
	} else {
		alert("查询失败！" + errMsg);
	}
    });
}

window.onload = function() {
	queryTOrderAll();
}

