package com.vc.cloudbalance.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateHelper {
	/**
	 * 根据日期获得所在周的日期
	 * 
	 * @param mdate
	 * @return
	 */
	public static List<Date> dateToWeek(Date mdate, int index) {
		int b = mdate.getDay();
		Date fdate;
		List<Date> list = new ArrayList<Date>();
		Long fTime = mdate.getTime() - b * 24 * 3600000;
		for (int a = 1; a <= 7; a++) {
			fdate = new Date();
			fdate.setTime(fTime + (a * 24 * 3600000));
			Calendar cal = Calendar.getInstance();
			cal.setTime(fdate);
			if (index != 0)
				cal.add(Calendar.DAY_OF_YEAR, index * 7);
			fdate = cal.getTime();
			list.add(a - 1, fdate);
		}
		return list;
	}

	/**
	 * 根据日期获得所在月的日期
	 * 
	 * @param mdate
	 * @return
	 */
	public static List<Date> dateToMonth(Date mdate, int index) {
		List<Date> list = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		// 不加下面2行，就是取当前时间前一个月的第一天及最后一天
		cal.add(Calendar.MONTH, index);

		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDate = cal.getTime();
		int lastDay = lastDate.getDate();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDate = cal.getTime();
		int firstDay = firstDate.getDate();
		for (int i = firstDay; i <= lastDay; i++) {
			Calendar c = Calendar.getInstance();
			c.set(cal.get(cal.YEAR), cal.get(cal.MONTH), i);
			Date d = c.getTime();
			list.add(d);

		}

		return list;
	}

	/**
	 * 根据日期获得所在月的日期
	 * 
	 * @param mdate
	 * @return
	 */
	public static List<Date> dateToSeason(Date mdate, int index) {
		List<Date> list = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		if (month == 1 || month == 2 || month == 3) {
			cal.set(Calendar.MONTH, 0);
		} else if (month == 4 || month == 5 || month == 6) {
			cal.set(Calendar.MONTH, 3);
		} else if (month == 7 || month == 8 || month == 9) {
			cal.set(Calendar.MONTH, 6);
		} else if (month == 10 || month == 11 || month == 12) {
			cal.set(Calendar.MONTH, 9);
		}
		for (int j = 0; j < 2; j++) {
			cal.add(Calendar.MONTH, j);

			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			Date lastDate = cal.getTime();
			int lastDay = lastDate.getDate();
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Date firstDate = cal.getTime();
			int firstDay = firstDate.getDate();
			for (int i = firstDay; i <= lastDay; i++) {
				Calendar c = Calendar.getInstance();
				c.set(cal.get(cal.YEAR), cal.get(cal.MONTH), i);
				Date d = c.getTime();
				list.add(d);

			}
		}

		return list;
	}
	/**
	 * 根据日期获得所在月的日期
	 * 
	 * @param mdate
	 * @return
	 */
	public static List<Date> dateToYear(Date mdate, int index) {
		List<Date> list = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		// 不加下面2行，就是取当前时间前一个月的第一天及最后一天
		cal.add(Calendar.YEAR, index);
		
		for (int i = 0; i <= 11; i++) {
			Calendar c = Calendar.getInstance();
			c.set(cal.get(cal.YEAR), i, 1);
			int maxdaymonth=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			for(int j=1;j<=maxdaymonth;j++)
			{
				Calendar cj= Calendar.getInstance();
				c.set(cal.get(cal.YEAR), i, j);
				Date d = c.getTime();
				list.add(d);
			}
			

		}

		return list;
	}
	public static int getDayYear(int year, int month, int day) {
		int sum = 0;
		for (int i = 1; i < month; i++) {
			switch (i) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				sum += 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				sum += 30;
				break;
			case 2:
				if (((year % 4 == 0) & (year % 100 != 0)) | (year %

				400 == 0))
					sum += 29;
				else
					sum += 28;
			}
		}
		return sum = sum + day;
	}
}
