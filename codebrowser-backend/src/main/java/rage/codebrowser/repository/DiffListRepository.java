package rage.codebrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.codeanalyzer.domain.DiffList;


public interface DiffListRepository extends JpaRepository<DiffList, Long> {
    
}
