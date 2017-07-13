//订单标题栏
//'[{'buyAddress':'山东省烟台市','buyDate':'20170627','flag':'0','freeNum':3,'id':'1','orderAmount':228,'orderName':'201706270001烟台竹叶6瓶','orderNo':'201706270001','orderNum':6,'orderObject':'竹叶','orderPrice':38,'orderStatus':'1'}]'
var headers = [{'buyDate':'交易日期','orderNo':'订单编号','orderName':'订单名称','orderObject':'产品类型','orderPrice':'产品单价','orderNum':'订单数量','orderAmount':'订单总额','orderStatus':'订单状态','freeNum':'赠送数量','buyAddress':'买家地址','create':'添加','update':'编辑','delete':'删除'}];

/* var datas = [
    { 'name': '马闯', 'subject': '语文', 'score': 90 },
    { 'name': '马户', 'subject': '数学', 'score': 100 },
    { 'name': '马伦', 'subject': '体育', 'score': 9 },
    { 'name': '马尧', 'subject': '音乐', 'score': 100 },
    { 'name': '马震', 'subject': '语文', 'score': 90 },
    { 'name': '马云', 'subject': '语文', 'score': 90 }
];*/
function queryTOrderAll(){
	
	var params=[];
	params.push('2017062700001');
	   
	remoteRetriver.query('TOrderRepository', 'findAll','',null, 0,10,false,function(errCode, errMsg, resultData,totalCount,pageCount){
	
	if (errCode == 0) {
		var datas =  JSON.parse(resultData);
		creatTable(document.body, headers, datas);
	} else {
		alert('查询失败!!!' + errMsg);
	}
	});
}

/////页面数据
window.onload = function() {
	queryTOrderAll();
}

