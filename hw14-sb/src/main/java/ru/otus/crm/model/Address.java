package ru.otus.crm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;


@Data
@NoArgsConstructor
@Table("address")
public
class Address {
    @Id
    @NonNull
    private String id;
    private String street;

    @PersistenceCreator
    public Address (String id, String street){
        this.id = (id==null) ? UUID.randomUUID().toString():id;
        this.street = street;
    }

}