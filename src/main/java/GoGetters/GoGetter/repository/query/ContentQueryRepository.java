package GoGetters.GoGetter.repository.query;

import GoGetters.GoGetter.domain.content.ContentType;
import GoGetters.GoGetter.dto.content.ContentQueryResponse;
import GoGetters.GoGetter.dto.content.ContentResponse;
import GoGetters.GoGetter.dto.image.ContentImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ContentQueryRepository {
    @PersistenceContext
    private final EntityManager em;

    public List<ContentResponse> findBestPlaceByDistance(Double memberLatitude, Double memberLongitude,
                                                         Integer offset, Integer limit,
                                                         ContentType contentType, Double limitDistance) {
        List<ContentQueryResponse> result = findBestContentList(memberLatitude,memberLongitude,offset,limit
                ,contentType,limitDistance);
        log.info("print size: {}",result.size());
//        Map<Long,List<ContentImageDto>> imageListMap=findContentImageMap(
//                toContentIds(result)
//        );
//        result.forEach(c-> c.setImageList(imageListMap.get(c.getContentId())));

        return new ArrayList<>();
    }

    private List<ContentQueryResponse> findBestContentList(Double memberLatitude,Double memberLongitude,
                                                                Integer offset,Integer limit,ContentType contentType,
                                                           Double limitDistance
                                                           ) {

//        String query="select c from Content c" +
//                " where ST_Distance_Sphere(point(c.longitude,c.latitude), point(:x,:y)) < 5000" +
//                " order by ST_Distance_Sphere(point(c.longitude,c.latitude), point(:x,:y))";
//        return em.createQuery(query, Content.class)
//                .setParameter("x",memberLongitude)
//                .setParameter("y",memberLatitude)
//                .getResultList();
        Query query = em.createNativeQuery("SELECT c.content_id,c.title, c.subtitle, c.content_type," +
                        " c.rating, c.blog_review, c.visitor_review," +
                        " ST_Distance_Sphere(point(c.longitude,c.latitude), point(:x,:y))/1000 from content c" +
                        " where c.content_type=:contentType" +
                        " and ST_Distance_Sphere(point(c.longitude,c.latitude), point(:x,:y))/1000 <= :limitDistance" +
                        " order by c.rating*c.visitor_review*c.blog_review desc")
                .setParameter("x", memberLongitude)
                .setParameter("y", memberLatitude)
                .setParameter("contentType",contentType.toString())
                .setParameter("limitDistance", limitDistance);
            List<Object[]> resultList = query.getResultList();
        List<ContentQueryResponse> contentQueryResponseList = new ArrayList<>();
            System.out.println("print: 00000");
            for (Object[] row : resultList) {
                contentQueryResponseList.add(new ContentQueryResponse(row[0].toString(), row[1].toString(),
                        row[2].toString(), row[3].toString(), row[4].toString(), row[5].toString(),
                        row[6].toString(), row[7].toString()));
                System.out.println("print id : "+row[0]);
                System.out.println("print title : "+row[1]);
                System.out.println("print subtitle: "+row[2]);
                System.out.println("print contentType: "+row[3]);
                System.out.println("print rating: "+row[4]);
                System.out.println("print blog: "+row[5]);
                System.out.println("print visitor: "+row[6]);
                System.out.println("print distance: "+row[7]);
            }

//        String query="select"+
//                " ST_Distance_Sphere(point(c.longitude,c.latitude), point(:x,:y))" +
//                " from Content c" +
//                " where ST_Distance_Sphere(point(c.longitude,c.latitude), point(:x,:y)) < 5000" +
//                " order by c.rating*c.visitorReview*c.blogReview desc";
//        try {
//            return em.createQuery(query, ContentQueryResponse.class)
//                    .setParameter("x",memberLongitude)
//                    .setParameter("y",memberLatitude)
//                    .getResultList();
//        } catch (Exception e) {
//            log.info("print : {}", e.getMessage());
//        }

////        return em.createQuery(
//                        "select new GoGetters.GoGetter.dto.content.ContentQueryResponse( c.id, c.title, c.subtitle ," +
//                                " c.contentType, c.rating, c.blogReview, c.visitorReview) " +
//                                " from Content c" +
//                                " where c.contentType=:contentType" +
//                                " and (6371*acos(cos(radians(c.latitude))*cos(radians(:latitude))*cos(radians(:longitude) \"\n" +
//                                "                + \"-radians(c.longitude))+sin(radians(c.latitude))*sin(radians(:latitude))))" +
//                                "< : limitDistance" +
////                                " and ST_Distance_Sphere(point(c.longitude,c.latitude), point(:x,:y)) < 5000" +
//                                " order by c.rating*c.visitorReview*c.blogReview desc", ContentQueryResponse.class)
//                .setParameter("contentType", contentType)
//                .setParameter("longitude",memberLongitude)
//                .setParameter("latitude", memberLatitude)
//                .setParameter("limitDistance",limitDistance)
//                .setFirstResult(offset)
//                .setMaxResults(limit)
//                .getResultList();
//        "(6371*acos(cos(radians(:memberLatitude))*cos(radians(c.latitude))*cos(radians(c.longitude)-" +
//                "radians(:memberLongitude))" +
//                "+sin(radians(:memberLatitude))*sin(radians(c.latitude)))) as distance)" +
        return contentQueryResponseList;
    }
    private List<Long> toContentIds(List<ContentQueryResponse> result) {
        return result.stream().map(contentQueryResponse -> contentQueryResponse.getContentId())
                .collect(Collectors.toList());
    }

    private Map<Long, List<ContentImageDto>> findContentImageMap(List<Long> contentIds) {
        List<ContentImageDto> contentImages = em.createQuery(
                        "select new GoGetters.GoGetter.dto.image.ContentImageDto(i.content.id," +
                                " i.id, i.imageUrl)" +
                                " from Image i" +
                                " where i.content.id in :contentIds", ContentImageDto.class)
                .setParameter("contentIds", contentIds)
                .getResultList();

        return contentImages.stream()
                .collect(Collectors.groupingBy(ContentImageDto::getContentId));
    }
}
