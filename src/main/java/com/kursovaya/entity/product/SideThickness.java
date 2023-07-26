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
@Table(name = "side_thickness")
public class SideThickness implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, name = "side_thickness_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    private double sideThickness;

    @ManyToMany(mappedBy = "sideThicknesses")
    @JsonIgnore
    private Set<ProductType> productTypes = new HashSet<>();

    public SideThickness() {
    }

    public SideThickness(Long id, double sideThickness, Set<ProductType> productTypes) {
        this.id = id;
        this.sideThickness = sideThickness;
        this.productTypes = productTypes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSideThickness() {
        return sideThickness;
    }

    public void setSideThickness(double sideThickness) {
        this.sideThickness = sideThickness;
    }

    public Set<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(Set<ProductType> productTypes) {
        this.productTypes = productTypes;
    }
}
