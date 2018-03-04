package org.bfsip.common.utils;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bfsip.common.constants.StringPool;

/**
 * 字符串工具类。 可以先找{@link org.apache.commons.lang3.StringUtils }的方法)
 * 
 * <pre>
 * 作者：eddy
 * 邮箱：1546077710@qq.com
 * 日期：2018年3月1日-下午6:15:38
 * 版权：
 * </pre>
 * 
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {
	
	private StringUtil(){}

	/**
	 * 判断指定的内容是否存在
	 * 
	 * @param content
	 *            内容
	 * @param begin
	 *            开始内容
	 * @param end
	 *            结束内容
	 * @return
	 */
	public static boolean isExistIgnoreCase(String content, String beginStr, String endStr) {
		final boolean isExist = true;
		// 转成小写
		String lowContent = content.toLowerCase();
		String lowBeginStr = beginStr.toLowerCase();
		String lowEndStr = endStr.toLowerCase();

		int beginIndex = lowContent.indexOf(lowBeginStr);
		int endIndex = lowContent.indexOf(lowEndStr);
		if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
			return isExist;
		}
		return !isExist;
	}

	/**
	 * 对字符串去掉前面的指定字符
	 * 
	 * @param content
	 *            待处理的字符串
	 * @param prefix
	 *            要去掉前面的指定字符串
	 * @return
	 */
	public static String trimPrefix(String content, String prefix) {
		String resultStr = content;
		while (resultStr.startsWith(prefix)) {
			resultStr = resultStr.substring(prefix.length());
		}
		return resultStr;
	}

	/**
	 * 对字符串去掉前面的指定字符
	 * 
	 * @param content
	 *            待处理的字符串
	 * @param suffix
	 *            要去掉后面的指定字符串
	 * @return
	 */
	public static String trimSuffix(String content, String suffix) {
		String resultStr = content;
		while (resultStr.endsWith(suffix)) {
			resultStr = resultStr.substring(0, resultStr.length() - suffix.length());
		}
		return resultStr;
	}

	/**
	 * 对字符串的前后均去掉前面的指定字符
	 * 
	 * @param content
	 * @param trimStr
	 * @return
	 */
	public static String trimBoth(String content, String trimStr) {
		return trimSuffix(trimPrefix(content, trimStr), trimStr);
	}

	/**
	 * 把字符串的第一个字母转为大写
	 * 
	 * @param str字符串
	 * @return
	 */
	public static String upperFirst(String str) {
		return convertFirst(str, true);
	}

	/**
	 * 把字符串的第一个字母转为小写
	 * 
	 * @param str
	 * @return
	 */
	public static String lowerFirst(String str) {
		return convertFirst(str, false);
	}

	/**
	 * 去除特殊字符（根据默认的特殊字符正则表达式进行处理）
	 * 
	 * @param content
	 * @return
	 */
	public static String removeSpecialChars(String content) {
		// 清除掉所有特殊字符
		Pattern p = Pattern.compile(StringPool.SPECIAL_REG_EX);
		Matcher m = p.matcher(content);
		return m.replaceAll("").trim();
	}

	/**
	 * 把字符串的第一个字母转为大写或者小写
	 * 
	 * @param str
	 *            字符串
	 * @param isUpper
	 *            是否大写
	 * @return
	 */
	public static String convertFirst(String str, boolean isUpper) {
		if (isEmpty(str))
			return "";
		char first = str.charAt(0);
		String firstChar = new String(new char[] { first });
		firstChar = isUpper ? firstChar.toUpperCase() : firstChar.toLowerCase();
		return firstChar + str.substring(1);
	}

	/**
	 * 把字符串的变成驼峰格式
	 * 
	 * @param str
	 *            字符串
	 * @param split
	 *            分隔符
	 * @return
	 */
	public static String convertSplit(String str, String split) {
		if (isEmpty(str)) {
			return "";
		}
		if (isEmpty(split)) {
			return "_";
		}

		str = str.toLowerCase();
		String[] strArr = str.split(split);

		if (strArr.length == 1) {
			return strArr[0];
		} else {
			for (int i = 1, len = strArr.length; i < len; i++) {
				strArr[i] = upperFirst(strArr[i]);
			}
		}

		return join(strArr, "");
	}

	/**
	 * 替换标量。<br>
	 * 
	 * <pre>
	 * 使用方法如下：
	 * String template="com/lc/ibps/{path}/model/{class}";
	 * Map<String,String> map=new HashMap<String,String>();
	 * map.put("path","platform");
	 * map.put("class","Role");
	 * String tmp=replaceVariable(template,map);
	 * </pre>
	 * 
	 * @param template
	 * @param map
	 * @return
	 * @throws CodegenException
	 */
	public static String replaceVariable(String template, Map<String, String> map) {
		return replaceVariable(template, map, false);
	}

	/**
	 * 替换标量。<br>
	 * 
	 * <pre>
	 * 使用方法如下：
	 * String template="com/lc/ibps/{path}/model/{class}";
	 * Map<String,String> map=new HashMap<String,String>();
	 * map.put("path","platform");
	 * map.put("class","Role");
	 * String tmp=replaceVariable(template,map);
	 * </pre>
	 * 
	 * @param template
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String replaceVariable(String template, Map<String, String> map, Boolean isException){
		Pattern regex = Pattern.compile("\\{(.*?)\\}");
		Matcher regexMatcher = regex.matcher(template);
		while (regexMatcher.find()) {
			String key = regexMatcher.group(1);
			String toReplace = regexMatcher.group(0);
			String value = map.get(key);
			if (value != null)
				template = template.replace(toReplace, value);
			else {
				if (isException)
					throw new RuntimeException("没有找到[" + key + "]对应的变量值，请检查表变量配置!");
			}
		}

		return template;
	}

	/**
	 * 替换表变量
	 * 
	 * @param template
	 * @param tableName
	 * @return
	 */
	public static String replaceVariable(String template, String tableName) {
		Pattern regex = Pattern.compile("\\{(.*?)\\}");
		Matcher regexMatcher = regex.matcher(template);
		if (regexMatcher.find()) {
			String toReplace = regexMatcher.group(0);
			template = template.replace(toReplace, tableName);
		}
		return template;
	}

	/**
	 * 删除后面的字符
	 * 
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trimSufffix(String toTrim, String trimStr) {
		while (toTrim.endsWith(trimStr)) {
			toTrim = toTrim.substring(0, toTrim.length() - trimStr.length());
		}
		return toTrim;
	}

	/**
	 * 删除指定的字符
	 * 
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trim(String toTrim, String trimStr) {
		return trimSufffix(trimPrefix(toTrim, trimStr), trimStr);
	}

	/**
	 * 
	 * 合并路径
	 *
	 * @param baseDir
	 * @param dir
	 * @return
	 */
	public static String combilePath(String baseDir, String dir) {
		baseDir = trimSufffix(baseDir, File.separator);
		dir = trimPrefix(dir, File.separator);

		return baseDir + File.separator + dir;
	}

	/**
	 * 合并路径
	 *
	 * @param args
	 * @return
	 */
	public static String pathJoin(String... args) {
		StringBuilder result = new StringBuilder();
		int i = 0;
		int j = args == null ? 0 : args.length;
		while (i < j) {
			String str = args[i];
			if (str == null) {
				str = "";
			}
			if ((i > 0) && ((str.startsWith("/")) || (str.startsWith("\\")))) {
				str = str.substring(1);
			}
			result.append(str);
			if ((i + 1 < j) && (!str.endsWith("/")) && (!str.endsWith("\\"))) {
				result.append("/");
			}
			i++;
		}
		return result.toString();
	}
}
