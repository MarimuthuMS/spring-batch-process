package com.springbatch.process.junit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springbatch.process.config.SpringBatchScheduler;

import org.mockito.Mock;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SpringBatchSchedulerTest {
    @Autowired
    @Mock JobLauncher jobLauncher;

    @Autowired
    @Mock Job job;
    
    @Autowired
    @Mock private SpringBatchScheduler springBatchScheduler;
    
    @Autowired
    private ScheduledTaskHolder scheduledTaskHolder;

    @Test 
    @DisplayName("Test_Cron_Success")
    public void testEveryMinutesCronTaskScheduled() {
        Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
        scheduledTasks.forEach(scheduledTask -> scheduledTask.getTask().getRunnable().getClass().getDeclaredMethods());
        long count = scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
                .map(scheduledTask -> (CronTask) scheduledTask.getTask())
                .filter(cronTask -> cronTask.getExpression().equals("0 */1 * * * ?"))
                .count();
        assertThat(count).isEqualTo(1L);
    }
    @Test 
    @DisplayName("Test_Cron_Failure")
    public void testCronFailure() {
        Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
        scheduledTasks.forEach(scheduledTask -> scheduledTask.getTask().getRunnable().getClass().getDeclaredMethods());
        long count = scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
                .map(scheduledTask -> (CronTask) scheduledTask.getTask())
                .filter(cronTask -> cronTask.getExpression().equals("0 0 0 1 1 *"))
                .count();
        assertThat(count).isEqualTo(0L);
    }
  
    
    
}
