package tra.orbit_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tra.orbit_be.domain.user.Position;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    // 프로젝트 id
    private Long projectId;
    // 총 댓글 수
    private Integer commentsCount;
    // 댓글 내용
    private String comment;
    // 유저 닉네임
    private String userNickname;
    // 유저 직군
    private List<Position> userPositions;
    // 프로필 사진
    private String userProfileImage;
    // 생성 날짜
    private LocalDateTime createdAt;
}
