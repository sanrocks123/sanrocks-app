package sanrocks.tradingbot.domain.graph;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleTracer {

    private String fieldName;
    private List<String> successful = new ArrayList<>();
    private List<String> failed = new ArrayList<>();
}
