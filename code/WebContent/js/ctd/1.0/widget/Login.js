define([
	"dojo/_base/declare", 
	"dojo/parser",
	"ctd/widget/CTDWidget",
	"dojo/text!./templates/Login.html"
], function(declare, parser, CTDWidget, template){

	return declare("ctd.widget.Login", [CTDWidget], {
		templateString: template,

		_registerClick: function(e) {
			this.onRegisterClick(e);
		},

		onRegisterClick: function(e) {
		},

		_loginClick: function(e) {
			this.onLoginClick(e);
		},

		onLoginClick: function(e) {
		},

		startup: function() {
			this.inherited(arguments);
			parser.parse();

			var scope = this;
			require(["dojo/on"], function(on) {
				on.once(commit, "click", dojo.hitch(scope, "_loginClick"));
			});
		}
	});
});