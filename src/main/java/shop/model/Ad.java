package shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ads", schema = "shop")
@AllArgsConstructor
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
            return this.creator.compareTo(o.creator);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return Objects.equals(title, ad.title) && Objects.equals(creator, ad.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, creator);
    }

    public void setTitle(String title) {
        if (title!=null ) {
            this.title = title;
        }
    }

    public void setDescription(String description) {
        if (description!=null ) {
            this.description = description;
        }
    }


    public void setPhone(String phone) {
        if (phone!=null ) {
            this.phone = phone;
        }
    }

    public void setPrice(String price) {
        if (price!=null ) {
            this.price = price;
        }
    }
}
