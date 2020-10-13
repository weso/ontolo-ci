import React from 'react';
import RepoList from './components/RepoList';
import TestCaseList from './components/TestCaseList';
import './css/header.css'
import './css/footer.css'
import './css/dashboard.css'

function App() {
  return (
    <div>
        <RepoList/>
        <TestCaseList/>
    </div>
  );
}

export default App;
