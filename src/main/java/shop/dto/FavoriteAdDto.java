package shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.model.Message;
import shop.model.Photo;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteAdDto implements Comparable<FavoriteAdDto> {

    Long adId;
    Long creatorId;
    String title;
    String description;
    String price = "";
    List<Photo> photos;
    boolean isInFavorites;

    private Set<MessageDto> messageDtos;

    @Override
    public int compareTo(FavoriteAdDto favoriteAdDto) {
        if (this.title.compareTo(favoriteAdDto.title) != 0) {
            return this.title.compareTo(favoriteAdDto.title);
        }else {
            return this.adId.compareTo(favoriteAdDto.adId);
        }
    }
    public void setPrice(String price){
        if (price!=null) {
            this.price = price;
        }
    }

    public void setCreator(Long creatorId) {
        this.creatorId = creatorId;
    }

    public boolean isInFavorites() {
        return isInFavorites;
    }

    public void setInFavorites(boolean inFavorites) {
        isInFavorites = inFavorites;
    }
}
