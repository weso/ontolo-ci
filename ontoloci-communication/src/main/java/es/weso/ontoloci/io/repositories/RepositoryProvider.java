package es.weso.ontoloci.io.repositories;

/**
 * A repository provider is a system that provides repositories that the validation system uses as inputs for the
 * validation. A repository provider therefore will need to provide a name, for information purposes. The base address
 * of its API, for communication purposes.
 *
 * @author Guillermo Facundo Colunga
 * @version 20201007
 */
public interface RepositoryProvider extends RepositoryProviderOperations {

    /**
     * Gets the name of the repository provider. For example GitHub.
     *
     * @return an string containing the repository provider.
     */
    String getName();

    /**
     * Gets the address where the repository provider will expose its API.
     *
     * @return an string containing the address where the repository provider exposes its API.
     */
    String getAPIAddress();
}
