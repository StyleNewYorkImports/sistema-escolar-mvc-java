package br.com.escola.mvc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aluno")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 30)
    private String rg;

    public Aluno() { }

    public Aluno(String nome, String email, String rg) {
        this.nome = nome;
        this.email = email;
        this.rg = rg;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
}
