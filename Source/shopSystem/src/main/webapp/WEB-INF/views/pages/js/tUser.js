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
    read();
  }).submit();

  // 点击新增按钮
  $('.add-btn').click(function() {
    user = {
      userNo:'',
      userName: '',
      password: '',
      remark:'',
      flag:'1'
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
    initUserForm(user, false);
  });

  /**
   * 初始化用户表单
   * @param user
   * @param isAdd
   */
  function initUserForm(user, isAdd) {
    $('.modal-content').load('../html/user-form.html', function() {
      $(this).html(template('userTmp', {
        user: user,
        isAdd: isAdd
      }));
      userModal.modal('show');
    });
  }

  // 表单验证
  App.initValidator();

  userModal.validate({
    submitHandler: function(form) {
      $(form).find('button[type="submit"]')
        .attr('disabled', true).addClass('disabled');
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

  /**
   * 读取
   */
  function read() {
    loader.show();
    var key = $.trim($('#keyName').val());
    
    remoteRetriver.query('TUserRepository', 'findAll','',null, 0,10,false,function(errCode, errMsg, resultData,totalCount,pageCount){
    	
    	if (errCode == 0) {
    		var datas =  JSON.parse(resultData);
    		loader.remove();
    	    users = datas;
    	    tbody.html(template('usersTmp', {users: users}));
    	} else {
    		alert('查询失败!!!' + errMsg);
    	}
    	});
   /* $.get(App.baseUrl + '/users?q={"name":{"$regex":"' + key + '"}}&apiKey=' + App.apiKey, function(data) {
      loader.remove();
      users = data;
      tbody.html(template('usersTmp', {users: users}));
    });*/
  }

  /**
   * 新增编辑
   */
  function save() {
	user.userNo =  parseInt($('#userNo').val());
    user.userName = $('#userName').val();
    user.password =$('#password').val();
    user.remark =$('#remark').val();
    
   /* user.age = parseInt($('#age').val());
    user.sex = parseInt($('input[name="sex"]:checked').val());*/
    loader.show();
    
    if (isAdd) {
		rCreate(user);
	}else{
		rUpdate();
	}
    loader.remove();
    tbody.html(template('usersTmp', {users: users,isAdd: isAdd}));
    
    /*$.post(App.baseUrl + '/users?apiKey=' + App.apiKey,   JSON.stringify(user), function(data) {
        userModal.modal('hide');
        loader.remove();
        // _id为空则新增
        isAdd = user._id === undefined;
        if (isAdd) {
          users.push(user)
        } else {
          users[index] = user;
        }
        tbody.html(template('usersTmp', {users: users,isAdd: isAdd}));
      });*/
  }

  
  function rCreate(user){
	 //var todo = JSON.stringify(user);
	 //var todo = user;
	 
	 var todo =  {'id':'3','userNo':'1', 'userName':'王勇','flag':'0','password':'11111'};
	 remoteCreate.create("TUserRepository", todo, true,function(errCode, errMsg, resultData){
     if(errCode == 0){
         alert("新建成功！");
     }
     else{
         alert("新建失败！" + errMsg);
     }});
 }
  
  function rUpdate(){
	    var id = 1;
	    if(!confirm("将更新id为" + id + "的记录!"))
        return;
    var todo = {"id":1, "title":"测试数据标题更新", "content":"测试数据内容更新"};
    cffex.repository.update("toDoRepository", id, todo, false, function(errCode, errMsg, resultData){
        if(errCode == 0){
            alert("更新成功！");
        }
        else{
            alert("更新失败！" + errMsg);
        }
    });
  }
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