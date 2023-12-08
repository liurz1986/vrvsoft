package cn.com.liurz.util.nio;

/**
 * NIO
 * https://blog.csdn.net/K_520_W/article/details/123454627
 * NIO支持面向缓冲区的、基于通道的IO操作。NIO将以更加高效的方式进行文件的读写操作
 *NIO可以理解为非阻塞IO,传统的IO的read和write只能阻塞执行，线程在读写IO期间不能干其他事情，
 * 比如调用socket.read()时，如果服务器一直没有数据传输过来，线程就一直阻塞，而NIO中可以配置socket为非阻塞模式。
 * NIO有三大核心部分:Channel(通道)，Buffer(缓冲区), Selector(选择器)
 *
 *
 * NIO是可以做到用一个线程来处理多个操作的。假设有1000个请求过来,根据实际情况，
 * 可以分配20或者80个线程来处理。不像之前的阻塞IO那样，非得分配1000个。
 */
public class NIODemo {
}
