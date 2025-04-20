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
public class SellAdDto {

    Long id;
    String title;
    String description;
    List<Photo> photos;

    private Set<MessageDto> messageDtos;

}
