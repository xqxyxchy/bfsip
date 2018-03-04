package org.bfsip.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class })
public class ProviderApplication {

	protected static final String DEF_PROP_FILE = "application.properties";

	public static void main(String[] args) throws IOException {
		SpringApplication app = new SpringApplication(ProviderApplication.class);
		app.setDefaultProperties(getDefProperties());
		app.run(args);
	}

	//@Bean
	public FilterRegistrationBean myFilter() {
		FilterRegistrationBean myFilter = new FilterRegistrationBean();
		myFilter.addUrlPatterns("/*");
		return myFilter;
	}

	protected static Properties getDefProperties() throws IOException {
		Properties p = new Properties();
		InputStream in = ProviderApplication.class.getClassLoader().getResourceAsStream(DEF_PROP_FILE);
		p.load(in);

		return p;
	}

}