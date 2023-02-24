package ru.otus.crm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

import org.springframework.data.relational.core.mapping.Table;


@Table("phone")
@Data
@NoArgsConstructor
public
class Phone {
    @Id
    @NonNull
    private Long id;
    private String number;
    @PersistenceCreator
    public Phone(Long id, String number){
        this.id = id;
        this.number = number;
    }
}