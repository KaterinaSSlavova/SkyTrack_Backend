package skytrack.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skytrack.business.exception.user.RefreshTokenException;
import skytrack.business.exception.user.UserNotFoundException;
import skytrack.persistence.entity.RefreshToken;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.RefreshTokenRepository;
import skytrack.persistence.repo.UserRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration-ms}")
    private Long expirationTime;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public RefreshToken createToken(Long userId){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();

        RefreshToken token = RefreshToken
                .builder().user(user)
                .expiryDate(Instant.now().plusMillis(expirationTime)).build();
        return refreshTokenRepository.save(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(Instant.now().isAfter(token.getExpiryDate())){
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException();
        }
        return token;
    }

    public RefreshToken verifyToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException());

        return verifyExpiration(refreshToken);
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
