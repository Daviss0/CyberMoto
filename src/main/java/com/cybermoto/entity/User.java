package com.cybermoto.entity;

import com.cybermoto.enums.StatusUser;
import com.cybermoto.enums.TypeUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {
    //colunas:

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "USERID")
    private Long userId;

    @Column(nullable = false, name= "NAME")
    private String name;

    @Column(nullable = false, name = "CPF")
    private String cpf;

    @Column(nullable = false, unique = true, name = "EMAIL")
    private String email;

    @Column(nullable = false, name = "PASSWORD")
    @NotBlank(message = "A senha é obrigatória")
    private String password;

    @Transient
    private String ConfPassword;


    @Column(nullable = false, name = "TYPEUSER")
    @Enumerated(EnumType.STRING)
    private TypeUser type;

    @Column(nullable = false, name = "STATUS")
    private StatusUser status = StatusUser.valueOf("ATIVO");


    //getters & setters
    public void setId(Long id) {this.userId = id;}

    public Long getId() {return userId;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getCpf() {return cpf;}

    public void setCpf(String cpf) {this.cpf = cpf;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public void setType(TypeUser type) {this.type = type;}

    public TypeUser getType() {return type;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public void setConfPassword(String confPassword) {ConfPassword = confPassword;}

    public String getConfPassword() {return ConfPassword;}

    public StatusUser getStatus() {return status;}

    public void setStatus(StatusUser status) {this.status = status;}

}
