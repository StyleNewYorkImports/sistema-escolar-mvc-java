package br.com.escola.mvc.service;

import br.com.escola.mvc.model.*;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.CursoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculaService {
    private final MatriculaRepository repository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    public MatriculaService(MatriculaRepository repository, AlunoRepository alunoRepository, CursoRepository cursoRepository) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }
    public List<Matricula> listarTodas() { return repository.findAll(); }
    public List<Aluno> listarAlunos() { return alunoRepository.findAll(); }
    public List<Curso> listarCursos() { return cursoRepository.findAll(); }

    @Transactional
    public Matricula criar(Long alunoId, Long cursoId) {
        if (alunoId == null) throw new IllegalArgumentException("Selecione um aluno cadastrado.");
        if (cursoId == null) throw new IllegalArgumentException("Selecione um curso cadastrado.");
        Aluno aluno = alunoRepository.findById(alunoId).orElseThrow(() -> new IllegalArgumentException("Selecione um aluno cadastrado."));
        Curso curso = cursoRepository.findById(cursoId).orElseThrow(() -> new IllegalArgumentException("Selecione um curso cadastrado."));
        if (repository.existsByAlunoIdAndCursoIdAndStatus(alunoId, cursoId, StatusMatricula.ATIVA)) {
            throw new IllegalArgumentException("Este aluno já tem uma matrícula ativa neste curso.");
        }
        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setCurso(curso);
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus(StatusMatricula.ATIVA);
        return repository.save(matricula);
    }

    @Transactional
    public void alterarStatus(Long id, StatusMatricula novoStatus) {
        Matricula matricula = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Matrícula não encontrada."));
        if (novoStatus == null) throw new IllegalArgumentException("Selecione um status válido.");
        if (novoStatus == StatusMatricula.ATIVA) {
            boolean existeOutraAtiva = matricula.getId() == null
                    ? repository.existsByAlunoIdAndCursoIdAndStatus(matricula.getAluno().getId(), matricula.getCurso().getId(), StatusMatricula.ATIVA)
                    : repository.existsByAlunoIdAndCursoIdAndStatusAndIdNot(matricula.getAluno().getId(), matricula.getCurso().getId(), StatusMatricula.ATIVA, id);
            if (existeOutraAtiva) throw new IllegalArgumentException("O aluno já possui outra matrícula ativa neste curso.");
        }
        matricula.setStatus(novoStatus);
        repository.save(matricula);
    }
}
