/**
 * @Author JiangR 2017/4/14.
 */
function getIdValue(id){
 return $.trim($("#"+id).val())
}
function getClassValue(cls){
    return $.trim($("."+cls).val())
}

/**
 *
 * @param name
 * @returns {string|*}
 */
function getInputValue(name){
    return $.trim(form.name.value)
}

//验证是否为空
function isEmpty(_obj,flag){

    /*var obj = document.getElementById(_obj.id);
    var info = document.getElementById(_obj.id+"Info");*/

    if(flag){
        if(obj.value.length==0){
            showInfo(info,"请输入相应的内容","red");
            return false;}
        else{
       showInfo(info,"","grey")
            return true;}
    }
    else{
     showInfo(info,"请输入相应的内容","grey")
        return false;
    }
}


/**
 *验证密码   必填内容 全为数字
 */
function password(_obj,flag){
    var obj = document.getElementById(_obj.id);
    var info = document.getElementById(_obj.id+"Info");
    var reg = /^\d{6}$/;
    if(flag){
        if(obj.value.length>0){
            if(reg.test(obj.value)==false){
                showInfo(info,"请输入6位数字为密码!","red")
                return false;}
            else{
                showInfo(info,"","")
                return true;}
        }
        else{
            showInfo(info,"请输入6位数字为密码!","red")
            return false;}
    }
    else{
        showInfo(info,"请输入6位数字为密码!","grey")
    }

}



//验证邮编,内容非必填字段,如果填写则进行验证
function isPostCode(_obj,flag){
    var obj = document.getElementById(_obj.id);
    var info = document.getElementById(_obj.id+"Info");
    var reg = /^\d{6}$/;
    if(flag){
        if(obj.value.length>0){
            if(reg.test(obj.value)==false){
                showInfo(info,"请输入6位数字为合法的邮政编码格式!","red")
                return false;}
            else{
                showInfo(info,"","")
                return true;}
        }
        else{
            showInfo(info,"邮编为可选填的内容","grey")
            return true;}
    }
    else{
        showInfo(info,"邮编的格式为6位数字","grey")
    }
}

//验证电子邮件
//参数:Email表单元素ID,是否有必填,表单状态
function isEmail(_obj,isempty,flag){
    var obj = document.getElementById(_obj.id);
    var info = document.getElementById(_obj.id+"Info");
    var reg =/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
    if (flag){
        if(isempty){
            if(obj.value == ""){
                showInfo(info,"电子邮件不能为空","red")
                return false;    }
            if (reg.test(obj.value)==false){
                showInfo(info,"电子邮件格式不正确","red")
                return false;}
            else{
                showInfo(info,"√","green")
                return true;}
        }
        else{
            if (obj.value.length>0){
                if (reg.test(obj.value)==false){
                    showInfo(info,"电子邮件格式不正确","red")
                    return false;}
                else{
                    showInfo(info,"","")
                    return true;    }
            }
            else{
                showInfo(info,"如果填写则进行格式验证","grey")
                return true;    }
        }
    }
    else{
        showInfo(info,"请填写电子邮件","grey")    }
}

//电话验证:非必填内容
function isPhone(_obj,flag){
    var obj=document.getElementById(_obj.id);
    var info=document.getElementById(_obj.id+"Info");
    //var reg=/(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/;
    var reg=/((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/;
    if(flag){
        if(obj.value.length>0){
            if(reg.test(obj.value)==false){
                showInfo(info,"电话格式不正确","red")
                return false;        }
            else{
                showInfo(info,"","")
                return true;
            }
        }
        else{
            showInfo(info,"如填写则进行格式验证","grey")
            return true;
        }
    }
    else{
        showInfo(info,"如填写则进行格式验证","grey")
    }
}

//显示信息
function showInfo(_info,msg,color){
    var info=_info;
    info.innerHTML = msg;
    info.style.color=color;
}
