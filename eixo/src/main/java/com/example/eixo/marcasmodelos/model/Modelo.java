package com.example.eixo.marcasmodelos.model;

import com.example.eixo.veiculo.model.Veiculo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "modelos_veiculo")
@NoArgsConstructor
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo", nullable = false)
    private Long idModelo;

    @Column(name = "nome_modelo", nullable = false)
    private String nomeModelo;

    @ManyToOne
    @JoinColumn(name = "marcas_veiculo_id_marca", nullable = false)
    Marca marca;

    @OneToMany(mappedBy = "modelo")
    @JsonIgnore
    List<Veiculo> listaVeiculos = new ArrayList<>();
}
