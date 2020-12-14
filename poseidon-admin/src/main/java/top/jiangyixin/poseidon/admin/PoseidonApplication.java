package top.jiangyixin.poseidon.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 波塞冬(Poseidon) 启动类
 * @author jiangyixin
 * @since 2020-12-14
 */
@SpringBootApplication
@MapperScan("top.jiangyixin.poseidon.admin.mapper")
public class PoseidonApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoseidonApplication.class, args);
    }
}
