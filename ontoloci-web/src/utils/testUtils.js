export function getHeaderInfo(commit,commitId,prNumber){
    let title = 'Pull Request';
    let path = 'pull/'+prNumber;
    let linkClass = 'pr-link ';
    let specialContent = ' #'+prNumber+' ';
    if(prNumber === 'none'){
      title = 'Push';
      path = 'commit/'+commit;
      linkClass = 'commit-link ';
      specialContent = ' ['+commitId+'] ';
    }

    return {
        title:title,
        path:path,
        linkClass:linkClass,
        specialContent:specialContent
    }
}