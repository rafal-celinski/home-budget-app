package xyz.celinski;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.jetbrains.annotations.NotNull;
import xyz.celinski.exceptions.InvalidCredentialsException;
import xyz.celinski.exceptions.RepositoryAccessException;
import xyz.celinski.exceptions.UserDoesntExists;

import java.util.Optional;

public class AuthenticationService {
    final private UserRepository userRepository;

    public AuthenticationService() {
        userRepository = new UserRepositoryImpl();
    }

    public User authenticateUser(String email, String passwordHash)
            throws UserDoesntExists, RepositoryAccessException, InvalidCredentialsException {

        Optional<User> optionalUser = userRepository.getUserByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPasswordHash().equals(passwordHash))
                return user;
            else
                throw new InvalidCredentialsException("Wrong password");
        }
        else
            throw new UserDoesntExists("User with this email doesn't exist");
    }

    public String generateJWT(@NotNull User user) {
        String secretKey = "secret"; // oczu kąpiel

        return JWT.create()
                .withClaim("user_id", user.getUserId())
                .sign(Algorithm.HMAC256(secretKey));
    }

}
