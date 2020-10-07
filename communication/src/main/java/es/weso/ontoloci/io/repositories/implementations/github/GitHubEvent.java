package es.weso.ontoloci.io.repositories.implementations.github;

/**
 * Represents a GitHub Event.
 *
 * @author Guillermo Facundo Colunga.
 */
public interface GitHubEvent {

    String WILDCARD_EVENT = "*";
    String CHECK_RUN_EVENT = "check_run";
    String CHECK_SUITE_EVENT = "check_suite";
    String COMMIT_COMMENT_EVENT = "commit_comment";
    String CONTENT_REFERENCE_EVENT = "content_reference";
    String CREATE_EVENT = "create";
    String DELETE_EVENT = "delete";
    String DEPLOY_KEY_EVENT = "deploy_key";
    String DEPLOYMENT_EVENT = "deployment";
    String DEPLOYMENT_STATUS_EVENT = "deployment_status";
    String FORK_EVENT = "fork";
    String GITHUB_APP_AUTHORIZATION_EVENT = "github_app_authorization";
    String GOLLUM_EVENT = "gollum";
    String INSTALLATION_EVENT = "installation";
    String INSTALLATION_REPOSITORIES_EVENT = "installation_repositories";
    String ISSUE_COMMENT_EVENT = "issue_comment";
    String ISSUES_EVENT = "issues";
    String LABEL_EVENT = "label";
    String MARKETPLACE_PURCHASE_EVENT = "marketplace_purchase";
    String MEMBER_EVENT = "member";
    String MEMBERSHIP_EVENT = "membership";
    String META_EVENT = "meta";
    String MILESTONE_EVENT = "milestone";
    String ORGANIZATION_EVENT = "organization";
    String ORG_BLOCK_EVENT = "org_block";
    String PAGE_BUILD_EVENT = "page_build";
    String PROJECT_CARD_EVENT = "project_card";
    String PROJECT_COLUMN_EVENT = "project_column";
    String PROJECT_EVENT = "project";
    String PUBLIC_EVENT = "public";
    String PULL_REQUEST_EVENT = "pull_request";
    String PULL_REQUEST_REVIEW_EVENT = "pull_request_review";
    String PULL_REQUEST_REVIEW_COMMENT_EVENT = "pull_request_review_comment";
    String PUSH_EVENT = "push";
    String PACKAGE_EVENT = "package";
    String RELEASE_EVENT = "release";
    String REPOSITORY_EVENT = "repository";
    String REPOSITORY_IMPORT_EVENT = "repository_import";
    String REPOSITORY_VULNERABILITY_ALERT_EVENT = "repository_vulnerability_alert";
    String SECURITY_ADVISOR_EVENT = "security_advisory";
    String SPONSORSHIP_EVENT = "sponsorship";
    String STAR_EVENT = "star";
    String STATUS_EVENT = "status";
    String TEAM_EVENT = "team";
    String TEAM_ADD_EVENT = "team_add";
    String WATCH_EVENT = "watch";
}
