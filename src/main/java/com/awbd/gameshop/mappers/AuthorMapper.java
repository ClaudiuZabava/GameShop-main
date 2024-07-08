package com.awbd.gameshop.mappers;

import com.awbd.gameshop.dtos.RequestAuthor;
import com.awbd.gameshop.dtos.RequestUser;
import com.awbd.gameshop.models.Author;
import com.awbd.gameshop.models.User;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public Author requestAuthor(RequestAuthor author) {
        return new Author(
            author.getFirstName(),
            author.getLastName(),
            author.getNationality()
        );
    }
}
