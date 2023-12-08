package cn.com.liurz.util.file;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

/**
 * 文件上传和文件下载
 * 
 * @author Administrator
 *
 */
public class FileUtil {
	static Logger log = LoggerFactory.getLogger(FileUtil.class);

	/*******************************文件上传****************************************/
	/**
	 * springMVC 文件上传
	 * @param request
	 * @param uploadFilePath
	 */
	public static boolean upload(HttpServletRequest request, String uploadFilePath) {
		
			MultipartFile file = getUploadMultipartFile(request);
			try {
				file.transferTo(new File(uploadFilePath));
				log.info("file upload success");
				return true;
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("upload file IllegalStateException:" + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("upload file IOException:" + e.getMessage());
			}
			
		
		return false;
	}

	/**
	 * springMVC 文件上传
	 * Spring MVC会将上传的文件绑定到MultipartFile对象中
	 * 采用file.Transto 来保存上传的文件
	 */
	public static boolean upload(MultipartFile file, String uploadFilePath) {
		try {
			file.transferTo(new File(uploadFilePath));
			log.info("upload file success");
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("upload file IllegalStateException:" + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("upload file IOException:" + e.getMessage());
		}
		return false;
	}

	/**
	 * 通用的文件上传
	 * 
	 * @param request
	 * @param uploadFileToPath
	 *            文件上传的地址
	 * @return
	 */

	public static boolean commonUpload(HttpServletRequest request, String uploadFileToPath) {
		ServletFileUpload fileupload = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> fileitems;
		try {
			fileitems = fileupload.parseRequest(request);
			if (!fileitems.isEmpty()) {
				for (FileItem fileitem : fileitems) {
					if (null != fileitem && !"".equals(fileitem)) {
						String filepath = uploadFileToPath + File.separator + fileitem.getName();
						FileUtils.copyInputStreamToFile(fileitem.getInputStream(), new File(filepath));
					}
				}
			}
			log.info("file upload success");
			return true;
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("file upload FileUploadException:" + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("file upload IOException:" + e.getMessage());
		}

		return false;

	}

	/***
	 *通过springMVC方式 获取上传文件流
	 * @param request
	 * @return
	 */
	public static MultipartFile getUploadMultipartFile(HttpServletRequest request) {
		MultipartFile file = null;
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			@SuppressWarnings("rawtypes")
			Iterator iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				// 一次遍历所有文件
				file = multiRequest.getFile(iter.next().toString());
			}
		}
		return file;
	}
	
    /***************************文件下载****************************/
   /***
    * 文件下载原理
    * 1、设置 response 响应头
    * 2、读取文件 -- InputStream
    * 3、写出文件 -- OutputStream
    * 4、执行操作
    * 5、关闭流 （先开后关）
    */
	/**
	 * 通用的文件下载
	 * @param downFilePath
	 * @param response
	 * js无法调用到浏览器的下载处理机制和程序。
	 * @return
	 */
	public static boolean down(HttpServletResponse response,String downFilePath) {
		InputStream input=null;
		BufferedInputStream buffer=null;
		OutputStream out=null;
		try {
			//获取将下载的文件临时存放起来
			File file = new File(downFilePath);
			input = new FileInputStream(file);
			buffer = new BufferedInputStream(input);
			byte[] by = new byte[buffer.available()];// 临时存放文件
			buffer.read(by);

			//设置响应的参数
			// 重置response
			response.reset();
			// 设置application/x-download，response编码方式
			response.setContentType("application/x-download");
			// 写明要下载文件的大小
			response.setContentLength((int) file.length());
			// 设置文件名
			String fileName = URLEncoder.encode(file.getName(), "utf-8");
			response.addHeader("Content-Dispostion", "attachment;filename=" + fileName);

			//将临时文件的数据执行下载
			out = response.getOutputStream();
			out.write(by);
	
			log.info("file down success");
			return true;
		} catch (Exception e) {
			log.error("file down failure："+e.getMessage());
			return false;

		}finally{
			if(null!=input){
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null!=buffer){
				try {
					buffer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null!=out){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
