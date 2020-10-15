package es.weso.ontoloci.hub;

import es.weso.ontoloci.worker.build.Build;

public interface OntolociHub {

    Build addTestsToBuild(Build build);
}
