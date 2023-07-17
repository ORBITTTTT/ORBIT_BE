package tra.orbit_be.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter //멤버 변수의 값을 조회 하기 위한 메서드
@Setter
public class ProjectRequestDto {    //필요한 정보를 물고 다니는 애

    private Long projectId;
    private Long userID;
    private Date pjStartDate;
    private String pjTitle;
    private String pjExplanation;
    private Boolean pjStatus;
    private int pjViewCount;
    private int beMemberNum;
    private int FeMemberNum;
    private int dgMemberNum;
    private int pmMemberNum;


}
