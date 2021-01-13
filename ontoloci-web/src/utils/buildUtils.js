export function sortBuilds(unsortedBuilds){
    return unsortedBuilds.sort((build1,build2)=>{
      if(build1.id > build2.id ){
        return -1;
      }
      if(build1.id < build2.id ){
        return 1;
      }

      return 0;
    })
}

