package es.weso.ontoloci.server.listener;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class ListenerEndpointController {

    @PostMapping(path = "/github-webhook-listener")
    public void endPoint(@RequestHeader("X-GitHub-Event") String event, @RequestBody Map<String, Object> payload) {
        /* We have to check if the event is a GitHubEvent.PULL_REQUEST_EVENT
         * We should be only listening to some events and not to all. For example run the CI cicle on a pull
         * request makes sense, and also on each push on each branch. But there are other events that we should not
         * listen to.
         *
         * The idea here is that GitHub -> Webhook(Ontoloci). Ontoloci -> Informs github about a new check run running.
         * Then. Ontoloci -> fetch data from github. Then. Ontoloci -> runs tests. Then. Ontoloci -> informs GitHub
         * about the results and publishes the results.
         *
         * The algorithm would be as follows.
         * */

        // 1. Filter the event type.

        // 2. If the event matches some the events that we listen to we inform github that a new check run has been
        // created.

        // 3. We fetch needed data from GitHub and create local objects for it.

        // 4. We run the tests and generate the results for each test.

        // 5. We inform github about the result of the previously creates check run and publish the results.
    }
}