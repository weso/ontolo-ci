import React from 'react';
import TestCase from './TestCase';


function TestCaseList() {
  return (
    <div className="main">
      <h2><a>Test Cases</a></h2>
      <div className="test-elements-list">
        <TestCase pass={true}/>
        <TestCase pass={true}/>
        <TestCase pass={true}/>
        <TestCase pass={true}/>
        <TestCase pass={true}/>
        <TestCase pass={true}/>
        <TestCase pass={true}/>
      </div>
    </div>
  );
}

export default TestCaseList;
