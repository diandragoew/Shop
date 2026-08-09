package shop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.dao.AdRepository;
import shop.dao.PhotoRepository;
import shop.dao.UserRepository;
import shop.dto.AdDto;
import shop.dto.MessageDto;
import shop.exceptions.UnauthorizedException;
import shop.model.Ad;
import shop.model.Communication;
import shop.model.Message;
import shop.model.Photo;
import shop.model.User;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class AdController {
    private final ResourceLoader resourceLoader;

    public AdController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private shop.dao.AdDao adDao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/ads/{id}")
    public AdDto getAd(@PathVariable long id) {
        // Check if the user is logged in
        if (!isLoggedIn()) {
            throw new UnauthorizedException("User is not logged in");
        }
        return new AdDto();
    }


    @PostMapping("/create-ad")
    public Resource createAd(@RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("phone") String phone,
                             @RequestParam("photos") MultipartFile[] photos,
                             @RequestParam("price") String price,
                             HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }

        Ad ad = new Ad();

        ad.setTitle(title);
        ad.setDescription(description);
        ad.setPhone(phone);
        ad.setPrice(price);
        User user;
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (adRepository.existsByTitleAndCreator(title, user)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT); // HTTP 409 Conflict
                return null;
            }
            ad.setCreator(user);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        ad = adRepository.save(ad);

        // Process the photos
        for (MultipartFile photo : photos) {
            Photo adPhoto = new Photo();

            String fileName = photo.getOriginalFilename();

            // Get the file extension
            String fileExtension = getFileExtension(fileName);

            // Save the photo to a file
            String workingDir = System.getProperty("user.dir");
            String imageDir = workingDir + "\\src\\main\\webapp\\adPage\\img";
            String newFileName = getNewFileName(imageDir, fileExtension);
            File adPhotoFile = new File(newFileName);
            try {
                photo.transferTo(adPhotoFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String name = adPhotoFile.getName();
            adPhoto.setPhotoPath("\\adPage\\img" + File.separator + name);
//            adPhoto.setPhotoPath(adPhotoFile.getPath());

            adPhoto.setAd(ad);
            adPhoto.setDeployer(user);
            photoRepository.save(adPhoto);
        }

//        adRepository.save(ad);


        Resource htmlFile = resourceLoader.getResource("/homePage/html/home.html");
        return htmlFile;
    }

    @Transactional
    @PostMapping("/edit-ad")
    public Resource editAd(@RequestParam("adId") String adId,
                           @RequestParam("title") String title,
                           @RequestParam("description") String description,
                           @RequestParam("phone") String phone,
                           @RequestParam("photos") MultipartFile[] photos,
                           @RequestParam("price") String price,
                           HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }

        Ad ad = null;

        User user = new User();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            user = userOptional.get();
            ad = user.getAds().stream().filter(a -> a.getId() == Long.parseLong(adId)).findFirst().orElse(null);

            if (ad != null) {
                ad.setTitle(title);
                ad.setDescription(description);
                ad.setPhone(phone);
                ad.setPrice(price);

                ad = adRepository.save(ad);

            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // HTTP 404 Not Found
                return null;
            }

        }


        boolean hasRealPhotos = Arrays.stream(photos)
                .anyMatch(photo -> photo != null && !photo.isEmpty());

        if (hasRealPhotos) {
            photoRepository.deleteAllByAd_Id(ad.getId());
            ad.getPhotos().clear(); // prevent re-persisting old photos
            photoRepository.flush(); // force execution before inserts

            // Process the photos
            for (MultipartFile photo : photos) {
                Photo adPhoto = new Photo();

                String fileName = photo.getOriginalFilename();

                // Get the file extension
                String fileExtension = getFileExtension(fileName);

                // Save the photo to a file
                String workingDir = System.getProperty("user.dir");
                String imageDir = workingDir + "\\src\\main\\webapp\\adPage\\img";
                String newFileName = getNewFileName(imageDir, fileExtension);
                File adPhotoFile = new File(newFileName);
                try {
                    photo.transferTo(adPhotoFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String name = adPhotoFile.getName();
                adPhoto.setPhotoPath("\\adPage\\img" + File.separator + name);
//            adPhoto.setPhotoPath(adPhotoFile.getPath());

                adPhoto.setAd(ad);
                adPhoto.setDeployer(user);
                photoRepository.save(adPhoto);
            }
        }
//        adRepository.save(ad);


        Resource htmlFile = resourceLoader.getResource("/homePage/html/home.html");
        return htmlFile;
    }

    @Transactional
    @GetMapping("/ads")
    public List<AdDto> listAllAds(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long loggedUserId = (Long) session.getAttribute("userId");
        User user = null;
        if (loggedUserId !=null) {
        user = userRepository.findById(loggedUserId).orElse(null);
        }

        List<Ad> ads = adRepository.findAll();
        return getAdDtos(ads, loggedUserId, user);
    }
    @Transactional
    @GetMapping("/publicUserProfil")
    public List<AdDto> listUserProfileAds(@RequestParam("userId") Long publicProfileUserId,HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long loggedUserId = (Long) session.getAttribute("userId");
        User loggedUser = null;
        if (loggedUserId !=null) {
            loggedUser = userRepository.findById(loggedUserId).orElse(null);
        }

        List<Ad> ads = adRepository.findByCreatorId(publicProfileUserId);
        return getAdDtos(ads, loggedUserId, loggedUser);
    }
    private static List<AdDto> getAdDtos(List<Ad> ads, Long loggedUserId, User user) {
        List<AdDto> adDtos = new ArrayList<>();
        AdDto adDto = new AdDto();
        for (Ad ad : ads) {
            adDto = new AdDto();

            adDto.setId(ad.getId());
            adDto.setTitleAd(ad.getTitle());
            adDto.setDescriptionAd(ad.getDescription());
            adDto.setCreatorName(ad.getCreator().getUserName());
            adDto.setCreatorId(ad.getCreator().getId());
            adDto.setLoggedUserId(loggedUserId);
            adDto.setPhone(ad.getPhone());
            adDto.setPhotos(ad.getPhotos().stream().map(Photo::getPhotoPath).collect(Collectors.toList()));
            adDto.setPrice(ad.getPrice());
            if(user != null) {
                boolean isInFavorites = user.isItFavorite(ad);
                adDto.setInFavorites(isInFavorites);
            }
            adDtos.add(adDto);
        }

        return adDtos;
    }

    @GetMapping("/ad-details") // A new API endpoint for fetching ad data
    public AdDto getAdDetails(@RequestParam("adId") Long adId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long loggedUserId = (Long) session.getAttribute("userId");

        System.out.println("Received API request for adId: " + adId);

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found with ID: " + adId));

        AdDto adDto = new AdDto();
        adDto.setId(ad.getId());
        adDto.setTitleAd(ad.getTitle());
        adDto.setDescriptionAd(ad.getDescription());
        adDto.setCreatorName(ad.getCreator().getUserName());
        adDto.setCreatorId(ad.getCreator().getId());
        adDto.setLoggedUserId(loggedUserId);
        adDto.setPhone(ad.getPhone());
        adDto.setPhotos(ad.getPhotos().stream().map(Photo::getPhotoPath).collect(Collectors.toList()));
        adDto.setPrice(ad.getPrice());
        adDto.setMessageDtos(buildMessageDtos(ad, loggedUserId));

        return adDto; // Spring automatically converts AdDto to JSON
    }

    private TreeSet<MessageDto> buildMessageDtos(Ad ad, Long loggedUserId) {
        TreeSet<MessageDto> messagesDtos = new TreeSet<>();
        if (loggedUserId == null) {
            return messagesDtos;
        }

        List<Communication> communications = ad.getCommunications();
        if (communications == null) {
            return messagesDtos;
        }

        boolean isOwner = loggedUserId.equals(ad.getCreator().getId());

        for (Communication communication : communications) {
            if (!isOwner && !loggedUserId.equals(communication.getCreator().getId())) {
                continue;
            }

            List<Message> messages = communication.getMessages();
            if (messages == null) {
                continue;
            }

            for (Message message : messages) {
                MessageDto messageDto = new MessageDto();
                messageDto.setDate(message.getDate());
                messageDto.setSenderName(message.getSender().getUserName());
                messageDto.setText(message.getText());
                messageDto.setCommunicationId(communication.getId());
                messageDto.setCommunicationCreatorId(communication.getCreator().getId());
                messageDto.setCommunicationCreatorName(communication.getCreator().getUserName());
                messageDto.setAdCreatorId(ad.getCreator().getId());
                messageDto.setAdCreatorName(ad.getCreator().getUserName());
                messagesDtos.add(messageDto);
            }
        }

        return messagesDtos;
    }

    @GetMapping("/ad-delete") // A new API endpoint for fetching ad data
    public void deleteAd(@RequestParam("adId") Long adId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long loggedUserId = (Long) session.getAttribute("userId");

        System.out.println("Received API request to delete ad with adId: " + adId);

        adDao.deleteAdByAdIdAndUserId(adId, loggedUserId);

    }

    // You would still need an endpoint to serve the static HTML page itself,
    // but often this is handled automatically if the HTML is in src/main/webapp
    // or you could add a simple @GetMapping that returns a RedirectView or similar.
    // For example, if you access /adPage/html/adPage.html directly, it would just serve the file.


    // You might also want to handle cases where adId is missing or invalid
    // For simplicity, the above method will throw a MissingServletRequestParameterException if adId is not provided.
    // You could add error handling or make @RequestParam optional with defaultValue or required = false


    // Helper method to get the file extension from a file name
    private String getFileExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex > 0) {
            return fileName.substring(extensionIndex);
        } else {
            return "";
        }
    }

    // Helper method to get a new file name with a unique name and the given file extension
    private String getNewFileName(String directory, String fileExtension) {
        File directoryFile = new File(directory);
        File[] files = directoryFile.listFiles();
        int maxFileNumber = 0;
        for (File file : files) {
            if (file.getName().endsWith(fileExtension)) {
                String fileName = file.getName().replace(fileExtension, "");
                int fileNumber = Integer.parseInt(fileName);
                if (fileNumber > maxFileNumber) {
                    maxFileNumber = fileNumber;
                }
            }
        }
        return directory + "\\" + String.valueOf(maxFileNumber + 1) + fileExtension;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("photos") MultipartFile file) {
        // Save the file to a directory
        String uploadPath = "/path/to/upload/directory";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File fileToSave = new File(uploadPath + "/" + file.getOriginalFilename());
        try {
            file.transferTo(fileToSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File uploaded successfully!";
    }

    private boolean isLoggedIn() {
        // Implement the logic to check if the user is logged in
        // Return true if the user is logged in, otherwise return false
        // Example implementation:
        // return authenticationService.isUserLoggedIn();
        return true;
    }

}
