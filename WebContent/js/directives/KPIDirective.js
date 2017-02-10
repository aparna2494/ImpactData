
BCCapp.directive("addkpibuttonnode", function(){
	return {
		restrict: "E",
		template: " <a  href='' data-original-title='' title='' addbuttonsnode> <span class='btn green circle'> <i class='fa fa-plus'></i></span></a>"
	}
});
BCCapp.directive("addkpibuttonkpi", function(){
	return {
		restrict: "E",
		template: " <a  href='' data-original-title='' title='' addbuttonskpi><span class=' input-circle-left'> <i class='fa fa-plus'></i></span></a>"
	}
});

//Directive for adding buttons on click that show an alert on click
BCCapp.directive("addbuttonsnode", function($compile){
	return function($scope, element, attrs){
		element.bind("click", function(){
			$scope.count++;
			
			/*angular.element(document.getElementById('space-for-buttons-node')).append($compile(" <div class='portlet box blue'><div class='portlet-title'><div class='caption'><i class='fa fa-gift'></i> KPI Data </div>"+
                                    "<div class='tools'></div></div><div class='portlet-body form'><div class='form-body'><div class='row'><div class='col-md-4'>"+
                                    "<div class='form-group '><label class='control-label'>KPI Name</label><input type='text' class='form-control' ng-model='kpiName["+scope.count+"]'> </div>"+
                                                "</div><div class='col-md-4'><div class='form-group '><label class='control-label'>KPI Status</label>"+
                                                "<input type='text' class='form-control' ng-model='kpiStatus["+scope.count+"]'> </div></div>"+
                                                "<div class='col-md-4'>"+
                                                "<div class='form-group '><label class='control-label'>SKMS ID</label><input type='text' class='form-control' ng-model='skmsId["+scope.count+"]'> </div></div></div><div class='row'>"+
                                                "<div class='col-md-4'><div class='form-group'><label class='control-label'>Threshold for Critical</label>"+
                                                "<input type='text' class='form-control' ng-model='criticalThreshold["+scope.count+"]'> </div></div>"+
                                                "<div class='col-md-4'><div class='form-group'><label class='control-label'>Threshold for High</label>"+
                                                "<input type='text' class='form-control' ng-model='highThreshold["+scope.count+"]'> </div></div><div class='col-md-4'>"+
                                                 "<div class='form-group'><label class='control-label'>Threshold for Medium</label><input type='text' class='form-control' ng-model='mediumThreshold["+scope.count+"]'> </div>"+
                                                "</div></div><div class='row'><div class='col-md-4'><div class='form-group'><label class='control-label'>Database</label>"+
                                                "<input type='text' class='form-control' ng-model='database["+scope.count+"]'> </div></div>"+
                                                "<div class='col-md-8'><div class='form-group'><label class='control-label'>Query</label>"+
                                                "<textarea class='form-control' ng-model='query["+scope.count+"]'></textarea> </div></div></div></div></div>")(scope));
		*/
			angular.element(document.getElementById('space-for-buttons-node')).append($compile('<div class="form-group"><label class="col-md-2 control-label">Dependency</label><div class="col-md-3"><div class="input-group"><span class="input-group-addon input-circle-left"><i class="fa fa-cogs"></i></span><input type="text" class="form-control input-circle-right" ng-model="dependency['+$scope.count+']" placeholder="Dependency"> </div></div><label class="col-md-2 control-label">Dependency Type</label><div class="col-md-3 "><div class="input-group"><span class="input-group-addon input-circle-left"><i class="fa fa-cogs"></i></span><select name="deptype" id="deptype" class="form-control col-md-2 input-circle-right" ng-model="dependencytype['+$scope.count+']"  ng-options="dependencytype.type for dependencytype in dependencytypes track by dependencytype.type" ng-required="true"></select></div></div></div>')($scope))});
	};
});
/*BCCapp.directive("addbuttonskpi", function($compile){
	return function(scope, element, attrs){
		element.bind("click", function(){
			scope.count++;
			scope.starttime[scope.count]=new Date()
			angular.element(document.getElementById('space-for-buttons-kpi')).append($compile(" <div class='portlet box blue'><div class='portlet-title'><div class='caption'><i class='fa fa-gift'></i> KPI Data </div>"+
                                    "<div class='tools'></div></div><div class='portlet-body form'><div class='form-body'><div class='row'><div class='col-md-4'>"+
                                    "<div class='form-group '><label class='control-label'>KPI Name</label><input type='text' class='form-control' ng-model='kpiName["+scope.count+"]'> </div>"+
                                                "</div><div class='col-md-4'><div class='form-group '><label class='control-label'>KPI Status</label>"+
                                                "<input type='text' class='form-control' ng-model='kpiStatus["+scope.count+"]'> </div></div>"+
                                                "<div class='col-md-4'>"+
                                                "<div class='form-group '><label class='control-label'>SKMS ID</label><input type='text' class='form-control' ng-model='skmsId["+scope.count+"]'> </div></div></div><div class='row'>"+
                                                "<div class='col-md-4'><div class='form-group'><label class='control-label'>Threshold for Critical</label>"+
                                                "<input type='text' class='form-control' ng-model='criticalThreshold["+scope.count+"]'> </div></div>"+
                                                "<div class='col-md-4'><div class='form-group'><label class='control-label'>Threshold for High</label>"+
                                                "<input type='text' class='form-control' ng-model='highThreshold["+scope.count+"]'> </div></div><div class='col-md-4'>"+
                                                 "<div class='form-group'><label class='control-label'>Threshold for Medium</label><input type='text' class='form-control' ng-model='mediumThreshold["+scope.count+"]'> </div>"+
                                                "</div></div><div class='row'><div class='col-md-4'><div class='form-group'><label class='control-label'>Database</label>"+
                                                "<input type='text' class='form-control' ng-model='database["+scope.count+"]'> </div></div>"+
                                                "<div class='col-md-8'><div class='form-group'><label class='control-label'>Query</label>"+
                                                "<textarea class='form-control' ng-model='query["+scope.count+"]'></textarea> </div></div>" +
                                                "<div class='col-md-4'><div class='form-group'>"+
                                                "<div class='input-group input-large'>"+
                                                "<input type='text' class='form-control' datetime-picker='dd/MM/yyyy HH:mm' ng-model='starttime["+scope.count+"]' is-open='obj.opened["+scope.count+"]' placeholder='loading'>"+
                                                "<span class='input-group-btn'>"+
                                                "<button type='button' class='btn btn-default' ng-click='openCalendar($event, "+scope.count+")'><i class='fa fa-clock-o'></i></button>"+
                                                "</span> </div>"+
                                               "</div></div>"+
                                               "<div class='col-md-4'><label class='control-label'>Frequency</label>"+
                                               "<input type='text' class='form-control' ng-model='frequency["+scope.count+"]'> </div>"+
                                               "</div></div></div></div></div>")(scope));
		});
	};
});*/


