package shop.model;
import lombok.*;

import javax.persistence.*;

import java.util.Set;
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "users")
@AllArgsConstructor
@EqualsAndHashCode
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
