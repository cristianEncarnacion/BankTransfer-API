import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class GenerateKeys {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
            Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(pair.getPrivate().getEncoded()) +
            "\n-----END PRIVATE KEY-----\n";
            
        String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(pair.getPublic().getEncoded()) +
            "\n-----END PUBLIC KEY-----\n";
            
        try (FileOutputStream fos = new FileOutputStream("src/main/resources/privateKey.pem")) {
            fos.write(privateKey.getBytes());
        }
        try (FileOutputStream fos = new FileOutputStream("src/main/resources/publicKey.pem")) {
            fos.write(publicKey.getBytes());
        }
        System.out.println("Keys generated successfully.");
    }
}
