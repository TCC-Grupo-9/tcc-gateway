package com.example.tcc_gateway.infrastructure.handler.exceptions;

public class ErroAoAbrirArquivo extends RuntimeException {
    public ErroAoAbrirArquivo(String message) {
        super(message);
    }
}
