package cn.demo.repo.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.time.DateUtils.parseDate;

/**
 * 时间操作
 */
public class DateUtil {
	public static SimpleDateFormat calednarFormat = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * yyyyMMdd
	 */
	public static SimpleDateFormat stringFormat = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat stringFormatyyyyMM = new SimpleDateFormat("yyyyMM");
	public static SimpleDateFormat detailCalendarFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
	public static SimpleDateFormat detailStringFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat hourMinSsFormat = new SimpleDateFormat("HHmmss");
	public static SimpleDateFormat millisecondStringFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	/**
	 * 获取 当前年、半年、季度、月、日、小时 开始结束时间
	 */

	private final static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
	public static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	/**
	 * 获取当前日期  yyyyMMdd
	 * @return
     */
	public static  String currentDate4yyyyMMdd(){
		return  stringFormat.format(new Date());
	}
	/**
	 * 获取当前日期  currentDate4yyyyMMddhhmmssSSS
	 * @return
     */
	public static  String currentDate4yyyyMMddhhmmssSSS(){
		return  millisecondStringFormat.format(new Date());
	}

	/**
	 * 获取当前日期  yyyyMMddhhminss
	 * @return
	 */
	public static  String currentDate4yyyyMMddHHmmss(){
		return  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	public static boolean checkMonthFirstDay(Date date) {
		
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(new Date());
		calDateA.set(Calendar.MONTH, calDateA.get(Calendar.MONTH));

		// 日期设置为1日
		calDateA.set(Calendar.DAY_OF_MONTH, 1);
	
		Date dateA = calDateA.getTime();
		
		return isTheSameDay(date,dateA);
	}
	
	public static boolean checkQuarterFirstDay(Date date) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	/**
	 * 判断时间是否为同一天（不判断时分秒）
	 * @author wangzx
	 * @param dateA
	 * @param dateB
	 * @return
	 */
	public static boolean isTheSameDay(Date dateA,Date dateB){
		Calendar calDateA = Calendar.getInstance();
		
		Calendar calDateB = Calendar.getInstance();
		
		calDateA.setTime(dateA);
		
	    calDateB.setTime(dateB);
	    
		return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
	            && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
	            &&  calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 判断字符串日期格式是否正确yyyyMMdd
	 * @author dingzl
	 * @param str
	 * @return
	 */
	public static boolean isValidDate(String str) {
		try {
			if (str != null && !str.equals("")) {
				if (str.length() == 8) {
					// 闰年标志
					boolean isLeapYear = false;
					String year = str.substring(0, 4);
					String month = str.substring(4, 6);
					String day = str.substring(6, 8);
					int vYear = Integer.parseInt(year);
					// 判断年份是否合法
					if (vYear < 1900 || vYear > 2200) {
						return false;
					}
					// 判断是否为闰年
					if (vYear % 4 == 0 && vYear % 100 != 0 || vYear % 400 == 0) {
						isLeapYear = true;
					}
					// 判断月份
					// 1.判断月份
					if (month.startsWith("0")) {
						String units4Month = month.substring(1, 2);
						int vUnits4Month = Integer.parseInt(units4Month);
						if (vUnits4Month == 0) {
							return false;
						}
						if (vUnits4Month == 2) {
							// 获取2月的天数
							int vDays4February = Integer.parseInt(day);
							if (isLeapYear) {
								if (vDays4February > 29)
									return false;
							} else {
								if (vDays4February > 28)
									return false;
							}

						}
					} else {
						// 2.判断非0打头的月份是否合法
						int vMonth = Integer.parseInt(month);
						if (vMonth != 10 && vMonth != 11 && vMonth != 12) {
							return false;
						}
					}
					// 判断日期
					// 1.判断日期
					if (day.startsWith("0")) {
						String units4Day = day.substring(1, 2);
						int vUnits4Day = Integer.parseInt(units4Day);
						if (vUnits4Day == 0) {
							return false;
						}
					} else {
						// 2.判断非0打头的日期是否合法
						int vDay = Integer.parseInt(day);
						if (vDay < 10 || vDay > 31) {
							return false;
						}
					}
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	/** 
	* @Title: formatDate 
	* @Description: 以  yyyy-MM-dd HH:mm:ss格式打印日期
	* @param date
	* @return 
	* @return String
	* @throws 
	*/
	public static String formatDate(Date date,SimpleDateFormat dateFormat){
		return dateFormat.format(date);
	}
	
	
	/**
	 * 将字符串格式化成时间
	 * @author wangzx
	 * @param dateVal
	 * @param dateFormat
	 * @return
	 */
	public static Date stringToDate(String dateVal, SimpleDateFormat dateFormat) {
		try {
			Date date = dateFormat.parse(dateVal);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}
	/**
	 * 日期字符串格式化成另一种格式
	 * @author wangzx
	 * @param formFormat
	 * @param toFormat
	 * @param dateVal
	 * @return
	 */
	public static String convertDate(SimpleDateFormat formFormat,SimpleDateFormat toFormat, String dateVal) {
		if (StringUtils.isBlank(dateVal)) {
			return "";
		}
		try {
			Date date = formFormat.parse(dateVal);
			return toFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			// System.out.println(e);
		}
		return "";
	}

	/**
	 * 根据结算区间信息获取sql查询时间点 指定2014-8-11，则查询的时间点为2014-8-10 23:00:00
	 * 
	 * @param time
	 *            指定区间
	 * @return
	 */
	public static String getBillTimeStamp(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		// 日期退后一天
		calendar.set(Calendar.DAY_OF_YEAR,
				calendar.get(Calendar.DAY_OF_YEAR) - 1);

		// 时间点为23:00:00
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date billTime = calendar.getTime();
		return detailStringFormat.format(billTime);
	}


	/**
	 * 验证时间是否在23点以后
	 * @param time 时间格式为  yyyyMMddHHmmss
	 * @return 是23点以后  true 否则 false
     */
	public static boolean isAfter23(String time) {
		boolean result = false;
		Date date_s = null;
		try {
			date_s = detailStringFormat.parse(time);
		}catch (Exception e){
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date_s);
		// 时间点为23:00:00
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Calendar calendar_e = Calendar.getInstance();
		calendar_e.setTime(date_s);
		// 时间点为23:59:59
		calendar_e.set(Calendar.HOUR_OF_DAY, 23);
		calendar_e.set(Calendar.MINUTE, 59);
		calendar_e.set(Calendar.SECOND, 59);

		Date billTime_s = calendar.getTime();
		Date billTime_e = calendar_e.getTime();
		if((date_s.equals(billTime_s) || date_s.after(billTime_s)) && (date_s.before(billTime_e)||date_s.equals(billTime_e))){
			result = true;
		}

		return result;

	}

	/**
	 * 获取指定日期下一天的yyyymmdd
	 * @param time
	 * @return
     */
	public static String getLastDate4yyyyMMdd(String time){
		Date d_source = stringToDate(time,detailStringFormat);
		String s =  getDefinedTime(d_source,1,0,0,0).substring(0,8);
		return s;
	}
	/**
	 * 获取指定日期下一天的yyyymmdd
	 * @param time
	 * @return
     */
	public static String getLastDate(String time){
		Date d_source = stringToDate(time,stringFormat);
		String s =  getDefinedTime(d_source,1,0,0,0).substring(0,8);
		return s;
	}
    /**
     * 获取指定区间的时间
     * @param date
     * @return
     */

    public static String getSectionTime(Date date, int section) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //获取小时
        if (section != 0) {
            int hourse = calendar.get(Calendar.HOUR_OF_DAY);
            calendar.set(Calendar.HOUR_OF_DAY, hourse + section);
        }

        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return detailStringFormat.format(calendar.getTime());
    }

    /**
	 * 获取自定义时间
	 * 
	 * @author wangzx
	 * @param time
	 *            当前时间
	 * @param dateCount
	 *            如果未负数向前推相应天数，反之亦然
	 * @param hour
	 *            指定某个小时
	 * @param min
	 *            指定某分
	 * @param s
	 *            指定某一秒
	 * @return
	 */
	public static String getDefinedTime(Date time, int dateCount, int hour,
			int min, int s) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		// 日期退后一天
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)
				+ dateCount);

		// 时间点为23:00:00
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, s);

		Date billTime = calendar.getTime();
		return detailStringFormat.format(billTime);
	}

	/**
	 * 
	 * @author wangzx
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	public static List<Date> getLostDays(Date start, Date end) {
		List<Date> days = new ArrayList<Date>();
		if (start == null || end == null) {
			return days;
		}

		Date ajustStart = getDateMorningDate(start);
		Date ajustEnd = getDateMorningDate(end);

		// 递增
		Date tempDate = DateUtil.getNextDay(ajustStart, 1);
		while (tempDate.before(ajustEnd)) {
			days.add(tempDate);
			tempDate = DateUtil.getNextDay(tempDate, 1);
		}

		return days;
	}
	
	/**
	 * 获取时间区间列表
	 * @author wangzx
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	public static List<Date> getDays(Date start, Date end) {
		List<Date> days = new ArrayList<Date>();
		if (start == null || end == null) {
			return days;
		}

		Date ajustStart = getDateMorningDate(start);
		Date ajustEnd = getDateMorningDate(end);

		ajustEnd = DateUtil.getNextDay(ajustEnd, 1);
		// 递增
		Date tempDate = ajustStart;
		while (tempDate.before(ajustEnd)) {
			days.add(tempDate);
			tempDate = DateUtil.getNextDay(tempDate, 1);
		}

		return days;
	}
	


	/**
	 * @param date
	 * @return
	 */
	public static Date getNextWorkDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);

		// 星期日到星期四都是明天
		if (dayInWeek >= 1 && dayInWeek <= 5) {
			return getNextDay(date, 1);
		} else if (dayInWeek == 6) {
			// 周五
			return getNextDay(date, 3);
		} else {
			// 周六
			return getNextDay(date, 2);
		}
	}

