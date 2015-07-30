// JavaScript Document
define([
	"dojo/_base/declare", 
	"dojo/parser", 
	"dijit/Dialog",
	"dojo/text!./templates/MessageBox.html"
], function(declare, parser, Dialog, template){

	var MessageBox = declare("ctd.widget.MessageBox", [Dialog], {
		//templateString: template
		
		onSubmitClick: function(e) {},
		
		onCancelClick: function(e) {},
		
		buildRendering: function() {
			this.inherited(arguments);
			dojo.place(template, this.containerNode, "first");
		},

		hide: function() {
			this.destroyRecursive(false);
		}
	});

	var buildMessageBox = function(defaultOpts, options, content, className) {

		var opts = {
			style: "width: 400px; min-height: 40px"
		};

		require(["dojo/_base/lang"], function(lang){
			defaultOpts = lang.mixin(opts, defaultOpts, options);
		});

		var msgBox = new MessageBox(defaultOpts);
		msgBox.show();

		require(["dojo/dom", "dojo/dom-class"], function(dom, domClass){
			// fetch a node by id="content"
			var node = dom.byId("content");
			node.innerHTML = content;

			domClass.add(node.parentNode, className);
		});
		
		return msgBox;
	};

	MessageBox.success = function (content, options) {
		var opts = {
			title: "成功"
		};

		var msgBox = buildMessageBox(opts, options, content, "msgBoxSuccess");

		require(["dijit/form/Button"], function(Button){
			// Create a button programmatically:
			var okBtn = new Button({
				label: "确定",
				onClick: function(){
					msgBox.hide();
				}
			}).placeAt("msgBoxActionBar", "last");
		});
	};

	MessageBox.error = function (content, options) {
		var opts = {
			title: "错误"
		};

		var msgBox = buildMessageBox(opts, options, content, "msgBoxError");

		require(["dijit/form/Button"], function(Button){
			// Create a button programmatically:
			var okBtn = new Button({
				label: "确定",
				onClick: function(){
					msgBox.hide();
				}
			}).placeAt("msgBoxActionBar", "last");
		});
	};

	MessageBox.info = function (content, options) {
		var opts = {
			title: "提示"
		};

		var msgBox = buildMessageBox(opts, options, content, "msgBoxInfo");

		require(["dijit/form/Button"], function(Button){
			// Create a button programmatically:
			var okBtn = new Button({
				label: "确定",
				onClick: function(){
					msgBox.hide();
				}
			}).placeAt("msgBoxActionBar", "last");
		});
	};

	MessageBox.warning = function (content, options) {
		var opts = {
			title: "警告"
		};

		var msgBox = buildMessageBox(opts, options, content, "msgBoxWarning");

		require(["dijit/form/Button"], function(Button){
			// Create a button programmatically:
			var okBtn = new Button({
				label: "确定",
				onClick: function(){
					msgBox.hide();
				}
			}).placeAt("msgBoxActionBar", "last");
		});
	};

	MessageBox.confirm = function (content, options) {
		var opts = {
			title: "确认"
		};

		var msgBox = buildMessageBox(opts, options, content, "msgBoxConfirm");

		require(["dijit/form/Button"], function(Button){
			// Create a button programmatically:
			var okBtn = new Button({
				label: "确定",
				onClick: function(){
					msgBox.hide();
					options.fnConfirmClick && options.fnConfirmClick();
				}
			}).placeAt("msgBoxActionBar", "last");

			// Create a button programmatically:
			var cancelBtn = new Button({
				label: "取消",
				onClick: function(){
					msgBox.hide();
				}
			}).placeAt("msgBoxActionBar", "last");
		});
	};

	return MessageBox;
});