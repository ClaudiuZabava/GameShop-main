package com.awbd.gameshop.mappers;

import com.awbd.gameshop.dtos.RequestCategory;
import com.awbd.gameshop.dtos.RequestUser;
import com.awbd.gameshop.models.Category;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Request;

@Component
public class CategoryMapper {
    public Category requestCategory(String category) {
        return new Category(category);
    }
}
