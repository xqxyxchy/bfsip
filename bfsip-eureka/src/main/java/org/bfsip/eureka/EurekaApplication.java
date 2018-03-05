package org.bfsip.eureka;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/** 
 * 服务注册中心-主类
 *
 * <pre> 
 * project: bfsip-eureka
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月5日-下午9:03:14
 * rights: eddy
 * </pre>
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {

	protected static final String DEF_PROP_FILE = "application.properties";
	
	public static void main(String[] args) throws IOException {
		run(args);
	}
	
	protected static void run(String[] args) throws IOException{
		SpringApplication app = new SpringApplication(EurekaApplication.class);
		app.setDefaultProperties(getDefProperties());
		app.run(args);
	}
	
	protected static Properties getDefProperties() throws IOException{
		Properties p = new Properties();
		InputStream in = EurekaApplication.class.getClassLoader().getResourceAsStream(DEF_PROP_FILE);
		p.load(in);
		
		return p;
	}
	
}
