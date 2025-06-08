package com.testest.apisimulacao.service;

import com.testest.apisimulacao.entity.Cliente;
import com.testest.apisimulacao.entity.Conta;
import com.testest.apisimulacao.entity.TipoDocumento;
import com.testest.apisimulacao.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.InvalidStateException;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final CPFValidator cpfValidator = new CPFValidator();
    private final CNPJValidator cnpjValidator = new CNPJValidator();

    public Cliente cadastrarCliente(Cliente cliente) {

        //* Código responsável para verificação do tipo de documento e validação do documento utilizando a biblioteca caelum-stella-core

        // 1. Remover formatação para validação e armazenamento
        String documentoLimpo = cliente.getDocumento()
                .replaceAll("[^0-9]", ""); // Remove tudo que não for dígito
        cliente.setDocumento(documentoLimpo); // Atualiza o documento no objeto cliente

        // 2. Validar com base no tipoDocumento
        if (cliente.getTipoDocumento() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de documento é obrigatório.");
        }

        if (cliente.getTipoDocumento() == TipoDocumento.CPF) {
            if (cliente.getDocumento().length() != 11) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF deve conter 11 dígitos.");
            }
            try {
                cpfValidator.assertValid(cliente.getDocumento());
            } catch (InvalidStateException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido.");
            }
        } else if (cliente.getTipoDocumento() == TipoDocumento.CNPJ) {
            if (cliente.getDocumento().length() != 14) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ deve conter 14 dígitos.");
            }
            try {
                cnpjValidator.assertValid(cliente.getDocumento());
            } catch (InvalidStateException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ inválido.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de documento desconhecido.");
        }

        //* Código responsável para implementação de conversão de HASH para garantir a segurança da conta utilizando a biblioteca spring-boot-starter-security
        String senhaPura = cliente.getSenha();

        String senhaHash = passwordEncoder.encode(senhaPura);

        cliente.setSenha(senhaHash);

        // Garantir que as contas sejam cadastradas no cliente atual
        if (cliente.getContas() != null && !cliente.getContas().isEmpty()) {
            for (Conta conta : cliente.getContas()) {
                conta.setCliente(cliente);
            }
        }

        //* Código responsável para tratar o erro do tipo CONFLICT / duplicidade vindo do banco de dados
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();

            // MENSAGEM DO H2
            if (errorMessage != null) {
                String lowerCaseErrorMessage = errorMessage.toLowerCase();

                if (lowerCaseErrorMessage.contains("unique index or primary key violation")) {
                    // Identificar se é a restrição de documento ou agência
                    if (lowerCaseErrorMessage.contains("numero_documento")) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Documento (CPF/CNPJ) já cadastrado.");
                    }
                    if (lowerCaseErrorMessage.contains("agencia") || lowerCaseErrorMessage.contains("uk_agencia")) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Agência de conta já cadastrada.");
                    }
                    // fallback
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflito de dados: um registro único já existe.");
                }
            }
            // Outra violação de integridade, 500
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro de integridade de dados: " + e.getMessage());
        }

    }


    public List<Cliente> listarClientes() {
        return clienteRepository.findAll(); // A chamada para o repositório
    }
}