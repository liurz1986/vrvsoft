package cn.com.liurz.util.zookeeper;/*
package com.liurz.util.zookeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZkDemo {
	
	private static ZooKeeper zk;
    static{	
         try {
			zk = new ZooKeeper("192.168.110.100:2181", 3000, new Watcher() {
				
				public void process(WatchedEvent event) {
					// TODO Auto-generated method stub
					
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    */
/**
     *
     * @throws InterruptedException 
     * @throws KeeperException 
     * @throws UnsupportedEncodingException 
     *//*

    public static void main(String[] args) throws KeeperException, InterruptedException, UnsupportedEncodingException {
    	    zk.create("/path01", "data01".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    	    zk.create("/path01/path01", "data01/data01".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    	    System.out.println(new String(zk.getData("/path01", false, null)));
    	    System.out.println(zk.getChildren("/path01", true));
    	    zk.create("/path01/path02", "data01/data02".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    	    System.out.println(zk.getChildren("/path01", true));
    	    zk.setData("/path01/path01", "data01/data01-01".getBytes(), -1);
    	    byte[] datas = zk.getData("/path01/path01", true, null);
    	    String str = new String(datas, "utf-8");
    	    System.out.println(str);

    	    zk.delete("/path01/path01", -1);
    	    zk.delete("/path01/path02", -1);
    	    zk.delete("/path01", -1);
    	    System.out.println(str);
    	    Thread.sleep(15000);
    	    zk.close();
    	    System.out.println("OK");
	}
   
}
*/
