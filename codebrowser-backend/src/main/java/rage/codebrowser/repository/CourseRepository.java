package rage.codebrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.dto.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByName(String name);
}
