// JavaScript Document
define([
	"dojo/_base/declare", 
	"dojo/parser", 
	"ctd/widget/CTDWidget",
	"dojo/text!./templates/Manage.html"
], function(declare, parser, CTDWidget, template){

	var Manager = declare("ctd.widget.Manage", [CTDWidget], {

		templateString: template,

		buildRendering: function () {
			this.inherited(arguments);
		},

		_bindEvents: function() {
			require(["dojo/on"], dojo.hitch(this, function(on){
					on(this.settingItem, "click", dojo.hitch(this, function(e){
					this._hashToMtab("setting");
				}));
			}));
		},

		hashChangedHandler: function(changedHash) {
			require(["dojo/io-query"], dojo.hitch(this, function(ioQuery) {
				var obj = ioQuery.queryToObject(changedHash);
				if (obj.mTab == undefined || obj.mTab == "setting") {
					this._createSettingView();
				}
			}));
		},

		_hashToMtab: function (mTab) {
			require(["dojo/hash", "dojo/io-query"], dojo.hitch(this, function(hash, ioQuery) {
				var obj = ioQuery.queryToObject(hash());
				obj.mTab = mTab;
				hash(ioQuery.objectToQuery(obj));
			}));
		},

		_createSettingView: function() {
			require(["ctd/widget/Setting"], dojo.hitch(this, function(Setting) {
				if (!(this._currentWidget instanceof Setting)) {
					this._selectItem(this.settingItem);
					var setting = new Setting();
					this._setContentWidget(setting, this.subManage);
				}
			}));
		},

		_selectItem: function(itemDomNode) {
			dojo.query(".manageNavItem").removeClass("manageNavItemSelected");
			dojo.query(itemDomNode).addClass("manageNavItemSelected");
		}
	});

	return Manager;
});