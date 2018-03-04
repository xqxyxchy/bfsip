package org.bfsip.tools.provider;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello/")
public class HelloWorldController {

	@RequestMapping("world/{name}")
	public String save(@PathVariable("name") String name){
		return "{\"result\" : 0, \"data\" : \"" + name + "，Hello World！\"}";
	}
	
}
