/**
 * Contains functions that are added to the root AngularJs scope.
 */
BCCapp.run(function($rootScope, $state,$timeout) {
	
	//before each state change, check if the user is logged in
	//and authorized to move onto the next state
	$rootScope.$on('$stateChangeStart', function (event, next,stateParam,prev) {
		console.log("in start state change")
		console.log(next)
		console.log(prev)
		if(prev.name=='neoflow' || prev.name=='flow')
			{
			$rootScope.stateParam=prev.name
			}
	      //
//		console.log(next)
		//console.log(next.name)
		//console.log(next.name!='login')
//		console.log(Auth.isAuthenticated())
		if((next.name=='neoflow' || next.name=='flow') && ($rootScope.stateParam!=next.name))
			{
			
//			console.log(Auth.isAuthenticated())
			
//			console.log(sessionStorage.getItem("user"))
			console.log("clearing selectedtrackarraylabels_neo")
			$rootScope.selectedtrackarraylabels_neo=[]
			$rootScope.countvar_neo=0
			
			
		      }
			})
})
		