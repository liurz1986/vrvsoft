package cn.com.liurz.util.kafka;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    @KafkaListener(topics = KafkaProducer.topic,groupId = "test1")
    public void consumer1(ConsumerRecord<String, String> consumerRecord) {
        LOGGER.info("消息消费test2--》" +  gson.toJson(consumerRecord.value()));
    }


    @KafkaListener(topics = KafkaProducer.topic,groupId = "test2")
    public void consumer2(ConsumerRecord<String, String> consumerRecord) {
        LOGGER.info("消息消费test2--》" +  gson.toJson(consumerRecord.value()));
    }
}
