	///配置信息
	var respositoryName = 'TOrderRepository';
	var idName          = 'id';
	var tableName       = 'Order';
	var initQueryFunction = 'findByDelFlag';
	
	function init(){
		hostCrud.hostInit(respositoryName,idName,initQueryFunction);
	}
	
	
	function showModelWindow() {
		hostCrud.showModelWindow(tableName);
		
	};

	function closeModelWindow() {
		
		hostCrud.closeModelWindow();
		init();
	}
		

	function newObj() {
		showModelWindow();
	};

	function saveObj() {
		var obj = $('#fm').serializeObject();
		hostCrud.saveObj(obj);
	};

	
	
	function rCreate(createObj) {
		hostCrud.rCreate(createObj);
	};

	function editObj() {
		
		var obj = $('#dg').datagrid('getSelected');
		hostCrud.editObj(obj,tableName);
	};

	

	function removeObj() {
		///物理删除代码
		var row = $('#dg').datagrid('getSelected');
		hostCrud.removeObj(row);
	}
