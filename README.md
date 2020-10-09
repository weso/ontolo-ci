## Welcome to Ontolo-CI

Ontolo-CI is a docker based system that integrates with GitHub to provide a continuos integration system for ontologies. It uses [Shape Expressions](http://shex.io) and test instances in order to validate ontologies.

Although inspired in [Travis-CI](https://github.com/travis-ci/travis-ci) and many other continuos integration systems, Ontolo-CI is focused on develop continuos integration for ontologies. This is done by means of test instances and Shape Expressions. Furthermore, Ontolo-CI is integrated with GitHub, so you can add it as a check to Pull Requests or Pushes to different branches.

The following diagram illustrates the architecture of Ontolo-CI. As can be seen, it can be deployed as a docker container in any machine. Then it will listen to [GitHub Webhooks](https://developer.github.com/webhooks/). Whenever a webhook from github arrives it inmediatelly schedulles a build and after the build is finished Ontolo-CI will notify GitHub and publish the data on its webpage.

![](docs/ontolo-ci-main-schema.png)

## Docker Container

Ontolo-CI is shipped as a Docker container so that anyone can deploy its own instance on demand. Inside the Docker container there are several components that work together. The following schema shows them.

![](docs/ontolo-ci-schema-docker.png)

 - **Ontolo-CI Listener:** The listener component receives notifications from GitHub when a Pull Request is started or when commits are pushed. This notifies the scheduller about the new build to perform.

 - **Ontolo-CI Hub:** It acts as a GitHub API interface client. It allows the system to collect files from GitHub but also to inform about the status of the builds.

 - **Ontolo-CI Scheduller:** This component receives builds to schedule from the listener, then creates a worker with the build and schedules its execution.

 - **Ontolo-CI Worker:** Each worker contains a build to execute. A build is a set of tests to execute over an ontology. It only knows how to execute tests when told and who to notificate when finished.

 - **Database:** When a build is finished by a worker the results of the build are stored in a database. Up to now the results that are being stored are: _repo_, _branch_, _event_, _result_. Where the result stores not only the results of the test cases, but also the execution time and other metrics.

 - **Ontolo-CI API:** The API provides an access layer for thrid party services that need to explore the data from an Ontolo-CI instance. It is also used by the web service. It only allows reading data at the time.

 - **Ontolo-CI Web:** Is a Web interface that stores the results of all executions schedulled, under execution and executed.
