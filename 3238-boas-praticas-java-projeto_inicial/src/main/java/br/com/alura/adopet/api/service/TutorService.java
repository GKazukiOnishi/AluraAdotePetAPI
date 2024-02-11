package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.SolicitacaoTutorDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

    @Autowired
    private TutorRepository repository;

    public void cadastrar(SolicitacaoTutorDTO dto) {
        boolean jaCadastrado = repository.existsByTelefoneOrEmail(dto.telefone(), dto.email());

        if (jaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro tutor!");
        }

        Tutor novoTutor = new Tutor(dto.nome(), dto.telefone(), dto.email());
        repository.save(novoTutor);
    }

    public void atualizar(SolicitacaoTutorDTO tutor) {
        Tutor tutorExistente = repository.getReferenceById(tutor.id());
        tutorExistente.setNome(tutor.nome());
        tutorExistente.setTelefone(tutor.telefone());
        tutorExistente.setEmail(tutor.email());
        repository.save(tutorExistente);
    }
}
