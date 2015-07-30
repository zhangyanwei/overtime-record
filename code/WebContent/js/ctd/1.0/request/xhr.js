define(function() {

	this.fnRequest =  function(url, opts, resultCallback, faultCallback) {

		opts = dojo.mixin({
			headers: {'Content-Type':'application/json'},
			handleAs: "json"
		}, opts);
		
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

	this.fnGet = function(url, opts, resultCallback, faultCallback) {
		this.fnRequest.apply(this, arguments);
	};

	this.fnPost = function(url, opts, resultCallback, faultCallback) {
		opts = dojo.mixin({
			method: "POST"
		}, opts);

		this.fnRequest.apply(this, arguments);
	};

	this.fnDelete = function(url, opts, resultCallback, faultCallback) {
		opts = dojo.mixin({
			method: "DELETE"
		}, opts);

		this.fnRequest.apply(this, arguments);
	};

	return this;

});
