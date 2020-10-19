import React from 'react';
import OCIRepo from './OCIRepo';


function OCIDashBoard() {
  return (
    <div className="main">
      <h2><a>Builds</a></h2>
      <div className="dashboard-elements-list">
        <OCIRepo owner="weso" 
              repo="hercules-ontology" 
              pass={true}
              commitName="Ontology Update"
              branchName="master"
              prId="#71 passed"
              commitId="f920308"
              executionTime="3 min 12 sec"
              date="28 days ago"/>
        <OCIRepo owner="weso" 
              repo="cidoc-ontology" 
              pass={false}
              commitName="Ontology Update"
              branchName="develop"
              prId="#71 passed"
              commitId="f920308"
              executionTime="3 min 12 sec"
              date="28 days ago"/>
        <OCIRepo owner="weso" 
              repo="asio-ontology" 
              pass={true}
              commitName="Ontology Update"
              branchName="gh-pages"
              prId="#71 passed"
              commitId="f920308"
              executionTime="3 min 12 sec"
              date="28 days ago"/>
       
      </div>
    </div>
  );
}

export default OCIDashBoard;
