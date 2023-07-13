package tra.orbit_be.dto;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@Getter
public class ProjectResponseDto {

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

}
