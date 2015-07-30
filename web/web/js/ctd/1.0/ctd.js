(function(window, undefined) {

	var CTD = function() {
	};

	CTD.xhr = new function() {
		this.fnRequest =  function(url, opts, resultCallback, faultCallback) {
			require(["dojo/request/xhr", "dojo/dom-attr"],
				function(xhr, domAttr){
					faultCallback || (faultCallback = function(resp){
						require(["ctd/widget/MessageBox"], function(MessageBox){
							require(["dojo/json"], function(JSON){
								var error = JSON.parse(resp.response.text, true);
								MessageBox.error(error.message);
							});
						});
					});

					xhr(url, opts).then(resultCallback, faultCallback);
				}
			);
		};
	};

	window.CTD = CTD;

})(window);