package ok.serge.taskmanagementsystem.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ok.serge.taskmanagementsystem.common.TestExpectedData;
import ok.serge.taskmanagementsystem.dto.TaskDto;
import ok.serge.taskmanagementsystem.exception.TaskNotFoundException;
import ok.serge.taskmanagementsystem.service.TaskService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test()
    void getAll_some() throws Exception {
        Mockito.when(taskService.findAll())
                .thenReturn(List.of(TestExpectedData.getTaskDto1(), TestExpectedData.getTaskDto2()));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(TestExpectedData.TASK_1_ID), Long.class))
                .andExpect(jsonPath("$[1].id", Matchers.is(TestExpectedData.TASK_2_ID), Long.class));
    }

    @Test
    void getAll_empty() throws Exception {
        Mockito.when(taskService.findAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.empty()));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(taskService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(TestExpectedData.getTaskDto1());

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(TestExpectedData.TASK_1_ID), Long.class))
                .andExpect(jsonPath("$.title", Matchers.is(TestExpectedData.TASK_1_TITLE)));
    }

    @Test
    void getById_notFound() throws Exception {
        String exceptionCauseMessage = "Причина";
        Mockito.when(taskService.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new TaskNotFoundException(exceptionCauseMessage));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/4"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("Ошибка", Matchers.is(exceptionCauseMessage)));
    }

    @Test
    void create_success() throws Exception {
        TaskDto taskDto = TestExpectedData.getValidTaskDto(3L);
        Mockito.when(taskService.create(ArgumentMatchers.any(TaskDto.class)))
                .thenReturn(taskDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestExpectedData.getValidTaskDto(null)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(3L), Long.class));
    }

    @Test
    void create_MethodArgumentNotValidException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestExpectedData.getNotValidTaskDto(1L)))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("Ошибка", Matchers.containsString("Невалидный формат запроса")))
                .andExpect(jsonPath("Ошибка", Matchers.containsString("Превышена максимальная длина заголовка")))
                .andExpect(jsonPath("Ошибка", Matchers.containsString("Статус не задан")))
                .andExpect(jsonPath("Ошибка", Matchers.containsString("Превышена максимальная длина описания")))
                .andExpect(jsonPath("Ошибка", Matchers.containsString("Дата выполнения не может быть раньше текущей")));
    }

    @Test
    void update_success() throws Exception {
        TaskDto taskDto = TestExpectedData.getValidTaskDto(null);
        Mockito.when(taskService.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(TaskDto.class)))
                .thenReturn(taskDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/tasks/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(taskDto.getId()), Long.class))
                .andExpect(jsonPath("$.dueDate", Matchers.is(taskDto.getDueDate().toString())));
    }

    @Test
    void update_MethodArgumentNotValidException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/tasks/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestExpectedData.getNotValidTaskDto(1L)))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("Ошибка", Matchers.containsString("Невалидный формат запроса")))
                .andExpect(jsonPath("Ошибка", Matchers.containsString("Превышена максимальная длина заголовка")))
                .andExpect(jsonPath("Ошибка", Matchers.containsString("Статус не задан")))
                .andExpect(jsonPath("Ошибка", Matchers.containsString("Превышена максимальная длина описания")))
                .andExpect(jsonPath("Ошибка", Matchers.containsString("Дата выполнения не может быть раньше текущей")));
    }

    @Test
    void update_OptimisticLockingFailureException() throws Exception {
        Mockito.when(taskService.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(TaskDto.class)))
                .thenThrow(new OptimisticLockingFailureException("Причина"));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/tasks/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestExpectedData.getValidTaskDto(null)))
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("Ошибка", Matchers.is("Попытка изменения или удаления неактуальной версии задачи")));
    }

    @Test
    void delete_success() throws Exception {
        Mockito.doNothing()
                .when(taskService).delete(ArgumentMatchers.any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void delete_OptimisticLockingFailureException() throws Exception {
        Mockito.doThrow(new OptimisticLockingFailureException("Причина"))
                .when(taskService).delete(ArgumentMatchers.anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tasks/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestExpectedData.getValidTaskDto(null)))
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("Ошибка", Matchers.is("Попытка изменения или удаления неактуальной версии задачи")));
    }
}