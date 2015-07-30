// JavaScript Document
define([
	"dojo/_base/declare", 
	"ctd/widget/ProjAddDialog"
], function(declare, ProjAddDialog){

	var ProjModifyDialog = declare("ctd.widget.ProjModifyDialog", [ProjAddDialog], {

		_setProjectIdAttr: function(value) {
			this._set("projectId", value);
			dojo.query("input[name='id']", this.domNode).attr("value", value);
		},

		_setProjectNameAttr: function(value) {
			this._set("projectName", value);
			dojo.query("input[name='name']", this.domNode).attr("value", value);
		},
		
		_setManagerAttr: function(value) {
			this._set("manager", value);
			dojo.query("input[name='manager']", this.domNode).attr("value", value);
		},

		_setOpenDateAttr: function(value) {
			this._set("openDate", value);
			//dojo.query("input[name='openDate']", this.domNode).attr("value", value);
			opendate.setValue(value);
		},

		startup: function() {
			this.inherited(arguments);
			submit.setLabel("确定");
		}
	});

	return ProjModifyDialog;
});