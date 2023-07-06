package tra.orbit_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tra.orbit_be.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping
    public ResponseEntity findComments(@PathVariable Long projectId) {
        return commentService.findComments(projectId);
    }
}
