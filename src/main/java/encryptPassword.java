import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;

public class encryptPassword {


//    public static void main(String[] args) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String rawPassword = "adil1981";
//        String encodedPassword = passwordEncoder.encode(rawPassword);
//
//        System.out.println("Mot de passe encodé : " + encodedPassword);
//        System.out.println("match :" + passwordEncoder.matches(rawPassword, "$2a$10$K32TliQW9we1AR.RqkZDr.VZbKoYQsdBhoM/bNl8DhpRzLGPMfPb6"));
//    }

    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        secureRandom.nextBytes(key);
        String secretKey = Base64.getEncoder().encodeToString(key);
        System.out.println("Clé secrète : " + secretKey);
    }
}
