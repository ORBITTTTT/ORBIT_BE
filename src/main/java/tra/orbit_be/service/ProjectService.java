package tra.orbit_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tra.orbit_be.domain.Project;
import tra.orbit_be.dto.ProjectRequestDto;
import tra.orbit_be.repository.ProjectRepository;
import tra.orbit_be.security.UserDetailsImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;


    // 프로젝트 생성(게시글 생성)
    /*
    글작성 후 repository에 저장
     */
    public Project createProject(ProjectRequestDto projectRequestDto) {
        Project project = new Project(projectRequestDto);
        projectRepository.save(project);
        return project;
    }

    // 프로젝트 내용 조회(게시글 조회)
    /*
    가져온 id를 repository에서 찾아온 후 id를 내보낸다.
     */
    public Project readProject(Long projectId) {
        Project readProject = projectRepository.findByProjectId(projectId).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );
        return readProject;
    }

    // 프로젝트 목록 조회(게시글 목록 조회)
    public List<Project> listProject() {
        List<Project> listProject = projectRepository.findAll();
        return listProject;
    }

    // 프로젝트 수정(게시글 수정) 테스트 해야함
    public Project modifyProject(Long projectId, ProjectRequestDto projectRequestDto) {
        Project modifyProject = projectRepository.findByProjectId(projectId).orElseThrow(
                () -> new RuntimeException("게시글이 존재하지 않습니다.")
        );
        return modifyProject;
    }
}
