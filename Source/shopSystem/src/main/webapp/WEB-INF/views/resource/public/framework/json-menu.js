(function($){



$.getJSON('../resource/public/framework/menu.json', function (data) {


    var builddata = function () {
        var source = [];
        var items = [];
        for (i = 0; i < data.length; i++) {
            var item = data[i];
            var label = item["name"];
            var parentid = item["parent_id"];
            var id = item["id"];
            var url = item["url"];

            if (items[parentid]) {
                var item = {parentid: parentid, label: label, url: url, item: item};

                if (!items[parentid].items) {
                    items[parentid].items = [];
                }
                items[parentid].items[items[parentid].items.length] = item;
                items[id] = item;
            }
            else {
                items[id] = {parentid: parentid, label: label, url: url, item: item};
                source[id] = items[id];
            }
        }
        return source;
    }


    var buildUL = function (parent, items) {

        $.each(items, function () {
            if (this.label) {
                var li = $("<li>" + "<a href='#' >" + this.label + "</a></li>");
                li.appendTo(parent);
                if (this.items && this.items.length > 0) {
                    li.children('a').addClass('js-menu')
                        .append("<i class='iconfont iconposition'>&#xe6a7;</i>");
                    var ul = $("<ul class='dropdown-submenu'></ul>");
                    ul.appendTo(li);
                    buildUL(ul, this.items);
                    li.children('a').unbind().bind('click', function () {
                        buildLeft(this);
                      /*  $(".json-menu").find('a').removeClass('active');
                        $(this).addClass('active');
                        $(this).siblings('ul.dropdown-submenu').slideToggle("fast")
                            .end()
                            .children('i').toggleClass('icon-transtion');
                        $(this).parent().siblings().children('ul.dropdown-submenu').slideUp()
                            .end();
                        $(this).parent().siblings().children('a').children('i').removeClass('icon-transtion');*/
               })
                } else {
                    li.addClass('lastNode');
                    li.children('a').attr('id', this.item.id);
                    li.children('a').data("item", this.item);
                    li.on("click",function (evt) {
                        evt = (evt) ? evt : ((window.event) ? window.event : null);
                        if (evt.stopPropagation) {
                            evt.stopPropagation()
                        } //取消opera和Safari冒泡行为;
                        buildTab(this)
                      /*  $(".json-menu").find('a').removeClass('active');
                        $(this).children('a').addClass('active');
                        var _test = test($(this).children('a').html());
                        var _item = $(this).children('a').data('item');
                        if (_test == 0) {
                            buildIframeHtml('content', _item);
                        } else {
                            tabLiChange(this)
                        }*/

                    });


                  /*  li.unbind.bind('click', function () {
                        console.log("ds")
                     /!*   $(".json-menu").find('a').removeClass('active');
                        $(this).children('a').addClass('active');
                        var _test = test($(this).children('a').html());
                        var _item = $(this).children('a').data('item');
                        if (_test == 0) {
                            buildIframeHtml('content', _item);
                        } else {
                            tabLiChange(this)
                        }*!/
                    })*/
                }
            }
        });
    };

    var source = builddata();
    var ul = $(".json-menu");
    /**
     * 创建左边导航树结构
     * ul容器  source  json数据树结构
     */
    buildUL(ul, source);


    /**
     * 左边点击动画
     * @param e
     */
    function buildLeft(e) {

        $(".json-menu").find('a').removeClass('active');
        $(e).addClass('active');
        $(e).siblings('ul.dropdown-submenu').slideToggle()
            .end()
            .children('i').toggleClass('icon-transtion');
        $(e).parent().siblings().children('ul.dropdown-submenu').slideUp()
            .end();
        $(e).parent().siblings().children('a').children('i').removeClass('icon-transtion');

    }

    /**
     * 顶部tab点击动画
     * @param e
     */
    function buildTab(e) {
        $(".json-menu").find('a').removeClass('active');
        $(this).children('a').addClass('active');
        var _test = test($(e).children('a').html());
        var _item = $(e).children('a').data('item');
        if (_test == 0) {
            buildIframeHtml('content', _item);
        } else {
            tabLiChange(e)
        }
    }



});
})(jQuery)



















