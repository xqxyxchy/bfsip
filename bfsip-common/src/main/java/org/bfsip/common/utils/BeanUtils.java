package org.bfsip.common.utils;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * BeanUtils的等价类，只是将check exception改为uncheck exception
 *
 * <pre> 
 * project: bfsip-common
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月5日-下午9:01:47
 * rights: eddy
 * </pre>
 */
public class BeanUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);
	
	private BeanUtils(){}
	
	/**
	 * BeanUtil类型转换器
	 */
	public static final ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();

	private static BeanUtilsBean beanUtilsBean = new BeanUtilsBean(
			convertUtilsBean, new PropertyUtilsBean());

	static {
		convertUtilsBean.register(new DateConverter(), Date.class);
		convertUtilsBean.register(new LongConverter(null), Long.class);
	}

	/**
	 * 可以用于判断 Map,Collection,String,Array,Long是否为空
	 * 
	 * @param o
	 *            java.lang.Object.
	 * @return boolean.
	 */
	public static boolean isEmpty(Object o) {
		if (o == null)
			return true;
		if (o instanceof String) {
			if (((String) o).trim().length() == 0)
				return true;
		} else if (o instanceof Collection) {
			if (((Collection<?>) o).isEmpty())
				return true;
		} else if (o.getClass().isArray()) {
			if (((Object[]) o).length == 0)
				return true;
		} else if (o instanceof Map && mapEmpty(o)) {
				return true;
		}
		return false;
	}
	
	private static boolean mapEmpty(Object o){
		return ((Map<?, ?>) o).isEmpty();
	}

	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	/**
	 * 判断对象是否为数字
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNumber(Object o) {
		if (o == null)
			return false;
		if (o instanceof Number)
			return true;
		if (o instanceof String) {
			try {
				Double.parseDouble((String) o);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}

	/**
	 * 封装
	 * 
	 * @param map
	 * @param entity
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object populateEntity(Map<String, ? extends Object> map, Object entity)
			throws IllegalAccessException, InvocationTargetException {
		beanUtilsBean.populate(entity, map);
		return entity;
	}

	/**
	 * 根据指定的类名判定指定的类是否存在。
	 * 
	 * @param className
	 * @return
	 */
	public static boolean validClass(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * 判定类是否继承自父类
	 * 
	 * @param cls
	 *            子类
	 * @param parentClass
	 *            父类
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean isInherit(Class cls, Class parentClass) {
		return parentClass.isAssignableFrom(cls);
	}

	/**
	 * 克隆对象
	 * 
	 * @param bean
	 * @return
	 */
	public static Object cloneBean(Object bean) {
		try {
			return beanUtilsBean.cloneBean(bean);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}
	
	/**
	 * 深度克隆对象。
	 * 
	 * <pre>
	 * 	将一个对象序列化一个内存数据组。
	 *  在从数组中反序列化这个类。
	 * </pre>
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Object cloneObject(Object obj){
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		Object rtn = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			out = new ObjectOutputStream(byteOut);
			out.writeObject(obj);
			
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			in = new ObjectInputStream(byteIn);
			rtn = in.readObject();
		} catch (IOException e) {
			logger.error("clone object failed {}.", e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.error("class not found {}.", e.getMessage());
		}finally{
			try {
				if(null != out)out.close();
				if(null != in)in.close();
			} catch (IOException ignore) {}
		}
		
		return rtn;
	}

	/**
	 * 拷贝一个bean中的非空属性于另一个bean中
	 * 
	 * @param dest
	 *            目标对象
	 * @param orig
	 *            源对象
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void copyNotNullProperties(Object dest, Object orig) {
		// Validate existence of the specified beans
		if (dest == null) {
			logger.error("No destination bean specified");
			return;
		}
		if (orig == null) {
			logger.error("No origin bean specified");
			return;
		}

		try {
			// Copy the properties, converting as necessary
			if (orig instanceof DynaBean) {
				DynaProperty[] origDescriptors = ((DynaBean) orig)
						.getDynaClass().getDynaProperties();
				for (int i = 0; i < origDescriptors.length; i++) {
					String name = origDescriptors[i].getName();
					if (beanUtilsBean.getPropertyUtils().isReadable(orig, name)
							&& beanUtilsBean.getPropertyUtils().isWriteable(
									dest, name)) {
						Object value = ((DynaBean) orig).get(name);
						beanUtilsBean.copyProperty(dest, name, value);
					}
				}
			} else if (orig instanceof Map) {
				Iterator<?> entries = ((Map<?, ?>) orig).entrySet().iterator();
				while (entries.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry entry = (Map.Entry) entries.next();
					String name = (String) entry.getKey();
					if (beanUtilsBean.getPropertyUtils()
							.isWriteable(dest, name)) {
						beanUtilsBean
								.copyProperty(dest, name, entry.getValue());
					}
				}
			} else {
				PropertyDescriptor[] origDescriptors = beanUtilsBean
						.getPropertyUtils().getPropertyDescriptors(orig);
				for (int i = 0; i < origDescriptors.length; i++) {
					String name = origDescriptors[i].getName();
					if ("class".equals(name)) {
						continue; // No point in trying to set an object's class
					}
					if (beanUtilsBean.getPropertyUtils().isReadable(orig, name)
							&& beanUtilsBean.getPropertyUtils().isWriteable(dest, name)) {
						Object value = beanUtilsBean.getPropertyUtils()
								.getSimpleProperty(orig, name);
						if (value != null) {
							beanUtilsBean.copyProperty(dest, name, value);
						}
					}
				}
			}
		} catch (Exception ex) {
			handleReflectionException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T copyProperties(Class<T> destClass, Object orig) {
		Object target = null;
		try {
			target = destClass.newInstance();
			copyProperties((Object) target, orig);
			return (T) target;
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static void copyProperties(Object dest, Object orig) {
		try {
			beanUtilsBean.copyProperties(dest, orig);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	public static void copyProperty(Object bean, String name, Object value) {
		try {
			beanUtilsBean.copyProperty(bean, name, value);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	public static Map<?, ?> describe(Object bean) {
		try {
			return beanUtilsBean.describe(bean);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String[] getArrayProperty(Object bean, String name) {
		try {
			return beanUtilsBean.getArrayProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return new String[0];
		}
	}

	public static ConvertUtilsBean getConvertUtils() {
		return beanUtilsBean.getConvertUtils();
	}

	public static String getIndexedProperty(Object bean, String name, int index) {
		try {
			return beanUtilsBean.getIndexedProperty(bean, name, index);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getIndexedProperty(Object bean, String name) {
		try {
			return beanUtilsBean.getIndexedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getMappedProperty(Object bean, String name, String key) {
		try {
			return beanUtilsBean.getMappedProperty(bean, name, key);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getMappedProperty(Object bean, String name) {
		try {
			return beanUtilsBean.getMappedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getNestedProperty(Object bean, String name) {
		try {
			return beanUtilsBean.getNestedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getProperty(Object bean, String name) {
		try {
			return beanUtilsBean.getProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static PropertyUtilsBean getPropertyUtils() {
		try {
			return beanUtilsBean.getPropertyUtils();
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static String getSimpleProperty(Object bean, String name) {
		try {
			return beanUtilsBean.getSimpleProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	public static void populate(Object bean, Map<String, ? extends Object> properties) {
		try {
			beanUtilsBean.populate(bean, properties);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	/**
	 * 通过反射设置对象的值。
	 * 
	 * @param bean
	 * @param name
	 * @param value
	 *            void
	 */
	public static void setProperty(Object bean, String name, Object value) {
		try {
			beanUtilsBean.setProperty(bean, name, value);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	private static void handleReflectionException(Exception e) {
		throw new RuntimeException(e);
	}

	/**
	 * java反射访问私有成员变量的值。
	 * 
	 * @param instance
	 * @param fieldName
	 * @return
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 *             Object
	 */
	public static Object getValue(Object instance, String fieldName)
			throws IllegalAccessException, NoSuchFieldException {

		Field field = getField(instance.getClass(), fieldName);
		// 参数值为true，禁用访问控制检查
		field.setAccessible(true);
		return field.get(instance);
	}

	/**
	 * 将字符串数据按照指定的类型进行转换。
	 * 
	 * @param typeName
	 *            实际的数据类型
	 * @param value
	 *            字符串值。
	 * @return Object
	 */
	public static Object convertByActType(String typeName, String value) {
		Object o = null;
		if (typeName.equals("int")) {
			o = Integer.parseInt(value);
		} else if (typeName.equals("short")) {
			o = Short.parseShort(value);
		} else if (typeName.equals("long")) {
			o = Long.parseLong(value);
		} else if (typeName.equals("float")) {
			o = Float.parseFloat(value);
		} else if (typeName.equals("double")) {
			o = Double.parseDouble(value);
		} else if (typeName.equals("boolean")) {
			o = Boolean.parseBoolean(value);
		} else if (typeName.equals("java.lang.String")) {
			o = value;
		} else {
			o = value;
		}
		return o;
	}

	/**
	 * 根据类和成员变量名称获取成员变量。
	 * 
	 * @param thisClass
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 *             Field
	 */
	@SuppressWarnings("rawtypes")
	public static Field getField(Class thisClass, String fieldName)
			throws NoSuchFieldException {

		if (fieldName == null) {
			throw new NoSuchFieldException("Error field !");
		}

		return thisClass.getDeclaredField(fieldName);
	}

	/**
	 * 合并两个对象。
	 * 
	 * @param srcObj
	 * @param desObj
	 *            void
	 */
	public static void mergeObject(Object srcObj, Object desObj) {
		if (srcObj == null || desObj == null)
			return;

		Field[] fs1 = srcObj.getClass().getDeclaredFields();
		Field[] fs2 = desObj.getClass().getDeclaredFields();
		for (int i = 0; i < fs1.length; i++) {
			try {
				fs1[i].setAccessible(true);
				Object value = fs1[i].get(srcObj);
				fs1[i].setAccessible(false);
				if (null != value) {
					fs2[i].setAccessible(true);
					fs2[i].set(desObj, value);
					fs2[i].setAccessible(false);
				}
			} catch (Exception e) {
				logger.error("mergeObject" + e.getMessage());
			}
		}
	}

}
