package Therapie_Management.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Therapie_Management.backend.dto.LoginRequest;
import Therapie_Management.backend.dto.LoginResponse;
import Therapie_Management.backend.entity.User;
import Therapie_Management.backend.exception.UnauthorizedException;
import Therapie_Management.backend.repository.UserRepository;
import Therapie_Management.backend.security.JwtTokenProvider;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final String LOGIN_FEHLGESCHLAGEN = "User-ID oder Passwort falsch";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findById(request.userId())
            // Bewusst dieselbe Fehlermeldung wie bei falschem Passwort: so laesst sich
            // von aussen nicht unterscheiden, ob die User-ID ueberhaupt existiert.
            .orElseThrow(() -> new UnauthorizedException(LOGIN_FEHLGESCHLAGEN));

        if (!validatePassword(request.password(), user.getPasswortHash())) {
            throw new UnauthorizedException(LOGIN_FEHLGESCHLAGEN);
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getRole());
        return new LoginResponse(token, user.getId(), user.getRole().name(), user.getVorname(), user.getNachname());
    }

    private boolean validatePassword(String rawPassword, String passwortHash) {
        return passwordEncoder.matches(rawPassword, passwortHash);
    }
}
