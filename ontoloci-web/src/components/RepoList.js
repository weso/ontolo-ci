import React from 'react';
import Repo from './Repo';


function App() {
  return (
    <div className="main">
      <h2><a>Builds</a></h2>
      <div className="repos-list">
        <Repo owner="weso" 
              repo="hercules-ontology" 
              pass={true}
              commitName="Ontology Update"
              branchName="master"
              prId="#71 passed"
              commitId="f920308"
              executionTime="3 min 12 sec"
              date="28 days ago"/>
        <Repo owner="weso" 
              repo="cidoc-ontology" 
              pass={false}
              commitName="Ontology Update"
              branchName="develop"
              prId="#71 passed"
              commitId="f920308"
              executionTime="3 min 12 sec"
              date="28 days ago"/>
        <Repo owner="weso" 
              repo="asio-ontology" 
              pass={true}
              commitName="Ontology Update"
              branchName="gh-pages"
              prId="#71 passed"
              commitId="f920308"
              executionTime="3 min 12 sec"
              date="28 days ago"/>
        <Repo owner="mistermboy" 
              repo="hercules-ontology" 
              pass={true}
              commitName="Ontology Update"
              branchName="emilio_20%"
              prId="#71 passed"
              commitId="f920308"
              executionTime="3 min 12 sec"
              date="28 days ago"/>
        <Repo owner="mistermboy" 
              repo="cidoc-ontology" 
              pass={false}
              commitName="Ontology Update"
              branchName="ontology_update"
              prId="#71 passed"
              commitId="f920308"
              executionTime="3 min 12 sec"
              date="28 days ago"/>
        <Repo owner="mistermboy" 
              repo="asio-ontology" 
              pass={true}
              commitName="Ontology Update"
              branchName="master"
              prId="#71 passed"
              commitId="f920308"
              executionTime="3 min 12 sec"
              date="28 days ago"/>
      </div>
    </div>
  );
}

export default App;
