package org.bfsip.zuul;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.bfsip.zuul.filter.DefaultZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/** 
 * 网关服务-主类
 *
 * <pre> 
 * project: bfsip-zuul
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月5日-下午9:04:04
 * rights: eddy
 * </pre>
 */
@SpringBootApplication
@EnableZuulProxy
public class ZuulApplication {

	protected static final String DEF_PROP_FILE = "application.properties";
	
	public static void main(String[] args) throws IOException {
		SpringApplication app = new SpringApplication(ZuulApplication.class);
		app.setDefaultProperties(getDefProperties());
		app.run(args);
	}
	
	protected static Properties getDefProperties() throws IOException{
		Properties p = new Properties();
		InputStream in = ZuulApplication.class.getClassLoader().getResourceAsStream(DEF_PROP_FILE);
		p.load(in);
		
		return p;
	}
	
	//@Bean
	public DefaultZuulFilter gatewayZuulFilter(){
		return new DefaultZuulFilter();
	}
		
}
