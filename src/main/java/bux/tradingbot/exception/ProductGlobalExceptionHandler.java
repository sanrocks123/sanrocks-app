package bux.tradingbot.exception;

import bux.tradingbot.domain.ErrorResponse;
import bux.tradingbot.util.TradeBotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * Java Source ProductGlobalExceptionHandler created on 12/24/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@ControllerAdvice
public class ProductGlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * we could do more application specific custom exceptions but keeping it simple for now
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpStatusCodeException.class)
    ResponseEntity<ErrorResponse> handleTradeAPIExceptions(HttpStatusCodeException ex) {
        log.error("handleTradeAPIExceptions", ex);

        String json = ex.getResponseBodyAsString();
        ErrorResponse response = TradeBotUtils.deserialize(json, ErrorResponse.class);
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler(Throwable.class)
    ResponseEntity<ErrorResponse> handleApplication(Throwable ex) {
        log.error("handleApplication", ex);
        ErrorResponse response = new ErrorResponse(String.format("Oops! something went wrong - [%s]", ex.getMessage()), "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
