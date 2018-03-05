package org.bfsip.common.utils;

import org.apache.commons.lang.StringEscapeUtils;

/** 
 * 字符串编码转换器
 *
 * <pre> 
 * project: bfsip-common
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月5日-下午9:02:20
 * rights: eddy
 * </pre>
 */
public class StringEscaper {
	
	private StringEscaper(){}

	/**
	 * 对字符串 escape 编码
	 * 
	 * @param htmlCode
	 * @return
	 */
	public static String encodeHtml(String htmlCode) {
		int i;
		char j;
		StringBuilder tmp = new StringBuilder();
		tmp.ensureCapacity(htmlCode.length() * 6);

		for (i = 0; i < htmlCode.length(); i++) {

			j = htmlCode.charAt(i);

			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j)) {
				tmp.append(j);
			} else if (j < 256) {
				tmp.append("%");
				if (j < 16) {
					tmp.append("0");
				}
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * 对编码的字符串解码
	 * 
	 * @param src
	 * @return
	 */
	public static String decodeHtml(String src) {
		StringBuilder tmp = new StringBuilder();
		tmp.ensureCapacity(src.length());
		int lastPos = 0;
		int pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf('%', lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * 对指定html内容进行编码
	 * 
	 * @param html
	 *            待编码的html内容
	 * @return
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml(html);
	}

	/**
	 * 反编码html
	 * 
	 * @param content
	 * @return
	 */
	public static String unescapeHtml(String content) {
		return StringEscapeUtils.unescapeHtml(content);
	}
	
	public static String escapeSql(String sql){
		return StringEscapeUtils.escapeSql(sql);
	}

	/**
	 * 替换json特殊字符转码
	 * 
	 * @param json
	 * @return
	 */
	public static String unescapeJson(String json) {
		return json.replace("&quot;", "\"").replace("&nuot;", "\n");
	}

	/**
	 * 字符串 编码转换
	 * 
	 * @param str
	 *            字符串
	 * @param fromEncode
	 *            原來的編碼
	 * @param toEncode
	 *            轉換后的編碼
	 * @return
	 */
	public static String changeEncode(String str, String fromEncode, String toEncode) {
		String result = str;
		try {
			result = new String(str.getBytes(fromEncode), toEncode);
		} catch (Exception e) {
			result = str;
		}
		return result;
	}
	
	/**
	 * 将字符串里面的所有英文点符号转成空格（字符实体形式）
	 * 
	 * @param stringWithPoint
	 *            待处理的字符串
	 * @return 每一个点对应一串：&nbsp;&emsp;
	 */
	public static String toSpace(String stringWithPoint) {
		StringBuilder space = new StringBuilder();
		if (StringUtil.isEmpty(stringWithPoint))
			return space.toString();
		String[] path = stringWithPoint.split("\\.");
		for (int i = 0; i < path.length - 1; i++) {
			space.append(path[i]).append("&nbsp;&emsp;");
		}
		return space.toString();
	}	
	
	/**
	 * 将人民币金额数字转成中文大写。
	 * 
	 * @param amount
	 * @return
	 */
	public static String toChineseAmount(double amount) {
		char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
		char[] vunit = { '万', '亿' }; // 段名表示
		char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
		long midVal = (long) (amount * 100); // 转化成整形
		String valStr = String.valueOf(midVal); // 转化成字符串

		String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
		String rail = valStr.substring(valStr.length() - 2); // 取小数部分

		StringBuilder prefix = new StringBuilder(); // 整数部分转化的结果
		String suffix = ""; // 小数部分转化的结果
		// 处理小数点后面的数
		if (rail.equals("00")) { // 如果小数部分为0
			suffix = "整";
		} else {
			suffix = digit[rail.charAt(0) - '0'] + "角"
					+ digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
		}
		// 处理小数点前面的数
		char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
		char zero = '0'; // 标志'0'表示出现过0
		byte zeroSerNum = 0; // 连续出现0的次数
		for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
			int idx = (chDig.length - i - 1) % 4; // 取段内位置
			int vidx = (chDig.length - i - 1) / 4; // 取段位置
			if (chDig[i] == '0') { // 如果当前字符是0
				zeroSerNum++; // 连续0次数递增
				if (zero == '0') { // 标志
					zero = digit[0];
				} else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					prefix.append(vunit[vidx - 1]);
					zero = '0';
				}
				continue;
			}
			zeroSerNum = 0; // 连续0次数清零
			if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
				prefix.append(zero);
				zero = '0';
			}
			prefix.append(digit[chDig[i]- '0']); // 转化该数字表示
			if (idx > 0) {
				prefix.append(hunit[idx - 1]);
			}
			if (idx == 0 && vidx > 0) {
				prefix.append(vunit[vidx - 1]); // 段结束位置应该加上段名如万,亿
			}
		}

