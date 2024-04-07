package me.edu.chifserver.controller;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import me.edu.chifserver.dto.UserDto;
import me.edu.chifserver.dto.UserRegisterDto;
import me.edu.chifserver.model.User;
import me.edu.chifserver.repository.UserRepository;
import me.edu.chifserver.utils.Base64Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserRepository userRepository;

    // injecting dependecies
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private String convertToUrl(String mediaLink){
        return "https://firebasestorage.googleapis.com/v0/" + mediaLink.substring(mediaLink.indexOf("b"));
    }

    @PostMapping("/profile")
    public ResponseEntity<String> registerUser(
            HttpServletRequest request,
            @Valid @RequestBody UserRegisterDto userRegister,
            @RequestHeader String PROFILE_IMAGE_TYPE, // png or jpg...
            @RequestHeader String BANNER_IMAGE_TYPE
    ){

        // checking if user exists
        String uid = (String) request.getAttribute("uid");
        Optional<User> user = userRepository.findById(uid);

        if(user.isPresent()) // user already exists
            return new ResponseEntity<>("User already exists", HttpStatusCode.valueOf(409));



        // checking if images type are compatible
        if(
                PROFILE_IMAGE_TYPE.equals("png") ||
                PROFILE_IMAGE_TYPE.equals("jpg") ||
                BANNER_IMAGE_TYPE.equals("png")  ||
                BANNER_IMAGE_TYPE.equals("jpg")
        ){

            Bucket bucket = StorageClient.getInstance().bucket();

            // getting the images from base64 encode
            byte[] bannerImage = Base64Image.convertToByteArray(userRegister.base64Banner());
            byte[] profileImage = Base64Image.convertToByteArray(userRegister.base64Profile());

            // inserting into the bucket
            Blob bannerBlob = bucket.create(userRegister.username() + "/images/banner." + BANNER_IMAGE_TYPE, bannerImage, "image/"+BANNER_IMAGE_TYPE);
            Blob profileBlob = bucket.create(userRegister.username() + "/images/profile." + PROFILE_IMAGE_TYPE, profileImage, "image/"+PROFILE_IMAGE_TYPE);

            // replacing media url to file url
            String bannerMediaLink = bannerBlob.getMediaLink();
            String profileMediaLink = profileBlob.getMediaLink();

            String bannerUrl = convertToUrl(bannerMediaLink);
            String profileUrl = convertToUrl(profileMediaLink);

            // creating the user in the database
            User newUser = new User(uid, userRegister.name(), userRegister.username(), bannerUrl, profileUrl);
            userRepository.insert(newUser);


            return new ResponseEntity<>("User created: " + newUser.getName() + profileUrl, HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Banner and Profile type must me either jpg or png", HttpStatus.BAD_REQUEST);
        }
    }
}
