package com.testest.apisimulacao.service;

import com.testest.apisimulacao.dto.ClienteResponseDTO;
import com.testest.apisimulacao.dto.CriarClienteRequestDTO;
import com.testest.apisimulacao.entity.Cliente;
import com.testest.apisimulacao.entity.Conta;
import com.testest.apisimulacao.entity.Endereco;
import com.testest.apisimulacao.entity.TipoDocumento;
import com.testest.apisimulacao.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.InvalidStateException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final CPFValidator cpfValidator = new CPFValidator();
    private final CNPJValidator cnpjValidator = new CNPJValidator();

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ClienteResponseDTO cadastrarCliente(CriarClienteRequestDTO dto) {
        // Validações iniciais
        String documentoLimpo = validarFormatoDocumento(dto.tipoDocumento(), dto.documento());

        if (clienteRepository.existsByDocumento(documentoLimpo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Documento (CPF/CNPJ) já cadastrado.");
        }

        // Mapeamento do DTO para a entidade
        Cliente novoCliente = new Cliente();
        novoCliente.setNome(dto.nome());
        novoCliente.setTipoDocumento(dto.tipoDocumento());
        novoCliente.setDocumento(documentoLimpo);
        novoCliente.setSenha(passwordEncoder.encode(dto.senha()));

        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setEndereco(dto.endereco().endereco());
//            endereco.setNumero(dto.endereco().numero());
//            endereco.setComplemento(dto.endereco().complemento());
//            endereco.setBairro(dto.endereco().bairro());
//            endereco.setCidade(dto.endereco().cidade());
//            endereco.setUf(dto.endereco().uf());
//            endereco.setCep(dto.endereco().cep());
            novoCliente.setEndereco(endereco);
        }

        if (dto.contas() != null && !dto.contas().isEmpty()) {
            List<Conta> novasContas = dto.contas().stream().map(contaDto -> {
                Conta novaConta = new Conta();
                novaConta.setAgencia(contaDto.agencia());
                novaConta.setSaldo(contaDto.saldoInicial());
                boolean statusDaConta = contaDto.status() != null ? contaDto.status() : true;
                novaConta.setStatus(statusDaConta);
                novaConta.setCliente(novoCliente);
                return novaConta;
            }).collect(Collectors.toList());
            novoCliente.setContas(novasContas);
        }

        // Salvar no banco
        Cliente clienteSalvo = clienteRepository.save(novoCliente);



        // Resposta final
        String agencias = clienteSalvo.getContas() != null && !clienteSalvo.getContas().isEmpty() ?
                clienteSalvo.getContas().stream()
                        .map(Conta::getAgencia) // Pega a agência de cada conta
                        .collect(java.util.stream.Collectors.joining(", ")) : // Junta todas as agências com ", "
                "nenhuma"; // Se não houver contas

        String mensagemDeSucesso = String.format(
                "Cliente '%s' (ID: %d) cadastrado com sucesso. Total de %d conta(s): %s.",
                clienteSalvo.getNome(),
                clienteSalvo.getId(),
                clienteSalvo.getContas() != null ? clienteSalvo.getContas().size() : 0,
                agencias // Adiciona a string de agências aqui
        );
        return new ClienteResponseDTO(clienteSalvo, mensagemDeSucesso);
    }

    public List<ClienteResponseDTO> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Metodo de validação
    private String validarFormatoDocumento(TipoDocumento tipo, String documento) {
        if (tipo == null || documento == null || documento.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo e número do documento são obrigatórios.");
        }
        String documentoLimpo = documento.replaceAll("[^0-9]", "");

        if (tipo == TipoDocumento.CPF) {
            if (documentoLimpo.length() != 11) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF deve conter 11 dígitos.");
            }
            try {
                cpfValidator.assertValid(documentoLimpo);
            } catch (InvalidStateException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido.");
            }
        } else if (tipo == TipoDocumento.CNPJ) {
            if (documentoLimpo.length() != 14) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ deve conter 14 dígitos.");
            }
            try {
                cnpjValidator.assertValid(documentoLimpo);
            } catch (InvalidStateException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ inválido.");
            }
        } else {

            // fallback
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de documento desconhecido.");
        }
        return documentoLimpo;
    }
}