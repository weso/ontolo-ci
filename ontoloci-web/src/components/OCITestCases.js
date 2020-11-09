import React,{useState,useEffect} from 'react';
import axios from 'axios';
import OCITest from './OCITest';
import {
  useParams
} from "react-router-dom";



function OCITestCases(props) {

  let { id } = useParams();
  const [tests,setTests] = useState([]);
  const [metadata,setMetadata] = useState({});
  const [status,setStatus] = useState('');
  let endpoint = 'http://localhost/api/v1/buildResults/'+id
  const getTestCases = function(){
    axios({
      method: 'get',
      url: endpoint,
      config: { headers: {'Access-Control-Allow-Origin': '*' }}
  }).then(function(response){
      setMetadata(response.data.metadata)
      setStatus(response.data.metadata.buildResult.toLowerCase())
      setTests(response.data.testCaseResults)
    })
    .catch(function (response) {
        console.log('error')
        console.log(response);
    });
  
  }

  useEffect(() => {
    getTestCases()
  }, []);



  return (
    <div className="main">
      <div className={"build-panel border-"+status}>
          <div className="top-panel">
            <svg className="build-cross" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="black" width="18px" height="18px"><path d="M0 0h24v24H0z" fill="none"/><path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/></svg>
            <h3 class={status}>Push <a class={"commit-link "+status} href={"https://github.com/"+metadata.owner+"/"+metadata.repo+"/tree/"+metadata.commit}>[{metadata.commitId}]</a> {metadata.commitName}</h3>
          </div>
          <div className="bottom-panel">
            <div>
            <svg    xmlns="http://www.w3.org/2000/svg" 
                    viewBox="0 0 16 16" 
                    className="icon " 
                    alt="Public repository">
                        <g fill="none" 
                          stroke="#9ea3a8" 
                          stroke-linecap="round"
                          stroke-linejoin="round" 
                          stroke-miterlimit="10">
                              <path d="M7.089 13.343h6.434L13.524 1H2.176v12.343H4.46M2.176 10.543h11.348"></path><path d="M4.46 12.239v2.756l1.394-1.285 1.235 1.236v-2.707zM4.199 1v9.543M5.635 8.737h.63M5.635 6.782h.63M5.635 4.828h.63M5.635 2.873h.63"/>
                    </g>
            </svg>
            <span>{metadata.owner}/{metadata.repo}</span>
            </div>
              


              <div>
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 17 17" className="icon"><circle cx="8.51" cy="8.5" r="3.5" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><path d="M16.5 8.5h-4.49m-7 0H.5" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></path></svg>
              <span>{metadata.commitName}</span>
              </div>
              
              <div>
               <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 17 17" className="icon"><circle cx="4.94" cy="2.83" r="1.83" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><circle cx="11.78" cy="5.15" r="1.83" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><circle cx="4.98" cy="14.17" r="1.83" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><path d="M11.78 6.99s.09 2.68-1.9 3.38c-1.76.62-2.92-.04-4.93 1.97V4.66" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></path></svg>
                <span className="branch-name">Branch {metadata.branch}</span>
            </div>
          </div>
      </div>
      <h2><a class="subtitle">Test Cases</a></h2>
      <div className="test-elements-list">
        {tests.map(test =>{
          return <OCITest 
                  testName= {test.testCase.name}
                  status = {test.status.toLowerCase()}
                  dataNode={test.testCase.expectedShapeMap.split("@")[0]}
                  shape={test.testCase.expectedShapeMap.split("@")[1]}
                  validationStatus={test.metadata.validation_status}
                  executionTime={test.metadata.execution_time}/>
        })}

                                    
      </div>
    </div>
  );
}

export default OCITestCases;
