package lunigorn.task_book_service.web;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import lombok.SneakyThrows;
import lunigorn.task_book_service.persistence.domain.Task;
import lunigorn.task_book_service.persistence.repository.TaskRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskControllerTests {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
	
	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }
	
	@Autowired
	TaskRepository tasks;
	
	@Test
	@SneakyThrows({Exception.class})
	public void taskInsertSucces() {
		// arrange
		String randomName = RandomStringUtils.randomAlphabetic(10);
		
		// act
		MvcResult result = mockMvc.perform(post("/tasks/")
                .content("{\"name\":\""+ randomName+"\"}")
                .contentType(contentType))
				.andDo(print())
		// assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", anything()))
                .andExpect(jsonPath("$.name", is(randomName)))
                .andReturn();
		String responseText = result.getResponse().getContentAsString();
		// assert returned task has id
		Integer savedTaskId = JsonPath.read(responseText, "$.id");
		assertNotNull(savedTaskId);
		// assert returned task is saved in database
		Task savedTask = tasks.findOne(savedTaskId.longValue());
		assertEquals(savedTask.getName(), randomName);
		
	}
	
	@Test
	@SneakyThrows({Exception.class})
	public void taskInsertWithIdFail() {
		// arrange
		Integer randomId = new Random().nextInt();
		String randomName = RandomStringUtils.randomAlphabetic(10);
		
		Task foundByRandomId = tasks.findOne(randomId.longValue());
		if (foundByRandomId != null)
			assertNotEquals(randomName, foundByRandomId.getName());
		
		// act
		MvcResult result = mockMvc.perform(post("/tasks/")
                .content("{\"id\":\""+randomId+"\",\"name\":\""+ randomName+"\"}")
                .contentType(contentType))
				.andDo(print())
		// assert
                .andExpect(status().isBadRequest())
                .andReturn();
		

		Task foundByRandomIdAfterError = tasks.findOne(randomId.longValue());
		
		assertEquals(foundByRandomId, foundByRandomIdAfterError);
		
	}
	
	@Test
	@SneakyThrows({Exception.class})
	public void taskUpdateSucces() {
		// arrange		
		Task task = new Task();
		String randomName = RandomStringUtils.randomAlphabetic(10);
		task.setName(randomName);	
		tasks.save(task);	
		String newRandomName = RandomStringUtils.randomAlphabetic(10);
		
		// assert name saved in database is correct
		tasks.findOne(task.getId()).getName().equals(randomName);
		
		// act
		mockMvc.perform(put("/tasks/")
                .content("{\"id\":\""+task.getId()+"\",\"name\":\""+ newRandomName+"\"}")
                .contentType(contentType))
				.andDo(print())
		// assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", anything()))
                .andExpect(jsonPath("$.name", is(newRandomName)));
		// assert name saved in database is changed
		tasks.findOne(task.getId()).getName().equals(newRandomName);
				
	}
	
	@Test
	@SneakyThrows({Exception.class})
	public void taskGetByIdSucces() {
		// arrange
		Task task = new Task();
		String randomName = RandomStringUtils.randomAlphabetic(10);
		task.setName(randomName);	
		tasks.save(task);		
		
		// act
		mockMvc.perform(get("/tasks/"+task.getId())
                .contentType(contentType))
				.andDo(print())
		// assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(task.getId().intValue())))
                .andExpect(jsonPath("$.name", is(task.getName())));
		
	}

	@Test
	@SneakyThrows({Exception.class})
	public void taskGetAlldSucces() {
		// arrange

		//результат прямого запроса в БД
		Iterable<Task> allRowsDB=tasks.findAll();
		ObjectMapper objectMapper = new ObjectMapper();
		String allRowsDbAll;
		try {
			allRowsDbAll = objectMapper.writeValueAsString(allRowsDB);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		// act
		MvcResult result = mockMvc.perform(get("/tasks")
				.contentType(contentType))
				.andDo(print())
				// assert
				.andReturn();

		//результат GET запроса
		String responseText = result.getResponse().getContentAsString();

		// assert
		System.out.println("responseText = " + responseText);
		System.out.println("allRowsDbAll = " + allRowsDbAll);

		assertEquals(responseText, allRowsDbAll);

	}

	@Test
	@SneakyThrows({Exception.class})
	public void taskDeleteByIdSucces() {
		// arrange
		Task task = new Task();
		String randomName = RandomStringUtils.randomAlphabetic(10);
		task.setName(randomName);	
		tasks.save(task);		
		
		Task foundBeforeDelete = tasks.findOne(task.getId());
		assertNotNull(foundBeforeDelete);
		
		// act
		mockMvc.perform(delete("/tasks/"+task.getId())
                .contentType(contentType))
				.andDo(print())
		// assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(task.getId().intValue())))
                .andExpect(jsonPath("$.name", is(task.getName())));
		Task foundAfterDelete = tasks.findOne(task.getId());
		assertNull(foundAfterDelete);
		
	}
}
