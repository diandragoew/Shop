package shop.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import shop.dto.AdDto;
import shop.dto.CreateUserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDao {

    private JdbcTemplate jdbcTemplate;
    public void addUser(CreateUserDto createUserDto) throws SQLException {
        Connection con=jdbcTemplate.getDataSource().getConnection();
        con.createStatement().executeUpdate("CREATE TABLE `shop`.`ads` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  PRIMARY KEY (`id`));");


//        PreparedStatement pst= con.prepareStatement("insert into users values(null,?,?,?,?)");
//       pst.setString(1,createUserDto.getUserName());
//       pst.setString(2,createUserDto.getEmail());
//       pst.setString(3,createUserDto.getPassword());
//       pst.setString(4,createUserDto.getPhone());
//        System.out.println( pst.toString());
//       pst.executeUpdate();

    }
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

