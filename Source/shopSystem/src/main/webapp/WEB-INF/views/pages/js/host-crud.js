
var hostCrud={
		
		hostInit: function (respositoryName,sortName,queryFunction) {
			remoteRetriver.resetSortFileds();
			
			/**
			 * 查询实例
			 *  升序
			 *  var sortParams = remoteRetriver.addSortFiled('createDatetime,createUser',remoteRetriver.SORT_TYPE_ASC);
			 *  降序
			 *  sortParams = remoteRetriver.addSortFiled('userName',remoteRetriver.SORT_TYPE_DESC)
			 */
			var sortParams = remoteRetriver.addSortFiled(sortName,remoteRetriver.SORT_TYPE_ASC);
			var params=[];
			params.push('0');
			
			//查询第一页
			remoteRetriver.query(respositoryName, queryFunction, params, sortParams, 0,10, false, function(errCode, errMsg, resultData, totalCount,pageCount) {

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
		},

		
		showModelWindow: function (tableName) {
			$('#dlg').dialog('open').dialog('setTitle', 'New' + tableName);
			$('#fm').form('clear');
		},

		closeModelWindow :function () {
			$('#dlg').dialog('close'); // close the dialog
		},

		newObj: function () {
			showModelWindow();

		},

		saveObj: function (obj) {
			//进入保存文件
			if(obj.id == ''){
				obj.delFlag = '0';
				this.rCreate(obj);
			}else{
				var tableId = obj.id;
				rUpdate(obj,tableId);
			}
		},

		//补充
		objComplete: function (obj,createFlag){
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
		},
		
		rCreate: function (createObj) {

			var todo =  this.objComplete(createObj,true);
			remoteCreate.create(respositoryName, todo, true, function(errCode,errMsg, resultData) {
				if (errCode == 0) {
					alert("新建成功！");
					//alert(App.getCookie('loginNo'));
					closeModelWindow();
				} else {
					alert("新建失败！" + errMsg);
				}
			});
		},
		
		editObj:function (obj,tableName) {
			var row = obj;
			if (row) {
				$('#dlg').dialog('open').dialog('setTitle', 'Edit ' + tableName);
				$('#fm').form('load', row);
			}
		},

		rUpdate: function (updateObj, tableId) {
			var id = tableId;
			var todo =  objComplete(updateObj,false);
			remoteUpdate.update(respositoryName, id, todo, false, function(errCode, errMsg, resultData) {
				if (errCode == 0) {
					closeModelWindow();
				} else {
					alert("更新失败！" + errMsg);
				}
			});
		},

		removeObj: function () {
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
	
}