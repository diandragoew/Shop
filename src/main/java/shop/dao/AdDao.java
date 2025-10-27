package shop.dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import shop.model.Ad;

@Component
public class AdDao {
//    @PersistenceContext
//    private EntityManager entityManager;

    @Autowired
    AdRepository adRepository;

    // Java
//    @Transactional
//    public void deleteByAdIdAndUserId(Long adId, Long userId) {
//        // Delete communications referencing the ad
//        entityManager.createQuery("DELETE FROM Communication c WHERE c.ad.id = :adId")
//                     .setParameter("adId", adId)
//                     .executeUpdate();
//        // Delete photos referencing the ad
//        entityManager.createQuery("DELETE FROM Photo p WHERE p.ad.id = :adId")
//                     .setParameter("adId", adId)
//                     .executeUpdate();
//        // Delete the ad itself
//        entityManager.createQuery("DELETE FROM Ad a WHERE a.id = :adId AND a.creator.id = :userId")
//                     .setParameter("adId", adId)
//                     .setParameter("userId", userId)
//                     .executeUpdate();
//    }

    @Transactional
    public void deleteAdByAdIdAndUserId(Long adId, Long userId) {
        Ad ad = adRepository.findById(adId)
                            .filter(a -> a.getCreator().getId().equals(userId))
                            .orElse(null);
        if (ad != null) {
            adRepository.delete(ad);
        }
    }
}
