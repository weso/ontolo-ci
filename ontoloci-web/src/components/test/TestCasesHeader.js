import React, {useContext} from 'react';
import {TestCasesContext} from '../OCITestCases'
import FailureIcon from '../icon/FailureIcon';
import SuccessIcon from '../icon/SuccessIcon';
import {getRepoPath,getCommitPath,getBranhcPath} from '../../utils/gitHubUtils';
import {getDate} from '../../utils/dateUtils';
import RepoIcon from '../icon/CommitIcon';
import CommitIcon from '../icon/CommitIcon';
import ClockIcon from '../icon/ClockIcon';
import CalendarIcon from '../icon/CalendarIcon';
import BranchIcon from '../icon/BranchIcon';
import {getHeaderInfo} from '../../utils/testUtils';

function TestCasesHeader(){

    const {
        buildStatus,
        owner,
        repo,
        commit,
        commitId,
        commitName,
        prNumber,
        branch,
        execution_time,
        execution_date } = useContext(TestCasesContext);

    const getSvgStatus = function(){
        if(buildStatus==='success')
          return <SuccessIcon/>
        return <FailureIcon/>
    }


    const getHeader = function(){
        let {title,path,linkClass,specialContent} = getHeaderInfo(commit,commitId,prNumber);
        return (
        <h3 className={'test-header-title '+buildStatus}>
          {title}  
          <a className={linkClass +buildStatus} href={"https://github.com/"+owner+"/"+repo+"/"+path}  target="_blank" rel="noopener noreferrer" >{specialContent}</a>
          {" "+commitName+" "}
        </h3>)
      }

    return (
    <div className={"build-panel border-"+buildStatus}>
        <div className="top-panel">
            {getSvgStatus()}
            {getHeader()}
        </div>
    <div className="bottom-panel">
        <div>
            <RepoIcon/>
            <span>
                <a href={getRepoPath(owner,repo)} target="_blank" rel="noopener noreferrer">{owner}/{repo}</a>
            </span>
        </div>
    
        <div>
            <ClockIcon/>
            <span>{execution_time}</span>
        </div>

        <div>
            <CommitIcon/>
            <span>
                <a href={getCommitPath(owner,repo,commit)} target="_blank" rel="noopener noreferrer">{"["+commitId+"] "+commitName}</a></span>
        </div>
        
        <div>
            <CalendarIcon/>
            <span>{getDate(execution_date)}</span>
        </div>
        
        <div>
            <BranchIcon/>
            <span className="branch-name">
                <a href={getBranhcPath(owner,repo,branch)} target="_blank" rel="noopener noreferrer">{branch}</a>
            </span>
      </div>
    </div>
</div>);

}

export default TestCasesHeader;


