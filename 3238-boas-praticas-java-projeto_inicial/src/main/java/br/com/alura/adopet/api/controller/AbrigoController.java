package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.DadosDetalheAbrigoDTO;
import br.com.alura.adopet.api.dto.DadosDetalhePetDTO;
import br.com.alura.adopet.api.dto.SolicitacaoAbrigoDTO;
import br.com.alura.adopet.api.dto.SolicitacaoPetDTO;
import br.com.alura.adopet.api.exception.NotFoundException;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.service.AbrigoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abrigos")
public class AbrigoController {

    @Autowired
    private AbrigoService abrigoService;

    @GetMapping
    public ResponseEntity<List<DadosDetalheAbrigoDTO>> listar() {
        return ResponseEntity.ok(abrigoService.listar());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid SolicitacaoAbrigoDTO dto) {
        try {
            abrigoService.cadastrar(dto);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idOuNome}/pets")
    public ResponseEntity<List<DadosDetalhePetDTO>> listarPets(@PathVariable String idOuNome) {
        try {
            List<DadosDetalhePetDTO> pets = abrigoService.listarPets(idOuNome);
            if (pets.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(pets);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{idOuNome}/pets")
    @Transactional
    public ResponseEntity<String> cadastrarPet(@PathVariable String idOuNome, @RequestBody @Valid SolicitacaoPetDTO dto) {
        try {
            abrigoService.cadastrarPet(idOuNome, dto);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
