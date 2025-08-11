package shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.model.Ad;
import shop.model.Message;
import shop.model.Photo;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellAdDto implements Comparable<SellAdDto> {

    Long adId;
    String title;
    String description;
    String price = "";
    List<Photo> photos;

    private Set<MessageDto> messageDtos;


    @Override
    public int compareTo(SellAdDto sellAdDto) {
        if (this.title.compareTo(sellAdDto.title) != 0) {
            return this.title.compareTo(sellAdDto.title);
        }else {
            return this.adId.compareTo(sellAdDto.adId);
        }
    }

    public void setPrice(String price){
        if (price!=null) {
            this.price = price;
        }
    }
}
