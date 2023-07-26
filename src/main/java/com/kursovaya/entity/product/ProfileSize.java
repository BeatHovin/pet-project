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
@Table(name = "profile_sizes")
public class ProfileSize implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, name = "profile_size_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    private int size;

    @ManyToMany(mappedBy = "profileSizes")
    @JsonIgnore
    private Set<ProductType> productTypes = new HashSet<>();

    public ProfileSize() {
    }

    public ProfileSize(Long id, int size, Set<ProductType> productTypes) {
        this.id = id;
        this.size = size;
        this.productTypes = productTypes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Set<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(Set<ProductType> productTypes) {
        this.productTypes = productTypes;
    }
}
