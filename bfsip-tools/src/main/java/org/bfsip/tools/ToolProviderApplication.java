package org.bfsip.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.bfsip.auth.client.filter.AccessClientFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/** 
 * 工具服务-主类
 *
 * <pre> 
 * project: bfsip-tools
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月5日-下午9:05:04
 * rights: eddy
 * </pre>
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class })
public class ToolProviderApplication {

	protected static final String DEF_PROP_FILE = "application.properties";

	public static void main(String[] args) throws IOException {
		SpringApplication app = new SpringApplication(ToolProviderApplication.class);
		app.setDefaultProperties(getDefProperties());
		app.run(args);
	}

	protected static Properties getDefProperties() throws IOException {
		Properties p = new Properties();
		InputStream in = ToolProviderApplication.class.getClassLoader().getResourceAsStream(DEF_PROP_FILE);
		p.load(in);

		return p;
	}
	
	@Bean
    public FilterRegistrationBean MyFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AccessClientFilter());
        registration.addUrlPatterns("/*");
        registration.setName("AccessClientFilter");
        registration.setOrder(1);
        return registration;
    }

}