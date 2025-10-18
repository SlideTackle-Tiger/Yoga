package dev.tiger.request;

import lombok.Data;

/**
 * @ClassName LoginRequestDTO
 * @Description
 * @Author tiger
 * @Date 2025/10/10 20:55
 */
@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}