	/**
	 * 时间沿时间轴向前推进若干天
	 * 
	 * @param day
	 *            当前时间点
	 * @param dis
	 *            向前推进天数
	 * @return 推进结果日期
	 */
	public static Date getNextDay(Date day, int dis) {
		Calendar cnd = Calendar.getInstance();
		cnd.setTime(day);
		cnd.set(Calendar.DAY_OF_YEAR, cnd.get(Calendar.DAY_OF_YEAR) + dis);
		return cnd.getTime();
	}
 
	/**
	 * 得到一个时间延后或前移几天的yyyyMMdd时间,nowdate为时间,delay为前移或后延的天数
	 */
	public static String fomatDateDefined(Date nowdate, int delay) {
		try {
			String mdate = "";
			Date date = new Date();
			long myTime = (nowdate.getTime() / 1000) + (delay * 24 * 60 * 60);
			date.setTime(myTime * 1000);
			mdate = stringFormat.format(date);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取下个星期一的日期
	 * 
	 * @param day
	 *            当前日期对象
	 * @return 下星期一时间对象
	 */
	public static Date getNextMonday(Date day) {
		Calendar cnd = Calendar.getInstance();
		cnd.setTime(day);

		int dayInWeek = cnd.get(Calendar.DAY_OF_WEEK) - 1;
		int dis = 1;
		if (dayInWeek > 0) {
			dis = 8 - dayInWeek;
		}
		return getNextDay(day, dis);
	}

	/**
	 * 获取下个月初的日期
	 * 
	 * @param day
	 *            当前日期
	 * @return 下个月月初日期
	 */
	public static Date getNextMonthStart(Date day) {
		Calendar cnd = Calendar.getInstance();
		cnd.setTime(day);

		// 月份向前推进一个值
		cnd.set(Calendar.MONTH, cnd.get(Calendar.MONTH) + 1);

		// 日期设置为1日
		cnd.set(Calendar.DAY_OF_MONTH, 1);

		return cnd.getTime();
	}

	/**
	 * 使用详细时间格式对象
	 * 
	 * @return
	 */
	public static String detailCalendarFormatter(Date date) {
		return detailCalendarFormat.format(date);
	}

	/**
	 * 日历格式转换为字符串格式
	 */
	public static String caledarToString(Date date) {
		return stringFormat.format(date);
	}

	/**
	 * 字符串格式转换为日历格式
	 */
	public static String stringToCalendar(String val) {
		if (StringUtils.isBlank(val)) {
			return "";
		}
		try {
			Date date = stringFormat.parse(val);
			return calednarFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static Date getDateMorningDate(Date date) {
		Calendar cnd = Calendar.getInstance();
		cnd.setTime(date);
		// 设置为00:00:00
		cnd.set(Calendar.HOUR_OF_DAY, 0);
		cnd.set(Calendar.MINUTE, 0);
		cnd.set(Calendar.SECOND, 0);
		cnd.set(Calendar.MILLISECOND, 0);
		return cnd.getTime();
	}
	public static String getDateMorning(Date date) {
		return detailStringFormat.format(getDateMorningDate(date));
	}

	public static String getDateEnd(Date date) {
		Calendar cnd = Calendar.getInstance();
		cnd.setTime(date);

		// 设置为23:59:59
		cnd.set(Calendar.HOUR_OF_DAY, 23);
		cnd.set(Calendar.MINUTE, 59);
		cnd.set(Calendar.SECOND, 59);
		return detailStringFormat.format(cnd.getTime());
	}
	/**
	 * 获取比当前时间早的时间
	 * 
	 * @author wangzx
	 * @param seconds
	 *            比当前时间早多少秒
	 * @return
	 */
	public static Date beforeNowDate(int seconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(c.SECOND, seconds);
		Date temp_date = c.getTime();
		return temp_date;
	}


	/**
	 * 获得本周的第一天，周一
	 * 
	 * @return
	 */
	public Date getCurrentWeekDayStartTime() {
		Calendar c = Calendar.getInstance();
		try {
			int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
			c.add(Calendar.DATE, -weekday);
			c.setTime(longSdf.parse(calednarFormat.format(c.getTime()) + " 00:00:00"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c.getTime();
	}

	/**
	 * 获得本周的最后一天，周日
	 * 
	 * @return
	 */
	public Date getCurrentWeekDayEndTime() {
		Calendar c = Calendar.getInstance();
		try {
			int weekday = c.get(Calendar.DAY_OF_WEEK);
			c.add(Calendar.DATE, 8 - weekday);
			c.setTime(longSdf.parse(calednarFormat.format(c.getTime()) + " 23:59:59"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c.getTime();
	}

	/**
	 * 获得本天的开始时间，即2012-01-01 00:00:00
	 * 
	 * @return
	 */
	public Date getCurrentDayStartTime() {
		Date now = new Date();
		try {
			now = calednarFormat.parse(calednarFormat.format(now));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 获得本天的结束时间，即2012-01-01 23:59:59
	 * 
	 * @return
	 */
	public static Date getCurrentDayEndTime() {
		Date now = new Date();
		try {
			now = longSdf.parse(calednarFormat.format(now) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}
	
	
	/**
	 * 获得上一天的开始时间，即2012-01-01 00:00:00
	 * 
	 * @return
	 */
	public static Date getYesterdayStartTime(Date date) {
		Date yesterday = null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, c.get(Calendar.DATE)-1);
		
		try {
			c.setTime(date);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-1);
			yesterday = c.getTime();
			yesterday = longSdf.parse(calednarFormat.format(yesterday) + " 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return yesterday;
	}

	/**
	 * 获得上一天的结束时间，即2012-01-01 23:59:59
	 * 
	 * @return
	 */
	public static Date getYesterdayEndTime(Date date) {
		Date yesterday = null;
		Calendar c = Calendar.getInstance();
		
		try {
			c.setTime(date);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-1);
			yesterday = c.getTime();
			yesterday = longSdf.parse(calednarFormat.format(yesterday) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return yesterday;
	}

	

	/**
	 * 获得本小时的开始时间，即2012-01-01 23:59:59
	 * 
	 * @return
	 */
	public Date getCurrentHourStartTime() {
		Date now = new Date();
		try {
			now = longHourSdf.parse(longHourSdf.format(now));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 获得本小时的结束时间，即2012-01-01 23:59:59
	 * 
	 * @return
	 */
	public Date getCurrentHourEndTime() {
		Date now = new Date();
		try {
			now = longSdf.parse(longHourSdf.format(now) + ":59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 获得本月的开始时间，即2012-01-01 00:00:00
	 * 
	 * @return
	 */
	public Date getCurrentMonthStartTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			now = calednarFormat.parse(calednarFormat.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前月的结束时间，即2012-01-31 23:59:59
	 * 
	 * @return
	 */
	public Date getCurrentMonthEndTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			now = longSdf.parse(calednarFormat.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}
	
	
	/**
	 * 获得传入时间的上个月开始时间，即2012-01-01 00:00:00
	 * 
	 * @return
	 */
	public static Date getLastMonthStartTime(Date date) {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.setTime(date);
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
			c.set(Calendar.DATE, 1);
			now = calednarFormat.parse(calednarFormat.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	
	
	/**
	 * 获得传入时间上个月的结束时间，即2012-01-31 23:59:59
	 * 
	 * @return
	 */
	public static Date getLastMonthEndTime(Date date) {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.setTime(date);
			c.set(Calendar.DATE, 1);
			c.add(Calendar.DATE, -1);
			now = longSdf.parse(calednarFormat.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}
	

	/**
	 * 当前年的开始时间，即2012-01-01 00:00:00
	 * 
	 * @return
	 */
	public Date getCurrentYearStartTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DATE, 1);
			now = calednarFormat.parse(calednarFormat.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前年的结束时间，即2012-12-31 23:59:59
	 * 
	 * @return
	 */
	public Date getCurrentYearEndTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DATE, 31);
			now = longSdf.parse(calednarFormat.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前季度的开始时间，即2012-01-1 00:00:00
	 * 
	 * @return
	 */
	public Date getCurrentQuarterStartTime() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				c.set(Calendar.MONTH, 0);
			else if (currentMonth >= 4 && currentMonth <= 6)
				c.set(Calendar.MONTH, 3);
			else if (currentMonth >= 7 && currentMonth <= 9)
				c.set(Calendar.MONTH, 4);
			else if (currentMonth >= 10 && currentMonth <= 12)
				c.set(Calendar.MONTH, 9);
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(calednarFormat.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}
	
	
	/**
	 * 获取前一季度的开始时间，即2012-01-1 00:00:00
	 * 
	 * @return
	 */
	public static Date getLastQuarterStartTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, c.get(Calendar.MONTH) -3);
		
		int currentMonth = c.get(Calendar.MONTH)+1;
		
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				c.set(Calendar.MONTH, 0);
			else if (currentMonth >= 4 && currentMonth <= 6)
				c.set(Calendar.MONTH, 3);
			else if (currentMonth >= 7 && currentMonth <= 9)
				c.set(Calendar.MONTH, 4);
			else if (currentMonth >= 10 && currentMonth <= 12)
				c.set(Calendar.MONTH, 9);
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(calednarFormat.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}
	

	

	/**
	 * 当前季度的结束时间，即2012-03-31 23:59:59
	 * 
	 * @return
	 */
	public Date getCurrentQuarterEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH,8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(calednarFormat.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
	
	
	/**
	 * 当前季度的结束时间，即2012-03-31 23:59:59
	 * 
	 * @return
	 */
	public static Date getLastQuarterEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
		c.add(Calendar.MONTH, c.get(Calendar.MONTH) -3);//往前推3个月
		
        int currentMonth = c.get(Calendar.MONTH) + 1;
        
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH,8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(calednarFormat.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
	
	
	
	
	/**
	 * 获取前/后半年的开始时间
	 * 
	 * @return
	 */
	public Date getHalfYearStartTime() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 0);
			} else if (currentMonth >= 7 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 6);
			}
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(calednarFormat.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;

	}
	/**
	 * 获取前/后半年的结束时间
	 * 
	 * @return
	 */
	public Date getHalfYearEndTime() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 5);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 7 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 11);
				c.set(Calendar.DATE, 31);
			}
			now = longSdf.parse(calednarFormat.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 *
	 * */
	public static int daysBetween(String date1,String date2) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

		Date d1 = sdf.parse(date1);
		Date d2 = sdf.parse(date2);

		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(d2);
		long time2 = cal.getTimeInMillis();
		long between_days=(time2-time1)/(1000*3600*24);

		return Integer.parseInt(String.valueOf(between_days));
	}



	public static void main(String[] args) throws ParseException {
		System.out.println(daysBetween("20191208","20180715"));
    }
}
