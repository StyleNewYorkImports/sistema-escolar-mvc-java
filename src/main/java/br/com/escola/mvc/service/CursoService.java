package br.com.escola.mvc.service;

import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.repository.CursoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CursoService {
    private final CursoRepository repository;
    private final MatriculaRepository matriculaRepository;
    public CursoService(CursoRepository repository, MatriculaRepository matriculaRepository) {
        this.repository = repository;
        this.matriculaRepository = matriculaRepository;
    }
    public List<Curso> listarTodos() { return repository.findAll(); }
    public Curso buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Curso não encontrado."));
    }
    @Transactional
    public Curso salvar(Curso curso) {
        if (curso.getNome() == null || curso.getNome().isBlank()) throw new IllegalArgumentException("O nome do curso é obrigatório.");
        if (curso.getCargaHoraria() == null || curso.getCargaHoraria() <= 0) throw new IllegalArgumentException("A carga horária deve ser maior que zero.");
        curso.setNome(curso.getNome().trim());
        if (curso.getDescricao() != null) curso.setDescricao(curso.getDescricao().trim());
        return repository.save(curso);
    }
    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Curso não encontrado.");
        if (matriculaRepository.existsByCursoId(id)) throw new IllegalArgumentException("Não é possível excluir este curso porque há matrículas vinculadas a ele.");
        repository.deleteById(id);
    }
}
