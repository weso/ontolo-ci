package es.weso.ontoloci.hub.utils;

import fansi.Str;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

public class KeyUtils {

    // APP ID
    private static final String PRIVATE_KEY_PAHT = "secrets/server-pkcs8.key";
    // PRIVATE KEY
    private static final String APP_ID_PATH = "secrets/ocitest.appid";
    // CLIENT ID
    private static final String CLIENT_ID_PATH = "secrets/client.id";
    // CLIENT SECRET
    private static final String CLIENT_SECRET_PATH = "secrets/client.secret";

    public static String getJWT() {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
        String appId = getAppId();
        Instant now = Instant.now();

        PrivateKey privateKey = KeyUtils.loadPrivateKey(PRIVATE_KEY_PAHT);

        //Let's set the JWT Claims
        String jwt = Jwts.builder()
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5L, ChronoUnit.MINUTES)))
                .setIssuer(appId)
                .signWith(signatureAlgorithm, privateKey).compact();

        return jwt;
    }


    public static String getClientId(){
        return getSecret(CLIENT_ID_PATH);
    }

    public static String getClientSecret(){
        return getSecret(CLIENT_SECRET_PATH);
    }

    private static String getAppId(){
        return getSecret(APP_ID_PATH);
    }

    private static String getSecret(String path){
        String appId = "";
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                appId += myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return appId;
    }

    public static PrivateKey loadPrivateKey(String path)  {
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
