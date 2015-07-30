// JavaScript Document
define([
	"dojo/_base/declare", 
	"ctd/widget/SubmitDialog",
	"dojo/text!./templates/ProjAddDialog.html"
], function(declare, SubmitDialog, template){

	var ProjAddDialog = declare("ctd.widget.ProjAddDialog", [SubmitDialog], {
		contentAreaTemplate: template
	});

	return ProjAddDialog;
});