package shop.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AdDto {


  private   String titleAd;
  private   String descriptionAd;
  private   String userName;
  private   String phone;
  private   List<String> photos;

    public String getTitleAd() {
        return titleAd;
    }

    public String getDescriptionAd() {
        return descriptionAd;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getPhotos() {
        return Collections.unmodifiableList(photos);
    }
}
