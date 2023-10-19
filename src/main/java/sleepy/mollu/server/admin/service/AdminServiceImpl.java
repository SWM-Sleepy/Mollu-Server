package sleepy.mollu.server.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.report.domain.CommentReport;
import sleepy.mollu.server.content.report.domain.ContentReport;
import sleepy.mollu.server.content.report.repository.CommentReportRepository;
import sleepy.mollu.server.content.report.repository.ContentReportRepository;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final String SALT = "mollu";
    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_HASHED_PASSWORD = "2JAzegyW2vS0T3WycIntGUz7hodNmQIzeN3nkfVxgY6bPtXBnU9QHsrzBnYB0OwLGUf721Nj/TcLNJXkYGhAfw==";

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final ContentReportRepository contentReportRepository;
    private final CommentReportRepository commentReportRepository;

    @Override
    public boolean isAdmin(String id, String password) throws NoSuchAlgorithmException {

        final String hashedPassword = getHashedPassword(password);
        return id.equals(ADMIN_ID) && hashedPassword.equals(ADMIN_HASHED_PASSWORD);
    }

    @Override
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    @Override
    public List<Content> getContents() {
        return contentRepository.findAllByOrderByContentTime_UploadDateTimeDesc();
    }

    @Override
    public List<ContentReport> getContentReports() {
        return contentReportRepository.findAll();
    }

    @Override
    public List<CommentReport> getCommentReports() {
        return commentReportRepository.findAll();
    }

    private String getHashedPassword(String password) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(SALT.getBytes(StandardCharsets.UTF_8));
        final byte[] hashedPassword = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}