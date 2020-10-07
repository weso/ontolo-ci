package es.weso.ontoloci.io.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A repository represents the object that holds the code. Up to this point we only need to know the owner, the name and
 * the branch. For example, owner/name:branch.
 *
 * @author Guillermo Facundo Colunga
 * @version 20201007
 */
public class Repository {

    // LOGGER CREATION
    private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);

    public final String owner;
    public final String name;
    public final String branch;

    /**
     * Main repository constructor, it takes all posible parameters for a repository.
     *
     * @param owner of the repository.
     * @param name of the repository.
     * @param branch that we are representing.
     */
    public Repository(String owner, String name, String branch) {
        this.owner = owner;
        this.name = name;
        this.branch = branch;

        LOGGER.debug("Creating a new repository " + this.toString());
    }

    /**
     * Gets the owner of the repository.
     *
     * @return the owner of the repository.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the name of the repository.
     *
     * @return the name of the repository.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the branch of the repository.
     *
     * @return the branch of the repository.
     */
    public String getBranch() {
        return branch;
    }

    @Override
    public String toString() {
        return "Repository{" +
                    "owner='" + owner + '\'' +
                    ", name='" + name + '\'' +
                    ", branch='" + branch + '\'' +
                '}';
    }
}
