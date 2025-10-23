package com.phuclq.student.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class SignatureUtil {
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }

    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }

    public static PublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byteKey = Base64.getDecoder().decode(key.getBytes());
        X509EncodedKeySpec x509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(x509publicKey);
    }

    public static PrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(key.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }


    public static String signature(String contentToSign) throws Exception {

        System.out.println("Start sign document: ");

        String priKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDPVXDtSxfxqqyq" + "pP3pVqBjaSPUkgPPRaUI+j0cLffCO4dDw1VJmJ52thVY0JB+8lXKxA3hqDjMabuY" + "+TV0+PIHfZNZKK/uLzyI3wMmSMv5U14M46Igw4OCOC6ICNfyi606rRo21duzJXHB" + "WCZsshv2SpYPNrgBMuCo/XMbosCG1dglzHcRfj5lZnGPqm2Z91HGla0/Un7H0Zmv" + "wkMDtXxauxytrWhiRXFp546BL6xRTIg2nv7LFgxkOcj87XuFA4iqVZoOkaQoO/bT" + "7tTM7aMHII9hkiPGBWPFHNi4DbmH2WbXX/R+aRhO4D6wEBCNZaTjavjyiMQTNaAs" + "9n+4jjJ1AgMBAAECggEADGJLmPjbyMULhDPPsMDKWrW8fKVuEdLUnTkpLkY2nsTk" + "evEvZnJ1wJx2oPPwNYzu/+n+9EQShG8qU+RKw55dAoKHKxOpkn4CzSAY9Y/ykwVe" + "QEHWfMOA1uXZLantkCC0q6UMREs40LYDWeuUe/iVf/wLg4QesH+j+KQO3I83lbYT" + "jzaUW2Lfs+BsY3Gs1lD3/OQhq+Cyc0/JA3WWqNsUiEwDU2Gvi83h6vo8zNo4eNe2" + "tzZExMjuq8N1n/VL4iP+TGAhJserHrs8udZ6ORBr6PqtiiheSDW5BLfmWtV2WPe+" + "RrzEAQb6RcFE6eDIq6LtaRn8tVJJgrRK+Bjku4/KsQKBgQDTMRGbxkqFRZjinQV1" + "Y2jBfoE2rkhtqpueCnScAsRGIGnDH1+zBuNtWiSAbK1gxubsgDPJ7Kxq/pfAGI/m" + "TeWiFOwJW37IP2CfyoESQcswwcIGmSVvr5T08V9z7a8USboBBLJ6wpNOwozChBNX" + "3cO9Oj07TEfFcO4Q6X3VXxKR6QKBgQD7UtQQGtNBZaPAdnMeNbgrYb2ktEPCTyBy" + "IXRcMTCwEnRtglXB0N/1KPZwh96VPWlVBbb4okQgrmEq1sSYSwncOgYHJBfNmBEq" + "TL4Y5g+UrT+AdJcu7rWsocf7vwj+voaDpUnslgDuJ99LcNaXXv2OlkkOjyLTFFim" + "Wy/jgp3YrQKBgDi5rlQb/7oqRnVf3a55JBMM6qIIT/X85E2f21BoQdXgMOgzDh5l" + "FEMz+ifwiOU8p5wQl1h+VJVsitATCNvAQpS1b+zXgYdB1q800lRYDb6r3HSvP+nu" + "zGC6UHwv9F3Lq8VIQLROfQijovFATn3EWCYEdZvu0jxLJ/as8JqWLcO5AoGAEn7S" + "+mhu+ZYYSfatnCCJ8C9ePHvDrM4T7jJGFO7NzXdeOBzqYLwVbz5XfIqTN9pwIQCt" + "qXiPmPQcVIG/sBF9pUZj4Synz8qMINfK+Zwcs21YigSKh7qrhgvV4tH5QH4W+iM0" + "4INdiClJBo1ETZmBAXhP4hFol64cpAl76btYEHUCgYEAkvIxSzTkKD5YFMiQRI9Q" + "asEF9OGP9lWZJ657BCounWTYIDaWjZ/8Z9kUy67e9wh6WcEl2Ssbx5b8DkU6e5ge" + "FcBQMAXOrQ/DwT0yerFq0kJ5tfZAMyYZt45KeUJVfWeqpKYzYH0tmZmh0qAPtLPj" + "nE0Ch+7LqIS9bMMRHJbX0H4=".replaceAll("\\s", "");
        String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz1Vw7UsX8aqsqqT96Vag" + "Y2kj1JIDz0WlCPo9HC33wjuHQ8NVSZiedrYVWNCQfvJVysQN4ag4zGm7mPk1dPjy" + "B32TWSiv7i88iN8DJkjL+VNeDOOiIMODgjguiAjX8outOq0aNtXbsyVxwVgmbLIb" + "9kqWDza4ATLgqP1zG6LAhtXYJcx3EX4+ZWZxj6ptmfdRxpWtP1J+x9GZr8JDA7V8" + "Wrscra1oYkVxaeeOgS+sUUyINp7+yxYMZDnI/O17hQOIqlWaDpGkKDv20+7UzO2j" + "ByCPYZIjxgVjxRzYuA25h9lm11/0fmkYTuA+sBAQjWWk42r48ojEEzWgLPZ/uI4y" + "dQIDAQAB".replaceAll("\\s", "");

        System.out.println("Private key: " + priKey);
        System.out.println("Public key: " + pubKey);

//        System.out.println("Plain text to sign: "+args[2]);


        String signature = sign(contentToSign, getPrivateKey(priKey));

        System.out.println("Signature: ");
        System.out.println(signature);
        System.out.println();

        // Let's check the signature
        boolean isCorrect = verify(contentToSign, signature, getPublicKey(pubKey));
        System.out.println("Signature correct: " + isCorrect);
        return signature;
    }

    public static String getContentFile(String path) {
        // Sử dụng class loader để lấy InputStream của tệp
        InputStream inputStream = SignatureUtil.class.getClassLoader().getResourceAsStream(path);

        if (inputStream == null) {
            log.info("Tệp không tồn tại : {}", path);
            return path;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            // Đọc từng dòng của tệp và thêm vào StringBuilder
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }

            // In ra nội dung của tệp PEM
            String fileContent = stringBuilder.toString();
            log.info("Signature : {}", fileContent);
            return fileContent.replaceAll("\\s", "");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String signature2(String contentToSign,String privateKey) throws Exception {

        try {
            String priKey = Objects.nonNull(privateKey)?privateKey: getContentFile("key/private.pem");
            log.info("Private key : {}", priKey);
            String pubKey = getContentFile("key/public.crt");
            log.info("Public key : {}", pubKey);

            String signature = sign(contentToSign, getPrivateKey(priKey));

            log.info("Signature : {}", signature);
            // Let's check the signature
            boolean isCorrect = verify(contentToSign, signature, getPublicKey(pubKey));
            if (isCorrect) {
                return signature;
            }
            return signature(contentToSign);
        } catch (Exception e) {
            log.error("Signature Error : {}", contentToSign);
            return signature(contentToSign);
        }

    }

    public static void main(String[] args) throws Exception {
        String contentToSign = "{\n" +
                "  \"token\" : \"eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MTg4Njk0NjksImV4cCI6MTcxODg2OTc0OSwiZ292SWQiOiIwMzgxOTgwMDI2MjEiLCJnb3ZJZFR5cGUiOiI2IiwiY3VzdG9tZXJJZCI6IjAxRzhXWjJIRVhDVFQwWVhOTVI4WkJTQlpGIiwibXNpc2RuIjoiODQzOTYzMjMzOTMiLCJ1c2VybmFtZSI6Ijg0Mzk2MzIzMzkzIiwic3ViIjoiODQzOTYzMjMzOTMifQ.qKJZ856iE3GMHcYphPZ3aP1cKMePLGcGCCIjU5JKnFzCeROE2A39zALHdQopYMa_bu72ZmJ6d1rU_5Ee9BMpUA\"\n" +
                "}";
        String s = signature2(contentToSign,null);
        System.out.println("Chu ky");
        System.out.println(s);
    }

}

