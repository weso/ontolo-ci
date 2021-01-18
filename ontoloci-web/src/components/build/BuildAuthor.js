import React, {useContext} from 'react';
import {BuildContext} from '../OCIBuild';

function BuildAuthor(){

    const {buildStatus,owner,repo} = useContext(BuildContext);

    return (<div className="dashboard-element-info">
                <span className={buildStatus}>{owner}</span>
                <span className={buildStatus}>{repo}</span>
            </div>);
}

export default BuildAuthor;