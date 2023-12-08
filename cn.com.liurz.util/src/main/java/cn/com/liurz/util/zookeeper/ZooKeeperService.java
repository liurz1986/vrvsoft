package cn.com.liurz.util.zookeeper;/*
package com.liurz.util.zookeeper;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ZooKeeperService {
		private Logger log = LoggerFactory.getLogger(ZooKeeperService.class);
		
		// 用户名唯一
		private String nameNode = "/userCenter/uniqueName";
		// 注册ip
		public final static  String serverNode = "/server/registerIp";
		
		private static ZookeeperUtil zkUtil;

		public ZooKeeperService() {
			
		}
		*/
/**
		 * 1.将服务注册到zookeeper中
		 * 2    同时初始化存放用户的父节点
		 * @param hostnames
		 *//*

		public void init(String hostnames){
			if(null==hostnames){
				log.error("hostnames is null");
				return;
			}
	        zkUtil = new ZookeeperUtil(hostnames);
		
			if(null==zkUtil){
				log.error("zk connection fail");
				return;
			}
			// 初始化节点
			initNode(nameNode);
			initNode(serverNode);

			// 注册服务(服务启动服务节点注册服务到zookeeper中，服务关闭服务节点自动从zookeeper中删除)
			registerServer();
		}
	    */
/**
	     * 将服务注册到zookeeper中
	     *//*

		private void registerServer() {

			InetAddress address;
			try {
				// 获取的是本地的IP地址
				address = InetAddress.getLocalHost();
				String hostAddress = address.getHostAddress();// 192.168.0.121
				zkUtil.createNodeEphemeral(createPath(serverNode,hostAddress));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("获取ip店址失败");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("创建节点失败："+e.getMessage());
			} 

		}

		*/
/**
		 * 用户名是不是存在
		 * 
		 * @param name
		 * @return
		 *//*

		public boolean isUserNameExist(String name) {

			try {
				String userName = zkUtil.getData(createPath(nameNode, name));
				if (StringUtils.isEmpty(name)) {
					return false;
				} else {
					if (name.equals(userName)) {
						return true;
					}
				}
			} catch (UnsupportedEncodingException e) {
				log.error("Unsupported EncodingException:" + e.getMessage());
			} catch (Exception e) {
				log.error("KeeperException:" + e.getMessage());
			}
			return false;
		}

		*/
/**
		 * 将用户名注册到zookeeper中
		 * 
		 * @param name
		 *//*

		public void createNode(String name) {

			try {
				String path=createPath(nameNode, name);
				if(!zkUtil.isExist(path)){
					zkUtil.createPersistent(path,name.getBytes());
				}else{
					zkUtil.setData(path, name.getBytes());
				}
				
			}  catch (Exception e) {
				log.error("KeeperException:" + e.getMessage());
			}
		}

		*/
/***
		 * 获取所有注册服务
		 * @return
		 *//*

		public List<String> getServerNodes(){
			
			try {
				return zkUtil.getChildrenNodes(serverNode);
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		*/
/**
		 * 创建path
		 * 
		 * @param name
		 * @return
		 *//*

		private String createPath(String path, String name) {

			return path + "/" + name;
		}

		// 初始化节点
		private void initNode(String path) {

			try {
				String namePath = path.substring(0, path.lastIndexOf("/"));
				if (!zkUtil.isExist(namePath)) {
					zkUtil.createNodePersistent(namePath);
				}
				if (!zkUtil.isExist(path)) {

					zkUtil.createNodePersistent(path);
				}

			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
*/
