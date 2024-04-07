package me.edu.chifserver.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@Getter
@Document
public class User {
    @Id
    private String _Id;
    @NotNull
    String name;

    @NotNull
    @Indexed(unique = true)
    String username;
    String bannerImageUrl;
    String profileImageUrl;
}
