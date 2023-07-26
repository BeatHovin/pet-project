package com.kursovaya.repository.product;

import com.kursovaya.entity.product.ProfileSize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileSizeRepository extends JpaRepository<ProfileSize, Long> {
    ProfileSize findProfileSizeBySize(int size);
}
