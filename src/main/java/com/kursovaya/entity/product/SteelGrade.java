package com.kursovaya.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "steel_grade")
public class SteelGrade implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, name = "steel_grade_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @ManyToMany(mappedBy = "steelGrades")
    @JsonIgnore
    private Set<ProductType> productTypes = new HashSet<>();

    private String steelGrade;

    public SteelGrade() {
    }

    public SteelGrade(Long id, Set<ProductType> productTypes, String steelGrade) {
        this.id = id;
        this.productTypes = productTypes;
        this.steelGrade = steelGrade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(Set<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public String getSteelGrade() {
        return steelGrade;
    }

    public void setSteelGrade(String steelGrade) {
        this.steelGrade = steelGrade;
    }
}
