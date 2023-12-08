package cn.com.liurz.test.kafka;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@EnableScheduling
public class KafkaProducer {
    Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public final static String topic = "kafka_data_001";

    Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    /**
     * 定时任务
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void send() {
        String uuid = UUID.randomUUID().toString();
        Map<String ,String> params = new HashMap<>();
        params.put("guid",uuid);
        params.put("time",new Date().getTime()+"");
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, gson.toJson(params));
        future.addCallback(o -> LOGGER.info("send-消息发送成功：" + gson.toJson(params)),
                throwable -> LOGGER.info("消息发送失败：" + gson.toJson(params)));
    }
}