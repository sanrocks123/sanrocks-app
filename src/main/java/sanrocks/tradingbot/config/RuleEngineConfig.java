/* (C) 2023 */
package sanrocks.tradingbot.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.AbstractRulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.support.composite.UnitRuleGroup;
import org.json.JSONObject;

@Slf4j
@Getter
@RequiredArgsConstructor
public final class RuleEngineConfig {

    private final Rules rules;
    private final RulesEngine rulesEngine;
    private final Map<String, List<String>> ruleExecutionMap;

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
        result.forEach(
                (k, v) -> {
                    formattedResult.put(k.getName(), v);
                });

        log.info("doCheck: {}", formattedResult.toString(4));
        return result;
    }

    /**
     * @param xRules
     * @return
     */
    public static <T> RuleEngineConfig getRuleEngineInstance(List<T> xRules) {
        Rules rules = new Rules();
        log.info("xRules: {}", xRules);

        Map<String, List<String>> ruleExecutionMap = new HashMap<>();

        UnitRuleGroup unitRuleGroup = new UnitRuleGroup("product-sell-flow");
        xRules.forEach(unitRuleGroup::addRule);
        // rules.register(unitRuleGroup);

        xRules.forEach(rules::register);

        AbstractRulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.registerRuleListener(
                new RuleListener() {

                    @Override
                    public void onSuccess(final Rule rule, final Facts facts) {
                        logRule(ruleExecutionMap, "success", rule.getName());
                    }

                    @Override
                    public void onFailure(Rule rule, Facts facts, Exception exception) {
                        logRule(
                                ruleExecutionMap,
                                "failures",
                                String.format(
                                        "%s : %s",
                                        rule.getName(), exception.getCause().getMessage()));
                    }
                });

        return new RuleEngineConfig(rules, rulesEngine, ruleExecutionMap);
    }

    private static void logRule(
            final Map<String, List<String>> ruleExecutionMap,
            final String key,
            final String ruleDescription) {
        ruleExecutionMap.computeIfAbsent(key, v -> new ArrayList<>());
        ruleExecutionMap.computeIfPresent(
                key,
                (k, v) -> {
                    v.add(ruleDescription);
                    return v;
                });
    }
}
