/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author phamt
 */
public class Security {

    public Security() {

    }

    public void createKeyRSA() {
        try {
            SecureRandom sr = new SecureRandom();

            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024, sr);

            KeyPair kp = kpg.genKeyPair();

            PublicKey publicKey = kp.getPublic();
            PrivateKey privateKey = kp.getPrivate();

            File publicKeyFile = createKeyFile(new File("publicKey.rsa"));
            File privateKeyFile = createKeyFile(new File("privateKey.rsa"));

            FileOutputStream fos = new FileOutputStream(publicKeyFile);
            fos.write(publicKey.getEncoded());
            fos.close();

            fos = new FileOutputStream(privateKeyFile);
            fos.write(privateKey.getEncoded());
            fos.close();

            System.out.println("Generate key successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createKeyFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }
        return file;
    }

    public byte[] readPublicKeyRSA() {
        try {
            FileInputStream fis = new FileInputStream("publicKey.rsa");
            byte[] b = new byte[fis.available()];
            fis.read(b);
            fis.close();
            return b;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String encrpytionRSA(String plainText, byte[] publicKey) {
        String strEncrypt = "";
        try {

            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = factory.generatePublic(spec);

            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, pubKey);

            byte encryptOut[] = c.doFinal(plainText.getBytes());
            strEncrypt = Base64.getEncoder().encodeToString(encryptOut);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strEncrypt;
    }

    public String decryptionRSA(String cipherText) {
        String strDecrypt = "";
        try {

            FileInputStream fis = new FileInputStream("privateKey.rsa");
            byte[] b = new byte[fis.available()];
            fis.read(b);
            fis.close();

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = factory.generatePrivate(spec);

            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, priKey);

            byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(cipherText.getBytes()));
            strDecrypt = new String(decryptOut);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strDecrypt;
    }

    public SecretKeySpec createKeyAES(String myKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] key = myKey.getBytes("UTF-8");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }

    public byte[] encryptAES(byte[] plainText, String myKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, createKeyAES(myKey));
            return Base64.getEncoder().encode(cipher.doFinal(plainText));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public byte[] decryptAES(byte[] cipherText, String myKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, createKeyAES(myKey));
            return cipher.doFinal(Base64.getDecoder().decode(cipherText));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public String createRandomString(int n) {

        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString = new String(array, Charset.forName("UTF-8"));
        StringBuffer r = new StringBuffer();

        for (int k = 0; k < randomString.length(); k++) {

            char ch = randomString.charAt(k);

            if (((ch >= 'a' && ch <= 'z')
                    || (ch >= 'A' && ch <= 'Z')
                    || (ch >= '0' && ch <= '9'))
                    && (n > 0)) {

                r.append(ch);
                n--;
            }
        }
        return r.toString();
    }

    public String hashMD5(String input) {
        return DigestUtils.md5Hex(input);
    }
}
