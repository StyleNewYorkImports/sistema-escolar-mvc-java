package br.com.escola.mvc;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.CursoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    public DataInitializer(AlunoRepository alunoRepository, CursoRepository cursoRepository, MatriculaRepository matriculaRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
    }

    @Override
    public void run(String... args) {
        if (alunoRepository.count() == 0 && cursoRepository.count() == 0) {
            Aluno aluno = alunoRepository.save(new Aluno("Ana Souza", "ana.souza@email.com", "12.345.678-9"));
            Curso curso = cursoRepository.save(new Curso("Desenvolvimento de Sistemas", "Lógica, programação e criação de sistemas.", 120));

            Matricula matricula = new Matricula();
            matricula.setAluno(aluno);
            matricula.setCurso(curso);
            matricula.setDataMatricula(LocalDate.now());
            matricula.setStatus(StatusMatricula.ATIVA);
            matriculaRepository.save(matricula);
        }
    }
}
