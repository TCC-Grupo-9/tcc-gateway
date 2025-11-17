package com.example.tcc_gateway.presentation.dto;


import com.example.tcc_gateway.infrastructure.handler.exceptions.EndereceDeRetornoIndefinidoException;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.URL;

public record EnviaImagemS3Request(
        @URL(message = "Deve ser informada uma URL válida.") String webhook,
        @Email(message = "Deve ser um email válido") String email
) {

    public EnviaImagemS3Request {
        if (webhook.isBlank() && email.isBlank()) {
            throw new EndereceDeRetornoIndefinidoException("Escolha entre os campos 'email' ou 'webhook' para receber o retorno.");
        }
    }
}
