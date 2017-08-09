
/* ---登录--*/
function login() {
	
	var userNo = document.getElementById('userNo').value;  
	var userPass = document.getElementById('userPass').value; 
	
	remotePermission.login(userNo, userPass, function(errCode, errMsg, resultData){
	    if(errCode == 0){
	    	///set cookie
	    	//App.setCookie("loginNo",userNo,20);
	    	var fullUrl = gofirstConfig.domain + '/' + gofirstConfig.project +'/pages';
	    	window.location.href=fullUrl+"/html/tOrder.html"; 
	    	
	    }
	    else{
	        alert("登录失败！" + errMsg);
	    }
		
	});
   
}

function logout() {
	remotePermission.logout(function(errCode, errMsg,resultData) {
		  if(errCode == 0){
			  alert("登出成功！" + errMsg);
		    }
		    else{
		        alert("登录失败！" + errMsg);
		    }
	});
}



