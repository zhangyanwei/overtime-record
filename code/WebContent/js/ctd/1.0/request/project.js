define(["ctd/request/xhr"], function(xhr) {

	var URL_PROJECT_CREATE = "project";

	var URL_PROJECT_DELETE = "project/{projectid}";

	var URL_PROJECT_UPDATE = "project/{projectid}";

	var URL_PROJECT_DATA = "project";

	var URL_PROJECT_MEMBER_CREATE = "project/{projectid}/member";

	var URL_PROJECT_MEMBER_DELETE = "project/{projectid}/member/{usrid}";

	var URL_PROJECT_MEMBER_DATA = "project/{projectid}/member";

	var methods = {
		createProject: function (project, resultCallback, faultCallback) {
			xhr.fnPost(URL_PROJECT_CREATE, {
				data: dojo.toJson(project)
			}, resultCallback, faultCallback);
		},

		deleteProject: function(projectid, resultCallback, faultCallback) {
			var url = dojo.replace(URL_PROJECT_DELETE, {"projectid": projectid});
			xhr.fnDelete(url, undefined, resultCallback, faultCallback);
		},

		updateProject: function(projectid, project, resultCallback, faultCallback) {
			var url = dojo.replace(URL_PROJECT_UPDATE, {"projectid": projectid});
			xhr.fnPost(url, {
				data: dojo.toJson(project)
			}, resultCallback, faultCallback);
		},

		getProjects: function (resultCallback, faultCallback) {
			xhr.fnGet(URL_PROJECT_DATA, undefined, resultCallback, faultCallback);
		},

		addMember: function(projectid, member, resultCallback, faultCallback) {
			var url = dojo.replace(URL_PROJECT_MEMBER_CREATE, {"projectid": projectid});
			xhr.fnPost(url, {
				data: dojo.toJson(member)
			}, resultCallback, faultCallback);
		},

		deleteMember: function(projectid, userid, resultCallback, faultCallback) {
			var url = dojo.replace(URL_PROJECT_MEMBER_DELETE, {"projectid": projectid, "usrid": userid});
			xhr.fnDelete(url, undefined, resultCallback, faultCallback);
		},

		getProjectMembers: function(projectid, resultCallback, faultCallback) {
			var url = dojo.replace(URL_PROJECT_MEMBER_DATA, {"projectid": projectid});
			xhr.fnGet(url, undefined, resultCallback, faultCallback);
		}		
	};

	return methods;

});
