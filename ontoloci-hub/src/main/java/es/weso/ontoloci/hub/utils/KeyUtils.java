package es.weso.ontoloci.hub.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

/**
 * This class provides a static method to obtain a JWT by the current time and the AppId signed with a private Key
 * The AppId and the private key are defined in the secrets folder. This folder will not be uploaded to GitHub or any other repository provider.
 *
 * @author Pablo Menéndez Suárez
 */
public class KeyUtils {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyUtils.class);

    private static final String APP_ID = System.getenv("ONTOLOCI_GITHUB_APP_ID") != null ? System.getenv("ONTOLOCI_GITHUB_APP_ID") : "12345";
    private static final String KEY_PATH = System.getenv("ONTOLOCI_GITHUB_KEY_PATH") != null ? System.getenv("ONTOLOCI_GITHUB_KEY_PATH") : "C:/";

    /**
     * Gets a JSON Web Token by the current time and the AppId signed with a private Key
     * Inspired on https://docs.github.com/en/free-pro-team@latest/developers/apps/authenticating-with-github-apps#jwt-payload
     * @return
     */
    public static String getJWT() {

        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

        Instant now = Instant.now();
        PrivateKey privateKey = KeyUtils.loadPrivateKey(KEY_PATH);

        if(privateKey == null)
            return null;

        // Let's set the JWT Claims
        String jwt = Jwts.builder()
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5L, ChronoUnit.MINUTES)))
                .setIssuer(APP_ID)
                .signWith(signatureAlgorithm, privateKey).compact();

        return jwt;
    }



    /**
     * Allows loading a private key contained in a file
     * @param path to the private key file
     * @return PrivateKey
     */
    private static PrivateKey loadPrivateKey(String path)  {
        String privateKeyPEM = null;
        try {
            privateKeyPEM = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);

            // strip of header, footer, newlines, whitespaces
            privateKeyPEM = privateKeyPEM
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            // decode to get the binary DER representation
            byte[] privateKeyDER = Base64.getDecoder().decode(privateKeyPEM);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyDER));

            return privateKey;

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error("Unable to load Private Key");
        }

        return  null;

    }


}
