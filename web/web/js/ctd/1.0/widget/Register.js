// JavaScript Document
define([
	"dojo/_base/declare", 
	"dojo/parser", 
	"ctd/widget/CTDWidget",
	"dojo/text!./templates/Register.html"
 ], function(declare, parser, CTDWidget, template){
	 

     var Register = declare("ctd.widget.Register", [CTDWidget], {
         templateString: template,
		 
		 _backLoginClick: function(e) {
			 this.onBackLoginClick(e);
		 },
		 
		 onBackLoginClick: function(e) {
		 },
		 
		 _isValidPwd: function() {
			 if (pwdTextBox.value != pwdAgainTextBox.value) {
			   pwdAgainTextBox.invalidMessage = "两次输入的密码不一致，请重新输入。";
			   return false;
			 }
			 
			 return true;
		 },
		 
		 buildRendering: function () {
			 this.inherited(arguments);
		 },
		 
		 startup: function() {
			 this.inherited(arguments);
			 parser.parse();
		 }
	 });
	 
	 return Register;
});