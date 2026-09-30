package br.com.escola.mvc.controller;

import br.com.escola.mvc.model.StatusMatricula;
import br.com.escola.mvc.service.MatriculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/matriculas")
public class MatriculaController {
    private final MatriculaService service;
    public MatriculaController(MatriculaService service) { this.service = service; }
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("matriculas", service.listarTodas());
        model.addAttribute("statusOpcoes", StatusMatricula.values());
        return "matriculas/lista";
    }
    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("alunos", service.listarAlunos());
        model.addAttribute("cursos", service.listarCursos());
        return "matriculas/formulario";
    }
    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Long alunoId, @RequestParam(required = false) Long cursoId, RedirectAttributes redirect) {
        try { service.criar(alunoId, cursoId); redirect.addFlashAttribute("sucesso", "Matrícula criada com status ativa."); return "redirect:/matriculas"; }
        catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            redirect.addFlashAttribute("alunoSelecionado", alunoId);
            redirect.addFlashAttribute("cursoSelecionado", cursoId);
            return "redirect:/matriculas/nova";
        }
    }
    @PostMapping("/{id}/status")
    public String alterarStatus(@PathVariable Long id, @RequestParam StatusMatricula status, RedirectAttributes redirect) {
        try { service.alterarStatus(id, status); redirect.addFlashAttribute("sucesso", "Status da matrícula atualizado."); }
        catch (IllegalArgumentException e) { redirect.addFlashAttribute("erro", e.getMessage()); }
        return "redirect:/matriculas";
    }
}
