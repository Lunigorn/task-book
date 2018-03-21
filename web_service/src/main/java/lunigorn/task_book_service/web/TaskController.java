package lunigorn.task_book_service.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lunigorn.task_book_service.persistence.domain.Task;
import lunigorn.task_book_service.persistence.repository.TaskRepository;

@RestController
public class TaskController {
	
	@Autowired
	TaskRepository tasks;
	
	@RequestMapping(path = "/tasks", method = RequestMethod.POST)
    public Task insert(@RequestBody Task task) {
		return tasks.save(task);		
    }
	
	

}
