package ok.serge.taskmanagementsystem.service;

import ok.serge.taskmanagementsystem.common.TestExpectedData;
import ok.serge.taskmanagementsystem.dto.TaskDto;
import ok.serge.taskmanagementsystem.exception.TaskNotFoundException;
import ok.serge.taskmanagementsystem.model.Task;
import ok.serge.taskmanagementsystem.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    private Task task1;

    private Task task2;

    @BeforeEach
    void initTasks() {
        task1 = new Task(
                TestExpectedData.TASK_VERSION,
                TestExpectedData.TASK_1_ID,
                TestExpectedData.TASK_1_TITLE,
                TestExpectedData.TASK_1_DESCRIPTION,
                TestExpectedData.TASK_1_DUE_DATE,
                TestExpectedData.TASK_1_STATUS
        );
        task2 = new Task(
                TestExpectedData.TASK_VERSION,
                TestExpectedData.TASK_2_ID,
                TestExpectedData.TASK_2_TITLE,
                TestExpectedData.TASK_2_DESCRIPTION,
                TestExpectedData.TASK_2_DUE_DATE,
                TestExpectedData.TASK_2_STATUS
        );
    }

    @Test
    void findAll_some() {
        Mockito.when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2));

        List<TaskDto> dtoList = taskService.findAll();

        Assertions.assertEquals(2, dtoList.size());
        Assertions.assertEquals(TestExpectedData.getTaskDto1(), dtoList.get(0));
        Assertions.assertEquals(TestExpectedData.getTaskDto2(), dtoList.get(1));
    }

    @Test
    void findAll_empty() {
        Mockito.when(taskRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<TaskDto> dtoList = taskService.findAll();

        Assertions.assertTrue(dtoList.isEmpty());
    }

    @Test
    void findById_found() throws TaskNotFoundException {
        Mockito.when(taskRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.ofNullable(task1));

        TaskDto taskDto = taskService.findById(0L);

        Assertions.assertEquals(TestExpectedData.getTaskDto1(), taskDto);
    }

    @Test
    void findById_notFound() {
        Mockito.when(taskRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                TaskNotFoundException.class,
                () -> taskService.findById(0L),
                "Задание не было найдено"
        );
    }

    @Test
    void create_success() {
        Mockito.when(taskRepository.save(ArgumentMatchers.any(Task.class)))
                .thenReturn(task1);

        TaskDto taskDto = taskService.create(TestExpectedData.getTaskDto1());

        Assertions.assertEquals(TestExpectedData.getTaskDto1(), taskDto);
    }

    @Test
    void update_success() throws TaskNotFoundException {
        Mockito.when(taskRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(task1));

        TaskDto taskDto = taskService.update(0L, TestExpectedData.getTaskDto2());

        Assertions.assertEquals(TestExpectedData.TASK_1_ID, taskDto.getId());
        Assertions.assertEquals(TestExpectedData.TASK_2_TITLE, taskDto.getTitle());
        Assertions.assertEquals(TestExpectedData.TASK_2_DESCRIPTION, taskDto.getDescription());
        Assertions.assertEquals(TestExpectedData.TASK_2_DUE_DATE, taskDto.getDueDate());
        Assertions.assertEquals(TestExpectedData.TASK_2_STATUS, taskDto.getCompleted());
    }

    @Test
    void update_TaskNotFoundException() {
        Mockito.when(taskRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                TaskNotFoundException.class,
                () -> taskService.update(0L, TestExpectedData.getTaskDto2()),
                "Задание не было найдено"
        );
    }

    @Test
    void delete_success() {
        Mockito.when(taskRepository.findById(ArgumentMatchers.any(Long.class)))
                .thenReturn(Optional.of(new Task()));

        Assertions.assertDoesNotThrow(() -> taskService.delete(0L));
    }

    @Test
    void delete_TaskNotFoundException() {
        Mockito.when(taskRepository.findById(ArgumentMatchers.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                TaskNotFoundException.class,
                () -> taskService.delete(0L),
                "Задание уже было удалено, либо не существует"
        );
    }
}