package org.bfsip.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.bfsip.common.constants.StringPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.context.RequestContext;

/** 
 * zuul还提供了一类特殊的过滤器，分别为：StaticResponseFilter和SurgicalDebugFilter
 * StaticResponseFilter：StaticResponseFilter允许从Zuul本身生成响应，而不是将请求转发到源。
 * SurgicalDebugFilter：SurgicalDebugFilter允许将特定请求路由到分隔的调试集群或主机。
 *
 * <pre> 
 * project: bfsip-zuul
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月5日-下午9:04:33
 * rights: eddy
 * </pre>
 */
public class AccessFilter extends com.netflix.zuul.ZuulFilter {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.setSendZuulResponse(true);
		ctx.setResponseStatusCode(200);
		// 传给服务方过滤器的参数
		ctx.addZuulRequestHeader(StringPool.FROM_GATEWAY, StringPool.Y);
		// 传给网关后续过滤器的参数
		ctx.set(StringPool.IS_SUCCESS, StringPool.Y);
		
		if(logger.isDebugEnabled()){
			HttpServletRequest request = ctx.getRequest();
			logger.debug("Request to {}.", request.getRequestURL().toString());
		}
		
		return null;
	}

	/**
	 * <pre>
	 * 如果前一个过滤器的结果为true，则说明上一个过滤器成功了，需要进入当前的过滤;
	 * 如果前一个过滤器的结果为false，则说明上一个过滤器没有成功，则无需进行下面的过滤动作了，直接跳过后面的所有过滤器并返回结果;
	 * </pre>
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	/**
	 *<pre>
	 * pre------>前置
	 * route---->转发
	 * post----->后置
	 * custom--->自定义
	 * error---->错误
	 *</pre>
	 */
	@Override
	public String filterType() {
		return "pre";
	}

}
