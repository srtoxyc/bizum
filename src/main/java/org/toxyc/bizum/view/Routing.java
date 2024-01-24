package org.toxyc.bizum.view;    

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.toxyc.bizum.controller.AppController;
import org.toxyc.bizum.controller.Controller;
import org.toxyc.bizum.model.entities.Email;
import org.toxyc.bizum.model.entities.User;

@RestController
public class Routing {
    private Controller appController = new AppController();

    @GetMapping("/login")
	public Boolean login(
        @RequestParam(value = "user", defaultValue = "") String user, 
        @RequestParam(value = "password", defaultValue = "") String password
    ) {
        if(user.matches(Email.EMAIL_REGEX)) {
            return appController.checkLogin(new Email(user), password);
        } else {
            return appController.checkLogin(user, password);
        }
	}

    @GetMapping("/signup")
	public Integer signUp(
        @RequestParam(value = "username", defaultValue = "") String username,
        @RequestParam(value = "email", defaultValue = "") String email,
        @RequestParam(value = "password", defaultValue = "") String password
    ) {
        return appController.signUp(new User(username, new Email(email)), password).toInt();
	}

    @GetMapping("/update/username")
	public Integer updateUsername(
        @RequestParam(value = "old", defaultValue = "") String oldUsername,
        @RequestParam(value = "new", defaultValue = "") String newUsername,
        @RequestParam(value = "password", defaultValue = "") String password
    ) {
        return appController.updateUserUsername(oldUsername, newUsername, password).toInt();
	}

    @GetMapping("/update/password")
	public Integer updatePassword(
        @RequestParam(value = "username", defaultValue = "") String username,
        @RequestParam(value = "old", defaultValue = "") String oldPassword,
        @RequestParam(value = "new", defaultValue = "") String newPassword
    ) {
        return appController.updateUserPassword(username, oldPassword, newPassword).toInt();
	}

    @GetMapping("/update/password/forgot")
	public Integer updatePasswordForgot(
        @RequestParam(value = "username", defaultValue = "") String username,
        @RequestParam(value = "email", defaultValue = "") String email,
        @RequestParam(value = "new", defaultValue = "") String newPassword
    ) {
        return appController.updateUserPasswordForgotten(new User(username, new Email(email)), newPassword).toInt();
	}

    @GetMapping("/update/email")
	public Integer updateEmail(
        @RequestParam(value = "username", defaultValue = "") String username,
        @RequestParam(value = "email", defaultValue = "") String email,
        @RequestParam(value = "password", defaultValue = "") String password
    ) {
        return appController.updateUserEmail(new User(username, new Email(email)), password).toInt();
	}
}