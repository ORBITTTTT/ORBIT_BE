package tra.orbit_be.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tra.orbit_be.dto.ProjectRequestDto;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Project extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userID;

    @Column(nullable = false)
    private Date pjStartDate; //프로젝트 시작일

    @Column(nullable = false)
    private String pjTitle;    //프로젝트 제목(프로젝트 이름)

    @Column(nullable = false)
    private String pjExplanation; //프로젝트 설명

    @Column(nullable = false)
    private Boolean pjStatus;    //프로젝트 모집여부

    @Column
    private int pjViewCount;    //프로젝트 조회 수

    @Column
    private int beMemberNum;    //프로젝트 백엔드 모집인원 수

    @Column
    private int feMemberNum;    //프로젝트 프론트엔드 모집인원 수

    @Column
    private int dgMemberNum;    //프로젝트 디자이너 모집인원 수

    @Column
    private int pmMemberNum;    //프로젝트 Pm 모집인원 수


    public Project(ProjectRequestDto projectRequestDto) {
        this.pjStartDate = projectRequestDto.getPjStartDate();
        this.pjTitle = projectRequestDto.getPjTitle();
        this.pjExplanation = projectRequestDto.getPjExplanation();
        this.pjStatus = projectRequestDto.getPjStatus();
        this.pjViewCount = projectRequestDto.getPjViewCount();
        this.beMemberNum = projectRequestDto.getBeMemberNum();
        this.feMemberNum = projectRequestDto.getFeMemberNum();
        this.dgMemberNum = projectRequestDto.getDgMemberNum();
        this.pmMemberNum = projectRequestDto.getPmMemberNum();
    }
}
