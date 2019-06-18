package com.sinodata.kettle;

import org.apache.log4j.Logger;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;

public class ThreadRun {
    public String unit;

    public ThreadRun(String unitName) {
        this.unit = unitName;
    }

    public void runJob() {
        try {
            KettleEnvironment.init();
//            KettleFileRepository repository = new KettleFileRepository();
            KettleDatabaseRepository repository=new KettleDatabaseRepository();
            JobMeta jobMeta = new JobMeta("./job/Job_GetData.kjb", null);
            Job job = new Job(null, jobMeta);
            job.setVariable("UNIT_NAME", this.unit);
            job.setVariable("TASK_SEQ", "1");
            job.start();
            job.waitUntilFinished();
            if (job.getErrors() > 0) {
                System.err.println("job run Failure!");
                job.setFinished(true);
                jobMeta.eraseParameters();
                job.eraseParameters();
                Logger.shutdown();
            } else {
                System.out.println("job run successfully!");
                job.setFinished(true);
                jobMeta.eraseParameters();
                job.eraseParameters();
                Logger.shutdown();
            }
//            repository.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ThreadRun t=new ThreadRun("123");
    }
}