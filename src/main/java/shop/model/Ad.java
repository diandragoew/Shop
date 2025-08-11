package shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ads", schema = "shop")
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"photos", "messages"})
@Access(AccessType.FIELD)
public class Ad implements Comparable<Ad> {
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
    @Column(name = "price")
    private String price;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    @JsonIgnore
    private User creator;
    @OneToMany(mappedBy = "ad", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos= new ArrayList<>();
    @OneToMany(mappedBy = "ad", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Communication> communications;

    public void addPhoto(Photo photo)    {
        photos.add(photo);
    }

    @Override
    public int compareTo(Ad o) {
        if (this.title.compareTo(o.title) != 0) {
            return this.title.compareTo(o.title);
        }else {
            return this.id.compareTo(o.id);
        }
    }
}
