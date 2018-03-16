package org.bfsip.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.bfsip.common.constants.StringPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 文件处理帮助类
 *
 * <pre> 
 * project: bfsip-common
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月16日-下午10:27:18
 * rights: eddy
 * </pre>
 */
public class FileUtil {
	
	private FileUtil(){}
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 写入文件.(以默认UTF-8方式)
	 * 
	 * @param fileName
	 *            文件名称
	 * @param content
	 *            文件内容
	 */
	public static void writeFile(String fileName, String content) {
		writeFile(fileName, content, StringPool.UTF_8);
	}

	/**
	 * 指定字符集，写入文件。
	 * 
	 * @param fileName
	 *            文件名称
	 * @param content
	 * @param charset
	 */
	public static void writeFile(String fileName, String content, String charset) {
		Writer out = null;
		try {
			createFolder(fileName, true);
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), charset));
			out.write(content);
		} catch (IOException e) {
			logger.error("write file failed {}.", e.getMessage());
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException ignore) {}
		}
	}

	/**
	 * 写入文件
	 * 
	 * @param fileName
	 * @param is
	 * @throws IOException
	 */
	public static void writeFile(String fileName, InputStream is) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			byte[] bs = new byte[512];
			int n = 0;
			while ((n = is.read(bs)) != -1) {
				fos.write(bs, 0, n);
			}
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (fos != null)
				fos.close();
		}
	}

	/**
	 * 读取文件
	 * 
	 * @param fileName
	 *            文件路径
	 * @param charset
	 *            编码
	 * @return
	 */
	public static String readFile(String filePath, String charset, boolean isLine) {
		BufferedReader in = null;
		try {
			InputStream is = new FileInputStream(filePath);
			
			return readFile(is, charset, isLine);
		} catch (IOException e) {
			logger.error("read file failed {}.", e.getMessage());
			return "";
		}finally {
			try {
				if(null != in) in.close();
			} catch (IOException ignore) {}
		}
	}
	
	/**
	 * 读取文件
	 * 
	 * @param fileName
	 *            文件路径
	 * @param charset
	 *            编码
	 * @return
	 */
	public static String readFile(InputStream is, String charset, boolean isLine) {
		BufferedReader in = null;
		try {
			StringBuilder content = new StringBuilder();
			in = new BufferedReader(new InputStreamReader(is, charset));
			String str;
			while ((str = in.readLine()) != null) {
				content.append(str + (isLine ? "\r\n" : ""));
			}
			
			return content.toString();
		} catch (IOException e) {
			logger.error("read file failed {}.", e.getMessage());
			return "";
		}finally {
			try {
				if(null != in) in.close();
			} catch (IOException ignore) {}
		}
	}

	/**
	 * 读取文件
	 * 
	 * @param fileName
	 *            文件路径
	 * @param charset
	 *            编码
	 * @return
	 */
	public static String readFile(String filePath, String charset) {
		return readFile(filePath, charset, true);
	}

	/**
	 * 读取文件
	 * 
	 * @param fileName
	 *            文件路径
	 * @return
	 */
	public static String readFile(String filePath) {
		try {
			File file = new File(filePath);
			String charset = getCharset(file);
			return readFile(filePath, charset);
		} catch (Exception e) {
			logger.error("read file failed {}.", e.getMessage());
			return "";
		}
	}

	/**
	 * 通过类路径读取文件
	 * 
	 * @param filePath
	 *            文件类路径
	 * @return
	 */
	public static String readByClassPath(String filePath) {
		return readByClassPath(filePath, null);
	}

	/**
	 * 读取类路径下的文件。
	 * 
	 * @param filePath
	 * @param classLoader
	 * @return
	 */
	public static String readByClassPath(String filePath, ClassLoader classLoader) {
		BufferedReader br = null;
		try {
			URL url = null;
			if (classLoader != null) {
				url = classLoader.getResource("/" + filePath);
			} else {
				url = FileUtil.class.getResource("/" + filePath);
			}

			br = new BufferedReader(new InputStreamReader(url.openStream()));

			String s = null;
			StringBuilder fileText = new StringBuilder();
			while ((s = br.readLine()) != null) {
				fileText.append(s).append("\r\n");
			}

			return fileText.toString();
		} catch (IOException e) {
			logger.error("read file failed {}.", e.getMessage());
			return null;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ignore) {}
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean isExistFile(String dir) {
		boolean isExist = false;
		File fileDir = new File(dir);
		if (fileDir.isDirectory()) {
			File[] files = fileDir.listFiles();
			if (files != null && files.length != 0)
				isExist = true;
		}
		return isExist;
	}

	/**
	 * 获取文件的字符集
	 * 
	 * @param file
	 * @return
	 */
	public static String getCharset(File file) {
		String charset = StringPool.GBK;
		byte[] first3Bytes = new byte[3];
		BufferedInputStream bis = null;
		try {
			boolean checked = false;
			bis = new BufferedInputStream(new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;

			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();

			if (!checked) {
				while ((read = bis.read()) != -1) {
					if (read >= 0xF0)
						break;
					// 单独出现BF以下的，也算是GBK
					if (0x80 <= read && read <= 0xBF)
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF)// 双字节 (0xC0 - 0xDF)
							// (0x80 -
							// 0xBF),也可能在GB编码内
							continue;
						else
							break;
						// 也有可能出错，但是几率较小
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = StringPool.UTF_8;
								break;
							} else
								break;
						} else
							break;
					}
				}
			}
		} catch (Exception e) {
			logger.error("read file failed {}.", e.getMessage());
		} finally {
			try {
				if (bis != null) bis.close();
			} catch (IOException ignore) {}
		}

		return charset;
	}

	/**
	 * 读取流到字节数组
	 * 
	 * @param is
	 * @return
	 */
	public static byte[] readByte(InputStream is) {
		try {
			byte[] r = new byte[is.available()];
			is.read(r);
			return r;
		} catch (Exception e) {
			logger.error("read file failed {}.", e.getMessage());
		}
		return new byte[0];
	}

	/**
	 * 读取文件到字节数组
	 * 
	 * @param fileName
	 * @return
	 */
	public static byte[] readByte(String fileName) {
	    ByteArrayOutputStream out = null;
		FileInputStream fis =null;
		try {
			out = new ByteArrayOutputStream();
			fis = new FileInputStream(fileName);
		    byte[] buffer = new byte[1024 * 4];
		    int n = 0;
		    while ((n = fis.read(buffer)) != -1) {
		        out.write(buffer, 0, n);
		    }
		    return out.toByteArray();
		} catch (Exception e) {
			logger.error("read file failed {}.", e.getMessage());
		} finally {
			try {
				if(null != out) out.close();
				if(null != fis) fis.close();
			} catch (IOException ignore) {}
		}
		
		return new byte[0];
	}

	/**
	 * 写字节数组到文件
	 * 
	 * @param fileName
	 * @param b
	 * @return
	 */
	public static boolean writeByte(String fileName, byte[] b) {
		BufferedOutputStream fos = null;
		try {
			fos = new BufferedOutputStream(new FileOutputStream(fileName));
			fos.write(b);
			fos.close();
			return true;
		} catch (Exception e) {
			logger.error("write file failed {}.", e.getMessage());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ignore) {}
		}
		return false;
	}

	/**
	 * 序列化对象到文件
	 * 
	 * @param obj
	 * @param fileName
	 */
	public static void serializeToFile(Object obj, String fileName) {
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(obj);
		} catch (IOException e) {
			logger.error("serialize to file failed {}.", e.getMessage());
		}finally{
			try {
				if(null != out) out.close();
			} catch (IOException ignore) {}
		}
	}

	/**
	 * 从文件反序列化出对象
	 * 
	 * @param fileName
	 * @return
	 */
	public static Object deserializeFromFile(String fileName) {
		ObjectInputStream in = null;
		try {
			File file = new File(fileName);
			in = new ObjectInputStream(new FileInputStream(file));
			return in.readObject();
		} catch (Exception e) {
			logger.error("deserialize from file failed {}.", e.getMessage());
			return null;
		}finally{
			try {
				if(null != in) in.close();
			} catch (IOException ignore) {}
		}
	}

	/**
	 * InputStream 转为字符串
	 * 
	 * @param input
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream input, String charset) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(input, charset));
		StringBuilder builder = new StringBuilder();
		String line = "";
		while ((line = in.readLine()) != null) {
			builder.append(line + "\n");
		}
		return builder.toString();
	}

	/**
	 * 将stream按照utf-8编码转换为字符串。
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream input) throws IOException {
		return inputStream2String(input, StringPool.UTF_8);
	}

	/**
	 * 根据目录取得文件列表
	 * 
	 * @param path
	 * @return
	 */
	public static File[] getFiles(String path) {
		File file = new File(path);
		return file.listFiles();
	}

	/**
	 * 创建文件目录
	 * 
	 * @param filePath
	 * @param fileName
	 *            文件名称
	 * @return 文件的完整目录
	 */
	public static String createFile(String filePath, String fileName) {
		File file = new File(filePath);
		// 文件夹不存在创建
		if (!file.exists())
			file.mkdirs();
		return file.getPath() + File.separator + fileName;
	}

	/**
	 * 根据文件路径创建文件夹,如果路径不存在则创建.
	 * 
	 * @param path
	 */
	public static void createFolderFile(String path) {
		createFolder(path, true);
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @param isFile
	 */
	public static void createFolder(String path, boolean isFile) {
		if (isFile)
			path = path.substring(0, path.lastIndexOf(File.separator));
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	}

	/**
	 * 复制文件来新路径上
	 * 
	 * @param path
	 *            原路径
	 * @param newPath
	 *            新路径
	 */
	public static boolean renameFolder(String path, String newName) {
		File file = new File(path);
		boolean result = false;
		if (file.exists())
			result = file.renameTo(new File(newName));
		
		return result;
	}

	/**
	 * 仅取得文件目录下的子目录。
	 * 
	 * @param dir
	 *            目录
	 * @return 子目录列表
	 */
	public static List<File> getDiretoryOnly(File dir) {
		ArrayList<File> dirs = new ArrayList<File>();
		if (dir != null && dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.isDirectory();
				}
			});
			for (int i = 0; i < files.length; i++) {
				dirs.add(files[i]);
			}
		}
		return dirs;
	}

	/**
	 * 列出子文件列表
	 * 
	 * @param dir
	 *            指定目录
	 * @return 子文件列表
	 */
	public List<File> getFileOnly(File dir) {
		ArrayList<File> dirs = new ArrayList<File>();
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		for (int i = 0; i < files.length; i++) {
			dirs.add(files[i]);
		}
		return dirs;
	}

	/**
	 * 删除目录
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success)
					return false;
			}
		}
		return dir.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}
	
	/**
	 * 文件拷贝
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean copyFile(String from, String to, boolean autoCreate) {
		File fromFile = new File(from);
		if(!fromFile.exists() || fromFile.isDirectory()){
			logger.error("源文件不存在或是目录！");
			return false;
		}
		File toFile = new File(to);
		boolean result = false;
		if(!toFile.exists()){
			if(autoCreate){
				File toParentFile = toFile.getParentFile();
				result = toParentFile.mkdirs();
				try {
					if(result) result = toFile.createNewFile();
				} catch (IOException e) {
					logger.error("create file failed {}.", e.getMessage());
					return false;
				}
			}else{
				logger.error("目标文件不存在！");
				return false;
			}
		}else if(toFile.isDirectory()){
			logger.error("目标文件是目录！");
			return false;
		}else if(toFile.isFile()){
			if(autoCreate){
				try {
					result = toFile.delete();
					if(result) result = toFile.createNewFile();
				} catch (IOException e) {
					logger.error("create file failed {}.", e.getMessage());
					return false;
				}
			}
		} else {
			logger.error("unknow exception.");
			return false;
		}
		
		if(!result) return false;
		
		return copy(fromFile, toFile);
	}

	/**
	 * 文件拷贝
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean copyFile(String from, String to) {
		File fromFile = new File(from);
		if(!fromFile.exists() || fromFile.isDirectory()){
			logger.error("源文件不存在或是目录！");
			return false;
		}
		File toFile = new File(to);
		return copy(fromFile, toFile);
	}

	private static boolean copy(File fromFile, File toFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);
			int bytesRead;
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			while ((bytesRead = fis.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
			}
		} catch (IOException e) {
			logger.error("copy file failed {}.", e.getMessage());
			return false;
		} finally {
			try {
				if(null != fos) fos.flush();
				if(null != fos) fos.close();
				if(null != fis) fis.close();
			} catch (IOException ignore) {}
		}
		return true;
	}

	/**
	 * 备份文件。 如果有备份文件，先删除。
	 * 
	 * @param filePath
	 */
	public static void backupFile(String filePath) {
		String backupName = filePath + ".bak";
		File file = new File(backupName);
		boolean result = false;
		if (file.exists())
			result = file.delete();
		if(result) copyFile(filePath, backupName);
	}

	/**
	 * 取得文件扩展名
	 * 
	 * @return
	 */
	public static String getFileExt(File file) {
		if (file.isFile())
			return getFileExt(file.getName());
		return "";
	}

	/**
	 * 根据文件名获取扩展名称。
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName) {
		int pos = fileName.lastIndexOf('.');
		if (pos > -1)
			return fileName.substring(pos + 1).toLowerCase();
		return "";
	}

	/**
	 * 复制目录
	 * 
	 * @param fromDir
	 *            源目录
	 * @param toDir
	 *            目标目录
	 * @throws IOException
	 */
	public static void copyDir(String fromDir, String toDir) throws IOException {
		(new File(toDir)).mkdirs();
		File[] file = (new File(fromDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				String fromFile = file[i].getAbsolutePath();
				String toFile = toDir + "/" + file[i].getName();

				copyFile(fromFile, toFile);
			}
			if (file[i].isDirectory())
				copyDirectiory(fromDir + "/" + file[i].getName(), toDir + "/" + file[i].getName());
		}
	}

	/**
	 * 递规调用目录拷贝。
	 * 
	 * @param fromDir
	 *            源目录
	 * @param toDir
	 *            目标目录
	 * @throws IOException
	 */
	private static void copyDirectiory(String fromDir, String toDir) throws IOException {
		(new File(toDir)).mkdirs();
		File[] file = (new File(fromDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				String fromName = file[i].getAbsolutePath();
				String toFile = toDir + File.separator + file[i].getName();
				copyFile(fromName, toFile);
			}
			if (file[i].isDirectory())
				copyDirectiory(fromDir + File.separator + file[i].getName(),
						toDir + File.separator + file[i].getName());
		}
	}

	/**
	 * 取得文件大小
	 * 
	 * @return 返回文件大小
	 * @throws IOException
	 */
	public static String getFileSize(File file) throws IOException {
		if (file.isFile()) {
			int size = 0;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				size = fis.available();
			} catch (Exception e) {
				logger.error(e.getMessage(), e.getCause());
			}finally {
				if(null != fis)fis.close();
			}
			
			return getSize((double) size);
		}
		return "";
	}

	/**
	 * 根据字节大小获取带单位的大小。
	 * 
	 * @param size
	 * @return
	 */
	public static String getSize(double size) {
		DecimalFormat df = new DecimalFormat("0.00");
		if (size > 1024 * 1024) {
			double ss = size / (1024 * 1024);
			return df.format(ss) + " M";
		} else if (size > 1024) {
			double ss = size / 1024;
			return df.format(ss) + " KB";
		} else {
			return size + " bytes";
		}
	}

	/**
	 * 取得文件的相对父目录
	 * 
	 * @param baseDir
	 *            基准目录
	 * @param currentFile
	 *            当前文件
	 * @return 相对基准目录路径
	 */
	public static String getParentDir(String baseDir, String currentFile) {
		File f = new File(currentFile);
		String parentPath = f.getParent();
		String path = parentPath.replace(baseDir, "");
		return path.replace(File.separator, "/");
	}

	/**
	 * 根据键在属性文件中获取数据。
	 * 
	 * @param fileName
	 *            属性文件名称。
	 * @param key
	 *            属性的键值。
	 * @return
	 */
	public static String readFromProperties(String fileName, String key) {
		String value = "";
		InputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream(fileName));
			Properties prop = new Properties();
			prop.load(stream);
			value = prop.getProperty(key);
		} catch (Exception e) {
			logger.error("read file failed {}.", e.getMessage());
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException ignore) {}
		}
		return value;
	}

	/**
	 * 保存属性文件。
	 * 
	 * @param fileName
	 *            文件名
	 * @param key
	 *            键名
	 * @param value
	 *            键值
	 * @return 保存是否成功。
	 */
	public static boolean saveProperties(String fileName, String key, String value) {
		StringBuilder tempProp = new StringBuilder();
		boolean isFound = false;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StringPool.UTF_8));
			String str;
			while ((str = in.readLine()) != null) {
				if (str.startsWith(key)) {
					tempProp.append(key + "=" + value + "\r\n");
					isFound = true;
				} else {
					tempProp.append(str + "\r\n");
				}
			}
			// 添加新的键值。
			if (!isFound) {
				tempProp.append(key + "=" + value + "\r\n");
			}
			FileUtil.writeFile(fileName, tempProp.toString(), StringPool.UTF_8);
			return true;
		} catch (Exception ex) {
			logger.error("write properties failed {}.", ex.getMessage());
			return false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ignore) {}
		}
	}

	/**
	 * 删除属性key。
	 * 
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static boolean delProperties(String fileName, String key) {
		StringBuilder tempProp = new StringBuilder();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String str;
			while ((str = in.readLine()) != null) {
				if (!str.startsWith(key)) {
					tempProp.append(str + "\r\n");
				}
			}
			FileUtil.writeFile(fileName, tempProp.toString(), "utf-8");
			return true;
		} catch (Exception ex) {
			logger.error("write properties failed {}.", ex.getMessage());
			return false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ignore) {}
		}
	}

	/**
	 * 获取接口的所有实现类
	 * 
	 * @param interfaceClass
	 *            接口类
	 * @param samePackage
	 *            是否为同一包路径下
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static List<Class<?>> getAllClassesByInterface(Class<?> interfaceClass, boolean samePackage)
			throws IOException, ClassNotFoundException {

		if (!interfaceClass.isInterface()) {
			throw new IllegalStateException("Class not a interface.");
		}

		ClassLoader loader = interfaceClass.getClassLoader();
		/** 获取包名称 */
		String packageName = samePackage ? interfaceClass.getPackage().getName() : "/";
		return findClasses(interfaceClass, loader, packageName);
	}

	/**
	 * 获取实现接口的实现类文件
	 * 
	 * @param interfaceClass
	 * @param loader
	 * @param packageName
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(Class<?> interfaceClass, ClassLoader loader, String packageName)
			throws IOException, ClassNotFoundException {

		List<Class<?>> allClasses = new ArrayList<Class<?>>();
		/** 获取包路径 */
		String packagePath = packageName.replace(".", "/");
		if (!packagePath.equals("/")) {
			Enumeration<URL> resources = loader.getResources(packagePath);
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				allClasses.addAll(findResources(interfaceClass, new File(url.getFile()), packageName));
			}
		} else {
			String path = loader.getResource("").getPath();
			allClasses.addAll(findResources(interfaceClass, new File(path), packageName));
		}
		return allClasses;
	}

	/**
	 * 获取文件资源信息
	 * 
	 * @param interfaceClass
	 * @param directory
	 * @param packageName
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findResources(Class<?> interfaceClass, File directory, String packageName)
			throws ClassNotFoundException {

		List<Class<?>> results = new ArrayList<Class<?>>();
		if (!directory.exists())
			return Collections.emptyList();
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				// 文件夹-->继续遍历
				if (!file.getName().contains(".")) {
					if (!packageName.equals("/")) {
						results.addAll(findResources(interfaceClass, file, packageName + "." + file.getName()));
					} else {
						results.addAll(findResources(interfaceClass, file, file.getName()));
					}
				}
			} else if (file.getName().endsWith(".class")) {
				Class<?> clazz = null;
				if (!packageName.equals("/")) {
					clazz = Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6));
				} else {
					clazz = Class.forName(file.getName().substring(0, file.getName().length() - 6));
				}
				if (interfaceClass.isAssignableFrom(clazz) && !interfaceClass.equals(clazz)) {
					results.add(clazz);
				}
			}
		}
		return results;
	}

	/**
	 * 获取classes路径。
	 * 
	 * @return 返回如下的路径E:/ibps/ibps-base-root/modules/ibps-base-core/target/
	 *         classes 。
	 */
	public static String getClassesPath() {
		String path = FileUtil.class.getClassLoader().getResource("").getPath();
		if ("\\".equals(File.separator)) {
			path = StringUtil.trimPrefix(path, "/");
		}
		path = path.replace("\\", "/");
		path = StringUtil.trimSuffix(path, "/");
		return path.replaceAll("%20", " ");
	}

	/**
	 * 获取应用程序根路径。
	 * 
	 * @return 返回如下路径 E:/ibps/src/main/webapp
	 */
	public static String getWebRootPath() {
		String path = getClassesPath();
		path = path.substring(0, path.lastIndexOf("WEB-INF"));
		path = StringUtil.trimSuffix(path, "/");
		return path;
	}

	/**
	 * 合并文件路径
	 *
	 * @param path
	 * @return
	 */
	public static String pathJoin(String[] path) {
		StringBuilder pathBuilder = new StringBuilder();
		int i = 0;
		int j = path == null ? 0 : path.length;
		while (i < j) {
			String str = path[i];
			if (str == null)
				str = "";
			if ((i > 0) && ((str.startsWith("/")) || (str.startsWith("\\"))))
				str = str.substring(1);
			pathBuilder.append(str);
			if ((i + 1 < j) && (!str.endsWith("/")) && (!str.endsWith("\\")))
				pathBuilder.append("/");
			i++;
		}
		return pathBuilder.toString();
	}
	
	public static File[] list2Array(List<File> files){
		File[] rs = null;
		
		if(BeanUtils.isEmpty(files)){
			return rs;
		}
		
		rs = new File[files.size()];
		for(int i = 0, len = files.size(); i < len; i ++){
			rs[i] = files.get(i);
		}
		
		return rs;
	}

	public static List<File> list(String path, final String endWith){
		return list(path, endWith, -1);
	}
	
	public static List<File> list(String path, final String endWith, int level){
		if(level == 0){
			return Collections.emptyList();
		}
		
		if(StringUtil.isBlank(path)){
			return Collections.emptyList();
		}
		
		File parent = new File(path);
		if(!parent.exists()){
			return Collections.emptyList();
		}
		
		if(parent.isFile()){
			return Collections.emptyList();
		}
		
		List<File> rsList = new ArrayList<File>();
		List<File> tempRsList = null;
		File[] rsFiles = null;
		if(level < 0){
			if(StringPool.ASTERISK.equals(endWith)){
				rsFiles = parent.listFiles();
				if(BeanUtils.isNotEmpty(rsFiles)){
					for(File file : rsFiles){
						if(file.isFile()){
							rsList.add(file);
						}else{
							tempRsList = list(file.getAbsolutePath(), endWith, -1);
							if(BeanUtils.isNotEmpty(tempRsList)){
								rsList.addAll(tempRsList);
							}
						}
					}
				}
			}else{
				rsFiles = parent.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isFile() && pathname.getName().endsWith(endWith);
					}
				});
				if(BeanUtils.isNotEmpty(rsFiles)){
					rsList.addAll(Arrays.asList(rsFiles));
				}
				
				File[] tempRsFiles = parent.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isDirectory();
					}
				});
				if(BeanUtils.isNotEmpty(tempRsFiles)){
					for(File file : tempRsFiles){
						tempRsList = list(file.getAbsolutePath(), endWith, -1);
						if(BeanUtils.isNotEmpty(tempRsList)){
							rsList.addAll(tempRsList);
						}
					}
				}
			}
		}else{
			if(StringPool.ASTERISK.equals(endWith)){
				rsFiles = parent.listFiles();
				if(BeanUtils.isNotEmpty(rsFiles)){
					for(File file : rsFiles){
						if(file.isFile()){
							rsList.add(file);
						}else{
							tempRsList = list(file.getAbsolutePath(), endWith, level - 1);
							if(BeanUtils.isNotEmpty(tempRsList)){
								rsList.addAll(tempRsList);
							}
						}
					}
				}
			}else{
				rsFiles = parent.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isFile() && pathname.getName().endsWith(endWith);
					}
				});
				if(BeanUtils.isNotEmpty(rsFiles)){
					rsList.addAll(Arrays.asList(rsFiles));
				}
				
				File[] tempRsFiles = parent.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isDirectory();
					}
				});
				if(BeanUtils.isNotEmpty(tempRsFiles)){
					for(File file : tempRsFiles){
						tempRsList = list(file.getAbsolutePath(), endWith, level - 1);
						if(BeanUtils.isNotEmpty(tempRsList)){
							rsList.addAll(tempRsList);
						}
					}
				}
			}
		}
		
		return rsList;
	}
	
	/**
     * 查找该目录下的所有的jar文件
     *
     * @param jarPath
     * @throws Exception
     */
    public static String getJarFiles(String baseJarDir){
        File sourceFile = new File(baseJarDir);
        final StringBuilder jars = new StringBuilder();
        String osName = System.getProperty("os.name").toLowerCase();
        final String splt = osName.contains("win") ? ";" : ":";
        if (sourceFile.exists() && sourceFile.isDirectory()) {// 文件或者目录必须存在
            // 得到该目录下以.java结尾的文件或者目录
            sourceFile.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                    	jars.append(getJarFiles(pathname.getPath()));
                        return true;
                    } else {
                        String name = pathname.getName();
                        if (name.endsWith(".jar") ? true : false) {
                            jars.append(pathname.getPath()).append(splt);
                            return true;
                        }
                        return false;
                    }
                }
            });
        }
        
        return jars.toString();
    }
	
}
