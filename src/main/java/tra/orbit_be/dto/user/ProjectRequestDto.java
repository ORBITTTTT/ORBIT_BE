package tra.orbit_be.dto.user;

import lombok.Getter;

import java.util.Date;

@Getter //멤버 변수의 값을 조회 하기 위한 메서드
public class ProjectRequestDto {    //필요한 정보를 물고 다니는 애

    private Date pjStartDate;
    private String pjTitle;
    private String pjExplanation;
    private int pjViewCount;
    private int beMemberNum;
    private int FeMemberNum;
    private int dgMemberNum;
    private int pmMemberNum;
}
