package br.com.escola.mvc.service;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlunoServiceTest {
    private final AlunoRepository alunos = mock(AlunoRepository.class);
    private final MatriculaRepository matriculas = mock(MatriculaRepository.class);
    private final AlunoService service = new AlunoService(alunos, matriculas);

    @Test
    void rejeitaRgVazio() {
        Aluno aluno = new Aluno("Ana", "ana@email.com", " ");
        assertEquals("O RG é obrigatório.", assertThrows(IllegalArgumentException.class, () -> service.salvar(aluno)).getMessage());
        verify(alunos, never()).save(any());
    }

    @Test
    void rejeitaRgDuplicadoAoCadastrar() {
        when(alunos.existsByRgIgnoreCase("123")).thenReturn(true);
        Aluno aluno = new Aluno("Ana", "ana@email.com", "123");
        assertTrue(assertThrows(IllegalArgumentException.class, () -> service.salvar(aluno)).getMessage().contains("RG"));
        verify(alunos, never()).save(any());
    }

    @Test
    void edicaoIgnoraOProprioRg() {
        Aluno aluno = new Aluno("Ana", "ana@email.com", "123");
        aluno.setId(8L);
        when(alunos.existsByRgIgnoreCaseAndIdNot("123", 8L)).thenReturn(false);
        when(alunos.save(aluno)).thenReturn(aluno);
        assertSame(aluno, service.salvar(aluno));
        verify(alunos).existsByRgIgnoreCaseAndIdNot("123", 8L);
    }
}
