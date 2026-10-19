package vn.edu.uit.da2.exchange.service;

import vn.edu.uit.da2.exchange.dto.RegisterRequest;
import vn.edu.uit.da2.exchange.dto.UserResponse;
import vn.edu.uit.da2.exchange.entity.User;
import vn.edu.uit.da2.exchange.repository.UserRepository;

import vn.edu.uit.da2.exchange.dto.LoginRequest;
import vn.edu.uit.da2.exchange.dto.LoginResponse;
import vn.edu.uit.da2.exchange.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) {

        String username = request.getUsername()
                .trim()
                .toLowerCase(Locale.ROOT);

        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ten dang nhap da ton tai"
            );
        }

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email da duoc su dung"
            );
        }

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);

        // Ma hoa mat khau truoc khi luu
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        String fullName = request.getFullName();

        user.setFullName(
                fullName == null || fullName.isBlank()
                        ? null
                        : fullName.trim()
        );

        user.setStatus("ACTIVE");
        user.setRatingScore(0.0);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getCreatedAt()
        );
    }
    
public LoginResponse login(LoginRequest request) {

    String identifier = request.getUsernameOrEmail()
            .trim()
            .toLowerCase(java.util.Locale.ROOT);

    User user = userRepository.findByUsername(identifier)
            .or(() -> userRepository.findByEmail(identifier))
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Thong tin dang nhap khong chinh xac"
                )
            );

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPasswordHash())) {
        throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Thong tin dang nhap khong chinh xac"
        );
    }

    if (!"ACTIVE".equals(user.getStatus())) {
        throw new ResponseStatusException(
            HttpStatus.FORBIDDEN,
            "Tai khoan khong hoat dong"
        );
    }

    String token = jwtService.generateToken(user.getUsername());

    return new LoginResponse(token, "Bearer", 3600);
    }
}
