package cn.com.liurz.util.nio;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class TestBuffer {
    private Logger logger= LoggerFactory.getLogger(TestBuffer.class);
    @Test
    public void test1(){
        // 创建一个新的缓冲区，并指定大小为1024
        ByteBuffer byteBuffer=  ByteBuffer.allocate(1024);
        logger.info("缓冲区当前的位置："+byteBuffer.position()); // 0 表示当前位置为0
        logger.info("缓冲区界限："+byteBuffer.limit()); // 1024 表示界限为1024，前1024个位置数据是允许我们读写的
        logger.info("缓冲区最大容量："+byteBuffer.capacity()); //  1024 表示容量大小为1024

        // 向缓冲区加入数据
        String data="restsdddd22";
        byteBuffer.put(data.getBytes());
        logger.info("缓冲区当前的位置："+byteBuffer.position()); // 11表示下一个可以写入的位置是11，
        // 因为我们写入的字节是11个，从0开始已经写了11个，位置为12的position为11
        logger.info("缓冲区界限："+byteBuffer.limit()); // 1024 表示界限为1024，前1024个位置数据是允许我们读写的
        logger.info("缓冲区最大容量："+byteBuffer.capacity()); // 1024 表示容量大小为1024

        //切换读取模式
        byteBuffer.flip();
        logger.info("缓冲区当前的位置："+byteBuffer.position()); // 0 表示从0开始读取
        logger.info("缓冲区界限："+byteBuffer.limit()); // 11 表示界限，表示前11个位置有数据可以读取
        logger.info("缓冲区最大容量："+byteBuffer.capacity()); // 1024 表示容量大小为1024

        // 读取缓冲区数据 get
        byte[] datas = new byte[byteBuffer.limit()]; // 创建一个界面为limit字节数组
        byteBuffer.get(datas); // 批量将limit大小的字节写入datas字节数组中
        logger.info("输出读取缓冲区数据："+new String(datas)); //
        logger.info("缓冲区当前的位置："+byteBuffer.position()); // 11 读取的位置变为11，因为前面的11字节已经全部读取了，下一个可以读取的位置为11
        logger.info("缓冲区界限："+byteBuffer.limit()); // 11 可读取的界限为11
        logger.info("缓冲区最大容量："+byteBuffer.capacity()); // 1024 表示容量大小为1024

        // 可重复读rewind
        byteBuffer.rewind(); // 将位置设置为0，从0开始读取
        logger.info("可重复读==缓冲区当前的位置："+byteBuffer.position()); //  0 表示从0开始读取
        logger.info("可重复读==缓冲区界限："+byteBuffer.limit()); // 11 可读取的界限为11
        logger.info("可重复读==缓冲区最大容量："+byteBuffer.capacity()); // 1024 表示容量大小为1024

        // 清理缓冲区 clear 缓冲区数据依然存在，处于被遗忘的状态
        byteBuffer.clear();
        logger.info("清理缓冲区==缓冲区当前的位置："+byteBuffer.position()); //  0 表示从0开始读取
        logger.info("清理缓冲区==缓冲区界限："+byteBuffer.limit()); // 11 可读取的界限为11
        logger.info("清理缓冲区==缓冲区最大容量："+byteBuffer.capacity()); // 1024 表示容量大小为1024
    }
}
