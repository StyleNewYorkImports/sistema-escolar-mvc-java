package br.com.escola.mvc.controller;

import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.service.CursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cursos")
public class CursoController {
    private final CursoService service;
    public CursoController(CursoService service) { this.service = service; }
    @GetMapping
    public String listar(Model model) { model.addAttribute("cursos", service.listarTodos()); return "cursos/lista"; }
    @GetMapping("/novo")
    public String novo(Model model) { model.addAttribute("curso", model.containsAttribute("cursoRascunho") ? model.asMap().get("cursoRascunho") : new Curso()); return "cursos/formulario"; }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) { model.addAttribute("curso", model.containsAttribute("cursoRascunho") ? model.asMap().get("cursoRascunho") : service.buscarPorId(id)); return "cursos/formulario"; }
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Curso curso, RedirectAttributes redirect) {
        try {
            service.salvar(curso);
            redirect.addFlashAttribute("sucesso", "Curso salvo com sucesso.");
            return "redirect:/cursos";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            redirect.addFlashAttribute("cursoRascunho", curso);
            return "redirect:" + (curso.getId() == null ? "/cursos/novo" : "/cursos/editar/" + curso.getId());
        }
    }
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try { service.excluir(id); redirect.addFlashAttribute("sucesso", "Curso excluído."); }
        catch (IllegalArgumentException e) { redirect.addFlashAttribute("erro", e.getMessage()); }
        return "redirect:/cursos";
    }
}
