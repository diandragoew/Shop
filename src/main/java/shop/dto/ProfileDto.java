package shop.dto;

import lombok.*;
//import shop.model.Message;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProfileDto {
   private String userName;
   private String title;
   private String description;
   private List<String> photos;
}
