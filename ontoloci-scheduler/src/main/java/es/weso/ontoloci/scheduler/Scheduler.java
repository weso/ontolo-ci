package es.weso.ontoloci.scheduler;

import es.weso.ontoloci.worker.build.Build;

public interface Scheduler {

    void scheduleBuild(Build build);
}
