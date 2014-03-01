package rage.codebrowser.dto;

import javax.persistence.Entity;
import javax.persistence.Transient;


@Entity
public class Testresult extends AbstractNamedPersistable {
    
    private String message;
    private boolean passed;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the passed
     */
    public boolean isPassed() {
        return passed;
    }

    /**
     * @param passed the passed to set
     */
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    
    
}
