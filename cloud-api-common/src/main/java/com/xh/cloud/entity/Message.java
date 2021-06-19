package com.xh.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author gg5
 * @description some desc
 * @date 2021/5/28 3:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;    //id
    private String msg; //消息
    private LocalDateTime sendTime;  //时间戳
}
