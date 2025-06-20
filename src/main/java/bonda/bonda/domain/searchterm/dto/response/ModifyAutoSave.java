package bonda.bonda.domain.searchterm.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyAutoSave {

    private Boolean autoSave;
}
