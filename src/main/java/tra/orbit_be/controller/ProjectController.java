package tra.orbit_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tra.orbit_be.domain.Project;
import tra.orbit_be.dto.ProjectResponseDto;
import tra.orbit_be.dto.ProjectRequestDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController //Controller라는걸 알려준다.
@RequiredArgsConstructor
public class ProjectController {

    private final Map<Long, Project> projectList = new HashMap<>();

    @PostMapping("/projects/create")    // 프로젝트 생성(게시글 생성)
    public ProjectResponseDto createProject(@RequestBody ProjectRequestDto projectRequestDto) {
        Project project = new Project(projectRequestDto);

        Long maxId = projectList.size() > 0 ? Collections.max(projectList.keySet()) + 1 : 1;


        return null;
    }
}
