package com.kursovaya.repository.product;

import com.kursovaya.entity.product.SideThickness;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SideThicknessRepository extends JpaRepository<SideThickness, Long> {
    SideThickness findSideThicknessBySideThickness(double sideThickness);
}
