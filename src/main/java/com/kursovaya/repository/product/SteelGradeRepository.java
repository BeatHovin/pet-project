package com.kursovaya.repository.product;

import com.kursovaya.entity.product.SteelGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SteelGradeRepository extends JpaRepository<SteelGrade, Long> {
    SteelGrade findSteelGradeBySteelGrade(String steelGrade);
}
