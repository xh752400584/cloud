package com.xh.cloud.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xianghui
 * @description kafka message tool
 * @date 2021/6/1 0:35
 */
@Component
public class MessageUtil {
    private static final Logger LOG = LoggerFactory.getLogger(MessageUtil.class);

    @Resource
    ObjectMapper objectMapper;

    @Resource
    KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, Object msg) throws JsonProcessingException {
        sendMessage(topic, null, System.currentTimeMillis(), String.valueOf(msg.hashCode()), msg);
    }

    private void sendMessage(String topic, Integer partition, Long timestamp, String key, Object msg) throws JsonProcessingException {
        // value 类型
        byte[] type = msg.getClass().getName().getBytes(StandardCharsets.UTF_8);
        RecordHeader header = new RecordHeader("type", type);
        List<Header> headers = new ArrayList<>();
        headers.add(header);
        String value = msg instanceof String ? (String) msg : objectMapper.writeValueAsString(msg);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, partition, timestamp, key, value, headers);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
        // 回调函数
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> sendResult) {
                LOG.info("生产者成功发送消息 topic: {}, partition: {}, message: {}", topic, partition, sendResult.getProducerRecord().value());
            }

            @Override
            public void onFailure(Throwable throwable) {
                LOG.error("生产者发送消息失败 topic: {}, message: {}, failed reason: {}", topic, value, throwable.getMessage());
            }
        });

    }

}
