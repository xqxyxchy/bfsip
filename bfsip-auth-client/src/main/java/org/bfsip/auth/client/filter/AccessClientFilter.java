package org.bfsip.auth.client.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bfsip.common.constants.StringPool;
import org.bfsip.common.entity.APIResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessClientFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpRequest.setCharacterEncoding(StringPool.UTF_8);
		httpResponse.setCharacterEncoding(StringPool.UTF_8);
		
		String fromGateway = httpRequest.getHeader(StringPool.FROM_GATEWAY);
	    
	    if(logger.isDebugEnabled()){
			logger.debug("Request to {}.", httpRequest.getRequestURI());
			logger.debug("FromGateway is {}.", fromGateway);
		}
	    
        if(!StringPool.Y.equalsIgnoreCase(fromGateway)){
        	APIResult result = new APIResult();
        	result.setResult(APIResult.FAIL);
        	result.setCause("非法请求！");
        	response.getWriter().print(result.toString());
        }else{
        	chain.doFilter(request, response);
        }
	}

	@Override
	public void destroy() {}

}
