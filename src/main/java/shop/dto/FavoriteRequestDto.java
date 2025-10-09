package shop.dto;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.EqualsAndHashCode
public class FavoriteRequestDto {
    private Long userId;
    private Long adId;
    // getters and setters
}
