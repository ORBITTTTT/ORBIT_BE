package tra.orbit_be.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PhotoDto {
    private String key;
    private String path;
}
