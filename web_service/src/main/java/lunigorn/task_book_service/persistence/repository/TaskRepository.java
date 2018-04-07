package lunigorn.task_book_service.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import lunigorn.task_book_service.persistence.domain.Task;


public interface TaskRepository extends CrudRepository<Task, Long> {
	Task findOneByName(String name);
}
