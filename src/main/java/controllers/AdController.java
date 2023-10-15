package controllers;

import dto.AdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AdController {

    @GetMapping("/ads/{id}")
   public AdDto getAd(@PathVariable long id){
        return new AdDto();
    }

}
