package cn.com.liurz.util.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密，一般用于用户注册密码进行加密.然后将加密后的结果保存在数据库中
 * MD5与SHA加密方式一样,但是加密后结果不一样
 * @author Administrator
 *
 */
public class MD5 {
    private static Logger log=LoggerFactory.getLogger(MD5.class);
    //MD5加密为16进制并全为大写
    public static String getMD5(String plainText){
    	try{
    		MessageDigest md=MessageDigest.getInstance("MD5");
    		md.update(plainText.getBytes("UTF-8"));
    		byte[] bytes=md.digest();
    		int i;
    		StringBuffer buffer=new StringBuffer("");
    		for(int offset=0;offset<bytes.length;offset++){
    			i=bytes[offset];
    			if(i<0){
    				i+=256;
    			}
    			if(i<16){
    				buffer.append("0");
    			}
    			buffer.append(Integer.toHexString(i));//toHexString把输入值转化成16进制
    		}
         	return buffer.toString().toUpperCase();	
    	}catch(NoSuchAlgorithmException e){
    		log.error("NoSuchAlgorithmException:MessageDigest.getInstance() failed",e);
    	}catch(UnsupportedEncodingException e){
    		log.error("MD5 error",e);
    	}
    	return null;
    }
  //SHA加密为16进制并全为大写
    public static String getSHA(String plainText){
    	try{
    		MessageDigest md=MessageDigest.getInstance("SHA");
    		md.update(plainText.getBytes("UTF-8"));
    		byte[] bytes=md.digest();
    		int i;
    		StringBuffer buffer=new StringBuffer("");
    		for(int offset=0;offset<bytes.length;offset++){
    			i=bytes[offset];
    			if(i<0){
    				i+=256;
    			}
    			if(i<16){
    				buffer.append("0");
    			}
    			buffer.append(Integer.toHexString(i));//toHexString把输入值转化成16进制
    		}
         	return buffer.toString().toUpperCase();	
    	}catch(NoSuchAlgorithmException e){
    		log.error("NoSuchAlgorithmException:MessageDigest.getInstance() failed",e);
    	}catch(UnsupportedEncodingException e){
    		log.error("SHA error",e);
    	}
    	return null;
    }
    public static String md52(String message){
    	StringBuilder builder=new StringBuilder();
    	MessageDigest md5=null;
    	byte[] mess=null;
    	try{
    		md5=MessageDigest.getInstance("MD5");
    		mess=message.getBytes("UTF-8");
    		md5.update(mess);
    		for(byte b:md5.digest()){
    			builder.append(String.format("%02x",b));
    		}
    	}catch(Exception e){
    		log.error("md5 error");
    	}
    	return builder.toString();
    }
    
    public static void main(String[] args) {
    	/*MD5 md=new MD5();
    	System.out.println(md.getMD5("hbyclrz1983226"));
    	//System.out.println(md.getSHA("liurz1986@"));
    	*/
	}
}
