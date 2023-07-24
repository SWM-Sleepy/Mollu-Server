package sleepy.mollu.server.content.report.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.member.domain.Member;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor
public abstract class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "content_report_reason"))
    private Reason reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Report(String reason, Member member) {
        this.reason = new Reason(reason);
        setMember(member);
    }

    private void setMember(Member member) {
        this.member = member;
        member.addContentReport(this);
    }
}
