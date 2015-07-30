// JavaScript Document
define([
	"dojo/_base/declare", 
	"dojo/parser", 
	"ctd/widget/CTDWidget",
	"dojo/text!./templates/TaskManage.html"
], function(declare, parser, CTDWidget, template){

	var Manager = declare("ctd.widget.TaskManage", [CTDWidget], {

		templateString: template

	});

	return Manager;
});