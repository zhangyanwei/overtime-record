// JavaScript Document
define([
	"dojo/_base/declare", 
	"ctd/widget/RecordAddDialog"
], function(declare, RecordAddDialog){

	var ProjModifyDialog = declare("ctd.widget.RecordModifyDialog", [RecordAddDialog], {

		_getValueAttr: function() {
			var value = this.inherited(arguments);
			if (!value.workDate) {
				var record = this.get("record");
				record && (value.workDate = record.workDate);
			}

			return value;
		},

		_setRecordAttr: function(value) {
			this._set("record", value);
			this._updateDisplay(value);
		},

		_updateDisplay: function(data) {
			var scope = this;
			require(["ctd/utils/dateUtil", "ctd/utils/dataFetch"], function(dateUtil, dataFetch) {
				dojo.query("input[name='workDate']", scope.domNode).forEach(function(node){
					var parentNode = node.parentNode;
					dojo.query("input", parentNode).attr("value", dataFetch.fetchData(data, "workDate"));
				});
				
				var parseTime = function(props) {
					var value = dataFetch.fetchData(data, props);
					return value ? dateUtil.formatTime(value, {timePattern: "HH:mm"}) : "";
				};

				planBeginTime.setDisplayedValue(parseTime("plan.beginTime"));
				planEndTime.setDisplayedValue(parseTime("plan.endTime"));
				holiday.setChecked(dataFetch.fetchData(data, "plan.holiday"));
				taxi.setChecked(dataFetch.fetchData(data, "plan.taxi"));

				var applied = dataFetch.fetchData(data, "plan.applied") ? "是" : "否";
				dojo.query(".planRecordTable label[name='applied']", scope.domNode).empty().addContent(applied);

				actualBeginTime.setDisplayedValue(parseTime("actual.beginTime"));
				actualEndTime.setDisplayedValue(parseTime("actual.endTime"));
				actualEndTime.setDisplayedValue(parseTime("actual.endTime"));
				taxiStartLocation.setDisplayedValue(dataFetch.fetchData(data, "actual.taxiStartLocation"));
				taxiTimeBegin.setDisplayedValue(parseTime("actual.taxiTimeBegin"));
				taxiEndLocation.setDisplayedValue(dataFetch.fetchData(data, "actual.taxiEndLocation"));
				taxiTimeEnd.setDisplayedValue(parseTime("actual.taxiTimeEnd"));

				var ticket = dataFetch.fetchData(data, "actual.taxiTicket");
				taxiTicket.setDisplayedValue(ticket ? ticket : "");
			});

			this._resetDijitRquiredAttr();
		},

		startup: function() {
			this.inherited(arguments);
			workDate.setDisabled(true);
			submit.setLabel("确定");
		}
	});

	return ProjModifyDialog;
});