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
@ComponentScan(basePackages = "shop")
@EntityScan(basePackages = "shop.model")
@EnableJpaRepositories(basePackages = "shop.dao")
public class ShopStart {
    public static void main(String[] args) {
        SpringApplication.run(ShopStart.class, args);

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        SessionFactory factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        CreateUserDto newC = new CreateUserDto();
        User newUser = new User();
        newUser.setUserName(newC.getUserName());
        newUser.setEmail(newC.getEmail());
        newUser.setPassword(newC.getPassword());
        newUser.setPhone(newC.getPhone());

        session.save(newUser);
        transaction.commit();
        session.close();
        factory.close();
//
//        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
//        metadataSources.addAnnotatedClass(User.class);
//        metadataSources.addAnnotatedClass(Ad.class);
//        metadataSources.addAnnotatedClass(Message.class);
//        metadataSources.addAnnotatedClass(Photo.class);
//
//        Metadata metadata = metadataSources.buildMetadata();
//
//        SchemaCreatorImpl schemaCreator = new SchemaCreatorImpl(serviceRegistry);
////
//        // Use the schemaCreator to create tables
//        schemaCreator.doCreation(metadata, true);
//        StandardServiceRegistryBuilder.destroy(serviceRegistry);

        // Database connection details
//        String url = "jdbc:mysql://localhost:3306/shop";
//        String username = "root";
//        String password = "root";
//
//        // SQL statement to create the table
//        String createTableSql = "    create table `users` (\n" +
//                "        `id` bigint not null auto_increment,\n" +
//                "        `email` varchar(255),\n" +
//                "        `password` varchar(255),\n" +
//                "        `phone` varchar(255),\n" +
//                "        `user_name` varchar(255),\n" +
//                "        primary key (`id`)\n" +
//                "    ) engine=InnoDB;";
//
//        try (Connection connection = DriverManager.getConnection(url, username, password);
//             Statement statement = connection.createStatement()) {
//
//            // Execute the create table statement
//            statement.executeUpdate(createTableSql);
//            System.out.println("Table created successfully!");
//
//        } catch (SQLException e) {
//            System.out.println("Error creating table: " + e.getMessage());
//        }

    }
}