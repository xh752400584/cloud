package com.xh.cloud.service.impl;

import com.xh.cloud.constant.MessageConstant;
import com.xh.cloud.service.MessageService;
import com.xh.cloud.util.MessageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author xianghui
 * @description some desc
 * @date 2021/5/28 3:46
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    MessageUtil messageUtil;

    @Override
    public void sendRegistrationMessage(Object msg) throws IOException {
        messageUtil.sendMessage(MessageConstant.KAFKA_TOPIC, msg);
    }

    @Override
    public void sendLoginMessage(Object msg) throws IOException {
        messageUtil.sendMessage("topic_login", msg);
    }

}
