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
        <a href="https://github.com/login/oauth/authorize?client_id=Iv1.944a97418a4c1b6f">GetStarted</a>
      </nav>
    </header>
  );
}

export default OCIHeader;
