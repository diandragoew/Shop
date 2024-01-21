package shop.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.sql.SQLException;

@RestController
public class HomeController {
    private final ResourceLoader resourceLoader;

    public HomeController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @GetMapping("/shop")
    @ResponseBody
    public Resource getHtmlPage() {
//      Load the HTML file from the specified path
        Resource htmlFile = resourceLoader.getResource("file:C:/Users/ddragoev/IdeaProjects/shop/src/main/webapp/homePage/html/home.html");
        return htmlFile;
    }
}
