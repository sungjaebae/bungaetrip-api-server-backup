package GoGetters.GoGetter.dto;

import GoGetters.GoGetter.domain.Article;
import GoGetters.GoGetter.dto.RequestDto.UpdateArticleRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private Long articleId;
    private String departure;

    private String destination;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime time;

    private Integer currentParticipants;

//    private Integer totalParticipants;
    private String title;

    private String content;

    public ArticleDto(Article article){
        this.articleId=article.getId();
        this.departure=article.getDeparture();
        this.destination=article.getDestination();
        this.date=article.getDate();
        this.time=article.getTime();
        this.currentParticipants=article.getCurrentParticipants();
//        this.totalParticipants=article.getTotalParticipants();
        this.title=article.getTitle();
        this.content=article.getContent();
    }

    public ArticleDto(ArticleRequest articleRequest, LocalDate date, LocalTime time) {
        this.departure=articleRequest.getDeparture();
        this.destination=articleRequest.getDestination();
        this.currentParticipants=articleRequest.getCurrentParticipants();
//        this.totalParticipants=articleRequest.getTotalParticipants();
        this.title=articleRequest.getTitle();
        this.content=articleRequest.getContent();
        this.date=date;
        this.time=time;
    }

    public ArticleDto(UpdateArticleRequest request){
        this.departure=request.getDeparture();
        this.destination=request.getDestination();
        this.currentParticipants=request.getCurrentParticipants();
//        this.totalParticipants=articleRequest.getTotalParticipants();
        this.title=request.getTitle();
        this.content=request.getContent();
        this.date=request.getDate();
        this.time=request.getTime();
    }
}
