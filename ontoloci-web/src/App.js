import React from 'react';
import RepoList from './components/RepoList';
import TestCaseList from './components/TestCaseList';
import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";
import './css/header.css'
import './css/footer.css'
import './css/dashboard.css'

function App() {
  return (

    <Router>
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
