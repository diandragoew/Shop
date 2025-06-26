package shop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EditPasswordDto {

    private String passOld;
    private String passNew;
    private String passNewConfirm;
}
