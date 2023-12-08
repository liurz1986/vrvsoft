package cn.com.liurz.test.base;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * LinkedBlockingQueue内部分别使用了takeLock 和 putLock 对并发进行控制。也就是说，添加和删除操作并不是互斥操作，可以同时进行，
 * 这样也就可以大大提高吞吐量。ArrayBlockingQueue添加与删除使用了同一把锁。
 */
public class QueueTest {
    // 数组对列
    ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(500);
    // 链表对列:建议制定容量，默认容量2的31次方-1.
    LinkedBlockingQueue<String> linkedBlockingQueue =new LinkedBlockingQueue<String>(10000);
    // 无容量对列
    SynchronousQueue<String> SynchronousQueue = new SynchronousQueue<String>();

    public void ArrayBlockingQueueHandle(){

    }
}
