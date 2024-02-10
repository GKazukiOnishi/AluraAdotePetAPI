package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class ValidacaoPetComAdocaoEmAndamento implements ValidacaoSolicitacaoAdocao {

    @Autowired
    private AdocaoRepository repository;
    @Autowired
    private PetRepository petRepository;

    @Override
    public void validar(SolicitacaoAdocaoDTO dto) {
        Pet pet = petRepository.getReferenceById(dto.idPet());
        List<Adocao> adocoes = repository.findAll();
        for (Adocao a : adocoes) {
            if (a.getPet() == pet && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                throw new ValidacaoException("Pet já possui outra adoção aguardando avaliação!");
            }
        }
    }
}
