package dev.tiger.response;

import lombok.Data;

/**
 * @ClassName UserResponse
 * @Description 用于后端返回响应给前端
 * @Author tiger
 * @Date 2025/10/5 12:08
 */
@Data
public class UserResponse {
    private Long id;

    private String username;

    private String idCard;
}
