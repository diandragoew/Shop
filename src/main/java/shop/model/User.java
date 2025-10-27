package shop.model;
import lombok.*;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", schema = "shop")
@AllArgsConstructor
@Entity
@Access(AccessType.FIELD)
public class User implements Comparable<User> {
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

    // Owning side of favorites; cascade ONLY persist/merge to avoid deleting Ads
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "favorite_ads",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "ad_id")
    )
    private Set<Ad> favoriteAds = new HashSet<>();

    // Convenience methods
    public void addfavorite(Ad ad) {
        if (ad != null && favoriteAds.add(ad)) {
            ad.addWatcher(this);
        }
    }

    public void removefavorite(Ad ad) {
        if (ad != null && favoriteAds.remove(ad)) {
            ad.removeWatcher(this);
        }
    }

    public void clearFavorites() {
        for (Ad ad : new java.util.HashSet<>(favoriteAds)) {
            removefavorite(ad);
        }
    }

    public boolean isItFavorite(Ad ad) {
       return favoriteAds.contains(ad);
    }

    public User(String userName, String email, String password, String phone) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(User o) {
        return this.id.compareTo(o.id);
    }
}
