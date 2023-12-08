package cn.com.liurz.util.common;

import java.math.BigDecimal;

/**
 * BigDecimal 加（add），减（subtract），乘（multiply），除（divide） 主要用于金钱
 * 
 * @ClassName: BigDecimalUtil
 * @Description: TODO
 * @author lwx393577：
 * @date 2019年8月4日 下午5:30:10
 *
 */
public class BigDecimalUtil {

	/**
	 * 四舍五入
	 * 
	 * @Title: getBigDecimalIsHalfUp
	 * @Description: TODO
	 * @param value
	 * @param newScale
	 * @return
	 * @return BigDecimal
	 */
	public BigDecimal getBigDecimalIsHalfUp(String value, int newScale) {
		return new BigDecimal(value).setScale(newScale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 直接截取
	 * 
	 * @Title: getBigDecimalIsDown
	 * @Description: TODO
	 * @param value
	 * @param newScale
	 * @return
	 * @return BigDecimal
	 */
	public BigDecimal getBigDecimalIsDown(String value, int newScale) {
		return new BigDecimal(value).setScale(newScale, BigDecimal.ROUND_DOWN);
	}

	public static void main(String[] args) {
		// 5加6.9
		BigDecimal test1 = new BigDecimal(5);
		BigDecimal test2 = new BigDecimal(6.9);
		BigDecimal sum = test1.add(test2);
		System.out.println(sum.doubleValue());

		// 11减去2.8
		BigDecimal test3 = new BigDecimal(11);
		BigDecimal test4 = new BigDecimal(2.8);
		BigDecimal substract = test3.subtract(test4);
		System.out.println(substract.doubleValue());

		// 3.9乘以1，8
		BigDecimal test5 = new BigDecimal(3.9);
		BigDecimal test6 = new BigDecimal(1.8);
		BigDecimal multiply = test5.multiply(test6);
		System.out.println(multiply.doubleValue());

		// 13除以2
		BigDecimal test7 = new BigDecimal(13);
		BigDecimal test8 = new BigDecimal(2);
		BigDecimal divide = test7.divide(test8);
		System.out.println(divide.doubleValue());

		// 保留三位小数,直接删除后面多余位数
		BigDecimal test9 = new BigDecimal(13.2324111);
		double res = test9.setScale(3, BigDecimal.ROUND_DOWN).doubleValue();
		System.out.println(res);

		// 保留三位小数,直接删除后面多余位数,进一位
		BigDecimal test10 = new BigDecimal(13.2324111);
		double res1 = test10.setScale(3, BigDecimal.ROUND_UP).doubleValue();
		System.out.println(res1);

		// 保留三位小数,四舍五入
		BigDecimal test11 = new BigDecimal(13.2325111);
		double result = test11.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println(result);

		// 保留三位小数,四舍六入
		BigDecimal test112 = new BigDecimal(13.2325111);
		double result1 = test112.setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		System.out.println(result1);

	}
}
