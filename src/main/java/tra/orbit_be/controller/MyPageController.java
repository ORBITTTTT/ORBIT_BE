package tra.orbit_be.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tra.orbit_be.dto.PhotoDto;
import tra.orbit_be.security.UserDetailsImpl;
import tra.orbit_be.service.S3Service;
import tra.orbit_be.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final UserService userService;
    private final S3Service s3Service;

    // 이미지 저장하기
    @ApiOperation(value = "프로필 이미지 저장", notes = "프로필 이미지 저장, 유저 정보 필요")
    @PutMapping("/users/my-page/image-update")
    public ResponseEntity<String> uploadImage(
            @ApiParam(value = "로그인된 회원만 접근 가능한 회원 권한 여부 판단")
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ApiParam(value = "프로필 이미지")
            @RequestPart("profileImg") List<MultipartFile> multipartFiles
    ) {
        System.out.println(multipartFiles);
        String defaultImage = "기본이미지 필요";
        String profileImage = "";

        // 이미지가 없으면 기본 이미지 추가
        if (multipartFiles.get(0).isEmpty())
            profileImage = defaultImage;
        else {
            List<PhotoDto> photoDtos = s3Service.uploadFile(multipartFiles);
            profileImage = photoDtos.get(0).getPath();
        }

        return userService.uploadImage(userDetails, profileImage);
    }
}
