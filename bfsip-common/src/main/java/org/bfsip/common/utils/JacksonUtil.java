package org.bfsip.common.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.bfsip.common.constants.StringPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/** 
 * json工具类-Jackson，标准json格式key必须使用双引号<code>"</code>
 * <pre> 
 * 作者：eddy
 * 邮箱：1546077710@qq.com
 * 日期：2018年3月1日-下午6:15:38
 * 版权：
 * </pre>
 */
public class JacksonUtil {

	private static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
	
	private JacksonUtil(){}
	
	/**
	 * 创建默认模型绑定对象
	 *
	 * @return 
	 */
	private static ObjectMapper mapper(){
		return mapper(StringPool.DATE_FORMAT_DATETIME);
	}
	
	/**
	 * 创建指定日期格式的模型绑定对象
	 *
	 * @param dateFormat
	 * @return 
	 */
	private static ObjectMapper mapper(String dateFormat){
		ObjectMapper mapper = new ObjectMapper();
		
		// 取消默认时间日期格式转换
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// 设置JSON时间格式 
		SimpleDateFormat myDateFormat = new SimpleDateFormat(dateFormat);
		mapper.setDateFormat(myDateFormat);
		
		// 设置北京时间
		mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		
		return mapper;
	}
	
	/**
	 * 判断JSON是否为空<br>
	 * 传入的对象为 JsonObject 或者JSONArray的对象
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object o) {
		return BeanUtils.isEmpty(o);
	}

	/**
	 * 判断JSON 是否为空<br>
	 * 传入的对象为 JsonObject 或者JSONArray的对象
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}
	
	/**
	 * 字符串是否为json格式【包含JSONArray和jsonObject的对象】
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static boolean isJson(String jsonStr) {
		return isJsonObject(jsonStr) || isJsonArray(jsonStr);
	}

	/**
	 * 字符串不是json格式【包含JSONArray和jsonObject的对象】
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static boolean isNotJson(String jsonStr) {
		return !isJson(jsonStr);
	}
	
	/**
	 * 字符串是否为json对象格式（区别JSONArray格式）
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static boolean isJsonObject(String jsonStr) {
		try {
			JsonNode jsonNode = mapper().readTree(jsonStr);
			return jsonNode.isObject();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 字符串是否为json对象格式（区别JSONArray格式）
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static boolean isNotJsonObject(String jsonStr) {
		return !isJsonObject(jsonStr);
	}

	/**
	 * 字符串是否为jsonArray格式
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static boolean isJsonArray(String jsonStr) {
		try {
			JsonNode jsonNode = mapper().readTree(jsonStr);
			return jsonNode.isArray();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 字符串是否为jsonArray格式
	 * 
	 * @param jsonString
	 * @return
	 */
	public static boolean isNotJsonArray(String jsonStr) {
		return !isJsonArray(jsonStr);
	}
	
	/**
	 * Bean转换为Json String
	 *
	 * @param obj
	 * @return 
	 */
	public static String toJsonString(Object obj){
		try {
			return mapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从一个JSON得到一个java对象
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> T getDTO(String jsonStr, Class<T> clazz){
		try {
			ObjectMapper mapper = mapper();
			return mapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从一个JSON得到一个java对象
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> T getDTO(String jsonStr, Class<T> clazz, String dateFormat){
		try {
			ObjectMapper mapper = mapper(dateFormat);
			return mapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从一个JSON数组得到一个java对象集合，其中对象中包含有集合属性
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getDTOList(String jsonStr, Class<T> clazz){
		try {
			ObjectMapper mapper = mapper();
			JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
			return mapper.readValue(jsonStr, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从一个JSON数组得到一个java对象集合，其中对象中包含有集合属性
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getDTOList(String jsonStr, Class<T> clazz, String dateFormat){
		try {
			ObjectMapper mapper = mapper(dateFormat);
			JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
			return mapper.readValue(jsonStr, type);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
}
