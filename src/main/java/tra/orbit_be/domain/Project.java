package tra.orbit_be.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tra.orbit_be.dto.user.ProjectRequestDto;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
public class Project extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userID;

    @Column(nullable = false)
    Date pjStartDate; //프로젝트 시작일

    @Column(nullable = false)
    String pjTitle;    //프로젝트 제목(프로젝트 이름)

    @Column(nullable = false)
    String pjExplanation; //프로젝트 설명

    @Column(nullable = false)
    Boolean pjStatus;    //프로젝트 모집여부

    @Column
    int pjViewCount;    //프로젝트 조회 수

    @Column
    int beMemberNum;    //프로젝트 백엔드 모집인원 수

    @Column
    int feMemberNum;    //프로젝트 프론트엔드 모집인원 수

    @Column
    int dgMemberNum;    //프로젝트 디자이너 모집인원 수

    @Column
    int pmMemberNum;    //프로젝트 Pm 모집인원 수


    public Project(Long projectId, Long userID, Date pjStartDate, String pjTitle, String pjExplanation,
                   Boolean pjStatus, int pjViewCount, int beMemberNum, int feMemberNum, int dgMemberNum,
                   int pmMemberNum) {
        this.projectId = projectId;
        this.userID = userID;
        this.pjStartDate = pjStartDate;
        this.pjTitle = pjTitle;
        this.pjExplanation = pjExplanation;
        this.pjStatus = false;
        this.pjViewCount = 0;
        this.beMemberNum = beMemberNum;
        this.feMemberNum = feMemberNum;
        this.dgMemberNum = dgMemberNum;
        this.pmMemberNum = pmMemberNum;
    }

}
