import React from 'react';
import OCIHeader from './components/OCIHeader'
import OCIDashBoard from './components/OCIDashBoard';
import OCITestCases from './components/OCITestCases';
import {
  BrowserRouter as Router,
  Switch,
  Route,
} from "react-router-dom";
import './css/header.css'
import './css/footer.css'
import './css/dashboard.css'


function App() {

  return (
    <Router>
    <OCIHeader/>
      <Switch>
        <Route path="/tests/:id">
          <OCITestCases/>
        </Route>
        <Route path="/">
          <OCIDashBoard/>
        </Route>
      </Switch>
    </Router>
 
  );
}

export default App;
