package shop.dto;

import lombok.*;
import shop.model.Ad;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateUserDto {
    private String userName="georgi";
    private String email="abv@bg";
    private String password="parola";
    private String phone="0435";
}
