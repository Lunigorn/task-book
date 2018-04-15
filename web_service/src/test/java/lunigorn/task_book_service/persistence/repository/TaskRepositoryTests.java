package lunigorn.task_book_service.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lunigorn.task_book_service.persistence.domain.Task;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskRepositoryTests {
		
	@Autowired
	TaskRepository tasks;
	
	@Test
	public void taskSaveSucces() {
		// arrange
		Task task = new Task();
		String randomName = RandomStringUtils.randomAlphabetic(10);
		String randomDescription = RandomStringUtils.randomAlphabetic(1000);
		task.setName(randomName);	
		task.setDescription(randomDescription);
		// act
		tasks.save(task);
		// assert
		Task found = tasks.findOneByName(randomName);
		assertEquals(task, found);		
	}
	
	@Test
	public void taskDeleteSuccess() {
		// arrange
		Task task = new Task();
		String randomName = RandomStringUtils.randomAlphabetic(10);
		String randomDescription = RandomStringUtils.randomAlphabetic(1000);
		task.setName(randomName);
		task.setDescription(randomDescription);
		tasks.save(task);
		Task toDelete = tasks.findOneByName(randomName);
		// act
		tasks.delete(toDelete);
		// assert
		assertNotNull(toDelete);
		assertNull(tasks.findOneByName(randomName));
		
	}
}
