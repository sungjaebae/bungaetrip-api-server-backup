package GoGetters.GoGetter.repository;

import GoGetters.GoGetter.domain.Article;
import GoGetters.GoGetter.domain.ArticleStatus;
import GoGetters.GoGetter.dto.ArticleFixes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ArticleRepository {
    @PersistenceContext
    private EntityManager em;

    public  Long save(Article article) {
        em.persist(article);
        return article.getId();
    }

    public Article findArticle(Long articleId) {
        return em.find(Article.class,articleId);
    }

    public List<Article> findAllArticles(){
        return em.createQuery("select a from Article a",Article.class).getResultList();
    }

    //글 삭제하기
    public Long deleteArticleState(Long articleId){
        Article article = em.find(Article.class, articleId);
        article.changeArticleStatus(ArticleStatus.DELETE);
        return article.getId();
    }

    //글 수정
    public Long modifyArticle(Long articleId,ArticleFixes fixes){
        Article article = em.find(Article.class, articleId);
        article.modifyArticle(fixes);
        return article.getId();
    }

    //글 검색
    public List<Article> findArticlesByKeyword(String keyword){
        String query="select a from Article a where a.title like %:"+keyword+"%";
        return em.createQuery(query, Article.class).setParameter("keyword", keyword).getResultList();
    }


}
