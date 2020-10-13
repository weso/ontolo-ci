package es.weso.ontoloci.worker;

import es.weso.ontoloci.worker.build.Build;
import es.weso.ontoloci.worker.build.BuildResult;

public interface Worker {

    /**
     * Executes a build in the worker.
     *
     * @param build to allocate in the worker.
     * @return the build result object.
     */
    BuildResult executeBuild(Build build);
}
