package cn.com.liurz.util.email;/*
package com.liurz.util.email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

*/
/**
 * @author liurz
 * @version V1.0
 * @Package org.com.liurz.iresources.servcie.util
 * @date 2020/8/17 20:13
 * @Copyright © 2020-2028
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest
public class EamilTest {
	public static final String mailBody = "您的账号快到期，请及时申请用户权限";
	public static final String mailSubject = "权限通知";
	public static final String[] toemail = { "hgdxglrz1986@126.com" };
	public static final String form = "451015469@qq.com" ;
	@Autowired
	MailService mailService;

	@Test
	public void test() {

		mailService.sendMsg(mailBody,mailSubject,form,toemail,null);
	}

}
*/
