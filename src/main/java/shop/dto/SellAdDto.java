package shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.model.Message;
import shop.model.Photo;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellAdDto {
    String title;
    String description;
    List<Photo> photos;

    private List<MessageDto> messageDtos;

}
