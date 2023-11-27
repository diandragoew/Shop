package shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "ads")
@AllArgsConstructor
@EqualsAndHashCode
@Access(AccessType.FIELD)
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "phone")
    private String phone;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User creator;
    @OneToMany(mappedBy = "ad", fetch = FetchType.EAGER)
    private List<Photo> photos;
    @OneToMany(mappedBy = "ad", fetch = FetchType.EAGER)
    private List<Message> messages;
}
