$(function() {
  var tbody = $('#userTbl tbody'),
    userModal = $('#userModal'),
    users = [],
    user = {},
    isAdd = false,
    index = -1;

  // 搜索
  $('#searchForm').submit(function(e) {
    e.preventDefault();
    initRead();
  }).submit();

  // 点击新增按钮
  $('.add-btn').click(function() {
    user = {
      userNo:'',
      userName: '',
      password: '',
      remark:'',
      delFlag:'1'
    };
    ////添加用户
    isAdd = true;
    initUserForm(user, isAdd);
  });

  // 点击编辑按钮
  tbody.on('click', '.edit-btn', function() {
    index = tbody.find('.edit-btn').index($(this));
    user = users[index];
    //编辑标志
    isAdd = false;
    initUserForm(user, isAdd);
  });

  /**
   * 初始化用户表单
   * @param user
   * @param isAdd
   */
  function initUserForm(user, isAdd) {
    $('.modal-content').load('../html/user-form.html', function() {
      $(this).html(template('userTmp',
    	{
        user: user,
        isAdd: isAdd
      }));
      userModal.modal('show');
    });
  }

  // 表单验证
  App.initValidator();

  userModal.validate({ submitHandler: function(form) {
      $(form).find('button[type="submit"]').attr('disabled', true).addClass('disabled');
      save();
    }
  });

  // 点击删除按钮
  tbody.on('click', '.delete-btn', function() {
    index = tbody.find('.delete-btn').index($(this));
    confirm.show('确认要删除该用户吗？', function() {
      user = users[index];
      remove();
    });
  });

  
   //读取首页
  function initRead() {
    loader.show();
    var key = $.trim($('#keyName').val());
    ///获取数据排序
    remoteRetriver.resetSortFileds();
    var sortParams = remoteRetriver.addSortFiled('id',remoteRetriver.SORT_TYPE_ASC);
    //查询第一页
    remoteRetriver.query('TUserRepository', 'findAll','',sortParams, 0,10,false,function(errCode, errMsg, resultData,totalCount,pageCount){
    	
    	if (errCode == 0) {
    		var datas =  JSON.parse(resultData);
    		loader.remove();
    	    users = datas;
    	    tbody.html(template('usersTmp', {users: users}));
    	} else {
    		alert('查询失败!!!' + errMsg);
    	}
    	});
  }

  /**
   * 保存数据，包括了增加和修改
   */
  function save() {
	user.userNo   = $('#userNo').val();
    user.userName = $('#userName').val();
    user.password = $('#password').val();
    user.remark   = $('#remark').val();
    
    loader.show();
    
    if (isAdd) {
		rCreate(user);
	}else{
		rUpdate();
	}
  }

  
  function rCreate(user){
	  
	 var todo = user;
	 remoteCreate.create("TUserRepository", todo, true,function(errCode, errMsg, resultData){

		 if(errCode == 0){
	         alert("新建成功！");
	         //users.push(user);
	         initRead();
	         userModal.modal('hide');
	         loader.remove();
	         tbody.html(template('usersTmp', {users: users,isAdd: isAdd}));
	     } else{ alert("新建失败！" + errMsg); }
     });
 }
  
  
  function rUpdate(){
	    var id = 61;
	    var todo =  {"delFlag":"1","id":61,"password":"111111","userName":"Test321","userNo":"6"};
	    remoteUpdate.update("TUserRepository", id, todo, false, function(errCode, errMsg, resultData){
	        if(errCode == 0){
	            alert("更新成功！");
	        }
	        else{
	            alert("更新失败！" + errMsg);
	        }
	    });
	}
  
  /**/
  /*function rUpdate(user){
	var todo = {"delFlag":"1","id":61,"password":"111111","userName":"苏州测试","userNo":"6"};
	var id = 61;
	//var todo = user;
	remoteUpdate.update("TUserRepository", id, todo, false, function(errCode, errMsg, resultData){
	    if(errCode == 0){
	        alert("更新成功！");
	        initRead();
	        userModal.modal('hide');
	        loader.remove();
	        tbody.html(template('usersTmp', {users: users,isAdd: isAdd}));
	    }
	    else{
	        alert("更新失败！" + errMsg);
	    }
	});
  }*/
  /**
   * 删除
   */
  function remove() {
    loader.show();
    $.ajax({
      url: App.baseUrl + '/users/' + user._id.$oid + '?apiKey=' + App.apiKey,
      type: "DELETE",
      success: function() {
        loader.remove();
        users.splice(index, 1);
        confirm.remove();
        tbody.html(template('usersTmp', {users: users}));
      }
    });
  }
});