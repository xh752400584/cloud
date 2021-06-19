package com.xh.cloud.listen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xh.cloud.entity.Employee;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gg5
 * @description some desc
 * @date 2021/5/28 3:35
 */
@Component
public class TopicMessageListener {
    private static final Logger LOG = LoggerFactory.getLogger(TopicMessageListener.class);

    @Resource
    ObjectMapper objectMapper;

    @KafkaListener(topics = "topic_registration", groupId = "myGroup")
    public void onRegistrationMessage(@Payload String message, @Header("type") String type) throws Exception {
        Object msg = objectMapper.readValue(message, getType(type));
        LOG.info("received registration message: {}", msg.toString());
    }

    /**
     * 消费单个消息
     *
     * @param record 消息体
     * @param type   消息序列化类型
     * @throws Exception 捕获异常
     */
    @KafkaListener(topics = "topic_login", groupId = "myGroup1")
    public void processLoginMessage(ConsumerRecord<?, ?> record, @Header("type") String type) throws Exception {
        LOG.info("--------------------------kafka listener ==》 topic: {}, partition: {}, message: {}", record.topic(), record.partition(), record.value().toString());
        List<Employee> msg = objectMapper.readValue((String) record.value(), getType(type));
        // todo 业务处理
    }

    /**
     * 批量消费消息
     *
     * @param record 消息体
     * @param type   消息序列化类型
     * @throws Exception 捕获异常
     */
    @KafkaListener(topics = "topic_login", groupId = "myGroup1")
    public void batchMessageConsumer(ConsumerRecord<?, ?> record, @Header("type") String type) throws Exception {
        LOG.info("--------------------------kafka listener ==》 topic: {}, partition: {}, message: {}", record.topic(), record.partition(), record.value().toString());
        List<Employee> msg = objectMapper.readValue((String) record.value(), getType(type));
        // todo 业务处理
    }


    /**
     * 获取消息所属类
     *
     * @param type 类路径
     * @param <T>  泛型
     * @return 类
     */
    private static <T> Class<T> getType(String type) {
        try {
            return (Class<T>) Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

