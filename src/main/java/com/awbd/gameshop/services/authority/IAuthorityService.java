package com.awbd.gameshop.services.authority;

import com.awbd.gameshop.models.Authority;

public interface IAuthorityService {
    Authority getAuthorityByName(String authority);
}
