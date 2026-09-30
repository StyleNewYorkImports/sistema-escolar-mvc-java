package br.com.escola.mvc.repository;

import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    boolean existsByAlunoIdAndCursoIdAndStatus(Long alunoId, Long cursoId, StatusMatricula status);
    boolean existsByAlunoIdAndCursoIdAndStatusAndIdNot(Long alunoId, Long cursoId, StatusMatricula status, Long id);
    boolean existsByCursoId(Long cursoId);
    boolean existsByAlunoId(Long alunoId);
}
