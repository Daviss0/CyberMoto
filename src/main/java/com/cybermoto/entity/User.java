package com.cybermoto.entity;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "USERID")
    private Long userId;

    @Column(nullable = false, unique = true, name = "EMAIL")
    private String email;

    @Column(nullable = false, name = "PASSWORD")
    @NotBlank(message = "A senha é obrigatória")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "TYPEUSER")
    private TypeUser type;

    public void setType(TypeUser type) {this.type = type;}

    public TypeUser getType() {return type;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public void setId(Long id) {this.userId = id;}

    public Long getId() {return userId;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}
