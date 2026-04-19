package skytrack.business.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String HashPassword(String password){
        return encoder.encode(password);
    }

    public boolean checkPassword(String rawPassword, String hashedPassword){
        return encoder.matches(rawPassword, hashedPassword);
    }
}