package org.bfsip.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bfsip.common.constants.StringPool;

/** 
 * 字符串验证工具类
 *
 * <pre> 
 * project: bfsip-common
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月5日-下午9:02:52
 * rights: eddy
 * </pre>
 */
public class StringValidator {
	
	private StringValidator(){}
	
	/**
	 * 判读输入字符是否是数字
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNumberic(String s) {
		boolean rtn = valid("^[-+]{0,1}\\d*\\.{0,1}\\d+$", s);
		if (rtn)
			return true;
		return valid("^0[x|X][\\da-eA-E]+$", s);
	}

	/**
	 * 是否是整数。
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isInteger(String s) {
		return valid("^[-+]{0,1}\\d*$", s);
	}

	/**
	 * 是否是电子邮箱
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmail(String s) {
		return valid("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$", s);
	}

	/**
	 * 手机号码（13、15、18开头的11位数字手机号）
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isMobile(String s) {
		return valid("^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))\\d{8})$", s);
	}

	/**
	 * 电话号码
	 * 
	 * @param
	 * @return
	 */
	public static boolean isPhone(String s) {
		return valid("^((\\(0[0-9]{2,3}\\))|(0[0-9]{2,3})\\-)?([2-9][0-9]{6,7})(\\-[0-9]{1,4})?$", s);
	}

	/**
	 * 邮编
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isZip(String s) {
		return valid("^[0-9]{6}$", s);
	}
	
	/**
	 * 邮编
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isIDCard(String s) {
		return valid("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", s);
	}

	/**
	 * qq号码(1-9开头，4-9个数字)
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isQq(String s) {
		return valid("^[1-9]\\d{4,9}$", s);
	}

	/**
	 * ip地址
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isIp(String s) {
		return valid(
				"^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
				s);
	}

	/**
	 * 判断是否中文
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isChinese(String s) {
		return valid("^[\u4e00-\u9fa5]+$", s);
	}

	/**
	 * 字符和数字
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isChrNum(String s) {
		return valid("^([a-zA-Z0-9]+)$", s);
	}

	/**
	 * 判断是否是URL
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url) {
		return valid(
				"(http://|https://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?",
				url);
	}
	
	/**
	 * 为空判断,0做空处理。
	 * 
	 * <pre>
	 * 这里判断：
	 * 1.字符串为NULL
	 * 2.字符串为空串。
	 * 3.字符串为0。
	 * </pre>
	 * 
	 * @param tmp
	 * @return boolean
	 * 
	 */
	public static boolean isZeroEmpty(String str) {
		boolean isEmpty = StringUtil.isEmpty(str);
		if (isEmpty)
			return true;
		return StringPool.ZERO.equals(str);
	}
	
	/**
	 * 使用正则表达式验证。
	 * 
	 * @param regex
	 * @param input
	 * @return
	 */
	public static boolean valid(String regex, String input) {
		if (StringUtil.isEmpty(regex))
			return false;
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher regexMatcher = p.matcher(input);
		return regexMatcher.find();
	}
	
}
