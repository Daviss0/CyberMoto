package com.cybermoto.entity;

import com.cybermoto.enums.UF;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ADDRESS")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CEP", nullable = false)
    @NotBlank(message = "CEP é obrigatório")
    private String cep;

    @Column(name = "STREET", nullable = false)
    @NotBlank(message = "O logradouro é obrigatório")
    private String street;

    @Column(name = "NUMBER", nullable = false)
    @NotNull(message = "O número é obrigatório")
    @Min(value = 1, message = "Número deve ser maior que zero")
    private Integer number;

    @Column(name = "COMPLEMENT")
    @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres")
    private String complement;

    @Column(name = "NEIGHBORHOOD", nullable = false)
    @NotBlank(message = "O bairro é obrigatório")
    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres")
    private String neighborhood;

    @Column(name = "CITY", nullable = false)
    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "UF", nullable = false)
    @NotNull(message = "UF é obrigatório")
    private UF uf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


    //getters e setters
    public void setId(Long id) {this.id = id;}

    public Long getId() {return id;}

    public String getCep() {return cep;}

    public void setCep(String cep) {this.cep = cep;}

    public String getStreet() {return street;}

    public void setStreet(String street) {this.street = street;}

    public  Integer getNumber() {return number;}

    public void setNumber(Integer number) {this.number = number;}

    public String getComplement() {return complement;}

    public void setComplement(String complement) {this.complement = complement;}

    public String getNeighborhood() {return neighborhood;}

    public void setNeighborhood(String neighborhood) {this.neighborhood = neighborhood;}

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public UF getUf() {return uf;}

    public void setUf(UF uf) {this.uf = uf;}

    public Client getClient() {return client;}

    public void setClient(Client client) {this.client = client;}

}
