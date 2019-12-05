/**
 * 金额保留两位小数
 * @param money
 * @param inputId
 * @returns {String}
 */
function processMoney(money,inputId){
   inputId = $.trim(inputId);
   if(inputId){
	   money = $.trim($('#'+inputId).val());
   }else{
	   money = money || "0";
   }
   money = $.trim(money+"");
   if(isNaN(money)){
	   money = "0";
   }
   var ns = money.split(".");
   if(ns.length==1){
      money = money+".00";
   }else if(ns[1].length<2){
      money = money+"0";
   }
   return money;
}
/**
 * 将字符串date:20120221 转为：2012-02-21
 * @param date
 * @param inputId
 * @returns {String}
 */
function processDate(date,inputId){
	 inputId = $.trim(inputId);
     if(inputId){
    	 date = $.trim($('#'+inputId).val());
     }else{
    	 date = $.trim(date);
     }
	 var s = '';
	 if(date && date.length==8){
	     s = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
	 }
	 return s;
 }
/**
 * 将字符串datetime:20120221101010 转为：2012-02-21 10:10:10
 * @param datetime
 * @param inputId
 * @returns {String}
 */
function processDatetime(datetime,inputId){
	 inputId = $.trim(inputId);
     if(inputId){
    	 datetime = $.trim($('#'+inputId).val());
     }else{
    	 datetime = $.trim(datetime);
     }
	 var s = '';
	 if(datetime && datetime.length==14){
		 s = datetime.substring(0,4)+"-"+datetime.substring(4,6)+"-"+datetime.substring(6,8)+" "
	     +datetime.substring(8,10)+":"+datetime.substring(10,12)+":"+datetime.substring(12,14);
	 }
	 return s;
}
/**
 * 将字符串datetime:1408089762503毫秒 转为：2012-02-21 10:10:10
 * @param datetime
 * @param inputId
 * @returns {String}
 */
function processLongtimeToDatetime(datetime,inputId){
	 inputId = $.trim(inputId);
     if(inputId){
    	 datetime = $.trim($('#'+inputId).val());
     }else{
    	 datetime = $.trim(datetime);
     }
    var d = new Date();
    d.setTime(datetime);
    if(datetime){
        var year = d.getFullYear();
 	    var month = (d.getMonth()+1)>9?(d.getMonth()+1):("0"+(d.getMonth()+1));
        return year+"-"+month+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
    }else{
         return "";
    }
}
/**
 * 数字：自动补全两位小数
 * @param obj
 * required：该值（obj对应的值）是否必填
 * fieldLabel：obj对应的标签名
 * @returns {Boolean}
 */
function setDoubleValue(obj,required,fieldLabel){
	var money = $.trim($(obj).val());
	var ms = null;
	var boo = testDecimal(null,required,fieldLabel,false,money);
	if(!boo){
		$(obj).val('');
		return false;
	}
	money = money+"";
	ms = money.split(".");
	if(ms.length==1){
		$(obj).val(money+".00");
	}else if(ms.length==2){
		if(ms[1].length==1){
			$(obj).val(money+"0");
		}
	}
}
/**
 * 获取两个格式为：2006-06-06的相隔天数
 * @param startDate
 * @param endDate
 */
function getDays(startDate,endDate){
	var sArr = startDate.split("-");
	var eArr = endDate.split("-");
	var sRDate = new Date(sArr[0], sArr[1], sArr[2]);
	var eRDate = new Date(eArr[0], eArr[1], eArr[2]);
	var result = (eRDate-sRDate)/(24*60*60*1000);
	return result;
}
function CurentTime()
{
    var now = new Date();

    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日

    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分

    var clock = year + "-";

    if(month < 10)
        clock += "0";

    clock += month + "-";

    if(day < 10)
        clock += "0";

    clock += day + "";

//    if(hh < 10)
//        clock += "0";
//
//    clock += hh + ":";
//    if (mm < 10) clock += '0';
//    clock += mm;
    return(clock);
}

//银行编号转成中文
function bankCodeRefund(bankCode) {
    bankCode = $.trim(bankCode);
    var s = '';
    if (bankCode == 'ICBC') {
        s = '工商银行';
    } else if (bankCode == 'ABC') {
        s = '农业银行';
    } else if (bankCode == 'BOC') {
        s = '中国银行';
    } else if (bankCode == 'BOCSH') {
        s = '中国银行';
    } else if (bankCode == 'CCB') {
        s = '建设银行';
    } else if (bankCode == 'CMB') {
        s = '招商银行';
    } else if (bankCode == 'SPDB') {
        s = '浦发银行';
    } else if (bankCode == 'GDB') {
        s = '广发银行';
    } else if (bankCode == 'BOCOM') {
        s = '交通银行';
    } else if (bankCode == 'CNCB') {
        s = '中信银行';
    } else if (bankCode == 'CMBC') {
        s = '民生银行';
    } else if (bankCode == 'CIB') {
        s = '兴业银行';
    } else if (bankCode == 'CEB') {
        s = '光大银行';
    } else if (bankCode == 'HXB') {
        s = '华夏银行';
    } else if (bankCode == 'BOS') {
        s = '上海银行';
    } else if (bankCode == 'SRCB') {
        s = '上海农商';
    } else if (bankCode == 'PSBC') {
        s = '邮政储蓄';
    } else if (bankCode == 'BCCB') {
        s = '北京银行';
    } else if (bankCode == 'BRCB') {
        s = '北京农商';
    } else if (bankCode == 'PAB') {
        s = '平安银行';
    }
    return s;
}