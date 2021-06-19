package com.xh.cloud.service;

import java.io.IOException;

/**
 * @author gg5
 * @description some desc
 * @date 2021/5/28 3:45
 */
public interface MessageService {

    void sendLoginMessage(Object msg) throws IOException;

    void sendRegistrationMessage(Object msg) throws IOException;
}
