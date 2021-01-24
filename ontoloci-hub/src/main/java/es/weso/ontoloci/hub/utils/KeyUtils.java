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

    // Private key path
    private static final String PRIVATE_KEY_PAHT = "/secrets/server-pkcs8.key";
    // APP ID path
    private static final String APP_ID_PATH = "/secrets/ocitest.appid";

    /**
     * Gets a JSON Web Token by the current time and the AppId signed with a private Key
     * Inspired on https://docs.github.com/en/free-pro-team@latest/developers/apps/authenticating-with-github-apps#jwt-payload
     * @return
     */
    public static String getJWT() {

        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

        // We need the absolutepath because when the project is deployed the root folder is ontolocy,
        // while if we are running the hub tests the root folder is ontoloci-hub
        //String absolutepath = KeyUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath().split("/ontolo-ci/")[0].substring(1);
        //String absolutepath = KeyUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
        LOGGER.debug("[FILE_PATH] + "+APP_ID_PATH);
        LOGGER.debug("[USER_PATH] + "+System.getProperty("user.dir"));

        String appId = getFileContent(APP_ID_PATH);
        Instant now = Instant.now();

        PrivateKey privateKey = KeyUtils.loadPrivateKey(PRIVATE_KEY_PAHT);

        // Let's set the JWT Claims
        String jwt = Jwts.builder()
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5L, ChronoUnit.MINUTES)))
                .setIssuer(appId)
                .signWith(signatureAlgorithm, privateKey).compact();

        return jwt;
    }


    /**
     * Gets the content of a file
     * @param path of the file
     * @return content as a string
     */
    private static String getFileContent(String path){
        String appId = "";
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                appId += myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            LOGGER.info("[FILE_PATH] + "+path);
            LOGGER.info("[USER_PATH] + "+System.getProperty("user.dir"));
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return appId;
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
            e.printStackTrace();
        }

        return  null;

    }


}
