package shop.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.AdDto;
import shop.exceptions.UnauthorizedException;


@RestController
public class AdController {

    @GetMapping("/ads/{id}")
    public AdDto getAd(@PathVariable long id) {
        // Check if the user is logged in
        if (!isLoggedIn()) {
            throw new UnauthorizedException("User is not logged in");
        }
        return new AdDto();
    }

    private boolean isLoggedIn() {
        // Implement the logic to check if the user is logged in
        // Return true if the user is logged in, otherwise return false
        // Example implementation:
        // return authenticationService.isUserLoggedIn();
        return true;
    }
}
