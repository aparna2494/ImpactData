
CaseimpactApp.factory('RemedyIncidentFactory', function ($resource) {
    return $resource('/api/incident/remedy/:incidentNumber', {incidentNumber:'@num'}, {
        query: {
            method: 'GET',
            params:{},
            isArray: false,
            headers:{'TEST_USER':'ckoyi'}
        },
        update: {
        	method:'PUT',
        	params:{},
        	headers:{'TEST_USER':'ckoyi'}
        }
    })
});