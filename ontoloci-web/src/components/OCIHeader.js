import React from 'react';
import { Link } from "react-router-dom";

function OCIHeader() {

  return (
    <header>
      <nav>
        <h1>Ontolo CI</h1>
        <Link to="/">
        <span>Dashboard</span>
        </Link>
        <a href={"https://github.com/login/oauth/authorize?client_id="+process.env.REACT_APP_ONTOLOCI_GITHUB_CLIENT_ID}>GetStarted</a>
      </nav>
    </header>
  );
}

export default OCIHeader;
