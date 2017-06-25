
/* ---登录--*/
function login(userName,userPass) {
	
	console.log(userName);
	console.log(userPass);
	
	// remotePermission.login("1", "1", function(errCode, errMsg, resultData){}
    var f1=true;
    if(f1){
        alert('login success!');
        window.location.href = '../index.html'
    }
}


function isLogin(){
    var islogin=localStorage.getItem('isLogin');
    if(islogin == false){
        window.location.href=gofirstConfig.domain + '/' + gofirstConfig.project + '/pages/html/login.html';
    }
}


function setstorage(objname,obj){
    localStorage.setItem(objname,JSON.stringify(obj));
}
function getstorage(objname){
   JSON.parse(localStorage.getItem(objname)) ;
}