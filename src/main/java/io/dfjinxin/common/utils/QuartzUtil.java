package io.dfjinxin.common.utils;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

public class QuartzUtil {
    public static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void startDelayTimeJob(String key, String group, int delayMillisecond, Class clz) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
            long fTime = System.currentTimeMillis() + delayMillisecond;
            JobDetail job = newJob(clz).withIdentity(key, group).build();
            Trigger trigger = newTrigger().withIdentity(key, group).startAt(new Date(fTime))
                    .withSchedule(simpleSchedule().withRepeatCount(0)).build();
            sched.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void startFixedDateJob(String key, String group, Date triggerStartTime, boolean isRepeat, Class clz) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
            JobDetail job = newJob(clz).withIdentity(key, group).build();
            Trigger trigger = newTrigger().withIdentity(key, group).startAt(triggerStartTime)
                    .withSchedule(isRepeat?simpleSchedule().repeatForever():simpleSchedule().withRepeatCount(0)).build();

            sched.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void startCycleLimitedJob(String key, String group, int interval, int count, Class clz) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
            JobDetail job = newJob(clz).withIdentity(key, group).build();
            Trigger trigger = newTrigger().withIdentity(key, group).startNow()
                    .withSchedule(simpleSchedule().withIntervalInSeconds(interval).withRepeatCount(count)).build();
            sched.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void startCronJob(String key, String group, String cron, Class clz) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
            JobDetail job = newJob(clz).withIdentity(key, group).build();
            Trigger trigger = newTrigger().withIdentity(key, group).startNow().withSchedule(cronSchedule(cron)).build();
            sched.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    public static void startCronJob(String key, String group, Date startTime,Date endTime,String cron, Class clz) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
            JobDetail job = newJob(clz).withIdentity(key, group).build();
            Trigger trigger = newTrigger().withIdentity(key, group).startAt(startTime).endAt(endTime).withSchedule(cronSchedule(cron)).build();
            sched.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void startCronJobWithData(String key, String group, String cron, Class clz, JobDataMap map) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
            JobDetail job = newJob(clz).withIdentity(key, group).setJobData(map).build();
            Trigger trigger = newTrigger().withIdentity(key, group).startNow().withSchedule(cronSchedule(cron)).build();
            sched.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void startCronJobWithData(String jobName, String jobGroup, String triggerName, String triggerGroup,
                                     String cron, Class clz, JobDataMap map) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
            JobDetail job = newJob(clz).withIdentity(jobName, jobGroup).setJobData(map).build();
            Trigger trigger = newTrigger().withIdentity(triggerName, triggerGroup).startNow()
                    .withSchedule(cronSchedule(cron)).build();
            sched.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void startCronJob(String jobName, String jobGroup, String triggerName, String triggerGroup, String cron,
                             Class clz) {
        try {
            System.out.println("schedFactoryId : " + schedulerFactory.toString());
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
            System.out.println("schedId : " + sched.getSchedulerInstanceId() + ",schedName : "
                    + sched.getSchedulerName() + ", " + sched.toString());
            JobDetail job = newJob(clz).withIdentity(jobName, jobGroup).build();
            Trigger trigger = newTrigger().withIdentity(triggerName, triggerGroup).startNow()
                    .withSchedule(cronSchedule(cron)).build();
            sched.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static void stopJob(String jobName, String jobGroup) {
        JobKey jk = new JobKey(jobName, jobGroup);
        Collection<Scheduler> collection;
        try {
            collection = schedulerFactory.getAllSchedulers();
            Iterator<Scheduler> iter = collection.iterator();
            while (iter.hasNext()) {
                Scheduler sched = iter.next();
                for (String groupName : sched.getJobGroupNames()) {
                    for (JobKey jobKey : sched.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                        if (jobKey.equals(jk)) {
                            sched.deleteJob(jk);
                            System.out.println("[Stop] job : " + jobKey);
                        }
                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    public static void printJob() {
        Collection<Scheduler> collection;
        try {
            collection = schedulerFactory.getAllSchedulers();
            System.out.println("[Print] Current Scheduler Size : " + collection.size());
            Iterator<Scheduler> iter = collection.iterator();
            while (iter.hasNext()) {
                Scheduler sched = iter.next();
                List<String> groupList = sched.getJobGroupNames();
                System.out.println("[Print] Current Group Size : " + groupList.size());
                for (String groupName : groupList) {
                    Set<JobKey> jobKeySet = sched.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
                    System.out.println("[Print] Current JOB Size : " + jobKeySet.size());
                    for (JobKey jobKey : jobKeySet) {
                        System.out.println("[Print] Current JOB : " + jobKey);
                        // System.out.println(sched.getTriggersOfJob(jobKey));
                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}