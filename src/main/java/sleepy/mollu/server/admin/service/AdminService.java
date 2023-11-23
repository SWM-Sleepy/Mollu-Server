package sleepy.mollu.server.admin.service;

import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.report.domain.CommentReport;
import sleepy.mollu.server.content.report.domain.ContentReport;
import sleepy.mollu.server.member.domain.Member;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface AdminService {

    boolean isAdmin(String id, String password) throws NoSuchAlgorithmException;

    List<Member> getMembers();

    List<Content> getContents();

    List<ContentReport> getContentReports();

    List<CommentReport> getCommentReports();

    void sendNotification();
}
