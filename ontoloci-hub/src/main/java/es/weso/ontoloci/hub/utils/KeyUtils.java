package es.weso.ontoloci.hub.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

public class KeyUtils {

    // APP ID PATH
    private static final String PRIVATE_KEY_PAHT = "ontoloci-hub/secrets/server-pkcs8.key";
    // PRIVATE KEY PATH
    private static final String APP_ID_PATH = "ontoloci-hub/secrets/ocitest.appid";

    public static String getJWT() throws Exception {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
        String appId = KeyUtils.getAppId(APP_ID_PATH);
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

    public static String getAppId(String path){
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

    public static PrivateKey loadPrivateKey(String path) throws Exception {
        String privateKeyPEM = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);

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
    }


}
