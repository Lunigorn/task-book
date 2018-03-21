package lunigorn.task_book_service.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PathVariable;
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
		if (task.getId() != null)
			throw new HttpMessageNotReadableException("Field \"id\" should be empty.");
		return tasks.save(task);
    }
	
	@RequestMapping(path = "/tasks", method = RequestMethod.PUT)
    public Task update(@RequestBody Task task) {
		return tasks.save(task);
    }
	
	
	@RequestMapping(path = "/tasks/{id}", method = RequestMethod.GET)
    public Task getById(@PathVariable Long id) {
		return tasks.findOne(id);
    }
	
	@RequestMapping(path = "/tasks/{id}", method = RequestMethod.DELETE)
    public Task deleteById(@PathVariable Long id) {
		Task found = tasks.findOne(id);
		if (found.equals(null))
			return null;		
		tasks.delete(id);
		return found;
    }	

}
