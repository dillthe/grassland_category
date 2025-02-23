package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import repository.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
}
