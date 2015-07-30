// JavaScript Document
define([
	"dojo/_base/declare", 
	"dojo/parser", 
	"dijit/Dialog",
	"dojo/text!./templates/SubmitDialog.html"
], function(declare, parser, Dialog, template){

	var SubmitDialog = declare("ctd.widget.SubmitDialog", [Dialog], {

		contentAreaTemplate: undefined,

		onSubmitClick: function(e) {
			return this.validate();
		},
		
		onCancelClick: function(e) {
			this.hide();
		},
		
		postCreate: function() {
			this.inherited(arguments);
			var scope = this;
			dojo.place(template, this.containerNode, "first");
			dojo.query(".contentAreaNode", this.domNode).forEach(function(node){
				dojo.place(scope.contentAreaTemplate, node, "after");
			});
		},
		
		startup: function() {
			this.inherited(arguments);
			this.value = undefined;
			submit.on("click", dojo.hitch(this, function(e){
				return this.onSubmitClick(e);
			}));
			
			cancel.on("click", dojo.hitch(this, function(e){
				return this.onCancelClick(e);
			}));
		},

		/*
		hide: function() {
			this.inherited(arguments);
			//this.destroyRecursive(false);
		},*/

		onHide: function() {
			this.destroyRecursive(false);
		}

	});

	return SubmitDialog;
});