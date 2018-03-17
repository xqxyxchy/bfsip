package org.bfsip.zuul.filter;

import org.bfsip.common.constants.StringPool;

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

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();

		ctx.set(StringPool.IS_SUCCESS, true);
		ctx.set(StringPool.FROM_GATEWAY, true);
		
		return null;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		/*
		 * 如果前一个过滤器的结果为true，则说明上一个过滤器成功了，需要进入当前的过滤;
		 * 如果前一个过滤器的结果为false，则说明上一个过滤器没有成功，则无需进行下面的过滤动作了，直接跳过后面的所有过滤器并返回结果;
		 */
		
		return ctx.containsKey(StringPool.IS_SUCCESS) && null != ctx.get(StringPool.IS_SUCCESS) && Boolean.valueOf(ctx.get(StringPool.IS_SUCCESS).toString());
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
