package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(4)
public class ValidacaoTutorComLimiteDeAdocoes implements ValidacaoSolicitacaoAdocao {

    @Autowired
    private AdocaoRepository repository;
    @Autowired
    private TutorRepository tutorRepository;

    @Override
    public void validar(SolicitacaoAdocaoDTO dto) {
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());
        List<Adocao> adocoes = repository.findAll();
        for (Adocao a : adocoes) {
            int contador = 0;
            if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.APROVADO) {
                contador = contador + 1;
            }
            if (contador == 5) {
                throw new ValidacaoException("Tutor atingiu o limite máximo de 5 adoções!");
            }
        }
    }
}
