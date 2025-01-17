package me.edu.chifserver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseInitialization {
    @PostConstruct
    public void initialization() {
        if(FirebaseApp.getApps().isEmpty()){
            try {
                InputStream serviceAccount = getClass().getResourceAsStream("/serviceAccountKey.json");
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket(System.getenv("FIREBASE_STORAGE_BUCKET") + ".appspot.com")
                        .build();
                // initializing firebase
                FirebaseApp.initializeApp(options);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ;
        }

    }
}

