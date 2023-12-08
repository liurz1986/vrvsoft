package cn.com.liurz.util.common;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * 对称加密
 * 
 * @author Administrator
 *
 */

public class AESUtil {
	static Logger log = LoggerFactory.getLogger(AESUtil.class);
	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	private static Key secretKey = null;// 密钥
    
	public AESUtil(){
		
	}
    public AESUtil(String key){
		try {
			secretKey=toKey(key.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
	
	// 加密
	@SuppressWarnings("restriction")
	public static String AESencrypt(String result) {
		String encryData=null;
		try {
			initSecretKey();
			//log.info("加密前的数据：" + result);
			//log.info("生成key：" + new String(secretKey.getEncoded(),"utf-8"));
			byte[] by = encrypt(result.getBytes(), secretKey);
		    encryData=Base64.encode(by);//用base64对ASCCII码进行编码，为了传输方便
			log.info("加密后的数据：" + encryData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("AES encry failuer");
		}
		return encryData;
	}

	// 解密
	@SuppressWarnings("restriction")
	public static String  AESdecrypt(String encryptData) {
		String decryptDataStr=null;
		try {
			initSecretKey();
			byte[] date=Base64.decode(encryptData);//base64进行解码后，然后进行解密
		//	log.info("生成key：" + new String(secretKey.getEncoded(),"utf-8"));
			byte[] decryptData = decrypt(date, secretKey);
			decryptDataStr=new String(decryptData,"utf-8");
		//	log.info("解密后的数据：" + decryptDataStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("AES decry failuer");
		}
		return decryptDataStr;
	}

	// 初始化 自动生成密钥
	public static void initSecretKey() {
		if (null == secretKey) {
			// 返回生成指定算法的秘密密钥的 KeyGenerator 对象
			KeyGenerator kg = null;
			try {
				kg = KeyGenerator.getInstance(KEY_ALGORITHM);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			// 初始化此密钥生成器，使其具有确定的密钥大小
			// AES 要求密钥长度为 128  是共128个二进制位，换算成字节为16个（一个字节等于8个二进制位），换成字符最多16个字符
			kg.init(128);
			// 生成一个密钥
		     secretKey = kg.generateKey();
			
		}
	}

	// key转换-生成密钥
	private static Key toKey(byte[] key) {
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}

	// 生成key的内容
	public static byte[] encrypt(byte[] data, Key key) throws Exception {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}

	// 还原密钥
	public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm);
	}

	// 加密
	public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
		// 实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		// 使用密钥初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// 执行操作
		return cipher.doFinal(data);
	}

	// 解密
	public static byte[] decrypt(byte[] data,Key key) throws Exception {
		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}


	public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
		// 实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		// 使用密钥初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, key);
		// 执行操作
		return cipher.doFinal(data);
	}
	public static Key getSecretKey() {
		return secretKey;
	}

	public static void main(String[] args) {
		//String res = AESUtil.AESencrypt("222ww");
		//AESUtil.AESdecrypt(res);
		/*AESUtil aesUtil=new AESUtil("liurzliurzliurz1");
		String encry=aesUtil.AESencrypt("liurz");
		System.out.println("加密后："+encry);
		String decry=aesUtil.AESdecrypt(encry);
		System.out.println("解密后："+decry);*/
	}
	
}