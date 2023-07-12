package tra.orbit_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tra.orbit_be.domain.Project;
import tra.orbit_be.dto.user.ProjectRequestDto;
import tra.orbit_be.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;


    // 게시글 작성(작성자)
    public Long update(Long id, ProjectRequestDto projectRequestDto) {
        Project project = projectRepository.findByProjectId(id).orElseThrow(
                () -> new NullPointerException("아이디 존재하지 않다ㅓ")
        );
        project.update(projectRequestDto);
        return id;

    }
}
