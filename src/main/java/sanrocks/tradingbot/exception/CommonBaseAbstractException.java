package sanrocks.tradingbot.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonBaseAbstractException extends RuntimeException implements GraphQLError {


    private Map<String, Object> extensions = new HashMap();

    public CommonBaseAbstractException(String message) {
        super(message);
    }

    public CommonBaseAbstractException(String message, Map<String, Object> extensions) {
        this(message);
        if (extensions != null) {
            this.extensions = extensions;
        }
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return null;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return this.extensions;
    }

}
