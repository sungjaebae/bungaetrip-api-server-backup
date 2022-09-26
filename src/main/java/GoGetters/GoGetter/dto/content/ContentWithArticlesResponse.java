package GoGetters.GoGetter.dto.content;

import GoGetters.GoGetter.domain.article.Article;
import GoGetters.GoGetter.domain.content.Content;
import GoGetters.GoGetter.dto.article.ArticleResponse;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ContentWithArticlesResponse {
    private Long contentId;
    private String title;

    private String subtitle;

    private String content;

    private String contentType;

    private Double latitude;
    private Double longitude;

    private String address;

    private Double rating;

    private Integer blogReview;

    private Integer visitorReview;

    private String phoneNumber;

    private String naverId;

    private String contentDetailUrl;

    private List<ArticleResponse> articles;

    public ContentWithArticlesResponse(Content content, List<Article> articles) {
        this.contentId=content.getId();
        this.title = content.getTitle();
        this.subtitle=content.getSubtitle();
        this.content=content.getContent();
        this.contentType=content.getContentType().toString();

        this.latitude = content.getLatitude();
        this.longitude=content.getLongitude();
        this.address=content.getAddress();

        this.rating=content.getRating();
        this.blogReview=content.getBlogReview();
        this.visitorReview=content.getVisitorReview();

        this.phoneNumber=content.getPhoneNumber();
        this.naverId=content.getNaverId();
        this.contentDetailUrl=content.getContentDetailUrl();

        this.articles = articles.stream().map(article -> new ArticleResponse(article)).collect(Collectors.toList());
    }

}
