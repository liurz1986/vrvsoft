package cn.com.liurz.util.email;/*
package com.liurz.util.email;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

public class Mail {

	protected Logger log = LoggerFactory.getLogger(Mail.class);
	final static String DefaultHostname = "smtp.qq.com";// 发邮件服务器为默认qq
	final static String DefaultProptocol = "pop3";// 收邮件服务器默认pop3
	final static String DefaultPort = "25";// 邮件服务器端口号默认为25
	final static String DefaultUserName="451015469";
	final static String DefaultPassword="mwktvycoemkkbhig";//密码(授权码)
	final static boolean DefaultNeedAuth=true;
	final static String DefaultFrom="451015469@qq.com";// 发件人的邮箱
	final static String DefaultTimeOut="2500";
	// 发邮件服务器
	protected String hostname;
	// 收邮件服务器、 收邮件协议
	protected String proptocol;
	// 邮件服务器端口号
	protected String port;
	// 邮件对象
	protected MimeMessage mimeMessage;
	// 邮件会话对象
	protected Session session;
	// 系统属性
	protected Properties properties;
	// smtp是否需要认证
	protected boolean needAuth;
	// smtp认证用户名
	protected String userName;
	// smtp认证密码(授权码)
	protected String password;
	// Multipart对象，邮件内容，标题，附件内容等添加到其中后再生产MimeMessage对象
	protected Multipart multipart;
	// 发件人
	protected String from;
	// 收件人
	protected String[] to;
	// 抄送人
	protected String[] cc;
	// 邮件标题
	protected String mailSubject;
	// 邮件主题
	protected String mailBody;
	// 邮件附件路径
	protected String fileAffixPath;
	//邮件超时时间
	protected String timeOut;
    
	public Mail(){
		
	}


	protected void setMail(String hostname, String proptocol, String port, String userName, String password, String from,
			String[] to, String[] cc, String mailSubject, String mailBody, String fileAffixPath, boolean needAuth,String timeOut) {
		this.hostname = hostname;
		this.proptocol = proptocol;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.mailSubject = mailSubject;
		this.mailBody = mailBody;
		this.fileAffixPath = fileAffixPath;
		this.needAuth = needAuth;
		this.timeOut=timeOut;
	}

	*/
/**
	 * 初始化数据
	 *//*

	protected void init() {
		setSmtpHost(hostname, proptocol, port);
		setNeedAuth(needAuth);
		createMimeMessage();
		setSubject(mailSubject);
		setMailBody(mailBody);
		setFileAffix(fileAffixPath);
		if (!setFrom(from)) {
			return;
		}
		if (!setTo(to)) {
			return;
		}
		setCopyTo(cc);

	}

	// 是否要求身份认证
	private void setNeedAuth(boolean needAuth) {
		if (null == properties) {
			properties = System.getProperties();// 获取系统属性对象
			if (needAuth) {
				properties.put("mail.smtp.auth", "true");
			} else {
				properties.put("mail.smtp.auth", "false");
			}
		}
		this.needAuth = needAuth;
	}
	*/
/**
	 * 设置邮件发送服务器
	 * 
	 * @param smtp
	 *//*

	private void setSmtpHost(String hostname, String proptocol, String port) {
		if (null == properties) {
			properties = System.getProperties();// 获取系统属性对象
			properties.put("mail.smtp.host", hostname);
			properties.put("mail.smtp.port", port); // SMTP邮件服务器默认端口
			properties.put("mail.store.protocol", proptocol);// 是否要求身份认证

		}
	}

	*/
/**
	 * 创建Mime邮件对象
	 *//*

	private void createMimeMessage() {
		// 创建Session实例对象，邮件会话对象
		session = Session.getInstance(properties);
		//session.setDebug(true);// 在后台打印发送邮件的实时信息
		// 创建MimeMessage实例对象
		mimeMessage = new MimeMessage(session);
		// 创建一个MIME子类型为"mixed"的MimeMultipart对象，表示这是一封混合组合类型的邮件
		multipart = new MimeMultipart("mixed");
	}

	*/
/**
	 * 设置邮件主题
	 * 
	 * @param mailSubject
	 *//*

	private void setSubject(String mailSubject) {
		try {
			mimeMessage.setSubject(mailSubject);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("set mail subject failure:" + e.getMessage());
		}
	}

	*/
/**
	 * 设置邮件正文
	 * 
	 * @param mailSubject
	 *//*

	private void setMailBody(String mailBody) {

		try {
			BodyPart body = new MimeBodyPart();
			body.setContent("" + mailBody, "text/html;charset=GBK");
			multipart.addBodyPart(body);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("set mail body failure:" + e.getMessage());
		}
	}

	*/
