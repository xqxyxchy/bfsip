package org.bfsip.auth.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/** 
 * 认证授权中心-主类
 *
 * <pre> 
 * project: bfsip-auth-server
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月9日-下午9:36:37
 * rights: eddy
 * </pre>
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class })
public class AuthServerApplication {

	protected static final String DEF_PROP_FILE = "application.properties";

	public static void main(String[] args) throws IOException {
		SpringApplication app = new SpringApplication(AuthServerApplication.class);
		app.setDefaultProperties(getDefProperties());
		app.run(args);
	}

	protected static Properties getDefProperties() throws IOException {
		Properties p = new Properties();
		InputStream in = AuthServerApplication.class.getClassLoader().getResourceAsStream(DEF_PROP_FILE);
		p.load(in);

		return p;
	}

}