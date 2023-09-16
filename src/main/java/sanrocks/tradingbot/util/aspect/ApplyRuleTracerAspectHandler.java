package sanrocks.tradingbot.util.aspect;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import sanrocks.tradingbot.domain.graph.GraphBaseAttributes;

@Slf4j
@Component
@Aspect
public class ApplyRuleTracerAspectHandler {

    @Before(value = "@annotation(applyRuleTracer)")
    public void before(JoinPoint jp, ApplyRuleTracer applyRuleTracer) {
        log.info("doExecute, before");

        Optional<Object> object =
                Stream.of(jp.getArgs()).filter(o -> o instanceof GraphBaseAttributes).findFirst();

        if (object.isEmpty()) {
            throw new RuntimeException(
                    "failed to find any method input args matching GraphBaseAttributes instance");
        }

        RequestContextHolder.getRequestAttributes()
                .setAttribute("fieldName", jp.getSignature().getName(), SCOPE_REQUEST);

        RequestContextHolder.getRequestAttributes()
                .setAttribute("objectName", object.get(), SCOPE_REQUEST);
    }

    @AfterReturning(value = "@annotation(applyRuleTracer)")
    public void after(JoinPoint jp, ApplyRuleTracer applyRuleTracer) {
        log.info("doExecute, after");
        RequestContextHolder.getRequestAttributes().removeAttribute("fieldName", SCOPE_REQUEST);
        RequestContextHolder.getRequestAttributes().removeAttribute("objectName", SCOPE_REQUEST);
    }
}
