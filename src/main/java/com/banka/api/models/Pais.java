package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false, unique = true)
    private String nome;

    @Column(columnDefinition = "CHAR(3)", nullable = false, unique = true)
    private String isoCode;

    @OneToMany(mappedBy = "pais", fetch = FetchType.LAZY)
    private List<Moeda> moedas;

}
