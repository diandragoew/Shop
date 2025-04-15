package shop.dto;

import lombok.*;
//import shop.model.Message;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProfileDto {
   private Long id;
   private String userName;
   private List<SellAdDto> sellAdDtos = new ArrayList<>();
   private List<BuyAdDto> buyAdDtos = new ArrayList<>();

   public void addSellAdDto (SellAdDto sellAdDto) {
      sellAdDtos.add(sellAdDto);
   }
}
