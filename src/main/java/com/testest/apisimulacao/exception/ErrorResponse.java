package com.testest.apisimulacao.exception;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

// Para getters, setters, toString, equals, hashCode
@Data
// Gera construtor sem argumentos
@NoArgsConstructor
// Gera construtor com todos os argumentos
@AllArgsConstructor
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp; // Momento em que o erro ocorreu
    private int status;              // Código de status HTTP numérico
    private String error;            // Nome do status HTTP
    private String message;          // Mensagem de erro
    private String path;             // O endpoint da requisição

    // Construtor auxiliar para facilitar a criação do objeto ErrorResponse
    public ErrorResponse(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now(); // Timestamp atual
        this.status = status.value();         // HttpStatus (ex: 400)
        this.error = status.getReasonPhrase();// HttpStatus (ex: "Bad Request")
        this.message = message;               // Mensagem customizada
        this.path = path;                     // Caminho da requisição
    }
}