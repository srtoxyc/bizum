package org.toxyc.bizum.view;    

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.toxyc.bizum.controller.AppController;
import org.toxyc.bizum.controller.Controller;
import org.toxyc.bizum.model.entities.User;

@RestController
public class Routing {
    private Controller appController = new AppController();

    @GetMapping("/login")
	public Boolean login(
        @RequestParam(value = "username", defaultValue = "") String name, 
        @RequestParam(value = "password", defaultValue = "") String password
    ) {
        return appController.checkLogin(name, password);
	}
}