		if (prefix.length() > 0) {
			prefix.append('圆'); // 如果整数部分存在,则有圆的字样
		}
		return prefix + suffix; // 返回正确表示
	}

	/**
	 * 将传入字符串中的Html字符实体（character entities）转换成Html预留字符
	 * 
	 * @param contentWithCharEntities
	 *            待转换的字符串
	 * @return
	 */
	public static String toHtmlReserved(String contentWithCharEntities) {
		contentWithCharEntities = contentWithCharEntities.replace("&apos;", "'").replace("&quot;", "\"")
				.replace("&gt;", ">").replace("&lt;", "<")
				.replace("&amp;", "&");

		int start = 0;
		int end = 0;
		final StringBuilder result = new StringBuilder();

		while (start > -1) {
			int system = 10;// 进制
			if (start == 0) {
				int t = contentWithCharEntities.indexOf("&#");
				if (start != t) {
					start = t;
				}
				if (start > 0) {
					result.append(contentWithCharEntities.substring(0, start));
				}
			}
			end = contentWithCharEntities.indexOf(';', start + 2);
			String charStr = "";
			if (end != -1) {
				charStr = contentWithCharEntities.substring(start + 2, end);
				// 判断进制
				char s = charStr.charAt(0);
				if (s == 'x' || s == 'X') {
					system = 16;
					charStr = charStr.substring(1);
				}
			}
			// 转换
			try {
				char letter = (char) Integer.parseInt(charStr, system);
				result.append(Character.toString(letter));
			} catch (NumberFormatException e) {
			}

			// 处理当前unicode字符到下一个unicode字符之间的非unicode字符
			start = contentWithCharEntities.indexOf("&#", end);
			if (start - end > 1) {
				result.append(contentWithCharEntities.substring(end + 1, start));
			}

			// 处理最后面的非unicode字符
			if (start == -1) {
				int length = contentWithCharEntities.length();
				if (end + 1 != length) {
					result.append(contentWithCharEntities.substring(end + 1, length));
				}
			}
		}
		return result.toString();
	}

	/**
	 * 将传入的字符串的Html预留字符转换成Html字符实体（character entities）
	 * 
	 * @param contentWithHtmlReserved
	 *            待转换的字符串（一般为Html代码）
	 * @return
	 */
	public static String toCharEntities(String contentWithHtmlReserved) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < contentWithHtmlReserved.length(); i++) {
			char c = contentWithHtmlReserved.charAt(i);

			switch (c) {
			case 0x0A:
				result.append(c);
				break;

			case '<':
				result.append("&lt;");
				break;

			case '>':
				result.append("&gt;");
				break;

			case '&':
				result.append("&amp;");
				break;

			case '\'':
				result.append("&apos;");
				break;

			case '"':
				result.append("&quot;");
				break;

			default:
				if ((c < ' ') || (c > 0x7E)) {
					result.append("&#x");
					result.append(Integer.toString(c, 16));
					result.append(';');
				} else {
					result.append(c);
				}
			}
		}
		return result.toString();
	}	
	/**
	 * 替换掉包含富文本的json 字符串中特殊的字符
	 * @param json
	 * @return
	 */
	public static String escapeJson(String json) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < json.length(); i++) {

			char c = json.charAt(i);
			switch (c) {
			case '\"':
				result.append("\\\"");
				break;
			case '\\':
				result.append("\\\\");
				break;
			case '/':
				result.append("\\/");
				break;
			case '\b':
				result.append("\\b");
				break;
			case '\f':
				result.append("\\f");
				break;
			case '\n':
				result.append("\\n");
				break;
			case '\r':
				result.append("\\r");
				break;
			case '\t':
				result.append("\\t");
				break;
			default:
				result.append(c);
			}
		}

		return result.toString();
	}	
}
