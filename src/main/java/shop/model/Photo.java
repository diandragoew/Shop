package shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "photos", schema = "shop")
@AllArgsConstructor
@EqualsAndHashCode
@Access(AccessType.FIELD)
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "photo_path")
    private String photoPath;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "deployer_id", nullable = false, foreignKey =  @ForeignKey(name = "FK_photos_users" ))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User deployer;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ad_id", nullable = false)
    @JsonIgnore
    private Ad ad;


}
