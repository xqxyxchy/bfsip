package org.bfsip.auth.client.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.bfsip.common.constants.StringPool;
import org.bfsip.common.entity.APIResult;
import org.bfsip.common.utils.StringUtil;

public class AccessClientFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
	    String fromGateway = httpRequest.getParameter(StringPool.FROM_GATEWAY);
	    
        if(StringUtil.isEmpty(fromGateway) || !Boolean.valueOf(fromGateway)){
        	APIResult result = new APIResult();
        	result.setResult(APIResult.FAIL);
        	result.setCause("非法请求！");
        	response.getWriter().print(result.toString());
        }else{
        	chain.doFilter(request, response);
        }
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
