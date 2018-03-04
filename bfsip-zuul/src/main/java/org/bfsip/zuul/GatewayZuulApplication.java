package org.bfsip.zuul;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class GatewayZuulApplication {

	protected static final String DEF_PROP_FILE = "application.properties";
	
	public static void main(String[] args) throws IOException {
		SpringApplication app = new SpringApplication(GatewayZuulApplication.class);
		app.setDefaultProperties(getDefProperties());
		app.run(args);
	}
	
	//@Bean
	public GatewayZuulFilter gatewayZuulFilter(){
		return new GatewayZuulFilter();
	}
	
	protected static Properties getDefProperties() throws IOException{
		Properties p = new Properties();
		InputStream in = GatewayZuulApplication.class.getClassLoader().getResourceAsStream(DEF_PROP_FILE);
		p.load(in);
		
		return p;
	}
}
