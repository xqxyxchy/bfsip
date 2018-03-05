package org.bfsip.tools.provider;

import org.bfsip.common.entity.APIResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 
 * Hello World
 *
 * <pre> 
 * project: bfsip-tools
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月5日-下午9:05:20
 * rights: eddy
 * </pre>
 */
@RestController
@RequestMapping("/hello/")
public class HelloWorldController {

	@RequestMapping("world/{name}")
	public String save(@PathVariable("name") String name){
		APIResult result = new APIResult(APIResult.SUCCESS);
		result.setData("Hello " + name + "!");
		return result.toString();
	}
	
}
