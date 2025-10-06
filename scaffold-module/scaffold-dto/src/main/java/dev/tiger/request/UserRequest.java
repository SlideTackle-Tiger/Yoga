package dev.tiger.request;

import lombok.Data;

/**
 * @ClassName UserRequest
 * @Description 用于接收前端请求
 * @Author tiger
 * @Date 2025/10/5 12:07
 */
@Data
public class UserRequest {

    private Long id;

    private String username;
}
