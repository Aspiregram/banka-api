package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Moeda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String nome;

    @Column(columnDefinition = "CHAR(3)", nullable = false, unique = true)
    private String sigla;

    @ManyToMany(mappedBy = "moedas")
    private List<Pais> paises;

}
