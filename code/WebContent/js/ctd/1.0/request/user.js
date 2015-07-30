define(["ctd/request/xhr"], function(xhr) {

	var URL_PROPS_PROJECT_DEFAULT = "user/props/project/default";

	var methods = {

		getDefaultProject : function (resultCallback, faultCallback) {
			xhr.fnGet(URL_PROPS_PROJECT_DEFAULT, undefined, resultCallback, faultCallback);
		},

		setDefaultProject : function (project, resultCallback, faultCallback) {
			xhr.fnPost(URL_PROPS_PROJECT_DEFAULT, {
				data: dojo.toJson(project)
			}, resultCallback, faultCallback);
		}

	};

	return methods;

});