

/**
 * 创建表格
 * @param order 展示顺序
 * @param title 标题
 * @param content 标题
 * @param tableDiv 表格容器
 */
function createTable(order, title, content, tableDiv) {

    var creatTable = ' <table width="100%" id="tb"  border="1"   style="border-collapse :collapse "><tr><th>' +
        '<input type="checkbox" id="checkAll"></th>';
    for (var i = 0; i < order.length; i++) {
        creatTable += "<th>" + order[i] + "</th>"
    }
    creatTable = creatTable + '</tr>';
    var creatTrTd = '';
    for (var j = 0; j < content.length; j++) {
        var trs = '<tr><td><input type="checkbox"></td>';
        for (var i = 0; i < order.length; i++) {
            trs += "<td>" + content[j]['' + order[i] + ''] + "</td>"
        }
        trs = trs + '</tr>';
        creatTrTd += trs;
    }
    creatTable = creatTable + creatTrTd + '</table><input type="button" id="SaveAll" value="保存全部" /><input type="button" id="Add" value="添加" />';


    document.getElementById(tableDiv).innerHTML = creatTable;

}
/**
 * 表格json数据展示
 */

function TableData(options) {

    this.config = {
        container: '',         // 默认tbody容器
        JSONData: '',                 // json数据
        isRadio: false,               // 是否单选
        isCheck: false,                 // 是否多选
        callback: null,
        title: '',                   //标题
        oddColor: '#FFE009',         //奇行color
        evenColor: 'white',         //偶行
        page: '',    // 页码容器
        perPage: 10,    // 一页多少条数据 默认情况下10条数据
        curIndex: 1,     // 当前的索引 从第几开始
        itemCount: 300,   // 记录总数 默认设为100条
        totalPages: 0,     // 总页数
        buttonAmount: 5,    // 每页显示按钮的数量
        isAutoClick: true    // 上一页 下一页是否封装在里面作为点击 默认为true


    };
    /*  缓存*/
    this.cache = {
        selectLen: 0
    };

}

