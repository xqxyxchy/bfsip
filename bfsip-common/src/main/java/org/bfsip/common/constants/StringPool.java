package org.bfsip.common.constants;

/** 
 * 常用的变量
 *
 * <pre> 
 * project: bfsip-common
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月5日-下午9:01:15
 * rights: eddy
 * </pre>
 */
public interface StringPool {
	/**&*/
	String AMPERSAND = "&";
	/**and*/
	String AND = "and";
	/**<code>@</code>*/
	String AT = "@";
	/**<code>*</code>*/
	String ASTERISK = "*";
	/**<code>*</code>*/
	String STAR = ASTERISK;
	/**\\*/
	String BACK_SLASH = "\\";
	/**:*/
	String COLON = ":";
	/**,*/
	String COMMA = ",";
	/**-*/
	String DASH = "-";
	/**$*/
	String DOLLAR = "$";
	/**.*/
	String DOT = ".";
	/**..*/
	String DOTDOT = "..";
	/**.class*/
	String DOT_CLASS = ".class";
	/**.java*/
	String DOT_JAVA = ".java";
	/**.js*/
	String DOT_JS = ".js";
	/**.jsp*/
	String DOT_JSP = ".jsp";
	/**.map.xml*/
	String DOT_MAP_XML = ".map.xml";
	
	String EMPTY = "";

	/**=*/
	String EQUALS = "=";

	String FALSE = "false";

	/** <code>/</code> */
	String SLASH = "/";
	/** # */
	String HASH = "#";
	/** ^ */
	String HAT = "^";
	/** { */
	String LEFT_BRACE = "{";
	/** ( */
	String LEFT_BRACKET = "(";
	/** < */
	String LEFT_CHEV = "<";
	/** <= */
	String LEFT_CHEV_EQUAL = "<=";

	String NEWLINE = "\n";

	String N = "N";

	String NO = "no";

	String NULL = "null";

	String OFF = "off";

	String ON = "on";

	String OR = "or";
	/** % */
	String PERCENT = "%";
	/** | */
	String PIPE = "|";
	/** + */
	String PLUS = "+";
	/** ? */
	String QUESTION_MARK = "?";
	/** ! */
	String EXCLAMATION_MARK = "!";
	/** " */
	String QUOTE = "\"";

	String RETURN = "\r";

	String TAB = "\t";

	String RETURN_NEW_LINE = "\r\n";

	/** } */
	String RIGHT_BRACE = "}";

	/** ) */
	String RIGHT_BRACKET = ")";
	/** > */
	String RIGHT_CHEV = ">";
	/** >= */
	String RIGHT_CHEV_EQUAL = ">=";

	/** ; */
	String SEMICOLON = ";";

	/** ' */
	String SINGLE_QUOTE = "'";

	String SPACE = " ";

	/** [ */
	String LEFT_SQ_BRACKET = "[";

	/** ] */
	String RIGHT_SQ_BRACKET = "]";

	String TRUE = "true";

	/** _ */
	String UNDERSCORE = "_";

	String UTF_8 = "UTF-8";
	
	String GBK = "GBK";

	String ISO_8859_1 = "ISO-8859-1";
	
	/** text/html;charset=UTF-8 */
	String  CONTENT_TYPE_UTF_8 ="text/html;charset=UTF-8";

	String Y = "Y";

	String YES = "yes";

	/** 1 */
	String ONE = "1";

	/** 0 */
	String ZERO = "0";

	/** ${ */
	String DOLLAR_LEFT_BRACE = "${";
	/** LIKE */
	String LIKE = "LIKE";
	/** 特殊字符 */
	String SPECIAL_REG_EX = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\"]";
	
	
	/** yyyy-MM-dd HH:mm:ss */
	String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	/** yyyy-MM-dd */
	String DATE_FORMAT_DATE = "yyyy-MM-dd";
	/** yyyy-MM-dd HH:mm */
	String DATE_FORMAT_DATETIME_NOSECOND = "yyyy-MM-dd HH:mm";
	/** yyyy-MM-dd HH */
	String DATE_FORMAT_DATETIME_NOMINUTE = "yyyy-MM-dd HH";
	/** HH:mm:ss */
	String DATE_FORMAT_TIME = "HH:mm:ss";
	/** HH:mm */
	String DATE_FORMAT_TIME_NOSECOND = "HH:mm";
	/** yyyy-MM-dd HH:mm:ss.SSS */
	String DATE_FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
	/** yyyyMMddHHmmssSSS */
	String DATE_FORMAT_TIMESTAMP_NOSPLIT = "yyyyMMddHHmmssSSS";
	/** yyyy-MM-dd'T'HH:mm:ss.sssZZ */
	String DATE_FORMAT_TIMESTAMP_T = "yyyy-MM-dd'T'HH:mm:ss.sssZZ";
	
	/** yyyy-MM */
	String DATE_FORMAT_MONTH="yyyy-MM";
	
	/** yyyyMMdd */
	String DATE_FORMAT_SHORT_DATE ="yyyyMMdd";
	/** yyyyMM */
	String DATE_FORMAT_SHORT_MONTH="yyyyMM";
	/** yyyy */
	String DATE_FORMAT_YEAR="yyyy";	

	/** 固定不变 */
	String FIXED = "fixed";
	/** 大写 */
	String UPPER = "upper";
	/** 小写 */
	String LOWER = "lower";
	
	/** access_token */
	String TOKEN = "access_token";
	/** isSuccess */
	String IS_SUCCESS = "isSuccess";
	/** from_gateway */
	String FROM_GATEWAY = "from_gateway";
	
}
