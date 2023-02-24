package ru.otus.crm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;


@Data
@NoArgsConstructor
@Table("address")
public
class Address {
    @Id
    @NonNull
    @MappedCollection(idColumn = "address_id", keyColumn= "id")
    private Long id;
    private String street;

    @PersistenceCreator
    public Address (Long id, String street){
        this.id = id;
        this.street = street;
    }

}