TableData.prototype = {

    constructor: TableData,


    init: function (customConfig, callback) {
        this.config = $.extend(this.config, customConfig || {});
        var self = this,
            _config = self.config;
        self._renderHTML();
        self._itemSelect();
        self._query(callback);
        return this;
    },


    /*
     * 渲染tbody里面的数据
     * @method _renderHTML
     */
    _renderHTML: function () {
        //this.config = $.extend(this.config, customConfig || {});

        var self = this,
            _config = self.config;

        // 先清空
        $(_config.container).html('');
        // 后台数据的字段
        var arrContent = [];
        //表头、列宽
        var arrTitle = [];
        var arrWidth = [];
        self._returnKeyValue(_config.title, arrTitle, arrContent, arrWidth);
        self._returnTitle(arrTitle, arrWidth);
        self._returnContent(arrContent);
        /*
         *奇偶行展示不一样

         * */
        var trs = document.getElementsByTagName('tr');
        for (var i = 1; i < trs.length; i++) {
        	 trs[0].style.backgroundColor = '#E2E2E2';
            if (i % 2 == 0) {
                trs[i].style.backgroundColor = _config.oddColor;
            } else {
                trs[i].style.backgroundColor = _config.evenColor;
            }
        }

        _config.callback && $.isFunction(_config.callback) && _config.callback();
        return this;
    },
    /*
     * 返回数组
     * @private _returnArrs
     * @return {arrs} 返回数组
     */
    _returnArrs: function (obj) {
        var arrs = [];
        for (var k in obj) {
            if (obj.hasOwnProperty(k)) {
                arrs.push(obj[k]);
            }
        }
        return arrs;
    },
    /**
     * 全选
     * */
    _itemSelect: function () {
        var self = this,
            _config = self.config;
        $("#containCheckAll").unbind().bind('click', function (e) {
            if ($(this).prop('checked') == true) {
                var code_Values = document.getElementsByTagName("input");
                for (i = 0; i < code_Values.length; i++) {
                    if (code_Values[i].type == "checkbox") {
                        code_Values[i].checked = true;
                    }
                }
            } else {
                var code_Values = document.getElementsByTagName("input");
                for (i = 0; i < code_Values.length; i++) {
                    if (code_Values[i].type == "checkbox") {
                        code_Values[i].checked = false;
                    }
                }
            }

        })
    },

    /**
     * 生成表头
     * @param arrTitle
     * @private
     */
    _returnTitle: function (arrTitle, arrWidth) {
        var self = this,
            _config = self.config;
        var tr = document.createElement("tr");
    
      
        for (var i = 0; i < arrTitle.length; i++) {
            var td = document.createElement('td');
            td.style.width = arrWidth[i] + 'px';
            $(td).html(arrTitle[i]);

            tr.appendChild(td);

        }
        if (_config.isCheck) {
            thead = $("<td><input type='checkbox' id='containCheckAll' /></td>")
            $(tr).prepend(thead);
        }
        if (_config.isRadio) {
            var radio = $('<td><input type="radio" class=""/></td>');
            $(tr).prepend(radio);
        }
        //$(_config.container).html(tr);
        $(_config.container)[0].appendChild(tr);

    },
    /**
     *生成数据内容
     * @param arrContent
     * @private
     */

    _returnContent: function (arrContent) {
        var self = this,
            _config = self.config;
        for (var i = 0; i < _config.JSONData.length; i++) {
            var tr = document.createElement("tr");
            for (var j = 0; j < arrContent.length; j++) {
                var td = document.createElement('td');
                $(td).html(_config.JSONData[i]['' + arrContent[j] + ''])
                tr.appendChild(td);
            }

            if (_config.isRadio) {
                var radio = $('<td><input type="radio" class=""/></td>');
                $(tr).prepend(radio);
            }
            if (_config.isCheck) {
                var radio = $('<td><input type="checkbox" class=""/></td>');
                $(tr).prepend(radio);
            }
            $(_config.container)[0].appendChild(tr);
        }
    },

    /**
     *  表头获取数据 函数
     * @param arrContent
     * @private
     */
    _returnKeyValue: function (obj, _arrTitle, _arrContent, _arrWidth) {
        $.each(obj, function (key, value) {
            _arrContent.push(key); // 后台数据的字段
            _arrTitle.push(value.value);  //表头
            _arrWidth.push(value.width);  //列宽

        });

    },
    /**
     * 动态分页
     * @param callback
     * @returns {TableData}
     * @private
     */
    _query: function (callback) {
        var _self = this,
            _config = _self.config;
        var start,
            end,
            html = '',
            str = '';
        _self._calculate();
        start = Math.max(1, _config.curIndex - parseInt(_config.buttonAmount / 2));

        end = Math.min(_config.totalPages, start + _config.buttonAmount - 1);

        str += '<div class="PagerView">';

        // 如果总页数大于1的话
        if (_config.totalPages > 1) {
            if (_config.curIndex != 1) {
                str += '<a href="javascript://1"><span>|<</span></a>';
                str += '<a href="javascript://' + (_config.curIndex - 1) + '"><span><<</span></a>';
            } else {
                str += '<span>|<</span>';
                str += '<span><<</span>';
            }
        }
        for (var i = start; i <= end; i += 1) {
            if (i == _config.curIndex) {
                str += '<span class="on">' + i + "</span>";
            } else {
                str += '<a href="javascript://' + i + '"><span>' + i + "</span></a>";
            }
        }
        if (_config.totalPages > 1) {
            if (_config.curIndex != _config.totalPages) {
                str += '<a href="javascript://' + (_config.curIndex + 1) + '"><span>>></span></a>';
                str += '<a href="javascript://' + _config.totalPages + '"><span>>|</span></a>';
            } else {
                str += '<span>>></span>';
                str += '<span>>|</span>';
            }
        }

        str += ' 一共' + _config.totalPages + '页, ' + _config.itemCount + '条记录 ';

        str += "</div>";

        // 把分页放到容器里面
        $(_config.page).html(str);

        if (_config.isAutoClick) {
            //点击某一项分页的时候
            var a_list = $(_config.page + ' a');

            for (var i = 0; i < a_list.length; i++) {
                a_list[i].onclick = function () {
                    var index = $(this).attr('href');
                    if (index != undefined && index != '') {
                        index = parseInt(index.replace('javascript://', ''));
                        _self.click(index, callback);
                    }
                };
            }
        }
        return this;
    },

    _getSelectValue: function (select) {
        var idx = select.selectedIndex,   //获取选中的索引
            option,
            value;
        if (idx > -1) {
            option = select.options[idx];  //获取选中的option元素
           
            value = option.attributes.value;
            return (value && value.specified) ? option.value : option.text;
        }
        return null;
    },

    /**
     * 点击生成上一页、下一页
     * @param index
     * @param callback
     * @returns {TableData}
     */
    click: function (index, callback) {
        var _self = this,
            _config = _self.config;
        _config.curIndex = index;
        _self._query(callback);
        //_self._renderHTML();
        callback && $.isFunction(callback) && callback(_config);
        return this;
    },

    /**
     * 在显示之前计算各种页码变量的值.
     */

    _calculate: function () {
        var _self = this,
            _config = _self.config;

        // 计算总页数 = parseInt(Math.ceil(记录总数/每页多少条数据),10)
        _config.totalPages = parseInt(Math.ceil(_config.itemCount / _config.perPage), 10);
        _self.curIndex = parseInt(_self.curIndex, 10);

        if (_self.curIndex > _config.totalPages) {
            _self.curIndex = _config.totalPages;
        }
    }
};

/**
 * 判断浏览器
 * @returns {*}
 */
function getBrowser() {
    var b;
    if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {
        b = "IE";
    } else if (navigator.userAgent.indexOf("Firefox") >= 0) {
        b = "Firefox";
    } else if (navigator.userAgent.indexOf("Opera") >= 0) {
        b = "Opera";
    } else {
        b = "Other";
    }
    return b;
}





