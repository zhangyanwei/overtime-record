define([
	"dojo/_base/declare",
	"ctd/widget/CTDWidget",
	"dojo/text!./templates/Header.html"
 ], function(declare, CTDWidget, template){
 
	return declare("ctd.widget.Header", [CTDWidget], {

		templateString: template,

		onNavitoAdminClick: function(e) {
			require(["dojo/hash"], function(hash) {
				hash("admin");
			});
		},

		onNavitoPreviousClick: function(e) {
			require(["dojo/hash"], function(hash) {
				hash("");
			});
		},

		hashChangedHandler: function(changedHash) {
			require(["dojo/io-query", "dojo/on"], dojo.hitch(this, function(ioQuery, on) {
				var obj = ioQuery.queryToObject(changedHash);
				var node = dojo.byId("globalNaviAdmin");
				if (obj.admin != undefined) {
					node.innerHTML = "退出管理页面";
					on.once(node, "click", this.onNavitoPreviousClick);
				} else {
					node.innerHTML = "进入管理页面";
					on.once(node, "click", this.onNavitoAdminClick);
				}
			}));
		},

		buildRendering: function () {
			this.inherited(arguments);
			require(["ctd/request/env"], function(env){
				env.getUserEnv( function(data) {
					var ENV = {
						userEnv: data
					};

					window.ENV = ENV;
					dojo.byId("usrName").innerHTML = data.userName;
				});
			});
		}
	});
});