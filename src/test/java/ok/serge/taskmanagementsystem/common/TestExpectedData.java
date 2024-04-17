package ok.serge.taskmanagementsystem.common;

import ok.serge.taskmanagementsystem.dto.TaskDto;
import ok.serge.taskmanagementsystem.utils.Status;

import java.time.LocalDateTime;
import java.time.Month;

public class TestExpectedData {

    public static final Long TASK_VERSION = 0L;

    public static final Long TASK_1_ID = 1L;

    public static final String TASK_1_TITLE = "title 1";

    public static final String TASK_1_DESCRIPTION = "description 1";

    public static final LocalDateTime TASK_1_DUE_DATE = LocalDateTime.of(2024, Month.JANUARY, 1, 1, 1, 1);

    public static final Status TASK_1_STATUS = Status.UNCOMPLETED;

    public static final Long TASK_2_ID = 2L;

    public static final String TASK_2_TITLE = "title 2";

    public static final String TASK_2_DESCRIPTION = "description 2";

    public static final LocalDateTime TASK_2_DUE_DATE = LocalDateTime.of(2024, Month.FEBRUARY, 2, 2, 2, 2);

    public static final Status TASK_2_STATUS = Status.COMPLETED;

    private static final String stringWithLen65 = "12345678901234567890123456789012345678901234567890123456789012345";
    private static final LocalDateTime pastTime = LocalDateTime.of(2024, Month.JANUARY, 1, 1, 1, 1);
    private static final LocalDateTime futureTime = LocalDateTime.of(2124, Month.JANUARY, 1, 1, 1, 1);

    public static TaskDto getTaskDto1() {
        return new TaskDto(
                TestExpectedData.TASK_1_ID,
                TestExpectedData.TASK_1_TITLE,
                TestExpectedData.TASK_1_DESCRIPTION,
                TestExpectedData.TASK_1_DUE_DATE,
                TestExpectedData.TASK_1_STATUS
        );
    }

    public static TaskDto getTaskDto2() {
        return new TaskDto(
                TestExpectedData.TASK_2_ID,
                TestExpectedData.TASK_2_TITLE,
                TestExpectedData.TASK_2_DESCRIPTION,
                TestExpectedData.TASK_2_DUE_DATE,
                TestExpectedData.TASK_2_STATUS
        );
    }

    public static TaskDto getNotValidTaskDto(Long taskId) {
        return new TaskDto(
                taskId,
                stringWithLen65,
                stringWithLen65.repeat(4),
                pastTime,
                null
        );
    }

    public static TaskDto getValidTaskDto(Long taskId) {
        return new TaskDto(
                taskId,
                TestExpectedData.TASK_1_TITLE,
                TestExpectedData.TASK_1_DESCRIPTION,
                futureTime,
                TestExpectedData.TASK_1_STATUS
        );
    }
}
