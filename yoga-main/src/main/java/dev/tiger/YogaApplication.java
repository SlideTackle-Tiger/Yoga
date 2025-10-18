package dev.tiger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @ClassName dev.taiger.ScaffoldApplication
 * @Description 启动类
 * @Author tiger
 * @Date 2025/10/3 04:15
 */

@ServletComponentScan
@EnableSwagger2
@EnableKnife4j
@Slf4j
@MapperScan(basePackages = {"dev.tiger.mapper"})
@SpringBootApplication()
/**
 * 注意启动类在dev.tiger包下，那么controller、service等类也必须在dev.tiger.controller/service包下，否则会扫描不到
 * 这样即使不在同一个模块目录下也可以扫描到
 * */
//@ComponentScan(basePackages = {"dev.tiger.controller","dev.tiger.service"})

public class YogaApplication {
    public static void main(String[] args) throws UnknownHostException {
        // 启动类
        ConfigurableApplicationContext applicationContext = SpringApplication.run(YogaApplication.class, args);

        // 打印信息
        info(applicationContext);
    }

    static void info(ConfigurableApplicationContext application) throws UnknownHostException{
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String active = env.getProperty("spring.profiles.active");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        log.info("asdasd");
        log.info("\n----------------------------------------------------------\n\t" +
                "欢迎访问  \thttps://blog.javadog.net\n\t" +
                "示例程序【" + active + "】环境已启动! 地址如下:\n\t" +
                "Local: \t\thttp://localhost:" + port + contextPath + "\n\t" +
                "External: \thttp://" + ip + ':' + port + contextPath + '\n' +
                "Swagger文档: \thttp://" + ip + ":" + port + contextPath + "/doc.html\n" +
                "----------------------------------------------------------");
    }

}
