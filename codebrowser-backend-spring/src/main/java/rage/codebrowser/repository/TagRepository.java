package rage.codebrowser.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.dto.Course;
import rage.codebrowser.dto.Exercise;
import rage.codebrowser.dto.Student;
import rage.codebrowser.dto.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    public List<Tag> findByStudentAndCourseAndExercise(Student student, Course course, Exercise exercise);
}
