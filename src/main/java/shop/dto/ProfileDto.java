package shop.dto;

import lombok.*;

import java.util.HashSet;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
   private Long id;
   private String userName;
   private Set<SellAdDto> sellAdDtos = new TreeSet<>();
   private Set<BuyAdDto> buyAdDtos = new TreeSet<>();
   private Set<FavoriteAdDto> favoriteAdDtos = new HashSet<>();

   public void addSellAdDto (SellAdDto sellAdDto) {
      sellAdDtos.add(sellAdDto);
   }
   public void addBuyAdDto (BuyAdDto buyAdDto) {
      buyAdDtos.add(buyAdDto);
   }
   public void addFavoriteAdDto(FavoriteAdDto favoriteAdDto) {
      this.favoriteAdDtos.add(favoriteAdDto);
   }


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ProfileDto that = (ProfileDto) o;
      return Objects.equals(id, that.id) && Objects.equals(userName, that.userName);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, userName);
   }
}
