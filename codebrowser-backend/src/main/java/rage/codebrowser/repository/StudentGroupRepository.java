package rage.codebrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.dto.StudentGroup;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    StudentGroup findByName(String name);
}
