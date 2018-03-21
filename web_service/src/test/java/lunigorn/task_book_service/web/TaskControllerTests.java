package lunigorn.task_book_service.web;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

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
	public void taskSaveSucces() {
		// arrange
		Task task = new Task();
		String randomName = RandomStringUtils.randomAlphabetic(10);
		task.setName(randomName);	
		
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
	
}
