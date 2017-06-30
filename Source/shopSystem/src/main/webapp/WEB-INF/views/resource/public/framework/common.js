/***** 功能方法 *****/

/*********************************************************************************
*   函数:        browser
*   描述:        浏览器判断
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
var browser = (function () {//浏览器判断
    var b = navigator.userAgent.toLowerCase();
    return {
        safari: /webkit/.test(b),
        opera: /opera/.test(b),
        ie: /msie/.test(b) && !/opera/.test(b),
        mozilla: /mozilla/.test(b) && !/(compatible|webkit)/.test(b)
    };
})();


/*********************************************************************************
*   函数:        trim
*   描述:        用正则表达式将前后空格,用空字符串替代。
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
String.prototype.$trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

function trim(str) {
    return (str + '').replace(/(^\s*)|(\s*$)/g, "");
}

/*********************************************************************************
*   函数:        bind
*   描述:        改变方法用的this
*   参数:		 obj		方法对象
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/

Function.prototype.$bind = function (obj) {
    var _this = this; //改变this
    var _args = arguments; //转换化一般变量
    return function () {
        return _this.apply(obj, _args);
    }
}


/*********************************************************************************
*   函数:        clone
*   描述:        浅层次克隆
*   作者:        tony
*   时间:        2012/2/22
********************************************************************************/
$clone = function () {
    var newObj = new Object();
    for (elements in this) {
        newObj[elements] = this[elements];
    }
    return newObj;
}


/*********************************************************************************
*   函数:        cloneAll
*   描述:        深层次克隆
*   作者:        tony
*   时间:        2012/2/22
*********************************************************************************/
$cloneAll = function () {
    function clonePrototype() { }
    clonePrototype.prototype = this;
    var obj = new clonePrototype();
    for (var ele in obj) {
        if (typeof (obj[ele]) == "object") obj[ele] = obj[ele].cloneAll();
    }
    return obj;
}

