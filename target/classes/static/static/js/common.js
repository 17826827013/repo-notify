(function ($) {
    'use strict';

    /** 设置ajax 同步 **/
    $.ajaxSetup({async : false});
    /** 选择框 **/
    $.fn.combobox = function(arg) {
        if (arg == "disable") {
            var cb = $(this);
            cb.attr("disabled", "disabled");
            return cb;
        }
        if (arg == "enable") {
            var cb = $(this);
            cb.removeAttr("disabled");
            return cb;
        }
        if (arg == "values") {
            var vs = [];
            $(this).find("option").each(function (i, e) {
                vs.push($(e).attr("value"));
            });
            return vs;
        }
        var selections = function(g) {
            var vs = [];
            g.find("option").each(function (i, e) {
                var selected = $(e).attr("selected");
                if (typeof(selected) != "undefined" && selected == true) {
                    vs.push($(e).attr("value"));
                }
            });
            return vs;
        };
        var unselections = function(g) {
            var vs = [];
            g.find("option").each(function (i, e) {
                var selected = $(e).attr("selected");
                if (typeof(selected) == "undefined" || selected == false) {
                    vs.push($(e).attr("value"));
                }
            });
            return vs;
        };
        var removeOthers = function (g) {
            var o = unselections(g);
            if (o && o.length > 0) {
                for (var i in o) {
                    g.find("option[value='" + o[i] + "']").remove();
                }
            }
            return g;
        };
        if (arg == "getValues") {
            return selections($(this));
        }
        if (arg == "getValue") {
            var o = selections($(this));
            if (o && o.length > 0) {
                return o[0];
            }
            return null;
        }
        if (arg == "removeOthers") {
            return removeOthers($(this));
        }
        var select = function(g, v) {
            g.find("option[value='" + v + "']").attr("selected",true);
        };
        if (arg && arg['setValue']) {
            var v = arg['setValue'];
            if(!v) return;
            var g = $(this);
            select(g, v);
            return g;
        }
        if (arg && arg['url']) {
            var url = arg['url'];
            var vf = arg['value']==undefined?'value':arg['value'];
            var tf = arg['text']==undefined?'text':arg['text'];
            var $select = $(this);
            $.getJSON(url, function(data,status){
                if (status == 'success') {
                    $.each(data, function(i,item){
                        $select.append("<option value='" + item[vf] + "'>" + item[tf] + "</option>");
                    });
                } else {
                    layer.msg(status);
                }
            });
            return $select;
        }
    };
    /** 表单序列化 **/
    $.fn.serializeForm = function(){
        var o = {};
        var arr = $(this).serializeArray();
        $.each(arr, function(index) {
            if(this['value']){
                this['value'] = this['value'].trim();
                if(this['value'] != ''){
                    if (o[this['name']])
                        o[this['name']] = o[this['name']] + "," + this['value'].trim();
                    else
                        o[this['name']] = this['value'].trim();
                }
            }
        });
        return o;
    };
})(jQuery);
/**
 * 多表单序列化
 * @param formArr
 * @returns {{}}
 */
var serializeForms = function (forms){
    var o = {};
    $.each(forms, function(findex) {
        var arr = forms[findex].serializeArray();
        $.each(arr, function(index) {
            if(this['value']){
                this['value'] = this['value'].trim();
                if(this['value'] != ''){
                    if (o[this['name']])
                        o[this['name']] = o[this['name']] + "," + this['value'].trim();
                    else
                        o[this['name']] = this['value'].trim();
                }
            }
        });
    });
    return o;
};
/**
 * 时间戳转换
 * @param timestamp
 * @returns {*}
 */
var formatTimestamp = function (timestamp){
    if(timestamp == null || timestamp == ''){
        return "";
    }
    return formatDate(new Date(parseInt(timestamp)));
};
/**
 * 日期转换
 * @param date
 * @returns {string}
 */
var formatDate = function (date){
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    var dates = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();
    return year+"-"+(month<10?'0'+month:month)+"-"+(dates<10?'0'+dates:dates)+" "+(hour<10?'0'+hour:hour)+":"+(minute<10?'0'+minute:minute)+":"+(second<10?'0'+second:second);
};