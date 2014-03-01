
package rage.codebrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.dto.TagCategory;

public interface TagCategoryRepository extends JpaRepository<TagCategory, Long> {
    TagCategory findByName(String name);
}
