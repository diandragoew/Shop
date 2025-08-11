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
public class BuyAdDto implements Comparable<BuyAdDto> {

    Long adId;
    String title;
    String description;
    String price = "";
    List<Photo> photos;

    private Set<MessageDto> messageDtos;

    @Override
    public int compareTo(BuyAdDto buyAdDto) {
        if (this.title.compareTo(buyAdDto.title) != 0) {
            return this.title.compareTo(buyAdDto.title);
        }else {
            return this.adId.compareTo(buyAdDto.adId);
        }
    }
    public void setPrice(String price){
        if (price!=null) {
            this.price = price;
        }
    }
}
