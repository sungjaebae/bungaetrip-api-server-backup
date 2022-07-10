package GoGetters.GoGetter.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequest {
    private Long articleId;

    private String departure;

    private String destination;

    private Integer currentParticipants;

//    private Integer totalParticipants;

    private LocalDate date;

    private LocalTime time;

    private String title;

    private String content;
}
