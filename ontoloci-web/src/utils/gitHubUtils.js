export function getRepoPath(owner,repo){
    return 'https://github.com/'+owner+'/'+repo;
}

export function getCommitPath(owner,repo,commit){
    return 'https://github.com/'+owner+'/'+repo+'/commit/'+commit
}

export function getBranhcPath(owner,repo,branch){
    return 'https://github.com/'+owner+'/'+repo+'/tree/'+branch
}

