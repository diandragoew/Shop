package shop.service;
import jakarta.transaction.Transactional;
import shop.dao.AdRepository;
import shop.dao.UserRepository;
import shop.model.Ad;
import shop.model.User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
// Java
@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final UserRepository userRepository;
    private final AdRepository adRepository;

    @Transactional
    public void markfavorite(long userId, long adId) {
        User user = userRepository.findById(userId).orElseThrow();
        Ad ad = adRepository.findById(adId).orElseThrow();
        user.addfavorite(ad); // owning side change -> join table row will be inserted on flush
    }

    @Transactional
    public void unmarkfavorite(long userId, long adId) {
        User user = userRepository.findById(userId).orElseThrow();
        Ad ad = adRepository.findById(adId).orElseThrow();
        user.removefavorite(ad); // join row removed
    }
    
}

