package com.example.tcc_gateway.presentation.controller;

import com.example.tcc_gateway.infrastructure.handler.exceptions.EndereceDeRetornoIndefinidoException;
import com.example.tcc_gateway.infrastructure.handler.exceptions.ErroAoEnviarImagemException;
import com.example.tcc_gateway.infrastructure.handler.exceptions.ImagemVaziaException;
import com.example.tcc_gateway.service.ImagemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reconhece-imagem/v1")
@RequiredArgsConstructor
@Validated
public class ReconheceImagemController {

    private final ImagemService imagemServiceServie;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void endpointEnviaImagemS3(
            @Valid @URL(message = "Deve ser informada uma URL válida.") @RequestParam() String webhook,
            @Valid @Email(message = "Deve ser informada um email válido.") @RequestParam() String email,
            @Valid @RequestPart MultipartFile imagem
    ) {
        if (webhook.isBlank() && email.isBlank()) {
            throw new EndereceDeRetornoIndefinidoException("Escolha entre os campos 'email' ou 'webhook' para receber o retorno.");
        }

        if (imagem == null || imagem.isEmpty()) {
            throw new ImagemVaziaException("Nenhum uma imagem está sendo enviada.");
        }

        try {
            final Long timestamp = Instant.now().toEpochMilli();

            final String nomeImagemOriginal = imagem.getOriginalFilename();
            final String extensao = nomeImagemOriginal.substring(nomeImagemOriginal.lastIndexOf('.') + 1);
            final String novoNomeImagem = timestamp + "." + extensao;

            imagemServiceServie.PostImagemS3(imagem, novoNomeImagem, webhook, email);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErroAoEnviarImagemException("Ocorreu um erro ao enviar a imagem para o S3.");
        }
    }
}
