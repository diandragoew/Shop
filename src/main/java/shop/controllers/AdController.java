package shop.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.AdDto;


@RestController
public class AdController {

    @GetMapping("/ads/{id}")
   public AdDto getAd(@PathVariable long id){
        return new AdDto();
    }
}
