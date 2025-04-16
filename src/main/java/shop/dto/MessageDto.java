package shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.model.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Comparable<MessageDto> {
    private LocalDateTime date;
    private String senderName;
    private String text;
    private Long communicationId;

    @Override
    public int compareTo(MessageDto other) {
        // Compare by communicationId (nullable-safe)
        int result = Long.compare(
                this.communicationId != null ? this.communicationId : 0L,
                other.communicationId != null ? other.communicationId : 0L
        );
        if (result != 0) return result;

        // Compare by senderName (nulls last)
        if (this.senderName != null && other.senderName != null) {
            result = this.senderName.compareTo(other.senderName);
        } else if (this.senderName == null && other.senderName != null) {
            result = 1;
        } else if (this.senderName != null) {
            result = -1;
        } else {
            result = 0;
        }
        if (result != 0) return result;

        // Compare by date (nulls last)
        if (this.date != null && other.date != null) {
            result = this.date.compareTo(other.date);
        } else if (this.date == null && other.date != null) {
            result = 1;
        } else if (this.date != null) {
            result = -1;
        } else {
            result = 0;
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDto that = (MessageDto) o;
        return Objects.equals(date, that.date) && Objects.equals(senderName, that.senderName) && Objects.equals(communicationId, that.communicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, senderName, communicationId);
    }
}
