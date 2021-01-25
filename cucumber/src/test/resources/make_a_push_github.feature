Feature: Make a push in GitHub
  I want to make a push in my GitHub repository, where the ontology is stored

  Scenario: Push into ontolo-ci-test from WESO
    Given the repository ontolo-ci-test of weso organization
    When I make some changes and I push the content
    Then The ontology of the repository it´s validated
