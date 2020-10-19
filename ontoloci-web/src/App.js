import React from 'react';
import RepoList from './components/RepoList';
import TestCaseList from './components/TestCaseList';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import './css/header.css'
import './css/footer.css'
import './css/dashboard.css'

function App() {
  return (
    <Router>
    <header>
      <nav>
        <h1>Ontolo CI</h1>
        <Link to="/">
        <a>Dashboard</a>
        </Link>
        
      </nav>
    </header>
   
      <Switch>
        <Route path="/tests/:repo">
          <TestCaseList/>
        </Route>
        <Route path="/">
          <RepoList/>
        </Route>
      </Switch>
    </Router>
 
  );
}

export default App;
