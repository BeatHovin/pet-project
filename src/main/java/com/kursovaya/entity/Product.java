package com.kursovaya.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kursovaya.entity.order.OrderDetails;
import com.kursovaya.entity.product.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, name = "product_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    private int quantity;
    private double price;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderDetails> orderDetails = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_type_id", referencedColumnName = "product_type_id")
    private ProductType productType;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_length_id", referencedColumnName = "length_id")
    private ProductLength productLength;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_size_id", referencedColumnName = "profile_size_id")
    private ProfileSize profileSize;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "standard_id", referencedColumnName = "standard_id")
    private Standard standard;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "side_thickness_id", referencedColumnName = "side_thickness_id")
    private SideThickness sideThickness;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "steel_grade_id", referencedColumnName = "steel_grade_id")
    private SteelGrade steelGrade;

}
