package shop.model;
import lombok.*;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", schema = "shop")
@AllArgsConstructor
@EqualsAndHashCode(exclude = "ads")
@Entity
@Access(AccessType.FIELD)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "phone")
    private String phone;
    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Ad> ads;
    public User(String userName, String email, String password, String phone) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
