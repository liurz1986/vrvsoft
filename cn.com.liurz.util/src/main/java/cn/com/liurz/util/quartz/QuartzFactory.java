package cn.com.liurz.util.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class QuartzFactory {
    private SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    public static String CUSTOM_DATA_KEY = "CUSTOM_DATA_KEY";
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
    private Scheduler sched;
    private Logger logger = LoggerFactory.getLogger(QuartzFactory.class);

    public QuartzFactory() {
    }

    public Date getNextValidTimeAfter(String cronExpression_str) {
        try {
            CronExpression cronExpression = new CronExpression(cronExpression_str);
            Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(new Date());
            return nextValidTimeAfter;
        } catch (ParseException var4) {
            this.logger.error("获得下次执行的时间");
            throw new RuntimeException("获得下次时间异常", var4);
        }
    }

    public void addJob(String jobName, Class<? extends Job> jobClass, String time, Object obj) {
        try {
            String key = this.getJobName(jobName, jobClass);
            Scheduler sched1 = this.getSched();
            JobDetail jobDetail = JobBuilder.newJob(jobClass).build();
            jobDetail.getJobDataMap().put(CUSTOM_DATA_KEY, obj);
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(key, TRIGGER_GROUP_NAME);
            triggerBuilder.startNow();
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(time));
            Trigger trigger = triggerBuilder.build();
            sched1.scheduleJob(jobDetail, trigger);
        } catch (Exception var10) {
            throw new RuntimeException(var10);
        }
    }

    private String getJobName(String jobName, Class<?> jobClass) {
        String key = jobName + jobClass.getName();
        return key;
    }

    private Scheduler getSched() {
        if (this.sched == null) {
            try {
                this.sched = this.schedulerFactory.getScheduler();
                if (!this.sched.isShutdown()) {
                    this.sched.start();
                }
            } catch (SchedulerException var2) {
                var2.printStackTrace();
            }
        }

        return this.sched;
    }

    public void startJobs() {
        try {
            Scheduler sched = this.schedulerFactory.getScheduler();
            sched.start();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public void shutdownJobs() {
        try {
            Scheduler sched = this.schedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }

        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public void modifyJobTime(String jobName, Class<?> jobClass, String time) {
        try {
            String key = this.getJobName(jobName, jobClass);
            Scheduler sched = this.schedulerFactory.getScheduler();
            TriggerKey triggerKey = new TriggerKey(key, TRIGGER_GROUP_NAME);
            CronTrigger trigger = (CronTrigger)sched.getTrigger(triggerKey);
            if (trigger != null) {
                String oldTime = trigger.getCronExpression();
                if (!oldTime.equalsIgnoreCase(time)) {
                    JobKey jobKey = new JobKey(key, JOB_GROUP_NAME);
                    JobDetail jobDetail = sched.getJobDetail(jobKey);
                    JobDataMap dataMap = jobDetail.getJobDataMap();
                    Object object = dataMap.get(CUSTOM_DATA_KEY);
                    Class<? extends Job> objJobClass = jobDetail.getJobClass();
                    this.removeJob(jobName, jobClass);
                    this.addJob(jobName, objJobClass, time, object);
                }

            }
        } catch (Exception var14) {
            throw new RuntimeException(var14);
        }
    }

    public void removeJob(String jobName, Class<?> jobClass) {
        try {
            String key = this.getJobName(jobName, jobClass);
            Scheduler sched = this.schedulerFactory.getScheduler();
            sched.pauseTrigger(new TriggerKey(key, TRIGGER_GROUP_NAME));
            sched.unscheduleJob(new TriggerKey(key, TRIGGER_GROUP_NAME));
            sched.deleteJob(new JobKey(key, JOB_GROUP_NAME));
        } catch (Exception var5) {
            throw new RuntimeException(var5);
        }
    }

    public boolean existsJobs(String jobName, Class<?> jobClass) {
        try {
            String key = this.getJobName(jobName, jobClass);
            Scheduler sched = this.schedulerFactory.getScheduler();
            TriggerKey triggerKey = new TriggerKey(key, TRIGGER_GROUP_NAME);
            boolean checkExists = sched.checkExists(triggerKey);
            return checkExists;
        } catch (SchedulerException var7) {
            throw new RuntimeException(var7);
        }
    }

    public String printAllJob(String jobName, Class<?> jobClass) {
        String key = this.getJobName(jobName, jobClass);

        try {
            Scheduler scheduler = this.schedulerFactory.getScheduler();
            List<String> triggerGroups = scheduler.getTriggerGroupNames();
            String[] triggers = null;

            for(int i = 0; i < triggerGroups.size(); ++i) {
                String groupName = (String)triggerGroups.get(i);
                if (TRIGGER_GROUP_NAME.equals(groupName)) {
                    List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(new JobKey(key));
                    this.logger.info("begin find all jobs from TRIGGER_GROUP_NAME[EXTJWEB_TRIGGERGROUP_NAME] group:");

                    for(int j = 0; j < triggersOfJob.size(); ++j) {
                        Trigger tg = scheduler.getTrigger(new TriggerKey((String)((Object[])triggers)[j], groupName));
                        if (tg instanceof CronTrigger && tg.getKey().getName().startsWith("cycleReportJob")) {
                            this.logger.info("triggerName:[" + j + "] " + tg.getKey().getName());
                        }
                    }
                }
            }

            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(new TriggerKey(key, TRIGGER_GROUP_NAME));
            return trigger != null ? "true" : "false";
        } catch (SchedulerException var12) {
            throw new RuntimeException(var12);
        }
    }
}
