package es.weso.ontoloci.hub;

import es.weso.ontoloci.hub.build.HubBuild;

/**
 * This interface sets the contract for all the hub possible implementations
 * @author Pablo Menéndez Suárez
 */
public interface Hub {

    /**
     * Add the tests to an empty build object.
     *
     * @param hubBuild to populate with test cases.
     * @return the populated hub build.
     */
    HubBuild fillBuild(HubBuild hubBuild);


    /**
     * Updates an existing checkrun with a new status and a output message
     *
     * @param conclusion    new status
     * @param output        output message
     */
    void updateCheckRun(String conclusion,String output);

}
