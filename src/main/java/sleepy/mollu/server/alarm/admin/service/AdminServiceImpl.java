package sleepy.mollu.server.alarm.admin.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AdminServiceImpl implements AdminService {

    private static final String SALT = "mollu";
    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_HASHED_PASSWORD = "2JAzegyW2vS0T3WycIntGUz7hodNmQIzeN3nkfVxgY6bPtXBnU9QHsrzBnYB0OwLGUf721Nj/TcLNJXkYGhAfw==";

    @Override
    public boolean isAdmin(String id, String password) throws NoSuchAlgorithmException {

        final String hashedPassword = getHashedPassword(password);
        return id.equals(ADMIN_ID) && hashedPassword.equals(ADMIN_HASHED_PASSWORD);
    }

    private String getHashedPassword(String password) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(SALT.getBytes(StandardCharsets.UTF_8));
        final byte[] hashedPassword = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
