
package rage.codebrowser.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Comment extends AbstractNamedPersistable {

    @JoinColumn
    @ManyToOne
    @JsonIgnoreProperties({"exercises"})
    private Course course;

    @JoinColumn
    @ManyToOne
    @JsonIgnoreProperties({"courses"})
    private Student student;

    @JoinColumn
    @ManyToOne
    private Exercise exercise;

    // Null if the Comment is not related to specific snapshot
    @JoinColumn
    @ManyToOne
    @JsonIgnoreProperties({"files"})
    private Snapshot snapshot;


    private String username;
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    @PrePersist
    public void addCreatedAt() {
        this.createdAt = new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }
}
