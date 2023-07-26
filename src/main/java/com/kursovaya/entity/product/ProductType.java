package com.kursovaya.entity.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_types")

public class ProductType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, name = "product_type_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    private String productType;


    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "producttype_steelgrade",
            joinColumns = { @JoinColumn(name = "product_type_id") },
            inverseJoinColumns = { @JoinColumn(name = "steel_grade_id") }
    )
    Set<SteelGrade> steelGrades = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "producttype_length",
            joinColumns = { @JoinColumn(name = "product_type_id") },
            inverseJoinColumns = { @JoinColumn(name = "length_id") }
    )
    Set<ProductLength> lengths = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "producttype_profilesize",
            joinColumns = { @JoinColumn(name = "product_type_id") },
            inverseJoinColumns = { @JoinColumn(name = "profile_size_id") }
    )
    Set<ProfileSize> profileSizes = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "producttype_sidethickness",
            joinColumns = { @JoinColumn(name = "product_type_id") },
            inverseJoinColumns = { @JoinColumn(name = "side_thickness_id") }
    )
    Set<SideThickness> sideThicknesses = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "producttype_standard",
            joinColumns = { @JoinColumn(name = "product_type_id") },
            inverseJoinColumns = { @JoinColumn(name = "standard_id") }
    )
    Set<Standard> standards = new HashSet<>();

    public ProductType() {
    }

    public ProductType(Long id, String productType, Set<SteelGrade> steelGrades, Set<ProductLength> lengths, Set<ProfileSize> profileSizes, Set<SideThickness> sideThicknesses, Set<Standard> standards) {
        this.id = id;
        this.productType = productType;
        this.steelGrades = steelGrades;
        this.lengths = lengths;
        this.profileSizes = profileSizes;
        this.sideThicknesses = sideThicknesses;
        this.standards = standards;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Set<SteelGrade> getSteelGrades() {
        return steelGrades;
    }

    public void setSteelGrades(Set<SteelGrade> steelGrades) {
        this.steelGrades = steelGrades;
    }

    public Set<ProductLength> getLengths() {
        return lengths;
    }

    public void setLengths(Set<ProductLength> lengths) {
        this.lengths = lengths;
    }

    public Set<ProfileSize> getProfileSizes() {
        return profileSizes;
    }

    public void setProfileSizes(Set<ProfileSize> profileSizes) {
        this.profileSizes = profileSizes;
    }

    public Set<SideThickness> getSideThicknesses() {
        return sideThicknesses;
    }

    public void setSideThicknesses(Set<SideThickness> sideThicknesses) {
        this.sideThicknesses = sideThicknesses;
    }

    public Set<Standard> getStandards() {
        return standards;
    }

    public void setStandards(Set<Standard> standards) {
        this.standards = standards;
    }
}
