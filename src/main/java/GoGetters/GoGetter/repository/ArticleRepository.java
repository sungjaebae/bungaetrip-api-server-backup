package GoGetters.GoGetter.repository;

import GoGetters.GoGetter.MessageResource;
import GoGetters.GoGetter.domain.article.Article;
import GoGetters.GoGetter.domain.article.ArticleStatus;
import GoGetters.GoGetter.exception.Article.NoSuchArticleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class ArticleRepository {
    @PersistenceContext
    private EntityManager em;

    public  Long save(Article article) {
        em.persist(article);
        return article.getId();
    }

    public Article findArticle(Long articleId){
        String query = "select a from Article a" +
                " join fetch a.writer w" +
                " join fetch a.destinationContent" +
                " where a.id=:articleId" +
                " and a.status=:status" +
                " and w.deletedAt is null";
        List<Article> article = em.createQuery(query, Article.class)
                .setParameter("articleId", articleId)
                .setParameter("status", ArticleStatus.CREATE).getResultList();
        if (article.isEmpty()) {
            throw new NoSuchArticleException(MessageResource.articleNotExist);
        }
        return article.get(0);
//
    }


    public List<Article> findCreateArticles() {
        String query = "select a from Article a"
                + " join fetch a.writer w" +
                " join fetch a.destinationContent" +

                " where a.status=:status" +
                " and w.deletedAt is null" +
                " order by a.createdAt desc";
        log.debug("findCreateArticles query : {}", query);
        return em.createQuery(query, Article.class)
                .setParameter("status", ArticleStatus.CREATE)
                .getResultList();
    }

    //글 삭제하기
    public Long deleteArticle(Article article)  {
        article.changeArticleStatus(ArticleStatus.DELETE);
        return article.getId();
    }

    //글 수정
//    public Long modifyArticle(Article article) {
//        article.modifyArticle(departure,destination,destinationContent,date,time,
//                currentParticipants,title,content,departureLongitude,departureLatitude
//                ,destinationLongitude,destinationLatitude);
//        return article.getId();
//    }

    //글 검색
    public List<Article> findArticlesByKeyword(String keyword){
        String likeVariable="'%"+keyword+"%'";
        String query="select a from Article a" +
                " join fetch a.writer w" +
                " join fetch a.destinationContent" +
                " where a.status=:status and (a.title like "+likeVariable
                +" or a.content like "+likeVariable
                +" or a.departure like "+likeVariable
                +" or a.destination like "+likeVariable
                +" )" +
                " and w.deletedAt is null" +
                " order by a.createdAt desc";
        return em.createQuery(query, Article.class).setParameter("status",ArticleStatus.CREATE).getResultList();
    }


    public List<Article> findArticlesByMemberId(Long memberId) {
        String query="select a from Article a" +
                " join fetch a.writer w" +
                " join fetch a.destinationContent" +
                " where w.id=:memberId" +
                " and w.deletedAt is null" +
                " and a.status=:status order by a.createdAt desc";
        log.debug("Log findArticle query: {}", query);

        return em.createQuery(query, Article.class)
                .setParameter("memberId", memberId)
                .setParameter("status", ArticleStatus.CREATE)
                .getResultList();
    }

    public List<Article> sortByMeetingDate() {
        String query = "select a from Article a" +
                " join fetch a.writer w" +
                " join fetch a.destinationContent" +
                " where a.status=:status" +
                " and w.deletedAt is null" +
                " order by a.date, a.time";
        log.debug("Article Repo sort query:{}",query);
        return em.createQuery(query, Article.class)
                .setParameter("status", ArticleStatus.CREATE)
                .getResultList();
    }

    public List<Article> findArticlesByLocation(String travelPlace) {
        String query="select a from Article a" +
                " where a.status=:status" +
                " and a.destination=:travelPlace";
        return em.createQuery(query, Article.class)
                .setParameter("status",ArticleStatus.CREATE)
                .setParameter("travelPlace",travelPlace)
                .getResultList();
    }
}
