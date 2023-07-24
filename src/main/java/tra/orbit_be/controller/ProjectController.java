package tra.orbit_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tra.orbit_be.domain.Project;
import tra.orbit_be.dto.ProjectRequestDto;
import tra.orbit_be.service.ProjectService;

import java.util.List;


@RestController//Controller라는걸 알려준다.
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;


    // 프로젝트 생성(게시글 생성)
    @PostMapping("/projects/create")
    public Project createProject(@RequestBody ProjectRequestDto projectRequestDto) {
        return projectService.createProject(projectRequestDto);
    }

    // 프로젝트 내용 조회(게시글 조회)
    @GetMapping("/projects/{id}")
    public Project readProject(@PathVariable Long id) {
        return projectService.readProject(id);
    }

    // 프로젝트 목록 조회(게시글 목록 조회)
    @GetMapping("/projects/all")
    public List<Project> ListProject() {
        return projectService.listProject();
    }
}
