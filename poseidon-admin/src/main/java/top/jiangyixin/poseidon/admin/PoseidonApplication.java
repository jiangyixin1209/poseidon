package top.jiangyixin.poseidon.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jiangyixin
 */
@SpringBootApplication
@MapperScan("top.jiangyixin.poseidon.admin.mapper")
public class PoseidonApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoseidonApplication.class, args);
    }
}
