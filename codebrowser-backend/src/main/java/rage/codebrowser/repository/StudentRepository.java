package rage.codebrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.dto.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByName(String name);
}
