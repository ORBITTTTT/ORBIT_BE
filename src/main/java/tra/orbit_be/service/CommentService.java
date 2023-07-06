package tra.orbit_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tra.orbit_be.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 조회
    public ResponseEntity findComments(Long projectId) {
        /**
         * projectId로 comment 모두 찾기 findAll() - 만들어진 순서에 따른 정렬
         */

        return null;
    }
}
