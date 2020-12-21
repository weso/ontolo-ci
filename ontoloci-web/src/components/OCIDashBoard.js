import React,{useState,useEffect} from 'react';
import axios from 'axios';
import OCIBuild from './OCIBuild';
import {getDate} from '../utils/dateUtils';



function OCIDashBoard() {

  const [builds,setBuilds] = useState([]);
  const getBuilds = function(){

    axios({
      method: 'get',
      url: 'http://156.35.82.21:8085/api/v1/buildResults',
      config: { headers: {'Access-Control-Allow-Origin': '*' }}
  }).then(function(response){
        console.log(response.data)
        setBuilds(sortBuilds(response.data));
    })
    .catch(function (response) {
        console.log('error')
        console.log(response);
    });
  
  }

  const sortBuilds = function(unsortedBuilds){
    return unsortedBuilds.sort((build1,build2)=>{
      if(build1.id > build2.id ){
        return -1;
      }
      if(build1.id < build2.id ){
        return 1;
      }

      return 0;
    })
  }

  useEffect(() => {
    getBuilds()
  }, []);
 
  return (
   
    <div className="main-2">
      <h2><a className="subtitle">Builds</a></h2>
      <div className="dashboard-elements-list">

        {builds.map(build =>{
          return <OCIBuild 
                      key={build.id}
                      build={build}
                      owner={build.metadata.owner}
                      repo={build.metadata.repo}
                      branchName={build.metadata.branch}
                      commitName={build.metadata.commitName}
                      commitId={build.metadata.commitId}
                      prNumber={build.metadata.prNumber}
                      buildResult={build.metadata.buildResult?.toLowerCase()}
                      executionTime={build.metadata.execution_time}
                      exceptions={build.metadata.exceptions}
                      date={getDate(build.metadata.execution_date)}/>
        })}
       
      </div>
    </div>
  );
}

export default OCIDashBoard;
