package rage.codebrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.codeanalyzer.domain.Diff;

public interface DiffRepository extends JpaRepository<Diff, Long> {
    
}
