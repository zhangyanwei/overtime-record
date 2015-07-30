// JavaScript Document
define([
	"dojo/_base/declare", 
	"ctd/widget/SubmitDialog",
	"dojo/text!./templates/MemberAddDialog.html"
], function(declare, SubmitDialog, template){

	var MemberAddDialog = declare("ctd.widget.MemberAddDialog", [SubmitDialog], {
		contentAreaTemplate: template,

		_setProjectIdAttr: function(value) {
			this._set("projectId", value);
			dojo.query("input[name='projectid']", this.domNode).attr("value", value);
		},

		_setProjectNameAttr: function(value) {
			this._set("projectName", value);
			dojo.query("input[name='projectName']", this.domNode).attr("value", value);
		}
	});

	return MemberAddDialog;
});