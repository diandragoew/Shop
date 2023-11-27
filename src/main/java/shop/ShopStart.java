package shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import shop.dao.DatabaseConfig;

import javax.sql.DataSource;

@SpringBootApplication
@ComponentScan(basePackages = "shop")
@EntityScan(basePackages = "shop.model")
@EnableJpaRepositories(basePackages = "shop.dao")
public class ShopStart {
    public static void main(String[] args) {
        SpringApplication.run(ShopStart.class, args);
    }
}