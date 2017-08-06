///配置信息
var respositoryName = 'TUserRepository';
var idName          = 'id';

function init() {
		remoteRetriver.resetSortFileds();
		var sortParams = remoteRetriver.addSortFiled(idName,remoteRetriver.SORT_TYPE_ASC);
		
		//查询第一页
		remoteRetriver.query(respositoryName, 'findAll', '', sortParams, 0,10, false, function(errCode, errMsg, resultData, totalCount,pageCount) {

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
		$('#dlg').dialog('open').dialog('setTitle', 'New User');
		$('#fm').form('clear');
	};

	function closeModelWindow() {
		$('#dlg').dialog('close'); // close the dialog
		init();
	}

	function newUser() {
		showModelWindow();

	};

	function saveUser() {

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

	function rCreate(createObj) {

		var todo = createObj;
		remoteCreate.create(respositoryName, todo, true, function(errCode,
				errMsg, resultData) {
			if (errCode == 0) {
				alert("新建成功！");
				closeModelWindow();
			} else {
				alert("新建失败！" + errMsg);
			}
		});
	};

	function editUser() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			$('#dlg').dialog('open').dialog('setTitle', 'Edit User');
			$('#fm').form('load', row);
		}
	};

	function rUpdate(updateObj, tableId) {
		var id = tableId;
		var todo = updateObj;
		remoteUpdate.update(respositoryName, id, todo, false, function(errCode, errMsg, resultData) {
			if (errCode == 0) {
				/* alert("更新成功！"); */
				closeModelWindow();
			} else {
				alert("更新失败！" + errMsg);
			}
		});
	}

	function removeUser() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			 var msg = "您真的确定要删除吗？  \n请确认！"; 
			  if (confirm(msg)==true){ 
				  var tableId = row[idName];
					remoteDeleter.deleter("TUserRepository", tableId, false, function(errCode, errMsg, resultData) {
						if (errCode == 0) {
							//alert("删除成功！" + errMsg);
							closeModelWindow();
						} else {
							alert("删除失败！" + errMsg);
						} });
			  }else{ 
			    return false; 
			  } 
		}
	}