/**
 *  查询数据
 */
var remoteRetriver = {
		
		/**
		 * 排序规则：升序
		 */
		SORT_TYPE_ASC : 'ASC',
		/**
		 * 排序规则：降序
		 */
		SORT_TYPE_DESC : 'DESC',
		
		sortParams:'',
		
	addSortFiled : function(sortField, sortType) {
		
		if (sortParams != '') {
			sortParams += '&';
		}
		sortParams += 'sort=';
		sortParams += sortField;
		if (sortType ==remoteRetriver.SORT_TYPE_ASC) {
			sortParams += ',ASC';
		} else if (sortType == remoteRetriver.SORT_TYPE_DESC) {
			sortParams += ',DESC';
		}
		
		return sortParams;
	},
	
	/**
	 * 清除排序字段
	 * */
	resetSortFileds : function() {
		sortParams = ' ';
	},
	/**
	 * 
	 * @param repositoryName
	 * @param funcName  对应其中到中函数
	 * @param funcParams 函数中的参数
	 * @param pageSize
	 * @param pageIndex
	 * @param async
	 * @param callback
	 * 	@RequestMapping(value = "/repository/{repositoryName}/{funcName}/{page}/{size}
	 */
	query : function(repositoryName,funcName,funcParams,pageSorts,pageIndex,pageSize,async,callback) {
		
		//传递页面索引
		if (isNaN(pageIndex)) {
			pageIndex = 0;
		} else if (pageIndex < 0) {
			pageIndex = 0;
		}
		
		//分页查询需要部分 '/pageIndex/pageSize'
		var pageParts= '';
		
		if(pageSize!=null&&pageIndex!=null){
			pageParts =  '/' + pageIndex + '/' + pageSize;
			
		}
		
		///测试分页
		var url = '/repository/' + repositoryName + '/' + funcName + pageParts;
		
		//拼接成后台函数
		var params = 'params=[' + funcParams + ']' ;
		//var params = funcParams;
		if(pageSorts!=null){
			params += pageSorts;
		}
	
		baseRequest.sendGet(url, params, async, callback);
	},
	queryList : function(repositoryName,funcName,funcParams,async,callback) {
		
		remoteRetriver.query(repositoryName,funcName,funcParams,null,null,null,async,callback);
	},
	
	

}