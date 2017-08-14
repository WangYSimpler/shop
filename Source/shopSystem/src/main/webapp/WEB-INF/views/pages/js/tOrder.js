	///配置信息
	var respositoryName = 'TOrderRepository';
	var idName          = 'id';
	var tableName       = 'Order';
	var initQueryFunction = 'findByDelFlag';
	
	//表中初始化
	function init(){
		hostCrud.hostInit(respositoryName,idName,initQueryFunction);
	}
	
	//模态框显示
	function showModelWindow() {
		hostCrud.showModelWindow(tableName);
		
	};

	//关闭模态框
	function closeModelWindow() {
		
		hostCrud.closeModelWindow();
		init();
	}
	//点击新建
	function newObj() {
		showModelWindow();
	};

	//保存数据
	function saveObj() {
		var obj = $('#fm').serializeObject();
		hostCrud.saveObj(obj);
	};
	
	//编辑对象
	function editObj() {
		var obj = $('#dg').datagrid('getSelected');
		hostCrud.editObj(obj,tableName);
	};

	//删除
	function removeObj() {
		///物理删除代码
		var row = $('#dg').datagrid('getSelected');
		hostCrud.removeObj(row);
	}
