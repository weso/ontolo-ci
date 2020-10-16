package es.weso.ontoloci.scheduler;

import es.weso.ontoloci.worker.WorkerExecutor;
import es.weso.ontoloci.worker.WorkerSequential;
import es.weso.ontoloci.worker.build.Build;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class Scheduler {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    // Singleton instance.
    private static final Scheduler INSTANCE = new Scheduler();

    private final LinkedList<Build> buildStack;
    private final WorkerExecutor workerExecutor;

    /**
     * Private singleton constructor.
     */
    private Scheduler() {
        LOGGER.debug("Creating the scheduler instance");
        this.buildStack = new LinkedList<>();

        // This line uses dependency injection to set the specific type of worker to use.
        this.workerExecutor = WorkerExecutor.from(new WorkerSequential());
    }

    /**
     * Gets the instance of the scheduler.
     *
     * @return the instance of the scheduler.
     */
    public static Scheduler getInstance() {
        return INSTANCE;
    }

    /**
     * Schedule a build. This adds the build to the last of the build stack.
     *
     * ¡¡¡ As it is implemented at the moment you cannot insert a build till the previous has finished!!!
     *
     * @param build to add to the build stack.
     */
    public void scheduleBuild(Build build) {
        LOGGER.debug("New build scheduled " + build);
        this.buildStack.addLast(build);
        this.cron();
    }

    /**
     * This cron task shoud be asynchronos but we need to be carefull with the accessed data structure.
     */
    private void cron() {
        LOGGER.debug("Scheduler cron task executed");
        while(!this.buildStack.isEmpty()) {
            LOGGER.debug("Scheduler consuming " + this.buildStack.getFirst());
            this.workerExecutor.executeBuild(this.buildStack.getFirst());
            this.buildStack.removeFirst();
        }
    }
}
