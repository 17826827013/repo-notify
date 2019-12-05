package cn.demo.repo.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * ParamUtil
 *
 * @auther dingzl
 * @create 2018-05-07 12:31
 */
public class ParamUtil {
    private static final Log log = LogFactory.getLog(ParamUtil.class);
    public static String getParam(String name, HttpServletRequest req) {
        String val = req.getParameter(name);
        if (StringUtils.isBlank(val)) {
            return "";
        }
        return val.trim();
    }

    /**
     * 处理导出Excel文件名 fileName 文件名
     */
    public static String fileNameEncoder(String fileName,HttpServletRequest req) throws UnsupportedEncodingException {

        String userAgent = req.getHeader("USER-AGENT");
        log.info("userAgent:" + userAgent);

        if(StringUtils.contains(userAgent, "Firefox")){
            fileName = new String(fileName.getBytes(), "ISO8859-1");

            log.info("firefox转码方式transformethod:" + "new String(ISO8859-1)");
        } else if (StringUtils.containsIgnoreCase(userAgent, "MSIE") || StringUtils.containsIgnoreCase(userAgent, ".NET") || StringUtils.containsIgnoreCase(userAgent, "Trident/7.0") &&StringUtils.containsIgnoreCase(userAgent, "rv:11.0")) {// IE浏览器
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            log.info("IE转码方式transformethod:" + "encode(UTF-8)");
        } else {// 其他浏览器
            fileName = new String(fileName.getBytes(), "ISO8859-1");
            log.info("其他浏览器other转码方式transformethod:" + "new String(ISO8859-1)");
        }

        return fileName;
    }

    public static String getParamUTF8(String name, HttpServletRequest req){
        String val = getParam(name, req);
        if(StringUtils.isBlank(val)){
            return val;
        }
        return toUTF8(val);
    }
    public static String toUTF8(String val){
        try {
            return new String(val.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
    /**
     * 获取boolean参数
     *
     * @param name
     *            参数名称
     * @param req
     *            Http请求对象
     * @return
     */
    public static boolean getBooleanParam(String name, HttpServletRequest req) {
        String val = getParam(name, req);
        if ("true".equalsIgnoreCase(val.trim())) {
            return true;
        } else if ("1".equals(val.trim())) {
            return true;
        }
        return false;
    }

    public static long getLongParam(String name, HttpServletRequest req, long errorVal) {
        String val = getParam(name, req);
        long longVal = errorVal;
        if (StringUtils.isBlank(val)) {
            return longVal;
        }
        try {
            if (!StringUtils.isBlank(val)) {
                longVal = Long.parseLong(val);
            }
        } catch (NumberFormatException e) {
//            e.printStackTrace();
        }
        return longVal;
    }

    public static int getIntParam(String name, HttpServletRequest request, int defaultVal) {
        String parmVval = getParam(name, request);
        int val = defaultVal;
        if (StringUtils.isBlank(parmVval)) {
            return val;
        }

        try {
            val = Integer.parseInt(parmVval);
        } catch (NumberFormatException e) {
//            e.printStackTrace();
        }
        return val == 0 ? defaultVal : val;
    }
}