/*********************************************************************************
*   函数:        $G
*   描述:        得到对象
*   参数:		 obj		id或者对象
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
var g_ele_id_cache = {};  //缓存对象
//得到对象
function $G(obj) {
    if (arguments.length == 1) {
        return g_ele_id_cache[obj] ? g_ele_id_cache[obj] : (g_ele_id_cache[obj] = (typeof (obj) == 'string' ? document.getElementById(obj) : obj));
    }
    else if (arguments.length == 2) {
        var obj = 'string' == typeof (arguments[0]) ? $G(arguments[0]) : arguments[0];
        var fix = ["input", "select", "a"];  //标签集合
        for (var tag in fix) {
            var elms = obj.getElementsByTagName(fix[tag]);
            if (elms && elms.length) {
                if (elms[arguments[1]])
                    return elms[arguments[1]];
            }
        }
    }
    else if (arguments.length == 3) {
        var obj = 'string' == typeof (arguments[0]) ? $G(arguments[0]) : arguments[0];
        var elms = obj.getElementsByTagName(arguments[1]);
        if (elms && elms.length) {
            if (elms[arguments[2]])
                return elms[arguments[2]];
        }
    }
    return undefined;
}

/*********************************************************************************
*   函数:        $C
*   描述:        DOM对象创建函数
*   参数:		 eName		   元素标签
*   参数:		 propertys	   属性集合
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function $C(eName, propertys) {
    var childObj, argLen = arguments.length;
    var domAttr;
    var tmpObj = document.createElement(eName);
    var fix = { 'class': 'className', 'colspan': 'colSpan', 'rowspan': 'rowSpan', "html": "innerHTML", "text": "innerText" };
    for (var pro in propertys) {
        domAttr = fix[pro] || pro;
        tmpObj[domAttr] = propertys[pro];
    }
    if (argLen == 2) return tmpObj;
    for (var i = 2; i < argLen; i++) {  //为父级添加子，或者内容
        childObj = arguments[i];
        if ('string' == typeof (arguments[i]))
            tmpObj.innerHTML += arguments[i];
        else {
            try {
                tmpObj.appendChild(childObj);
            } catch (e) {
                if ('number' == typeof (arguments[i]))
                    tmpObj.innerHTML = arguments[i] + '';
                else
                    alert('创建' + eName + '参数中参数:' + arguments[i] + '有问题');
            }
        }
    }
    return tmpObj;
}

/*********************************************************************************
*   函数:        $C
*   描述:        事件绑定
*   参数:		 eName		   元素标签
*   参数:		 propertys	   属性集合
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
var eve = new Object(); //事件绑定
eve.observe = function (element, name, observer) {
    if (browser.ie)
        element.attachEvent("on" + name, observer);
    else
        element.addEventListener(name, observer, false);
}
eve.stopObserve = function (element, name, observer) {
    if (browser.ie)
        element.detachEvent("on" + name, observer);
    else
        element.removeEventListener(name, observer, false);

}


/*********************************************************************************
*   函数:        $addClass
*   描述:        事件绑定
*   参数:		 eName		   元素标签
*   参数:		 propertys	   属性集合
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function $addClass(elem, cName) {//增加一个className
    if (elem.className.indexOf(cName) >= 0) return; //如果已经有了这个classname,return
    var s = (elem.className == '') ? '' : ' ';
    elem.className += s + cName;
}

/*********************************************************************************
*   函数:        $removeClass
*   描述:        事件绑定
*   参数:		 eName		   元素标签
*   参数:		 propertys	   属性集合
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function $removeClass(elem, cName) {//去掉一个className
    var t = elem.className.indexOf(cName);
    if (cName == elem.className) return elem.className = '';
    var s = '';
    if (-1 == t) return;
    if (0 == t)
        s = cName + ' ';
    else
        s = ' ' + cName;
    elem.className = elem.className.replace(s, '');
}


var cookiedomain = isUndefined(cookiedomain) ? '' : cookiedomain;
var cookiepath = isUndefined(cookiepath) ? '' : cookiepath;

/*********************************************************************************
*   函数:        setCookie
*   描述:        设置cookie
*   参数:		 cookieName		   cookie名称
*   参数:		 cookieValue	   cookie值
*   参数:		 seconds	       保存时间【分钟】
*   参数:		 path    	     
*   参数:		 domain    
*   参数:		 secure    	
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function setCookie(cookieName, cookieValue, seconds, path, domain, secure) {
    var expires = new Date();
    if (cookieValue == '' || seconds < 0) {
        cookieValue = '';
        seconds = -2592000;
    }
    expires.setTime(expires.getTime() + seconds * 1000);
    domain = !domain ? cookiedomain : domain;
    path = !path ? cookiepath : path;
    document.cookie = escape(cookieName) + '=' + escape(cookieValue)
		+ (expires ? '; expires=' + expires.toGMTString() : '')
		+ (path ? '; path=' + path : '/')
		+ (domain ? '; domain=' + domain : '')
		+ (secure ? '; secure' : '');
}

/*********************************************************************************
*   函数:        getCookie
*   描述:        事件绑定
*   参数:		 name		   cookie名称
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function getCookie(name) {
    var cookie_start = document.cookie.indexOf(name);
    var cookie_end = document.cookie.indexOf(";", cookie_start);
    return cookie_start == -1 ? '' : unescape(document.cookie.substring(cookie_start + name.length + 1, (cookie_end > cookie_start ? cookie_end : document.cookie.length)));
}

/*********************************************************************************
*   函数:        getHost
*   描述:        事件绑定
*   参数:		 url		   地址
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function getHost(url) {
    var host = "null";
    if (typeof url == "undefined" || null == url) {
        url = window.location.href;
    }
    var regex = /^\w+\:\/\/([^\/]*).*/;
    var match = url.match(regex);
    if (typeof match != "undefined" && null != match) {
        host = match[1];
    }
    return host;
}


