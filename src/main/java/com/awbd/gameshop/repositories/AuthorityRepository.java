package com.awbd.gameshop.repositories;

import com.awbd.gameshop.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    Authority findByAuthority(String roleUser);
}
