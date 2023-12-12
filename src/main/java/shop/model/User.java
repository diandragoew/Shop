package shop.model;
import lombok.*;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;

import java.util.Set;
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "users", schema = "shop")
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Access(AccessType.FIELD)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "phone")
    private String phone;
    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
    private Set<Ad> ads;
}
