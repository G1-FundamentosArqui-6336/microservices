package org.upc.iamservice.iam.infrastructure.tokens.jwt.services;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtKeyProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtKeyProvider.class);

    @Value("${authorization.jwt.private-key-path}")
    private String privateKeyPath;

    @Value("${authorization.jwt.public-key-path}")
    private String publicKeyPath;

    // Getters públicos para que otros servicios los usen
    @Getter
    private PrivateKey privateKey;
    @Getter
    private PublicKey publicKey;

    private final ResourceLoader resourceLoader;

    public JwtKeyProvider(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct // Se ejecuta después de que se inyectan los @Value
    public void init() {
        try {
            this.privateKey = loadPrivateKey(privateKeyPath);
            this.publicKey = loadPublicKey(publicKeyPath);
            LOGGER.info("Claves RSA cargadas correctamente.");
        } catch (Exception e) {
            LOGGER.error("Error al cargar las claves RSA", e);
            throw new RuntimeException("No se pudieron cargar las claves de seguridad", e);
        }
    }

    private PrivateKey loadPrivateKey(String path) throws Exception {
        String key = readKeyFromFile(path);
        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    private PublicKey loadPublicKey(String path) throws Exception {
        String key = readKeyFromFile(path);
        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }

    private String readKeyFromFile(String path) throws Exception {
        Resource resource = resourceLoader.getResource(path);
        try (InputStream is = resource.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}