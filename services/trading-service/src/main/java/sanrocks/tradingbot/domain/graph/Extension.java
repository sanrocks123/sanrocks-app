package sanrocks.tradingbot.domain.graph;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Extension {
    private List<RuleTracer> ruleTracer = new ArrayList<>();
}
