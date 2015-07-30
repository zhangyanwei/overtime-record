// JavaScript Document
define([
	"dojo/_base/declare", 
	"dijit/_WidgetBase",
	"dijit/_TemplatedMixin"
], function(declare, _WidgetBase, _TemplatedMixin){

	var CTDWidget = declare("ctd.widget.CTDWidget", [_WidgetBase, _TemplatedMixin], {

		_currentWidget: undefined, /* widget */

		_topicHandle: undefined,

		_signals: [],

		startup: function() {
			this.inherited(arguments);

			require(["dojo/hash"], dojo.hitch(this, function(hash) {
				this.hashChangedHandler(hash());
			}));

			this.subscribeHashchange();
		},

		destroy: function(){
			var scope = this;
			require(["dijit/registry"], function(registry){
				dojo.query('.dijit', scope.domNode).forEach(function(dijitNode) {
					var dijitObject = registry.getEnclosingWidget(dijitNode);
					dijitObject.destroy();
					delete dijitObject;
				});
			});

			this.inherited(arguments);
		},

		subscribeHashchange: function() {
			require(["dojo/hash", "dojo/topic"], dojo.hitch(this, function(hash, topic){
				this._topicHandle = topic.subscribe("/dojo/hashchange", dojo.hitch(this, function(changedHash){
					// Handle the hash change publish
					this.hashChangedHandler(changedHash);
				}));
			}));
		},

		hashChangedHandler: function(changedHash) {
		},

		addSignal: function (signal) {
			this._signals.push(signal);
		},

		_setContentWidget: function(widget, wrapNode){

			if (this._currentWidget != undefined) {
				this._currentWidget.destroyRecursive(false);
			}

			widget.placeAt(wrapNode);
			this._currentWidget = widget;
		},

		_destroyWidgetRecursive: function(widget, preserveDom) {
			if (widget !== undefined) {
				if (preserveDom == undefined) {
					preserveDom = false;
				}

				widget.destroyRecursive(preserveDom);
				delete widget;
			}
		},

		destroyRecursive: function(preserveDom) {
			this.inherited(arguments);
			this._destroyWidgetRecursive(this._currentWidget, preserveDom);

			if (this._topicHandle != undefined) {
				this._topicHandle.remove();
				delete this._topicHandle;
			}

			for (var i = 0; i < this._signals.length; i++) {
				var signal = this._signals[i];
				signal.remove();
			};
		}
	});

	return CTDWidget;
});