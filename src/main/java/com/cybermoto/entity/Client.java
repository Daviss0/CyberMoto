package com.cybermoto.entity;

import com.cybermoto.enums.Gender;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "CLIENT", uniqueConstraints = {@UniqueConstraint (columnNames = "EMAIL")})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(name = "NAME", nullable = false)
   private String name;

   @Column(name = "CPF", nullable = false, unique = true)
    private String cpf;

   @NotBlank(message = "E-mail é obrigatório")
   @Email(message = "E-mail inválido")
   @Column (name = "EMAIL", nullable = false)
   private String email;

   @NotBlank(message = "A senha é obrigatória")
   @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres")
   @Column(name = "PASSWORD", nullable = false)
   private String password;

   @Transient
   @NotBlank(message = "Confirmação de senha é obrigatória")
   private String confPassword;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column (name = "BIRTH", nullable = false)
   private LocalDate birth;

   @Enumerated(EnumType.STRING)
   @Column(name = "GENDER", nullable = false)
   private Gender gender;


   @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
   private List<Address> addresses;

   // getters & setters
    public void setId(Long id) {this.id = id;}

    public Long getId() {return id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getCpf() {return cpf;}

    public void setCpf(String cpf) {this.cpf = cpf;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getConfPassword() {return confPassword;}

    public void setConfPassword(String confPassword) {this.confPassword = confPassword;}

    public LocalDate getBirth() {return birth;}

    public void setBirth(LocalDate birth) {this.birth = birth;}

    public Gender getGender() {return gender;}

    public void setGender(Gender gender) {this.gender = gender;}


    public List<Address> getAddresses() {return addresses;}

    public void setAddresses(List<Address> addresses) {this.addresses = addresses;}

}
