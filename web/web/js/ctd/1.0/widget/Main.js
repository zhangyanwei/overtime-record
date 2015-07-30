// JavaScript Document
define([
	"dojo/_base/declare", 
	"dojo/parser",
	"ctd/widget/CTDWidget",
	"dojo/text!./templates/Main.html"
], function(declare, parser, CTDWidget, template){

	var Main = declare("ctd.widget.Main", [CTDWidget], {

		templateString: template,

		hashChangedHandler: function(changedHash) {
			require(["dojo/io-query"], dojo.hitch(this, function(ioQuery) {
				var obj = ioQuery.queryToObject(changedHash);
				if (obj.records != undefined) {
					this._createCTDViewWidget();
				} else if (obj.admin != undefined) {
					this._createAdminWidget();
				} else {
					this._createAttendanceWidget();
				}
			}));
		},

		_createAttendanceWidget: function() {
			require(["ctd/widget/Attendance"], dojo.hitch(this, function(Attendance) {
				var attendance = new Attendance();
				//attendance.on("ShowRecordViewClick", dojo.hitch(this, "_loginClickHandler"));
				this._setContentWidget(attendance, this.content);
			}));
		},

		_createCTDViewWidget: function() {
			var ctdView;
			require(["ctd/widget/CTDView"], function(CTDView) {
				ctdView = new CTDView();
			});

			this._setContentWidget(ctdView, this.content);
		},

		_createAdminWidget: function() {
			require(["ctd/widget/Manage"], dojo.hitch(this, function(Manage) {
				if (!(this._currentWidget instanceof Manage)) {
					var manage = new Manage();
					this._setContentWidget(manage, this.content);
				}
			}));
		}
	});

	return Main;
});