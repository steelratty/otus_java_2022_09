package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.Entity;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public
class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street;


    /*
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = Client.class)
    @JoinColumn(name = "client_id",  foreignKey = @ForeignKey(name = "FK_ADDRESS_CLIENT"), referencedColumnName = "id")
    private Client client;

    public Address(Long id, String street){
        this.id = id;
        this.street = street;
        this.client = null;
    }
    */

}