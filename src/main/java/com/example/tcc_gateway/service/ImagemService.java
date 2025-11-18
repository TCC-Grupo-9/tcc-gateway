package com.example.tcc_gateway.service;

import com.example.tcc_gateway.infrastructure.handler.exceptions.ErroAoAbrirArquivo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@RequiredArgsConstructor
public class ImagemService {

    @Value("${amazon.bucket}")
    private String nomeBucket;

    private final S3Client s3Client;

    public void PostImagemS3(MultipartFile imagem, String nomeArquivo, String webhook, String email) {
        final File arquivoConvertido = new File(imagem.getOriginalFilename());

        Map<String, String> metadata = new HashMap<>();
        metadata.put("webhook", webhook);
        metadata.put("email", email);

        try (FileOutputStream fos = new FileOutputStream(arquivoConvertido)) {
            fos.write(imagem.getBytes());
        }catch (IOException e) {
            arquivoConvertido.delete();
            throw new ErroAoAbrirArquivo("Ocorreu um erro ao acessar a imagem.");
        }

        s3Client.putObject(request ->
                        request
                                .bucket(nomeBucket)
                                .key(nomeArquivo)
                                .metadata(metadata)
                                .ifNoneMatch("*"),
                arquivoConvertido.toPath()
        );

        arquivoConvertido.delete();
    }
}
