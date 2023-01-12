package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "phone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public
class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

   // @ManyToOne(fetch = FetchType.LAZY)
   // @JoinColumn(name = "client_id")
 //   @Column(name = "client_id")
  //  private Client client;

   // public Phone (Long id, String number){
  //    this.id = id;
  //    this.number = number;
  //    this.client = null;
  //  }



}