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

    public Project createProject(ProjectRequestDto projectRequestDto) {

        return createProject(projectRequestDto);
    }


    // 게시글 작성(작성자)

}
