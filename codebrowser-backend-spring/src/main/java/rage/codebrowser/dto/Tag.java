package rage.codebrowser.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Tag extends AbstractPersistable<Long> implements Comparable<Tag> {

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

    // Null if the tag is not related to specific snapshot
    @ManyToOne
    @JsonIgnoreProperties({"files"})
    @JsonInclude(Include.NON_NULL)
    private Snapshot snapshot;

    @ManyToOne
    @JsonIgnoreProperties({"tags"})
    private TagName tagName;

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

    public TagName getTagName() {
        return tagName;
    }

    public void setTagName(TagName tagName) {
        this.tagName = tagName;
    }

    @Override
    public int compareTo(Tag o) {
        if (getTagName() == null) {
            return -1;
        }

        return getTagName().compareTo(o.getTagName());
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return super.isNew();
    }
}
