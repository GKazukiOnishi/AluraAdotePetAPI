package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.SolicitacaoTutorDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TutorService {

    @Autowired
    private TutorRepository repository;

    public void cadastrar(SolicitacaoTutorDTO dto) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(dto.telefone());
        boolean emailJaCadastrado = repository.existsByEmail(dto.email());

        if (telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro tutor!");
        } else {
            Tutor novoTutor = new Tutor(dto.nome(), dto.telefone(), dto.email());
            repository.save(novoTutor);
        }
    }

    public void atualizar(SolicitacaoTutorDTO tutor) {
        Tutor tutorExistente = repository.getReferenceById(tutor.id());
        tutorExistente.setNome(tutor.nome());
        tutorExistente.setTelefone(tutor.telefone());
        tutorExistente.setEmail(tutor.email());
        repository.save(tutorExistente);
    }
}
