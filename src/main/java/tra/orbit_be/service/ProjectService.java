package tra.orbit_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tra.orbit_be.domain.Project;
import tra.orbit_be.dto.ProjectRequestDto;
import tra.orbit_be.repository.ProjectRepository;
import tra.orbit_be.security.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;


    // 프로젝트 생성(게시글 생성)
    /*
    글작성 후 repository에 저장
     */
    public Project createProject(ProjectRequestDto projectRequestDto) {
        Project project = new Project(projectRequestDto);
        projectRepository.save(project);
        return project;
    }

    // 프로젝트 내용 보기(게시글 보기)
    /*
    가져온 id를 repository에서 찾아온 후 id를 내보낸다.
     */
    public Project readProject(Long id) {
        Project readProject = projectRepository.findByProjectId(id).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );
        return readProject;
    }




}