function hostconvert(url) {
    if (!url.match(/^https?:\/\//)) url = SITEURL + url;
    var url_host = getHost(url);
    var cur_host = getHost().toLowerCase();
    if (url_host && cur_host != url_host) {
        url = url.replace(url_host, cur_host);
    }
    return url;
}
/*********************************************************************************
*   函数:        request
*   描述:        获取页面ID参数
*   参数:		 name		   参数值
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function request(name) {
    var URLParams = new Object();
    var aParams = document.location.search.substr(1).split('&');
    for (i = 0; i < aParams.length; i++) {
        var aParam = aParams[i].split('=');
        URLParams[aParam[0]] = aParam[1];
    }
    return URLParams[name];
}

/*********************************************************************************
*   函数:        doane
*   描述:        阻塞事件
*   参数:		 url		   地址
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function doane(event, preventDefault, stopPropagation) {
    var preventDefault = isUndefined(preventDefault) ? 1 : preventDefault;
    var stopPropagation = isUndefined(stopPropagation) ? 1 : stopPropagation;
    e = event ? event : window.event;
    if (!e) {
        e = getEvent();
    }
    if (!e) {
        return null;
    }
    if (preventDefault) {
        if (e.preventDefault) {
            e.preventDefault();
        } else {
            e.returnValue = false;
        }
    }
    if (stopPropagation) {
        if (e.stopPropagation) {
            e.stopPropagation();
        } else {
            e.cancelBubble = true;
        }
    }
    return e;
}

function getEvent() {
    if (document.all) return window.event;
    func = getEvent.caller;
    while (func != null) {
        var arg0 = func.arguments[0];
        if (arg0) {
            if ((arg0.constructor == Event || arg0.constructor == MouseEvent) || (typeof (arg0) == "object" && arg0.preventDefault && arg0.stopPropagation)) {
                return arg0;
            }
        }
        func = func.caller;
    }
    return null;
}


function stopDefault(e) {
    //如果提供了事件对象，则这是一个非IE浏览器   
    if (e && e.preventDefault) {
        //阻止默认浏览器动作(W3C)
        e.preventDefault();
    } else {
        //IE中阻止函数器默认动作的方式   
        e.returnValue = false;
        //event.returnValue = false;  
    }
    return false;
}


function stopBubble(e) {
    //如果提供了事件对象，则这是一个非IE浏览器  
    if (e && e.stopPropagation) {
        //因此它支持W3C的stopPropagation()方法  
        e.stopPropagation();
    } else {
        //否则，我们需要使用IE的方式来取消事件冒泡   
        window.event.cancelBubble = true;
    }
    return false;
}
/*********************************************************************************
*   函数:        isUndefined
*   描述:        是否为undefined
*   参数:		 variable		 值
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function isUndefined(variable) {
    return typeof variable == 'undefined' ? true : false;
}

/*********************************************************************************
*   函数:        thumbImg
*   描述:        生成缩略图
*   参数:		 obj		 图片对象
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function thumbImg(obj) {
    var zw = obj.width;
    var zh = obj.height;
    if (browser.ie && zw == 0 && zh == 0) {
        var matches;
        var re = /width=(["']?)(\d+)(\1)/i;
        matches = re.exec(obj.outerHTML);
        zw = matches[2];
        re = /height=(["']?)(\d+)(\1)/i;
        matches = re.exec(obj.outerHTML);
        zh = matches[2];
    }
    obj.resized = true;
    obj.style.width = zw + 'px';
    obj.style.height = 'auto';
    if (obj.offsetHeight > zh) {
        obj.style.height = zh + 'px';
        obj.style.width = 'auto';
    }
    if (browser.ie) {
        var imgid = 'img_' + Math.random();
        obj.id = imgid;
        setTimeout('try {if ($G(\'' + imgid + '\').offsetHeight > ' + zh + ') {$G(\'' + imgid + '\').style.height = \'' + zh + 'px\';$G(\'' + imgid + '\').style.width = \'auto\';}} catch(e){}', 1000);
    }
    obj.onload = null;
}

/*********************************************************************************
*   函数:        enterTextBox
*   描述:        文本框回车事件
*   参数:        obj   按扭ID
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function enterTextBox(obj) {
    if (event.keyCode == 13) {
        event.keyCode = 9;
        event.returnValue = false;
        $G(obj).click();
        $G(obj).focus();
    }
}

/*********************************************************************************
*   函数:        validator
*   描述:        正则表达式
*   参数:        expression   正则表达式对象
*   参数:        obj		  文本框对象  ID或者本身
*   参数:        msg		  提示
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function validator(expression, obj, msg) {
    var patrn = expression;
    obj = $G(obj);
    if (!patrn.test(obj.value)) {
        if (msg) alert(msg);
        obj.value = "";
        obj.focus();
        return false;
    }
    else {
        return true;
    }
}

/*********************************************************************************
*   函数:        setShow
*   描述:        设置一个对象是否显示
*   参数:        obj   对象  ID或者本身
*   参数:        sat   display属性值
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function setShow(obj, sat) {
    $G(obj).style.display = sat;
}

/*********************************************************************************
*   函数:        openModalDialog
*   描述:        打开模态对话框
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function openModalDialog(URL, arg, width, height) {
    return window.showModalDialog(URL, arg, "dialogWidth=" + width + "px;dialogHeight=" + height + "px;status=0;scroll=no;help:no;");
}


/*********************************************************************************
*   函数:        openPopup
*   描述:        从页面中央弹出窗口
*   参数:        url			目标地址
*   参数:        windowname		窗口名称
*   参数:        w				宽度
*   参数:        h				高度
*   参数:        resize			是否可以改变大小
*   参数:        noscroll		是否有滚动条
*   参数:        fullscreen		是否全屏
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function openPopup(url, windowname, fullscreen, resize, noscroll, w, h) {
    var _resize = resize ? '1' : '0';
    var _scrollbar = noscroll ? 'no' : 'yes';
    var windowconfig = "toolbar=no,status=yes,copyhistory=no,menubar=no,location=no,directories=no,scrollbars=" + _scrollbar + ",resizable=" + _resize;
    var subwin;

    //
    if (fullscreen) {
        subwin = window.open(url, windowname, "width=" + (window.screen.availWidth - 4) + ",height=" + (window.screen.availHeight - 50) + "," + windowconfig);
        subwin.moveTo(-4, -4);
    }
    else {
        var winWidth = window.screen.availWidth - 12;
        var winHeight = window.screen.availHeight - 50;
        //从中间呈现
        var intTop = (window.screen.availHeight - parseInt(h)) / 2;
        var intLeft = (window.screen.availWidth - parseInt(w)) / 2;
        if (intTop < 30) intTop = 0;
        if (intLeft < 30) intLeft = 0;
        if (w > winWidth) { w = winWidth; }

        subwin = window.open(url, windowname, "width=" + w + ",height=" + h + ",top=" + intTop + ",left=" + intLeft + "," + windowconfig);
    }

    if (subwin) {
        subwin.focus();
        return subwin;
    }
}

/*********************************************************************************
*   函数:        printView
*   描述:        打印
*   参数:        url   目标地址
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function printView(url) {
    var tmp1 = url.substring(0, url.indexOf("url=") + 4);
    var tmp2 = url.substring(url.indexOf("url=") + 4, url.length);

    if (tmp2.indexOf("http://") < 0) {
        tmp2 = document.URL.substring(0, document.URL.lastIndexOf("/") + 1) + tmp2;
    }

    url = tmp1 + tmp2;

    window.open(url, 'PrintView', 'height=580,width=680,resizable=no,scrollbars=auto,status=ye,toolbar=yes,menubar=no,location=no');
}

/*********************************************************************************
*   描述:   以下为对select操作
**********************************************************************************/

/*********************************************************************************
*   函数:        swap
*   描述:        将源select列表中的选定项移动到目的select列表，支持多选
*   参数:        from   源select列表
*   参数:        to      目的select列表
**********************************************************************************/
function swap(from, to) {
    var isExist = false;
    var count = 0;
    try {
        for (index = 0; index < from.options.length; index++) {
            isExist = false;
            if (from.item(index).selected) {
                count++;
                if (to.all.length > 0) {
                    for (var i = 0; i < to.all.length; i++) {
                        //if(to.item(i).text==from.item(index).text)
                        if (to.item(i).id.toLowerCase() == from.item(index).id.toLowerCase()) {
                            if (typeof (to.item(i).type) == "object") {
                                if (to.item(i).type.toLowerCase() == from.item(index).type.toLowerCase()) {
                                    isExist = true;
                                }
                            }
                            else {
                                isExist = true;
                            }
                        }
                    }
                }
                if (isExist == false) {
                    var oOption = document.createElement("OPTION");
                    oOption.text = from.item(index).text;
                    oOption.id = from.item(index).id;
                    oOption.value = from.item(index).value;
                    if (from.item(index).type != null && typeof (from.item(index).type) == "string") {
                        oOption.type = from.item(index).type;
                    }
                    to.add(oOption);
                }
            }
        } //end for
        if (count == 0) {
            alertWindow("请选中要添加的项！");
        }
        from.selectedIndex = -1;
    } //try
    catch (e) {
        alertWindow(e);
        //alert("请选中要添加的项！");
    }
    return false;
}

/*********************************************************************************
*   函数:        swapAll
*   描述:        将源select列表中的所有项移动到目的select列表
*   参数:        from   源select列表
*   参数:        to      目的select列表
**********************************************************************************/
function swapall(from, to) {
    for (index = 0; index < from.options.length; index++) {
        var isExist = true;
        try {
            if (to.all.length > 0) {
                for (var i = 0; i < to.all.length; i++) {
                    //if(to.item(i).text==from.item(index).text)
                    if (to.item(i).id.toLowerCase() == from.item(index).id.toLowerCase() && to.item(i).type.toLowerCase() == from.item(index).type.toLowerCase()) {
                        isExist = false;
                    }
                }
            }
            if (isExist == true) {
                var oOption = document.createElement("OPTION");
                oOption.text = from.item(index).text;
                oOption.id = from.item(index).id;
                oOption.value = from.item(index).value;
                oOption.type = from.item(index).type;
                to.add(oOption);
            }
            from.selectedIndex = -1;

        } //try
        catch (e) {
            //alert("请选中要添加的项！");
        }
    }
    return false;
}
/*********************************************************************************
*   函数:        swapSelectToInput
*   描述:        将源select列表中的选中项目移动到目的input中
*   参数:        from   源select列表
*   参数:        to      目的select列表
**********************************************************************************/
function swapSelectToInput(from, to) {
    try {
        to.value = from.item(from.selectedIndex).text;
        //from.selectedIndex = -1;
    } //try
    catch (e) {
        alertWindow("请选中要添加的项！");
    }
    return false;
}

/*********************************************************************************
*   函数:        del
*   描述:        删除select列表中的选定项，支持多选
*   参数:        from   源select列表
*   参数:        to      目的Input 对象
**********************************************************************************/
function del(obj) {
    if (obj.all.length == 0) return;
    try {
        if (obj.selectedIndex < 0) { alertWindow("请选中要删除的项"); return }
        for (index = 0; index < obj.options.length; index++) {
            if (obj.item(index).selected && obj.item(index).text != "Everyone") {
                obj.remove(index);
                index--;
            }
        } // end for
    } //end try
    catch (e) {
        alertWindow("Error");
    }
    return false;
}
/*********************************************************************************
*   函数:        del
*   描述:        删除select列表中的所有项
*   参数:        obj   select对象
**********************************************************************************/
function delall(obj) {
    if (obj.all.length == 0) return;
    try {
        var count = obj.all.length
        for (i = count - 1; i >= 0; i--) {
            if (obj.options[i].text != "Everyone") {
                obj.removeChild(obj.options[i]);
            }
        }
    }
    catch (e) {
    }
    return false;
}
/*********************************************************************************
*   函数:        swapSelectToInput
*   描述:        select中的选定项设置到input的value中
*   参数:        obj   select对象
**********************************************************************************/
function swapSelectToInput(from, to) {
    try {
        to.value = from.item(from.selectedIndex).text;
        //from.selectedIndex = -1;
    } //try
    catch (e) {
        alertWindow("请选中要添加的项！");
    }
    return false;
}


/*********************************************************************************
*   函数:        ListToString
*   描述:        将select中的列表成员组合成字符串，通过逗号分隔
*   参数:        from   被合成的select对象
*   参数:        attribute   要合成字符串的属性
**********************************************************************************/
function ListToString(from, attribute, separator) {
    var str = "";
    var objStr;
    var sep = ",";
    if (typeof (separator) == "string")
        sep = separator;
    try {
        for (i = 0; i < from.options.length; i++) {
            if (i != 0) str += sep;
            objStr = eval("from.options[" + i + "]." + attribute);
            str += objStr;
        }
        return str
    }
    catch (e) {
        alertWindow(e);
        return "";
    }
}

/*********************************************************************************
*   函数:        openModalDialog
*   描述:        打开模态对话框，弹出窗口，模拟alert();
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function alertWindow(txt) {
    window.showModalDialog(publicPath + "alert.aspx?model=alert", txt, "dialogWidth=450px;dialogHeight=250px;status=0;scroll=no;help:no;");
}

/*********************************************************************************
*   函数:        openModalDialog
*   描述:        弹出窗口，模拟confirm();
*   作者:        tony
*   时间:        2012/2/22
**********************************************************************************/
function confirmWindow(txt) {
    return window.showModalDialog(publicPath + "alert.aspx?model=confirm", txt, "dialogWidth=450px;dialogHeight=250px;status=0;scroll=no;help:no;");
}

function HY(obj) {
    if (arguments.length == 1) {
        return document.getElementById(obj);
    }
    else {
        var ary = new Array();
        var childs = obj.getElementsByTagName(arguments[1])
        for (var i = 0; i < childs.length; i++) {
            if (arguments[3]) {
                if (childs[i].type == arguments[2] && childs[i].id == arguments[3]) {
                    ary.push(childs[i]);
                }
            }
            else {
                if (childs[i].type == arguments[2]) {
                    ary.push(childs[i]);
                }
            }
        }
        return ary.length == 1 ? ary[0] : ary;
    }
}