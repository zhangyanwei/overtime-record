// JavaScript Document
define([
	"dojo/_base/declare", 
	"ctd/widget/SubmitDialog",
	"dojo/text!./templates/RecordAddDialog.html"
], function(declare, SubmitDialog, template){

	var RecordAddDialog = declare("ctd.widget.RecordAddDialog", [SubmitDialog], {

		contentAreaTemplate: template,

		_getValueAttr: function() {
			var formContents = this.inherited(arguments);

			var record = {
				workDate: formContents.workDate
			};

			planAvailable.checked && dojo.mixin(record, {
				plan: {
					beginTime: formContents.planBeginTime,
					endTime: formContents.planEndTime,
					holiday: holiday.checked,
					taxi: taxi.checked
				}
			});

			actualAvailable.checked && dojo.mixin(record, {
				actual: {
					beginTime: formContents.actualBeginTime,
					endTime: formContents.actualEndTime,
					taxiTimeBegin: formContents.taxiTimeBegin,
					taxiTimeEnd: formContents.taxiTimeEnd,
					taxiStartLocation: formContents.taxiStartLocation,
					taxiEndLocation: formContents.taxiEndLocation,
					taxiTicket: formContents.taxiTicket
				}
			});

			return record;
		},

		_setProjectIdAttr: function(value) {
			this._set("projectId", value);
			dojo.query("label[name='projectid']", this.domNode).empty().addContent(value);
		},

		_setProjectNameAttr: function(value) {
			this._set("projectName", value);
			dojo.query("label[name='projectname']", this.domNode).empty().addContent(value);
		},

		_setDijitComponentsDisabled: function(selctor, disabled) {
			require(["dijit/registry"], function(registry){
				dojo.query(selctor).forEach(function(dijitNode) {
					registry.byNode(dijitNode).setDisabled(disabled);
				});
			});
		},

		/*
		_updateDisplay: function(data) {
			require(["ctd/utils/dateUtil", "ctd/utils/dataFetch"], function(dateUtil, dataFetch) {
				dojo.query("input[name='workDate']").forEach(function(node){
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
				dojo.query(".planRecordTable label[name='applied']", this.domNode).empty().addContent(applied);

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
		},

		_workDateChangeHandler: function(e) {
			var projectid = this.get("projectId"), scope = this;
			require(["ctd/request/userRecord", "ctd/utils/dateUtil"], function(userRecord, dateUtil) {
				userRecord.getRecord(projectid, dateUtil.formatDate(workDate.value), function(data) {
					scope._updateDisplay(data);
				});
			});
		},
		*/

		_planAvailableChangeHandler: function(value) {
			value || actualAvailable.checked || actualAvailable.setChecked(true);
			this._setDijitComponentsDisabled('.planRecordTable .dijit', !value);
		},

		_actualAvailableChangeHandler: function(value) {
			value || planAvailable.checked || planAvailable.setChecked(true);
			this._setDijitComponentsDisabled('.actualRecordTable .dijit', !value);
		},

		_resetDijitRquiredAttr: function() {
			// complete 0: 混合，1: 都没有填写， 2: 都已经填写
			var taxiDijit = [], complete = 3, scope = this;
			require(["dijit/registry"], function(registry){
				dojo.query('.actualRecordTable .dijit input[name^="taxi"]', scope.domNode).forEach(function(dijitNode) {
					var dijitObject = registry.getEnclosingWidget(dijitNode);
					taxiDijit.push(dijitObject);
					
					var value = dijitObject.getValue();
					complete &= (value == null || ((typeof(value) == 'string') ? value.length == 0 : false)) ? 1 : 2;
				});
			});

			dojo.forEach(taxiDijit, function(dijitObject) {
				dijitObject.set("required", complete == 1 ? false : true);
			});
		},

		onSubmitClick: function(e) {
			this._resetDijitRquiredAttr();
			return this.validate();
		},

		startup: function() {
			this.inherited(arguments);
			//workDate.on("change", dojo.hitch(this, this._workDateChangeHandler));
			planAvailable.on("change", dojo.hitch(this, this._planAvailableChangeHandler));
			actualAvailable.on("change", dojo.hitch(this, this._actualAvailableChangeHandler));
		}
	});

	return RecordAddDialog;
});