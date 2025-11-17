package com.example.tcc_gateway.infrastructure.handler;

import com.example.tcc_gateway.infrastructure.handler.exceptions.EndereceDeRetornoIndefinidoException;
import com.example.tcc_gateway.infrastructure.handler.exceptions.ErroAoEnviarImagemException;
import com.example.tcc_gateway.infrastructure.handler.exceptions.ImagemVaziaException;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class RestExceptionHandler {

    private ProblemDetail problemDetailBuilder(HttpStatus status, String title, String message) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/" + status.value()));
        problemDetail.setTitle(title);
        problemDetail.setDetail(message);
        return problemDetail;
    }

    @ExceptionHandler(EndereceDeRetornoIndefinidoException.class)
    private ProblemDetail erroAoValidarPayloadExceptionHandler(EndereceDeRetornoIndefinidoException ex) {
        return problemDetailBuilder(HttpStatus.BAD_REQUEST, "Erro nos parâmetros.'", ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ProblemDetail erroNoParametroExceptionHandler(MissingServletRequestParameterException ex) {
        return problemDetailBuilder(HttpStatus.BAD_REQUEST, "Erro nos parâmetros.", ex.getMessage());
    }

    @ExceptionHandler(ImagemVaziaException.class)
    private ProblemDetail erroAoValidarPayloadExceptionHandler(ImagemVaziaException ex) {
        return problemDetailBuilder(HttpStatus.BAD_REQUEST, "Erro ao processar imagem.", ex.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    private ProblemDetail erroAoValidarPayloadExceptionHandler(MultipartException ex) {
        return problemDetailBuilder(HttpStatus.BAD_REQUEST, "Erro ao processar imagem.", ex.getMessage());
    }

    @ExceptionHandler(ErroAoEnviarImagemException.class)
    private ProblemDetail erroAoEnviarImagemExceptionHandler(ErroAoEnviarImagemException ex) {
        return problemDetailBuilder(HttpStatus.BAD_REQUEST, "Erro ao processar imagem.", ex.getMessage());
    }
}
