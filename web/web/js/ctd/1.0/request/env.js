define(["ctd/request/xhr"], function(xhr) {

	var URL_ENV_TIME_NOW = "env/time/now";

	var URL_ENV_USER_NAME = "env/user";

	var methods = {

		getTime : function (resultCallback, faultCallback) {

			var fnResultCall = function(data) {
				var date = new Date(data);
				resultCallback(date);
			};

			xhr.fnGet(URL_ENV_TIME_NOW, undefined, fnResultCall, faultCallback);
		},

		getUserEnv: function (resultCallback, faultCallback) {
			xhr.fnGet(URL_ENV_USER_NAME, undefined, resultCallback, faultCallback);
		}

	};

	return methods;

});