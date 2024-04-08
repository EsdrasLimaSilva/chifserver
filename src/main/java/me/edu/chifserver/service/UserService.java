package me.edu.chifserver.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import me.edu.chifserver.dto.UserDto;
import me.edu.chifserver.dto.UserRegisterDto;
import me.edu.chifserver.model.User;
import me.edu.chifserver.repository.UserRepository;
import me.edu.chifserver.utils.Base64Image;
import org.neo4j.cypherdsl.core.Use;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private String convertMediaLinkToUrl(String mediaLink){
        return "https://firebasestorage.googleapis.com/v0/" + mediaLink.substring(mediaLink.indexOf("b"));
    }

    public boolean userExists(String uid){
        return userRepository.findById(uid).isPresent();
    }

    public UserDto findUserByUsername(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if(optionalUser.isEmpty())
            return null;

        User user = optionalUser.get();

        return new UserDto(user.get_Id(), user.getName(), user.getUsername(), user.getBannerImageUrl(), user.getProfileImageUrl());
    }

    public ServiceStatus registerUser(String uid, UserRegisterDto userRegister){
        Bucket bucket = StorageClient.getInstance().bucket();

        // getting the images from base64 encode
        byte[] bannerImage = Base64Image.convertToByteArray(userRegister.base64Banner());
        byte[] profileImage = Base64Image.convertToByteArray(userRegister.base64Profile());

        // inserting into the bucket
        Blob bannerBlob = bucket.create(userRegister.username() + "/images/banner." + userRegister.bannerImgType(), bannerImage, "image/"+userRegister.bannerImgType());
        Blob profileBlob = bucket.create(userRegister.username() + "/images/profile." + userRegister.profileImgType(), profileImage, "image/"+userRegister.profileImgType());

        // replacing media url to file url
        String bannerMediaLink = bannerBlob.getMediaLink();
        String profileMediaLink = profileBlob.getMediaLink();

        String bannerUrl = convertMediaLinkToUrl(bannerMediaLink);
        String profileUrl = convertMediaLinkToUrl(profileMediaLink);

        // creating the user in the database
        User newUser = new User(uid, userRegister.name(), userRegister.username(), bannerUrl, profileUrl);
        userRepository.insert(newUser);

        return new ServiceStatus(true, "User created successfully");
    }
}
