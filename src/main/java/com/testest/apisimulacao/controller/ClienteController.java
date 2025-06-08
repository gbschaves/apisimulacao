package com.testest.apisimulacao.controller;

import com.testest.apisimulacao.entity.Cliente;
import com.testest.apisimulacao.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/cadastro/clientes")
    public Cliente cadastrarCliente(@RequestBody Cliente cliente) {
        return clienteService.cadastrarCliente(cliente);
    }

    @GetMapping("/listar/clientes")
    public List<Cliente> listarCliente() {
        return clienteService.listarClientes();
    }
}