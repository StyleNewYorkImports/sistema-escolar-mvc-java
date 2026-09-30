package br.com.escola.mvc.service;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AlunoService {
    private final AlunoRepository repository;
    private final MatriculaRepository matriculaRepository;

    public AlunoService(AlunoRepository repository, MatriculaRepository matriculaRepository) {
        this.repository = repository;
        this.matriculaRepository = matriculaRepository;
    }

    public List<Aluno> listarTodos() { return repository.findAll(); }
    public Aluno buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));
    }

    @Transactional
    public Aluno salvar(Aluno aluno) {
        if (aluno.getNome() == null || aluno.getNome().isBlank()) throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        if (aluno.getEmail() == null || aluno.getEmail().isBlank()) throw new IllegalArgumentException("O e-mail do aluno é obrigatório.");
        if (aluno.getRg() == null || aluno.getRg().isBlank()) throw new IllegalArgumentException("O RG é obrigatório.");
        aluno.setNome(aluno.getNome().trim());
        aluno.setEmail(aluno.getEmail().trim());
        aluno.setRg(aluno.getRg().trim());
        boolean rgEmUso = aluno.getId() == null
                ? repository.existsByRgIgnoreCase(aluno.getRg())
                : repository.existsByRgIgnoreCaseAndIdNot(aluno.getRg(), aluno.getId());
        if (rgEmUso) throw new IllegalArgumentException("Já existe um aluno cadastrado com esse RG.");
        return repository.save(aluno);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Aluno não encontrado.");
        if (matriculaRepository.existsByAlunoId(id)) throw new IllegalArgumentException("Não é possível excluir este aluno porque há matrículas vinculadas a ele.");
        repository.deleteById(id);
    }
}
