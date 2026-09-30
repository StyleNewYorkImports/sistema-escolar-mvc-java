package br.com.escola.mvc.repository;

import br.com.escola.mvc.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByRgIgnoreCase(String rg);
    boolean existsByRgIgnoreCaseAndIdNot(String rg, Long id);
}
