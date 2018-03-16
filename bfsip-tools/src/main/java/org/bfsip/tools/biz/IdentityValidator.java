/**
 * <pre>
 * 	1-2位省、自治区、直辖市代码； 
 *	3-4位地级市、盟、自治州代码； 
 *	5-6位县、县级市、区代码； 
 *	7-14位出生年月日，比如19670401代表1967年4月1日； 
 *	15-17位为顺序号，其中17位（倒数第二位）男为单数，女为双数； 
 *	18位为校验码，0-9和X。
 *		作为尾号的校验码，是由把前十七位数字带入统一的公式计算出来的，计算的结果是0-10，
 *		如果某人的尾号是0－9，都不会出现X，但如果尾号是10，那么就得用X来代替，
 *		因为如果用10做尾号，那么此人的身份证就变成了19位。
 *		X是罗马数字的10，用X来代替10。
 * </pre>
 */

package org.bfsip.tools.biz;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import org.bfsip.common.constants.StringPool;
import org.bfsip.common.utils.FileUtil;
import org.bfsip.common.utils.JacksonUtil;

/** 
 * Identity Card Number validator.
 *
 * <pre> 
 * project: bfsip-tools
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月16日-下午9:48:32
 * rights: eddy
 * </pre>
 */
public class IdentityValidator {

	private static final String ID_NO_PATTERN = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$";
	
	// 身份证前17位每位加权因子
	private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

	// 身份证第18位校检码
	private static final String[] REF_NUMBER = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
	
	/**
	 * 校验是否合法身份证号码
	 *
	 * @param idNo	待校验身份证号码
	 * @return true|false
	 */
	public static boolean verify(String idNo) {
		return isIdNoPattern(idNo) 
				&& isValidProvinceId(idNo.substring(0, 6)) 
				&& isValidDate(idNo.substring(6, 14))
				&& checkIdNoLastNum(idNo);
	}
	
	public static String getMeesage(String idNo){
		if(!verify(idNo)){
			return "非法身份证号码！";
		}
		
		try {
			StringBuilder builder = new StringBuilder();
			
			String sex = idNo.substring(16, 17);
			int sexint = Integer.valueOf(sex);
			int mode = sexint % 2;
			String sexmessage = mode == 1 ? "男" : "女";
			builder.append("此人为").append(sexmessage).append("性").append(";");
			
			String ind = idNo.substring(6, 14);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date indate = dateFormat.parse(ind);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			String indatemsg = sdf.format(indate);
			builder.append("于").append(indatemsg).append(";");
			
			String pid = idNo.substring(0, 6);
			Map<String, String> map = getProvinceMap();
			String text = map.get(pid);
			builder.append("出生在").append(text).append(";");
			
			return builder.toString();
		} catch (ParseException ignore) {
		}
		
		return "出生年月日异常！";
	}
	
	/**
	 * 正则匹配是否符合身份证号码表达式
	 *
	 * @param idNo	待校验身份证号码
	 * @return 
	 */
	private static boolean isIdNoPattern(String idNo) {
		return Pattern.matches(ID_NO_PATTERN, idNo);
	}
	
	/**
	 * 校验是否合法省编码
	 *
	 * @param provinceId	待校验编码
	 * @return 
	 */
	private static boolean isValidProvinceId(String provinceId){
		Map<String, String> map = getProvinceMap();
		
        if (map.containsKey(provinceId)) {
            return true;
        }
        
	    return false;
	}
	
	/**
	 * 校验是否合法日期（yyyyMMdd）
	 *
	 * @param inDate	待校验日期
	 * @return 
	 */
	private static boolean isValidDate(String inDate) {
		if (inDate == null) {
			return false;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		if (inDate.trim().length() != dateFormat.toPattern().length()) {
			return false;
		}

		// 执行严格的日期匹配
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 校验-校验码是否正确
	 *
	 * @param idNo
	 * @return 
	 */
	private static boolean checkIdNoLastNum(String idNo) {
		if (idNo.length() != 18) {
			return false;
		}
		char[] tmp = idNo.toCharArray();
		int[] cardidArray = new int[tmp.length - 1];
		int i = 0;
		for (i = 0; i < tmp.length - 1; i++) {
			cardidArray[i] = Integer.parseInt(tmp[i] + "");
		}
		String checkCode = sumPower(cardidArray);
		String lastNum = tmp[tmp.length - 1] + "";
		if (lastNum.equals("x")) {
			lastNum = lastNum.toUpperCase();
		}
		if (!checkCode.equals(lastNum)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 计算校验码
	 *
	 * @param cardIdArray
	 * @return 
	 */
	private static String sumPower(int[] cardIdArray) {
		int result = 0;
		for (int i = 0; i < POWER.length; i++) {
			result += POWER[i] * cardIdArray[i];
		}
		return REF_NUMBER[(result % 11)];
	}
	
	/**
	 * 获取文件流
	 *
	 * @param target
	 * @param suffix
	 * @return 
	 */
	private static InputStream getInputStream(Class<?> target, String suffix) {
		InputStream is = null;
		String targetName = target.getSimpleName();
		is = target.getResourceAsStream(targetName + suffix);
		if(null == is){
			is = target.getClassLoader().getResourceAsStream(targetName + suffix);
		}
		return is;
	}
	
	/**
	 * 获取省市县编码
	 *
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> getProvinceMap(){
		InputStream is = getInputStream(IdentityValidator.class, ".json");
		String content = FileUtil.readFile(is, StringPool.UTF_8, true);
		Map<String, String> map = JacksonUtil.getDTO(content, Map.class);
		
		return map;
	}
	
}