/**
	 * 设置邮件附件
	 * 
	 * @param mailSubject
	 *//*

	private void setFileAffix(String fileAffixPath) {
		try {
			MimeBodyPart attach2 = new MimeBodyPart();
			if (!StringUtils.isEmpty(fileAffixPath)) {
				DataSource ds2 = new FileDataSource(fileAffixPath); // 附件的路径
				DataHandler dh2 = new DataHandler(ds2);
				attach2.setDataHandler(dh2);
				attach2.setFileName(MimeUtility.encodeText(ds2.getName()));
				multipart.addBodyPart(attach2);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	*/
/**
	 * 设置发件人
	 * 
	 * @param form
	 *//*

	private boolean setFrom(String from) {
		if (null == from) {
			log.info("发件人为空");
			return false;
		}
		try {
			mimeMessage.setFrom(new InternetAddress(from));
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("set from failure:" + e.getMessage());
			return false;
		}
	}

	*/
/**
	 * 设置收件人
	 * 
	 * @param to
	 *//*

	private boolean setTo(String[] to) {
		if (null == to) {
			log.info("收件人为空");
			return false;

		} else {
			try {
				mimeMessage.setRecipients(RecipientType.TO, InternetAddressParse(to));
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("set recipients to failure:" + e.getMessage());
				return false;
			}

		}
	}

	*/
/**
	 * 设置抄送人
	 * 
	 * @param copyto
	 *//*

	private void setCopyTo(String[] copyto) {
		if (null != copyto) {
			try {
				mimeMessage.setRecipients(RecipientType.CC, InternetAddressParse(copyto));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("set recipients cc failure:" + e.getMessage());
			}
		}
	}
	
	
	private Address[] InternetAddressParse(String[] address) throws AddressException{
		Address[] addresss=new Address[0];
		if(null!=address&&address.length>0){
		     addresss=new Address[address.length];
			for(int i=0;i<address.length;i++){
				
				addresss[i]=InternetAddress.parse(address[i])[0];
			}
		}
		return addresss;
	}
	
	*/
/**
	 * 发送邮件
	 * 在spring中发送邮件，在本地执行会报错
	 *//*

	public boolean sendSpring() {
		try {
			JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
			// 设定邮件服务器
			senderImpl.setHost(hostname);
			// 建立邮件消息
			MimeMessage mailMessage = senderImpl.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "GBK");
			// 设置收件人,邮件接收地址
			helper.setTo(to);
			// 抄送人
			if(!StringUtils.isEmpty(cc)){
				helper.setCc(cc);
			}
			// 发件人，邮件发送地址
			helper.setFrom(from);
			// 标题
			helper.setSubject(mailSubject);
			// 邮件主体内容
			helper.setText(mailBody, true);
			// 添加附件
			if (!StringUtils.isEmpty(fileAffixPath)) {
				//ClassPathResource resource = new ClassPathResource(fileAffixPath);
			    FileSystemResource resource=new FileSystemResource(fileAffixPath);
				helper.addAttachment(resource.getFilename(), resource);
			}

			// 根据自己的情况,设置username
			senderImpl.setUsername(userName);
			senderImpl.setPassword(password); // 根据自己的情况, 设置password
			Properties prop = new Properties();
			prop.put(" mail.smtp.auth ", needAuth); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
			prop.put(" mail.smtp.timeout ",timeOut);
			senderImpl.setJavaMailProperties(prop);
			// 发送邮件
			log.info("---正在发送邮件---");
			senderImpl.send(mailMessage);
			log.info("邮件发送成功");
			return true;
		} catch (Exception e) {
			e.getStackTrace();
			log.error("邮件发送失败：" + e.getMessage());
			return true;
		}
	}
	
	*/
/**
	 * 发送邮件
	 * @return
	 * @throws MessagingException
	 *//*

	protected boolean sendCommon() {

		try {
			init();
			mimeMessage.setContent(multipart);
			// 设置发信时间
			mimeMessage.setSentDate(new Date());
			// 保存并生成最终的邮件内容
			mimeMessage.saveChanges();
			log.info("---正在发送邮件---");
			// 获得Transport实例对象
			Transport transport = session.getTransport();
			// 打开连接 :邮箱名(用户名@前面)和密码(授权码)
			transport.connect(userName, password);
			// 将message对象传递给transport对象，将邮件发送出去;其中第二个参数是所有已设好的收件人地址
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			log.info("邮件发送结束");
			return true;
		} catch (MessagingException e) {
			e.getStackTrace();
			log.error("邮件发送失败：" + e.getMessage());
			return false;
		}

	}
	
	
}*/
