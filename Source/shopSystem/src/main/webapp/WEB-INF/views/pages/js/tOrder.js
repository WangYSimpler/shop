///配置信息
var respositoryName = 'TOrderRepository';
var idName          = 'id';
var tableName       = 'Order';

function init() {
		remoteRetriver.resetSortFileds();
		var sortParams = remoteRetriver.addSortFiled(idName,remoteRetriver.SORT_TYPE_ASC);
		var params=[];
		params.push('0');
		
		//查询第一页
		remoteRetriver.query(respositoryName, 'findByDelFlag', params, sortParams, 0,10, false, function(errCode, errMsg, resultData, totalCount,pageCount) {

					if (errCode == 0) {
						var datas = JSON.parse(resultData);
						$('#dg').datagrid('loadData', {
							"total" : totalCount,
							"rows" : datas
						});
					} else {
						alert('查询失败!!!' + errMsg);
					}
				});
	};
	
	function showModelWindow() {
		$('#dlg').dialog('open').dialog('setTitle', 'New' + tableName);
		$('#fm').form('clear');
	};

	function closeModelWindow() {
		$('#dlg').dialog('close'); // close the dialog
		init();
	}

	function newObj() {
		showModelWindow();

	};

	function saveObj() {

		var obj = $('#fm').serializeObject();
		//进入保存文件
		if(obj.id == ''){
			obj.delFlag = '0';
			rCreate(obj);
		}else{
			var tableId = obj.id;
			rUpdate(obj,tableId);
		}
	};

	//补充
	function objComplete(obj,createFlag){
		var completeObj = obj;
		if (createFlag) {
			completeObj.delFlag='0';
			completeObj.createUser=getCookie("loginNo");
			completeObj.createDate = new Date().toLocaleDateString();
		}else {
			completeObj.updateUser='1';
			completeObj.updateDate = new Date().toLocaleDateString();
		}
		return completeObj;
	}
	
	function rCreate(createObj) {

		var todo =  objComplete(createObj,true);
		remoteCreate.create(respositoryName, todo, true, function(errCode,errMsg, resultData) {
			if (errCode == 0) {
				alert("新建成功！");
				//alert(App.getCookie('loginNo'));
				closeModelWindow();
			} else {
				alert("新建失败！" + errMsg);
			}
		});
	};

	function editObj() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			$('#dlg').dialog('open').dialog('setTitle', 'Edit ' + tableName);
			$('#fm').form('load', row);
		}
	};

	function rUpdate(updateObj, tableId) {
		var id = tableId;
		var todo =  objComplete(updateObj,false);
		remoteUpdate.update(respositoryName, id, todo, false, function(errCode, errMsg, resultData) {
			if (errCode == 0) {
				closeModelWindow();
			} else {
				alert("更新失败！" + errMsg);
			}
		});
	}

	function removeObj() {
		///物理删除代码
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			 var msg = "您真的确定要删除吗？  \n请确认！"; 
			  if (confirm(msg)==true){ 
				  var tableId = row[idName];
				  var todo = row;
				  todo.delFlag='1';
				  remoteDeleter.logicDeleter(respositoryName, tableId, todo, false, function(errCode, errMsg, resultData) {
						if (errCode == 0) {
							closeModelWindow();
						} else {
							alert("删除失败！" + errMsg);
						} });
			  }else{ 
			    return false; 
			  } 
		}
		
		/* 
		 * var row = $('#dg').datagrid('getSelected');
		if (row) {
			 var msg = "您真的确定要删除吗？  \n请确认！"; 
			  if (confirm(msg)==true){ 
				  var tableId = row[idName];
					remoteDeleter.deleter(respositoryName, tableId, false, function(errCode, errMsg, resultData) {
						if (errCode == 0) {
							closeModelWindow();
						} else {
							alert("删除失败！" + errMsg);
						} });
			  }else{ 
			    return false; 
			  } 
		}*/
	}
