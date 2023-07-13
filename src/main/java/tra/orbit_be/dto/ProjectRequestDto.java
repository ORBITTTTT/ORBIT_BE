package tra.orbit_be.dto;

import lombok.Getter;

import java.util.Date;

@Getter //멤버 변수의 값을 조회 하기 위한 메서드
public class ProjectRequestDto {    //필요한 정보를 물고 다니는 애

    private Long projectId;
    private Long userID;
    Date pjStartDate;
    String pjTitle;
    String pjExplanation;
    int pjViewCount;
    int beMemberNum;
    int FeMemberNum;
    int dgMemberNum;
    int pmMemberNum;


}
