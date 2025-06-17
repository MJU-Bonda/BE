package bonda.bonda.domain.searchterm.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecentSearchRes {

    private List<String> recentSearchTermList;

    private Boolean autoSave;
}
