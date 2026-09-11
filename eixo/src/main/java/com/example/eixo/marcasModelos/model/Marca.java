package com.example.eixo.marcasModelos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "marcas_veiculo")
@NoArgsConstructor
@Getter
@Setter
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca", nullable = false)
    private Long idMarca;

    @Column(name = "nome_marca", nullable = false)
    private String nomeMarca;

    @OneToMany(mappedBy = "marca")
    @JsonIgnore
    private List<Modelo> listaModelos = new ArrayList<>();
}
