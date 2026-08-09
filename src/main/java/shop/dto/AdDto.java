package shop.dto;

import lombok.*;


import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdDto {

  private   Long id;
  private   String titleAd="";
  private   String descriptionAd;
  private   String creatorName;
  private   Long creatorId;
  private   Long loggedUserId;
  private   String phone;
  private   List<String> photos;
  private   String price;
  private boolean isInFavorites;
  private Set<MessageDto> messageDtos;

    public String getTitleAd() {
        return titleAd;
    }

    public String getDescriptionAd() {
        return descriptionAd;
    }


    public String getPhone() {
        return phone;
    }

    public List<String> getPhotos() {
        return Collections.unmodifiableList(photos);
    }
}
