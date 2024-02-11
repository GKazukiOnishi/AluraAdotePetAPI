package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.DadosDetalheAbrigoDTO;
import br.com.alura.adopet.api.dto.DadosDetalhePetDTO;
import br.com.alura.adopet.api.dto.SolicitacaoAbrigoDTO;
import br.com.alura.adopet.api.dto.SolicitacaoPetDTO;
import br.com.alura.adopet.api.exception.NotFoundException;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoService {

    @Autowired
    private AbrigoRepository repository;

    public List<DadosDetalheAbrigoDTO> listar() {
        return repository.findAll().stream().map(DadosDetalheAbrigoDTO::new).toList();
    }

    public void cadastrar(SolicitacaoAbrigoDTO abrigo) {
        boolean jaCadastrado = repository.existsByNomeOrTelefoneOrEmail(abrigo.nome(), abrigo.telefone(), abrigo.email());

        if (jaCadastrado) {
            throw new ValidacaoException("Dados não podem ser duplicados para outro abrigo!");
        }
        
        Abrigo novoAbrigo = new Abrigo(abrigo.nome(), abrigo.telefone(), abrigo.email());
        repository.save(novoAbrigo);
    }

    public Abrigo buscarPorIdOuNome(String idOuNome) {
        Abrigo abrigo = null;
        try {
            Long id = Long.parseLong(idOuNome);
            abrigo = repository.getReferenceById(id);
        } catch (NumberFormatException e) {
            abrigo = repository.findByNome(idOuNome);
        } catch (EntityNotFoundException enfe) {
            return abrigo;
        }
        return abrigo;
    }

    private Abrigo buscarPorIdOuNomeExistente(String idOuNome) {
        Abrigo abrigo = buscarPorIdOuNome(idOuNome);
        if (abrigo == null) {
            throw new NotFoundException();
        }
        return abrigo;
    }

    public List<DadosDetalhePetDTO> listarPets(String idOuNome) {
        Abrigo abrigo = buscarPorIdOuNomeExistente(idOuNome);
        List<Pet> pets = abrigo.getPets();
        return pets.stream().map(DadosDetalhePetDTO::new).toList();
    }

    public void cadastrarPet(String idOuNome, SolicitacaoPetDTO dto) {
        Abrigo abrigo = buscarPorIdOuNomeExistente(idOuNome);

        Pet novoPet = new Pet(dto.tipo(), dto.nome(), dto.raca(), dto.idade(), dto.cor(), dto.peso());
        abrigo.adicionaPet(novoPet);

        repository.save(abrigo);
    }
}
