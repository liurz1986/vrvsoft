package cn.com.liurz.util.zookeeper;/*
package com.liurz.util.zookeeper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.server.LogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

*/
/**
 * create：用于创建节点，可以指定节点路径、节点数据、节点的访问权限、节点类型
 * delete：删除节点，每个节点都有一个版本，删除时可指定删除的版本，类似乐观锁。设置-1，则就直接删除节点。
 * exists：节点存不存在，若存在返回节点Stat信息，否则返回null getChildren：获取子节点 getData/setData：获取节点数据
 * getACL/setACL：获取节点访问权限列表，每个节点都可以设置访问权限，指定只有特定的客户端才能访问和操作节点。 CreateMode:
 * PERSISTENT (持续的，相对于EPHEMERAL，不会随着client的断开而消失)
 * PERSISTENT_SEQUENTIAL（持久的且带顺序的） EPHEMERAL (短暂的，生命周期依赖于client session)
 * EPHEMERAL_SEQUENTIAL (短暂的，带顺序的) List<ACL> acl:'选择访问权限
 * Ids.OPEN_ACL_UNSAFE：完全开放的ACL权限,支持读、写、创建、删除以及管理 Ids.READ_ACL_UNSAFE:为所有人赋予读权限
 * Ids.CREATOR_ALL_ACL: 为通过认证的人赋予写权限 Ids.ANYONE_ID_UNSAFE:world 模式，代表任何人
 * Ids.AUTH_IDS:它不需要id, 只要是通过认证的用户都有权限
 * 
 * 节点创建注意事项(节点与子节点用"/"标识)
 * 创建节点时，须加上”/”。例如   /path  
 * 不能同时创建节点和子节点（节点不存在）时，必须先创建字节，然后才能创建子节点。。例如：/path/path1 
 * 删除节点时：有子节点须先删除子节点，然后删除节点。
 *//*

public class ZookeeperUtil {
	private Logger log = LoggerFactory.getLogger(ZookeeperUtil.class);
	protected final static int DefaultSesssionOut = 3000;//session过期时间
	protected final static int connectionCount = 1;// 重复连接的次数
	protected final static String DefaultHostnames="127.0.0.1:2181";//默认本地
	private static ZooKeeper zk;

	public ZookeeperUtil() {

		connection(DefaultHostnames, DefaultSesssionOut);

	}
	public ZookeeperUtil(String hostnames) {

		connection(hostnames, DefaultSesssionOut);

	}

	public ZookeeperUtil(String hostnames, int sessionOut) {

		connection(hostnames, sessionOut);

	}

	private void connection(String hostnames, int sessionOut) {
        
		if (StringUtils.isEmpty(hostnames)) {
			log.error("hostnames is null");
			return;
		}
		if(null!=zk){
			
			return;
		}
		int count = 0;
		try {
			//创建一个与服务器的连接 需要(服务端的 ip+端口号)(session过期时间)(Watcher监听注册)
			zk = new ZooKeeper(hostnames, sessionOut, new Watcher() {
				// 对zookeeper所有进行监控
				public void process(WatchedEvent event) {

					log.info("监控节点的变化");
				}
			});
			// 对zookeeper集群，如果存在zookeeper服务一个节点挂了，这时刚好连接了该节点，
			// 使用时会报错。此时要做重复连接.States.CONNECTED表示连接成功.重复连接
			if (!zk.getState().equals(States.CONNECTED)) {
				log.error("exc zk repeat connection");
				while (true) {
					if (zk.getState().equals(States.CONNECTED)) {
						break;
					}
					if (count >= connectionCount) {
						log.error("zk repeat connection failkure");
						break;
					}
					count++;
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("zk connection failure:" + e.getMessage());
		}

	}

	*/
/**
	 * 持久的创建节点,
	 * 
	 * @param path
	 * @param data
	 *//*

	public void createPersistent(String path, byte[] data) throws KeeperException, InterruptedException {

		zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
    */
