package sanrocks.tradingbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Java Source ErrorResponse created on 12/25/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse implements Serializable {

    private static final long SerialVersionUID = 10l;

    private String message;
    private String errorCode;

    public ErrorResponse() {
    }

    public ErrorResponse(final String message, final String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String toString() {
        return new JSONObject(this).toString();
    }

    public byte[] toByteArray() {
        return new JSONObject(this).toString().getBytes();
    }
}
