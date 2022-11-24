package sanrocks.tradingbot.domain.rules;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.json.JSONObject;

@Slf4j
@Getter
@RequiredArgsConstructor
public final class ProductRuleEngine {

    private final Rules rules;
    private final RulesEngine rulesEngine;

    /**
     * @param facts
     */
    public void doExecute(final Facts facts) {
        getRulesEngine().fire(getRules(), facts);
    }

    /**
     * @param facts
     * @return
     */
    public Map<Rule, Boolean> doCheck(final Facts facts) {
        Map<Rule, Boolean> result = getRulesEngine().check(getRules(), facts);

        JSONObject formattedResult = new JSONObject();
        result.forEach((k, v) -> {
            formattedResult.put(k.getName(), v);
        });

        log.info("doCheck: {}", formattedResult.toString(4));
        return result;
    }
}
