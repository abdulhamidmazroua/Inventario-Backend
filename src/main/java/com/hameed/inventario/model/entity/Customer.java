package com.hameed.inventario.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers", schema = "inventario-directory")
@Getter
@Setter
public class Customer extends AbstractEntity {

    @Column(name = "name")
    private String customerName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Sale> sales = new HashSet<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductReturn> productReturns;

    public void addSale(Sale sale) {
        if (sale != null) {
            if (sales == null) {
                sales = new HashSet<>();
            }
            sales.add(sale);
            sale.setCustomer(this);
        }
    }

}
