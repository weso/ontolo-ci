import React,{useState,useEffect} from 'react';
import axios from 'axios';
import OCIBuild from './OCIBuild';
import {sortBuilds} from '../utils/buildUtils';
import {
  BUILD_ENDPOINT,
  REQUEST_METHOD,
  REQUES_HEADER} from '../utils/requestUtils';


function OCIDashBoard() {

  const [builds,setBuilds] = useState([]);

  const getBuilds = function(){
    axios({
      method: REQUEST_METHOD,
      url:    BUILD_ENDPOINT,
      config: { 
          headers: REQUES_HEADER
      }
    }).then(function(response){
        setBuilds(sortBuilds(response.data));
    }).catch(function (response) {
        console.log(response);
    });
  
  }

  useEffect(() => {
    getBuilds()
  }, []);
 
  return (
   
    <div className="main-2">
      <h2><a href='/' className="subtitle">Builds</a></h2>
      <div className="dashboard-elements-list">
        {builds.map(build =>{
            return <OCIBuild key={build.id} build={build}/>
        })}
      </div>
    </div>
  );
}

export default OCIDashBoard;
