import React from 'react';
import { Link } from "react-router-dom";

function OCIHeader() {
  return (
    <header>
      <nav>
        <h1>Ontolo CI</h1>
        <Link to="/">
        <a>Dashboard</a>
        </Link>
      </nav>
    </header>
  );
}

export default OCIHeader;
