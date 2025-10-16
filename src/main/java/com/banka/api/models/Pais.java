package com.banka.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pais")
public class Pais {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(length = 100, nullable = false, unique = true) // Tamanho ajustado para 100 do BD
    private String nome;

    @Column(columnDefinition = "CHAR(3)", nullable = false, unique = true) // Ajustado para CHAR(3) do BD
    private String isoCode;


    @OneToMany(mappedBy = "pais", fetch = FetchType.LAZY)
    private List<Moeda> moedas;



}