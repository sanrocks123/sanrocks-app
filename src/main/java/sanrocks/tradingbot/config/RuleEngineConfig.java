/* (C) 2023 */
package sanrocks.tradingbot.config;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import sanrocks.tradingbot.domain.graph.GraphBaseAttributes;
import sanrocks.tradingbot.domain.graph.RuleTracer;

@Slf4j
@Getter
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
        xRules.forEach(unitRuleGroup::addRule);
        // rules.register(unitRuleGroup);

        xRules.forEach(rules::register);

        AbstractRulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.registerRuleListener(
                new RuleListener() {

                    @Override
                    public void onSuccess(final Rule rule, final Facts facts) {
                        logRule(ruleExecutionMap, "successful", rule.getName());
                    }

                    @Override
                    public void onFailure(Rule rule, Facts facts, Exception exception) {
                        logRule(
                                ruleExecutionMap,
                                "failed",
                                String.format(
                                        "%s : %s",
                                        rule.getName(), exception.getCause().getMessage()));
                    }
                });

        rulesEngine.registerRulesEngineListener(
                new RulesEngineListener() {
                    @Override
                    public void afterExecute(final Rules rules, final Facts facts) {

                        Map<String, List<String>> prevContextBoundRuleExecutionMap =
                                (Map)
                                        RequestContextHolder.getRequestAttributes()
                                                .getAttribute("ruleExecutionMap", SCOPE_REQUEST);

                        if (Objects.isNull(prevContextBoundRuleExecutionMap)) {
                            setContextAttribute(ruleExecutionMap);

                        } else {
                            prevContextBoundRuleExecutionMap.forEach(
                                    (k, v) -> {
                                        ruleExecutionMap.computeIfPresent(
                                                k,
                                                (kk, vv) -> {
                                                    vv.addAll(
                                                            prevContextBoundRuleExecutionMap.get(
                                                                    k));
                                                    return vv;
                                                });
                                    });

                            setContextAttribute(ruleExecutionMap);
                        }

                        log.info(
                                "ruleEngineListener, afterExecute, requestContextHolder,"
                                        + " ruleExecutionMap : [{}]",
                                RequestContextHolder.getRequestAttributes()
                                        .getAttribute("ruleExecutionMap", SCOPE_REQUEST));

                        Object object =
                                RequestContextHolder.getRequestAttributes()
                                        .getAttribute("objectName", SCOPE_REQUEST);

                        if (Objects.nonNull(object) && object instanceof GraphBaseAttributes) {
                            GraphBaseAttributes graphBaseAttributes = (GraphBaseAttributes) object;

                            String fieldName =
                                    RequestContextHolder.getRequestAttributes()
                                            .getAttribute("fieldName", SCOPE_REQUEST)
                                            .toString();

                            Optional<RuleTracer> optionalRuleTracer =
                                    graphBaseAttributes.getExtension().getRuleTracer().stream()
                                            .filter(
                                                    e ->
                                                            e.getFieldName()
                                                                    .equalsIgnoreCase(fieldName))
                                            .findFirst();

                            RuleTracer ruleTracer = null;

                            if (optionalRuleTracer.isEmpty()) {
                                ruleTracer = new RuleTracer();
                                ruleTracer.setFieldName(fieldName);
                                graphBaseAttributes.getExtension().getRuleTracer().add(ruleTracer);
                            } else {
                                ruleTracer = optionalRuleTracer.get();
                            }

                            updateSuccessRules(ruleTracer, ruleExecutionMap);
                            updateFailedRules(ruleTracer, ruleExecutionMap);
                        }

                        ruleExecutionMap.clear();
                    }
                });

        return new RuleEngineConfig(rules, rulesEngine);
    }

    private static void updateFailedRules(
            final RuleTracer ruleTracer, final Map<String, List<String>> ruleExecutionMap) {
        ruleExecutionMap.computeIfPresent(
                "failed",
                (k, v) -> {
                    ruleTracer.getFailed().addAll(v);
                    return v;
                });
    }

    private static void updateSuccessRules(
            final RuleTracer ruleTracer, final Map<String, List<String>> ruleExecutionMap) {
        ruleExecutionMap.computeIfPresent(
                "successful",
                (k, v) -> {
                    ruleTracer.getSuccessful().addAll(v);
                    return v;
                });
    }

    private static void setContextAttribute(final Map<String, List<String>> ruleExecutionMap) {
        RequestContextHolder.currentRequestAttributes()
                .setAttribute("ruleExecutionMap", ruleExecutionMap, SCOPE_REQUEST);
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
