package tra.orbit_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tra.orbit_be.domain.Project;
import tra.orbit_be.dto.ProjectRequestDto;
import tra.orbit_be.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    // 프로젝트 생성(게시글 생성)
    public Project createProject(ProjectRequestDto projectRequestDto) {
        Project project = new Project(projectRequestDto);
        projectRepository.save(project);
        return project;
    }



}
