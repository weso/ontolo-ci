package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.build.HubBuild;

/**
 * Interface for ontoloci hub.
 *
 * @author Fablo Menéndez Suárez
 */
public interface OntolociHub {

    /**
     * Add the test to an empty build object.
     *
     * @param hubBuild to populate with test cases.
     * @return the populated hub build.
     */
    HubBuild addTestsToBuild(HubBuild hubBuild);


    void updateCheckRun(String conclusion,String output);

}
