package rage.codebrowser.codeanalyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Diff implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type;
    /**
     * Start position of diff in current file
     */
    private int rowStart;
    /**
     * End position of diff in current file
     */
    private int rowEnd;
    /**
     * Amount of deleted lines in file before this diff
     */
    private int offset;
    /**
     * StartPosition of delete in previous file.
     * Not set for other types.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer fromRowEnd;
     /**
     * EndPosition of delete in previous file.
     * Not set for other types.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer fromRowStart;
    /**
     * Deleted lines as a String.
     * Not set for other types.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lines;

    
    
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the startpos
     */
    public int getRowStart() {
        return rowStart;
    }

    /**
     * @param startpos the startpos to set
     */
    public void setRowStart(int startpos) {
        this.rowStart = startpos;
    }

    /**
     * @return the endpos
     */
    public int getRowEnd() {
        return rowEnd;
    }

    /**
     * @param endpos the endpos to set
     */
    public void setRowEnd(int endpos) {
        this.rowEnd = endpos;
    }

    /**
     * @return the id
     */
    @JsonIgnore
    public Long getId() {
        return id;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * @return the fromRowEnd
     */
    public Integer getFromRowEnd() {
        if (this.type.equals("delete")){
            return fromRowEnd;
        } else {
            return null;
        }
    }

    /**
     * @param fromRowEnd the fromRowEnd to set
     */
    public void setFromRowEnd(int fromRowEnd) {
        this.fromRowEnd = fromRowEnd;
    }

    /**
     * @return the fromRowStart
     */
    public Integer getFromRowStart() {
        if (this.type.equals("delete")){
            return fromRowStart;
        } else {
            return null;
        }
    }

    /**
     * @param fromRowStart the fromRowStart to set
     */
    public void setFromRowStart(int fromRowStart) {
        this.fromRowStart = fromRowStart;
    }

    /**
     * @return the lines
     */
    public String getLines() {
        if (type.equals("delete")){
            return lines;
        } else {
            return null;
        }
    }

    /**
     * @param lines the lines to set
     */
    public void setLines(String lines) {
        this.lines = lines;
    }
    
    
    
    
}
