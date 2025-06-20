package shop.dto;

import lombok.*;
import shop.model.Ad;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateUserDto {
    private String userName;
    private String email;
    private String password;
    private String phone;
}
