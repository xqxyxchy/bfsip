package org.bfsip.zuul;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * zuul还提供了一类特殊的过滤器，分别为：StaticResponseFilter和SurgicalDebugFilter
 * StaticResponseFilter：StaticResponseFilter允许从Zuul本身生成响应，而不是将请求转发到源。
 * SurgicalDebugFilter：SurgicalDebugFilter允许将特定请求路由到分隔的调试集群或主机。
 * 
 * <pre> 
 * 作者：eddy
 * 邮箱：1546077710@qq.com
 * 日期：2018年3月4日-下午2:38:19
 * 版权：eddy
 * </pre>
 */
public class GatewayZuulFilter extends ZuulFilter {

	//@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		String accessToken = request.getParameter("access_token");
		if (null != accessToken) {
			ctx.setSendZuulResponse(true);
			ctx.setResponseStatusCode(200);
			ctx.set("isSuccess", true);
			request.setAttribute("from_gateway", true);
			return null;
		} else {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			
			StringBuilder builder = new StringBuilder();
			builder.append("{");
			builder.append("\"");
			builder.append("result");
			builder.append("\"");
			builder.append(":");
			builder.append("1");
			builder.append(",");
			builder.append("\"");
			builder.append("cause");
			builder.append("\"");
			builder.append(":");
			builder.append("\"");
			builder.append("access token is not correct!");
			builder.append("\"");
			builder.append("}");
			
			ctx.setResponseBody(builder.toString());
			ctx.set("isSuccess", false);
			return null;
		}
	}

	//@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		/*
		 * 如果前一个过滤器的结果为true，则说明上一个过滤器成功了，需要进入当前的过滤;
		 * 如果前一个过滤器的结果为false，则说明上一个过滤器没有成功，则无需进行下面的过滤动作了，直接跳过后面的所有过滤器并返回结果;
		 */
		
		return ctx.containsKey("isSuccess") && null != ctx.get("isSuccess") && Boolean.valueOf(ctx.get("isSuccess").toString());
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
