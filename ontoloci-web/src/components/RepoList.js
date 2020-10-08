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
              branchName="master"/>
        <Repo owner="weso" repo="cidoc-ontology" pass={false}/>
        <Repo owner="weso" repo="asio-ontology" pass={true}/>
        <Repo owner="mistermboy" repo="murica-ontology" pass={true}/>
        <Repo owner="mistermboy" repo="hercules-ontology" pass={false}/>
        <Repo owner="mistermboy" repo="cidoc-ontology" pass={true}/>
      </div>
    </div>
  );
}

export default App;
