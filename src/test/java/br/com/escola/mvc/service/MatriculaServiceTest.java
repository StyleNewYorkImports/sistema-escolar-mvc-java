package br.com.escola.mvc.service;

import br.com.escola.mvc.model.*;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.CursoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatriculaServiceTest {
    private final MatriculaRepository matriculas = mock(MatriculaRepository.class);
    private final AlunoRepository alunos = mock(AlunoRepository.class);
    private final CursoRepository cursos = mock(CursoRepository.class);
    private final MatriculaService service = new MatriculaService(matriculas, alunos, cursos);

    @Test
    void bloqueiaMatriculaAtivaDuplicada() {
        Aluno aluno = new Aluno("Ana", "ana@email.com", "RG1"); aluno.setId(1L);
        Curso curso = new Curso("Java", "", 40); curso.setId(2L);
        when(alunos.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursos.findById(2L)).thenReturn(Optional.of(curso));
        when(matriculas.existsByAlunoIdAndCursoIdAndStatus(1L, 2L, StatusMatricula.ATIVA)).thenReturn(true);
        assertTrue(assertThrows(IllegalArgumentException.class, () -> service.criar(1L, 2L)).getMessage().contains("ativa"));
        verify(matriculas, never()).save(any());
    }
}
