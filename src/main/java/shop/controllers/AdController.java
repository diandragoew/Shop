package shop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.dao.AdRepository;
import shop.dao.PhotoRepository;
import shop.dao.UserRepository;
import shop.dto.AdDto;
import shop.exceptions.UnauthorizedException;
import shop.model.Ad;
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
        User user = new User();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // You can now access the user object without getting a LazyInitializationException
            ad.setCreator(user);
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
            adPhoto.setPhotoPath("\\adPage\\img"+File.separator+name);
//            adPhoto.setPhotoPath(adPhotoFile.getPath());

            adPhoto.setAd(ad);
            adPhoto.setDeployer(user);
            photoRepository.save(adPhoto);
        }

//        adRepository.save(ad);


        Resource htmlFile = resourceLoader.getResource("/homePage/html/home.html");
        return htmlFile;
    }

    @GetMapping("/ads")
    public List<AdDto> listAllAds(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long loggedUserId = (Long) session.getAttribute("userId");

        List<Ad> ads = adRepository.findAll();
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
            adDtos.add(adDto);
        }

        return adDtos;
    }

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
