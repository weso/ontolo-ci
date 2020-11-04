import React,{useState,useEffect} from 'react';
import axios from 'axios';
import OCIRepo from './OCIRepo';

function OCIDashBoard() {

  const [builds,setBuilds] = useState([]);
  const getTestCases = function(){

    axios({
      method: 'get',
      url: 'http://localhost/api/v1/buildResults',
      config: { headers: {'Access-Control-Allow-Origin': '*' }}
  }).then(function(response){
    console.log(response.data)
        setBuilds(response.data)
    })
    .catch(function (response) {
        console.log('error')
        console.log(response);
    });
  
  }

  useEffect(() => {
      getTestCases();
  });



  return (
    <div className="main-2">
      <h2><a>Builds</a></h2>
      <div className="dashboard-elements-list">

        {builds.map(build =>{
          return <OCIRepo 
                      key={build.id}
                      build={build}
                      owner="weso" 
                      repo="hercules-ontology" 
                      pass={true}
                      commitName="Ontology Update"
                      branchName="master"
                      prId="#71 passed"
                      commitId="f920308"
                      executionTime="3 min 12 sec"
                      date="28 days ago"/>
        })}
       
      </div>
    </div>
  );
}

export default OCIDashBoard;
