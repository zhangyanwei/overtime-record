// JavaScript Document
define([
     "dojo/_base/declare",
	 "dojo/parser",
	 "dojo/ready",
     "dijit/_WidgetBase",
	 "dijit/_TemplatedMixin",
	 "dojo/text!./templates/Bottom.html"
 ], function(declare, parser, ready, _WidgetBase, _TemplatedMixin, template){

     return declare("widget/Bottom", [_WidgetBase, _TemplatedMixin], {
         templateString: template
     });
	 
});