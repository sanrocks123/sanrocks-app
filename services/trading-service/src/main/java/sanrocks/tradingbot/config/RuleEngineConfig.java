/* (C) 2023 */
package sanrocks.tradingbot.config;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.api.RulesEngineListener;
import org.jeasy.rules.core.AbstractRulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.support.composite.UnitRuleGroup;
import org.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import sanrocks.tradingbot.domain.graph.GraphExtension;
import sanrocks.tradingbot.domain.graph.RuleTracer;

@Getter
@Slf4j
@RequiredArgsConstructor
public final class RuleEngineConfig {

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

        xRules.forEach(unitRuleGroup::addRule);        // rules.register(unitRuleGroup);

        xRules.forEach(rules::register);

        String ruleGroupName =
                xRules.size() == 1
                        ? xRules.get(0).getClass().getSimpleName() + "Node"
                        : xRules.get(0).getClass().getSuperclass().getSimpleName() + "Node";
        AbstractRulesEngine rulesEngine = new DefaultRulesEngine();

        rulesEngine.registerRuleListener(
                new RuleListener() {

                    @Override
                    public void onSuccess(final Rule rule, final Facts facts) {
                        getRuleTracer().getSuccessful().add(rule.getName());
                    }

                    @Override
                    public void onFailure(Rule rule, Facts facts, Exception exception) {
                        getRuleTracer()
                                .getFailed()
                                .add(
                                        String.format(
                                                "%s : %s",
                                                rule.getName(),
                                                ExceptionUtils.getMessage(exception.getCause())));
                    }
                });

        rulesEngine.registerRulesEngineListener(
                new RulesEngineListener() {

                    @Override
                    public void beforeEvaluate(Rules rules, Facts facts) {
                        log.info("beforeEvaluate, groupName: {}", ruleGroupName);
                        getRuleTracer().getSuccessful().add(ruleGroupName);
                    }

                    @Override
                    public void afterExecute(final Rules rules, final Facts facts) {
                        log.info("afterExecute, groupName: {}", ruleGroupName);
                        getRuleTracer().getSuccessful().add(ruleGroupName);
                    }
                });

        return new RuleEngineConfig(rules, rulesEngine);
    }

    private static RuleTracer getRuleTracer() {
        Object object =
                RequestContextHolder.getRequestAttributes()
                        .getAttribute("objectName", SCOPE_REQUEST);

        if (Objects.nonNull(object) && object instanceof GraphExtension) {
            GraphExtension graphExtension = (GraphExtension) object;

            String fieldName =
                    RequestContextHolder.getRequestAttributes()
                            .getAttribute("fieldName", SCOPE_REQUEST)
                            .toString();

            Optional<RuleTracer> optionalRuleTracer =
                    graphExtension.getExtension().getRuleTracer().stream()
                            .filter(e -> e.getFieldName().equalsIgnoreCase(fieldName))
                            .findFirst();

            RuleTracer ruleTracer = null;

            if (optionalRuleTracer.isEmpty()) {
                ruleTracer = new RuleTracer();
                ruleTracer.setFieldName(fieldName);
                graphExtension.getExtension().getRuleTracer().add(ruleTracer);
            } else {
                ruleTracer = optionalRuleTracer.get();
            }
            return ruleTracer;
        }
        return null;
    }
}
