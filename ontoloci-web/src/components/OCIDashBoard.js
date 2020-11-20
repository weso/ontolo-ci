import React,{useState,useEffect} from 'react';
import axios from 'axios';
import OCIRepo from './OCIRepo';



function OCIDashBoard() {

  const [builds,setBuilds] = useState([]);
  const getBuilds = function(){

    axios({
      method: 'get',
      url: 'http://156.35.82.21/api/v1/buildResults',
      config: { headers: {'Access-Control-Allow-Origin': '*' }}
  }).then(function(response){
        console.log(response.data)
        console.log(response.data)
        console.log(response.data)
        setBuilds(response.data)
    })
    .catch(function (response) {
        console.log('error')
        console.log(response);
    });
  
  }

  useEffect(() => {
    getBuilds()
  }, []);
 
  const getFormatedDate = function(miliseconds) {
    var days, hours, minutes, seconds, total_hours, total_minutes, total_seconds;
    
    total_seconds = parseInt(Math.floor(miliseconds / 1000));
    total_minutes = parseInt(Math.floor(total_seconds / 60));
    total_hours = parseInt(Math.floor(total_minutes / 60));

    days = parseInt(Math.floor(total_hours / 24));
    seconds = parseInt(total_seconds % 60);
    minutes = parseInt(total_minutes % 60);
    hours = parseInt(total_hours % 24);
    

    if(days>0){
      return days +" days ago";
    }

    if(hours>0){
      return hours +" hours ago";
    }

    if(minutes>0){
      return minutes +" minutes ago";
    }

    return "A few seconds ago";
  };

  const getDate = function(date){
    let executionDate = new Date(parseInt(date));
    let currentDate = new Date();
    let resta = currentDate.getTime() - executionDate.getTime();
    return getFormatedDate(Math.round(resta));
  }


  return (
   
    <div className="main-2">
      <h2><a className="subtitle">Builds</a></h2>
      <div className="dashboard-elements-list">

        {builds.map(build =>{
          return <OCIRepo 
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
                      date={getDate(build.metadata.execution_date)}/>
        })}
       
      </div>
    </div>
  );
}

export default OCIDashBoard;
