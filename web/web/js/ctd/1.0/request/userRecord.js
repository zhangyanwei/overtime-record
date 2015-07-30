define(["ctd/request/xhr"], function(xhr) {

	var URL_RECORD_DATA = "record/project/{projectid}";

	var URL_RECORD_GET = "record/project/{projectid}/date/{workDate}";

	var URL_TODAY_RECORD_GET = "record/project/{projectid}/date/today";

	var URL_PUNCH_RECORD = "record/project/{projectid}";

	var URL_INSERT_RECORD = "record/project/{projectid}/member/{usrid}";

	var URL_DELETE_RECORD = "record/project/{projectid}/member/{usrid}/date/{workDate}";

	var URL_MODIFY_RECORD = "record/project/{projectid}/member/{usrid}/date/{workDate}";

	var URL_DOWNLOAD_PROJECT_RECORD = "record/file/project/{projectid}";

	var URL_DOWNLOAD_USER_RECORD = "record/file/project/{projectid}/member/{usrid}";

	var methods = {

		getRecords : function(projectid, resultCallback, faultCallback) {
			var url = dojo.replace(URL_PROJECT_DELETE, {"projectid": projectid});
			xhr.fnDelete(url, undefined, resultCallback, faultCallback);
		},

		getRecord : function(projectid, workDate, resultCallback, faultCallback) {
			var url = dojo.replace(URL_RECORD_GET, {"projectid": projectid, "workDate": workDate});
			xhr.fnGet(url, undefined, resultCallback, faultCallback);
		},

		getTodayRecord : function(projectid, resultCallback, faultCallback) {
			var url = dojo.replace(URL_TODAY_RECORD_GET, {"projectid": projectid});
			xhr.fnGet(url, undefined, resultCallback, faultCallback);
		},

		punchRecord: function(projectid, resultCallback, faultCallback) {
			var url = dojo.replace(URL_PUNCH_RECORD, {"projectid": projectid});
			xhr.fnPost(url, undefined, resultCallback, faultCallback);
		},

		addRecord : function(projectid, usrid, record, resultCallback, faultCallback) {
			var url = dojo.replace(URL_INSERT_RECORD, {"projectid": projectid, "usrid": usrid});
			xhr.fnPost(url, {
				data: dojo.toJson(record)
			}, resultCallback, faultCallback);
		},

		delRecord : function(projectid, usrid, workDate, resultCallback, faultCallback) {
			var url = dojo.replace(URL_DELETE_RECORD, {"projectid": projectid, "usrid": usrid, "workDate": workDate});
			xhr.fnDelete(url, undefined, resultCallback, faultCallback);
		},

		updateRecord : function(projectid, usrid, workDate, record, resultCallback, faultCallback) {
			var url = dojo.replace(URL_MODIFY_RECORD, {"projectid": projectid, "usrid": usrid, "workDate": workDate});
			xhr.fnPost(url, {
				data: dojo.toJson(record)
			}, resultCallback, faultCallback);
		},

		downloadRecord: function(projectid, usrid) {
			var url;
			if (usrid != undefined) {
				url = dojo.replace(URL_DOWNLOAD_USER_RECORD, {"projectid": projectid, "usrid": usrid});
			} else {
				url = dojo.replace(URL_DOWNLOAD_PROJECT_RECORD, {"projectid": projectid});
			}
			//xhr.fnGet(url, undefined, resultCallback, faultCallback);
			window.open(url, '_blank');
		}
	};

	return methods;

});