package shop;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.TargetType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import shop.dao.DatabaseConfig;
import shop.dto.CreateUserDto;
import shop.model.Ad;
import shop.model.Message;
import shop.model.Photo;
import shop.model.User;
import org.hibernate.tool.schema.internal.SchemaCreatorImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


@SpringBootApplication
//@ComponentScan(basePackages = "shop")
//@EntityScan(basePackages = "shop.model")
//@EnableJpaRepositories(basePackages = "shop.dao")
public class ShopStart {
    public static void main(String[] args) {
        SpringApplication.run(ShopStart.class, args);

    }
}