package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.build.HubBuild;

/**
 * This interface sets the contract for all the hub possible implementations
 * @author Pablo Menéndez Suárez
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
