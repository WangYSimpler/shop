/**
 * @author JiangR 2017/4/20.
 * 选项卡
 */


/**
 *  判断tab是否存在
 * @param e
 * @returns {number}
 */

function test(e) {
    var thText = e;
    var _test = 0;
    var btnLength = $(".index-right-nav").children().children('li').length;
    if (btnLength != 0) {
        for (var i = 0; i < btnLength; i++) {
            if ($($(".index-right-nav").children().children('li')[i]).children('a').text() == thText) {
                _test++;
            }
        }
    }
    return _test;
}


/**
 *创建iframe/tab
 * @param _content   iframe容器
 * @param _item     tab对应的json
 */
function buildIframeHtml(_content, _item) {

    var tabLi = document.createElement('li');
    tabLi.innerHTML = "<a href='#' name='" + _item.name + "' id='" + _item.id + "'>" + _item.name + "</a><i class='iconfont' style='margin-left: 10px'>&#xe69a;</i>";

    //var li = "<a href='#' name='" + _item.name + "'>" + _item.name + "</a>";
    var _iframe = "<iframe name='tab_item_frame_" + _item.id + "' width='100%' height='100%' id='tab_item_frame_" + _item.id + "' src='" + _item.url + "' frameBorder='0' scrolling='no' allowTransparency='true' style='display: none'  onload='changeFrameHeight(this.id)' />";

    $(".index-right-nav ul").append(tabLi);
    $(tabLi).addClass('cur')
        .siblings('li').removeClass('cur');
    $("#" + _content).append(_iframe);
    $("#tab_item_frame_" + _item.id).show()
        .siblings('iframe').hide();
   /* $("#tab_item_frame_" + _item.id).addClass('iframeShow')
        .siblings('iframe').removeClass('iframeShow');*/

    /**
     * 选项卡绑定事件
     */
    var _tabLI = $("#index-right-nav ul").find('li');
    _tabLI.each(function () {
        $(this).unbind().bind('click', function () {
            tabLiChange(this)
        })

    });


    /**
     *
     * 选项卡关闭事件
     * @param evt
     */
    tabLi.getElementsByTagName('i')[0].onclick = function (evt) {
        evt = (evt) ? evt : ((window.event) ? window.event : null);
        if (evt.stopPropagation) {
            evt.stopPropagation()
        } //取消opera和Safari冒泡行为;
        $("#tab_item_frame_" + _item.id).hide(); //隐藏当前iframe
        this.parentNode.parentNode.removeChild(tabLi);//删除当前标签

        var tabliLen = $("#index-right-nav ul").find('li');

        //设置如果标签获取焦点时关闭，让最后一个标签得到焦点
        if ($($(this).parent()).hasClass('cur')) {
            if (tabliLen.length - 1 > 0) {
                tabliLen.removeClass('cur');
                $(tabliLen[tabliLen.length - 1]).addClass('cur');
                var _id = $(tabliLen[tabliLen.length - 1]).children('a').attr('id');
                $("#tab_item_frame_" + _id).show();
                $("#tab_item_frame_" + _id).siblings('iframe').hide();



            }
            else {
                $("#00").parent().addClass('cur');
                $("#tab_item_frame_00").show()
            }
        }
    };
}
/**
 * 选项卡切换
 * @param e
 */
function tabLiChange(e) {
    var $id = $($(e).children('a')).attr('id');
    $("#index-right-nav").find('a#' + $id).parent().addClass('cur')
        .siblings('li').removeClass('cur');
    $("#tab_item_frame_" + $id).show()
        .siblings('iframe').hide();
}

function Tab(option) {
    this.opts = $(option.opts);
    this.tabTag = this.opts.find('#index-right-nav li');
    this.hidden = this.opts.find('.hidden');
    this.init();
}


Tab.prototype = {
    init: function () {
        var that = this;
        this.tabTag.each(function (i) {
            $(this).hover(function () {
                that.tabTag.removeClass('cur');
                $(this).addClass('cur');
                that.hidden.eq(i).show().siblings().hide();
            })
        })
    }
};

/*

 $(function () {
 new Tab({'opts': $('#tab')});
 })


 */


/*
 ;(function($){
 $.fn.tab=function(options){
 var defaluts={
 event : 'click',
 callback : null
 };
 var opts= $.extend(defaluts,options);
 return






 }

 /*
 /!* $.fn.extend(
 {
 Tabs:function(options){
 // 处理参数
 options = $.extend({
 event : 'click',
 timeout : 0,
 auto : 0,
 callback : null
 }, options);

 var self = $(this),
 tabBox = self.children( 'div.tab_box' ).children( 'div' ),
 menu = self.children( 'ul.tab_menu' ),
 items = menu.find( 'li' ),
 timer;

 var tabHandle = function( elem ){
 elem.siblings( 'li' )
 .removeClass( 'current' )
 .end()
 .addClass( 'current' );

 tabBox.siblings( 'div' )
 .addClass( 'hide' )
 .end()
 .eq( elem.index() )
 .removeClass( 'hide' );
 },

 delay = function( elem, time ){
 time ? setTimeout(function(){ tabHandle( elem ); }, time) : tabHandle( elem );
 },

 start = function(){
 if( !options.auto ) return;
 timer = setInterval( autoRun, options.auto );
 },

 autoRun = function(){
 var current = menu.find( 'li.current' ),
 firstItem = items.eq(0),
 len = items.length,
 index = current.index() + 1,
 item = index === len ? firstItem : current.next( 'li' ),
 i = index === len ? 0 : index;

 current.removeClass( 'current' );
 item.addClass( 'current' );

 tabBox.siblings( 'div' )
 .addClass( 'hide' )
 .end()
 .eq(i)
 .removeClass( 'hide' );
 };

 items.bind( options.event, function(){
 delay( $(this), options.timeout );
 if( options.callback ){
 options.callback( self );
 }
 });

 if( options.auto ){
 start();
 self.hover(function(){
 clearInterval( timer );
 timer = undefined;
 },function(){
 start();
 });
 }

 return this;
 }
 });*!/
 })(jQuery);*!/
 */
