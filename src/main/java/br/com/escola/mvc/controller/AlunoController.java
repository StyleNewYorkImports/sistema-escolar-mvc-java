package br.com.escola.mvc.controller;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoService service;
    public AlunoController(AlunoService service) { this.service = service; }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alunos", service.listarTodos());
        return "alunos/lista";
    }
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("aluno", model.containsAttribute("alunoRascunho") ? model.asMap().get("alunoRascunho") : new Aluno());
        return "alunos/formulario";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", model.containsAttribute("alunoRascunho") ? model.asMap().get("alunoRascunho") : service.buscarPorId(id));
        return "alunos/formulario";
    }
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Aluno aluno, RedirectAttributes redirect) {
        try {
            service.salvar(aluno);
            redirect.addFlashAttribute("sucesso", "Aluno salvo com sucesso.");
            return "redirect:/alunos";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            redirect.addFlashAttribute("alunoRascunho", aluno);
            return "redirect:" + (aluno.getId() == null ? "/alunos/novo" : "/alunos/editar/" + aluno.getId());
        }
    }
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.excluir(id);
            redirect.addFlashAttribute("sucesso", "Aluno excluído.");
        } catch (IllegalArgumentException e) { redirect.addFlashAttribute("erro", e.getMessage()); }
        return "redirect:/alunos";
    }
}
