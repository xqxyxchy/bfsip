/**
 * <pre>
 * 	1 二代身份证号码编码规则
 * 	1.1 编码格式
 * 	1999年我国颁发了第二代居民身份证号，公民身份号码为18位，且终身不变。
 * 	居民身份证格式如：ABCDEFYYYYMMDDXXXR
 *	1.1.1地址码（ABCDEF）
 *	表示登记户口时所在地的行政区划代码（省、市、县），如果行政区划进行了重新划分，同一个地方进行户口登记的可能存在地址码不一致的情况。行政区划代码按GB/T2260的规定执行。
 *	1.1.2 出生日期码（YYYYMMDD）
 *	表示该居民的出生年月日，年4位数字，月和日分别用2位数字表示，如19491001，；出生日期码是按GB/T 7408的规定执行的。
 *	1.1.3 顺序码（XXX）
 *	表示同一地址码区域内，同年、同月、同日生的人所编订的顺序号，根据自己身份证的顺序码就可以知道：与我们同年同月同日生的同性至少有多少个，且在我们之前登记户籍的有多少人。身份证顺序码的奇数分配给男性，偶数分配给女性。这就是为什么倒数第二位奇数表示男生，偶数表示女生。
 *	1.1.4 校验码（R）
 *	R之前的17位被称为本体码，R是根据本体码，按照校验码算法(ISO 7064:1983,MOD 11-2校)计算出来的。当我们输入身份号码进行实名认证的时候，根据校验码算法可以初步判断你输入身份证号码格式是否正确。
 *	1.2 校验码算法
 *	将本体码各位数字乘以对应加权因子并求和，除以11得到余数，根据余数通过校验码对照表查得校验码。
 *	1.2.1 加权因子
 *	位置序号	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17
 *	加权因子	7	9	10	5	8	4	2	1	6	3	7	9	10	5	8	4	2
 *	（本体码每个位置对应的加权因子）
 *	1.2.2 校验码表
 *	余数		0	1	2	3	4	5	6	7	8	9	10
 *	校验码	1	0	X	9	8	7	6	5	4	3	2
 *	（每个余数对应的校验码）
 *	1.2.3 应用举例
 *	某公民的身份证号码是34052419800101001X
 *	第一步：本体码乘以加权因子：
 *	3*7+4*9+0*10+……0*4+1*2=189
 *	第二步：计算求和后除以11的余数
 *	189%11=2
 *	第三步：在检验码中查询余数对应的检验码
 *	2所对应的校验码是X，注意X必须大写
 * </pre>
 */
package org.bfsip.tools.provider;

import org.bfsip.common.entity.APIResult;
import org.bfsip.tools.biz.IdentityValidator;
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
@RequestMapping("/tool/identity/")
public class IdentityController {
	
	@RequestMapping("verify/{number}")
	public String save(@PathVariable("number") String number){
		APIResult result = IdentityValidator.getResult(number);
		
		return result.toString();
	}
	
}
