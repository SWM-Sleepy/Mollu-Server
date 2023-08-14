package sleepy.mollu.server.alarm.admin.service;

import java.security.NoSuchAlgorithmException;

public interface AdminService {

    boolean isAdmin(String id, String password) throws NoSuchAlgorithmException;
}