/**
     * 只做创建节点用
     * @param path
     * @param data
     * 例如  path="/path"或path="node/node1"
     *//*

	public void createNodePersistent(String path) throws KeeperException, InterruptedException {

		zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	*/
/**
	 * 临时的创建节点，随着客户端连接断开而消失
	 * 
	 * @param path
	 * @param data
	 *//*

	public void createEphemeral(String path, byte[] data) throws KeeperException, InterruptedException {

		zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}
    
	*/
/**
	 * 临时的创建节点，随着客户端连接断开而消失
	 * 
	 * @param path
	 * @param data
	 *//*

	public void createNodeEphemeral(String path) throws KeeperException, InterruptedException {

		zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}
	*/
/**
	 * 修改节点数据或给节点赋值
	 * 
	 * @param path
	 * @param data
	 *//*

	public void setData(String path, byte[] data) throws KeeperException, InterruptedException {

		zk.setData(path, data, -1);
	}

	*/
/**
	 * 获取节点数据
	 * 
	 * @param path
	 *//*

	public String getData(String path) throws KeeperException, InterruptedException, UnsupportedEncodingException {

		return new String(zk.getData(path, true, null), "utf-8");

	}

	*/
/**
	 * 删除节点
	 * 
	 * @param path
	 *            -1是删除所有版本
	 *//*

	public void deleData(String path) throws InterruptedException, KeeperException {

		zk.delete(path, -1);
	}
	
	*/
/**
	 * 判断节点是否存在
	 * @param path
	 *//*

	public  boolean isExist(String path) throws KeeperException, InterruptedException{
		
		return	null!=zk.exists(path, true);
	}
	
	*/
/**
	 * 获得子节点
	 * @param path
	 *//*

	public List<String> getChildrenNodes(String path) throws KeeperException, InterruptedException{
		
		return zk.getChildren(path, true);
	}
	*/
/**
	 * 删除zk连接
	 *//*

	public void close() {
		if (null != zk) {
			try {
				zk.close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	*/
/***
	 * 主要是讲二进制日志信息，转化后通过日志输出
	 * Zookeeper的LogFormatter查看日志文件(需要slf4j-log4j.jar适配器，来输出LogFormatter类中sf4j日志信息)
	 * @param logPath 日志路径
	 * @throws Exception
	 *//*

	public void printLog(String logPath) throws Exception{
		String[] ar={logPath};
		LogFormatter.main(ar);
	}
	*/
/**
	 * 异常org.apache.zookeeper.KeeperException$NoNodeException: KeeperErrorCode = NoNode for /felix0000000000
	 * 说明：path在zookeeper中不存在
	 * @param args
	 * @throws Exception
	 *//*

	public static void main(String[] args) throws Exception {
		//String host="127.0.0.1:2180,127.0.0.1:2181,127.0.0.1:2182";
		ZookeeperUtil zk=new ZookeeperUtil();
		try {
			*/
/*zk.createNodePersistent("/node01").","子节点的值".getBytes());//创建子节点并赋值
			System.out.println("获取节点的值："+zk.getData("/node01/node001"));
			zk.setData("/node01/node001", "修改子节点的内容".getBytes());
			System.out.println("子节点修改的内容："+zk.getData("/node01/node001"));
			System.out.println("删除子节点：/node01/node001");
			zk.deleData("/node01/node001");
			System.out.println("删除节点：node01");
			zk.deleData("/node01");
			System.out.println("关闭zk连接");*//*

			//zk.createNodePersistent("/node12");//创建子节点并赋值
			//zk.createPersistent("/node01","子节点的值".getBytes());//创建子节点并赋值
			*/
/*zk.createNodePersistent("/node");
			zk.createNodePersistent("/node/node1");
			zk.createNodePersistent("/node/node2");*//*

			List<String> childrens=zk.getChildrenNodes("/node");
			System.out.println(childrens.get(0));
			zk.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

*/
