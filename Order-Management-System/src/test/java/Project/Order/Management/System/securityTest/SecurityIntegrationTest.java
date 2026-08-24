package Project.Order.Management.System.securityTest;

import Project.Order.Management.security.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SecurityIntegrationTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService("test-secret-key-for-unit-tests");
    }

    @Test
    @DisplayName("Should generate a non-blank token for a valid username")
    void generateToken_ShouldGenerateValidToken() {
        String token = jwtService.generatedToken("gabriel@gmail.com");

        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("Should return true when token is valid")
    void verifyToken_WhenTokenIsValid_ShouldReturnTrue() {
        String token = jwtService.generatedToken("gabriel@gmail.com");

        boolean isValid = jwtService.validateToken(token);

        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should return false when token is invalid or malformed")
    void verifyToken_WhenTokenIsInvalid_ShouldReturnFalse() {
        boolean isValid = jwtService.validateToken("token-invalido-qualquer");

        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should extract the username (email) from a valid token")
    void extractUsername_ShouldReturnEmailFromToken() {
        String token = jwtService.generatedToken("gabriel@gmail.com");

        String username = jwtService.extractUsername(token);

        Assertions.assertThat(username).isEqualTo("gabriel@gmail.com");
    }
}