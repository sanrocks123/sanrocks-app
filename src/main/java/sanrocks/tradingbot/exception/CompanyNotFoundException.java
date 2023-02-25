/* (C) 2023 */
package sanrocks.tradingbot.exception;

import java.util.Map;

public class CompanyNotFoundException extends CommonBaseAbstractException {
    public CompanyNotFoundException(final String message) {
        super(message);
    }

    public CompanyNotFoundException(String message, Map<String, Object> extensions) {
        super(message, extensions);
    }
}
