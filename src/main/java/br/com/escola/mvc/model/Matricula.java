package br.com.escola.mvc.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "matricula")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMatricula status;

    public Matricula() { }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public LocalDate getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDate dataMatricula) { this.dataMatricula = dataMatricula; }
    public StatusMatricula getStatus() { return status; }
    public void setStatus(StatusMatricula status) { this.status = status; }
}
