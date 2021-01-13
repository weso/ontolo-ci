export function startTestCasesAnimation(){
    animate('main','hidePanel','hideLoader','showLoader')
}

export function stopTestCasesAnimation(){
    animate('hidePanel','main','showLoader','hideLoader')  
}

function animate(before1,after1,before2,after2){
    let e1 = document.getElementsByClassName(before1)[0];
    if(e1) e1.className = after1;
    let e2 = document.getElementsByClassName(before2)[0];
    if(e2) e2.className = after2;
}