package me.edu.chifserver.controller;


import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import me.edu.chifserver.dto.UserDto;
import me.edu.chifserver.dto.UserRegisterDto;
import me.edu.chifserver.service.ServiceStatus;
import me.edu.chifserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;

    // injecting dependencies
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(value = "/profile/{username}")
    public ResponseEntity<Object> getUserInfo(@PathVariable String username){
        UserDto user = userService.findUserByUsername(username);

        if(user == null)
            return new ResponseEntity<>(new ServiceStatus(false, "User Not Found!"), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/profile")
    public ResponseEntity<String> registerUser(
            HttpServletRequest request,
            @Valid @RequestBody UserRegisterDto userRegister
    ){

        // checking if user exists
        String uid = (String) request.getAttribute("uid");

        if(userService.userExists(uid)) // user already exists
            return new ResponseEntity<>("User already exists", HttpStatusCode.valueOf(409));


        // checking if images type are compatible
        if(
                userRegister.profileImgType().equals("png") ||
                userRegister.profileImgType().equals("jpg") ||
                userRegister.bannerImgType().equals("png")  ||
                userRegister.bannerImgType().equals("jpg")
        ){

            // registering user
            ServiceStatus serviceStatus = userService.registerUser(uid, userRegister);
            if(!serviceStatus.ok())
                return new ResponseEntity<>(serviceStatus.message(), HttpStatus.CREATED);

            return new ResponseEntity<>(serviceStatus.message(), HttpStatusCode.valueOf(409));
        }

        return new ResponseEntity<>("Banner and Profile type must me either jpg or png", HttpStatus.BAD_REQUEST);
    }
}
