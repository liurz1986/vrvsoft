package cn.com.liurz.util.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJobClass implements Job {
    public TestJobClass() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("123");
    }
}
