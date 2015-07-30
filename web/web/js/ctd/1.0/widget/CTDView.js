// JavaScript Document
define([
	"dojo/_base/declare", 
	"dojo/parser",
	"ctd/widget/CTDWidget",
	"dojo/text!./templates/CTDView.html"
 ], function(declare, parser, CTDWidget, template){

	var URL_RECORD_DATA_FOR_MEMBER = "record/project/{projectid}/member/{usrid}";

	var oSelectedProject = undefined;

	var oSelectedMember = undefined;

	var _aSelectedRecord = [];

	var CTDView = declare("ctd.widget.CTDView", [CTDWidget], {

		templateString: template,

		_oRecordGrid: undefined,

		_oMemberSelector: undefined,

		_oAddRecordDialog: undefined,

		_oModifyRecordDialog: undefined,

		buildRendering: function () {
			this.inherited(arguments);
		},

		startup: function() {
			this.inherited(arguments);
			this._createCTDGrid();
			parser.parse();
			this._createProjList();
			this._createMemberList();
			this._relayout();
			this._bindEvents();
		},

		destroyRecursive: function(preserveDom) {
			this.inherited(arguments);
			this._destroyWidgetRecursive(this._oAddRecordDialog);
			this._destroyWidgetRecursive(this._oModifyRecordDialog);
			this._oAddRecordDialog = undefined;
			this._oModifyRecordDialog = undefined;
		},

		_createCTDGrid: function () {
			var scope = this;
			require(["jquery/dataTables"], function(dataTables){

				var timePeriod = function(data, type, extra, opts) {
						var period = "";
						require(["ctd/utils/dataFetch"], function(dataFetch){
							var checkVar = dataFetch.fetchData(data, opts.checkVar);
							if (checkVar) {
								var fromTime = dataFetch.fetchData(data, opts.fromTime);
								var toTime = dataFetch.fetchData(data, opts.toTime);
								require(["ctd/utils/dateUtil"], function(dateUtil) {
									var timeFormat = "HH:mm",
										beginTime = dateUtil.formatTime(fromTime, {timePattern: timeFormat}),
										endTime = dateUtil.formatTime(toTime, {timePattern: timeFormat});
									period = dojo.replace("{beginTime} ~ {endTime}", {
										beginTime: beginTime,
										endTime: endTime
									});
								});
							}
						});

						return period;
					},

					timeInterval = function(data, type, extra, opts) {
						var interval = "";
						require(["ctd/utils/dataFetch"], function(dataFetch){
							var checkVar = dataFetch.fetchData(data, opts.checkVar);
							if (checkVar) {
								var fromTime = dataFetch.fetchData(data, opts.fromTime),
									toTime = dataFetch.fetchData(data, opts.toTime);
								require(["ctd/utils/dateUtil"], function(dateUtil) {
									interval = dateUtil.timeInterval(fromTime, toTime);
								});
							}
						});

						return interval;
					},

					fetchData = function(data, type, extra, props) {
						var value;
						require(["ctd/utils/dataFetch"], function(dataFetch) {
							value = dataFetch.fetchData(data, props);
							value || (value = "");
						});

						return value;
					};

				var recordGrid = $('#gridView').dataTable({
					"bProcessing": true,
					//"sAjaxSource": adjaxUrl,
					"sAjaxDataProp": function(data, type, extra) {
						return data;
					},

					"aoColumns": [
						{ "mData": "workDate" },
						{
							"mData": function (data, type, extra) {
								return timePeriod(data, type, extra, {
									checkVar: "plan",
									fromTime: "plan.beginTime",
									toTime: "plan.endTime"
								});
							}
						},
						{
							"mData": function (data, type, extra) {
								return timeInterval(data, type, extra, {
									checkVar: "plan",
									fromTime: "plan.beginTime",
									toTime: "plan.endTime"
								});
							}
						},
						{ "mData": function (data, type, extra) {
								return timePeriod(data, type, extra, {
									checkVar: "actual.beginTime",
									fromTime: "actual.beginTime",
									toTime: "actual.endTime"
								});
							}
						},
						{
							"mData": function (data, type, extra) {
								return timeInterval(data, type, extra, {
									checkVar: "actual.beginTime",
									fromTime: "actual.beginTime",
									toTime: "actual.endTime"
								});
							}
						},
						{
							"mData": function (data, type, extra) {
								return fetchData(data, type, extra, "actual.taxiTimeBegin");
							}
						},
						{
							"mData": function (data, type, extra) {
								return fetchData(data, type, extra, "actual.taxiTimeEnd");
							}
						},
						{
							"mData": function (data, type, extra) {
								return fetchData(data, type, extra, "actual.taxiStartLocation");
							}
						},
						{
							"mData": function (data, type, extra) {
								return fetchData(data, type, extra, "actual.taxiEndLocation");
							}
						},
						{
							"mData": function (data, type, extra) {
								return fetchData(data, type, extra, "actual.taxiTicket");
							}
						}
					],

					"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
						$(nRow).click(function(e) {
							var index = jQuery.inArray(aData, _aSelectedRecord);
							if (index < 0) {
								recordGrid.fnSelectRows(this, _aSelectedRecord);
							}
						});
					}
				});

				recordGrid.fnSelectCallback =  function( selectedData ) {
					var disable = selectedData.length < 1 || ENV.userEnv.userID !=  oSelectedMember.userid;
					delRecord.setDisabled(disable);
					modifyRecord.setDisabled(disable);
				};

				scope._oRecordGrid = recordGrid;
			});
		},
		
		_createProjList: function() {

			var scope = this;
			require(["dijit/form/FilteringSelect"], function(FilteringSelect) {
				var projectSelect = new FilteringSelect({
					id: "projSelect",
					name: "project",
					//value: "CA",
					//store: stateStore,
					searchAttr: "name",
					onChange: function(projectid){
						scope._ProjectChangeHandler(this, projectid);
					}
				}, "projSelect");

				require(["ctd/request/project"], function(project) {
					project.getProjects(function(data) {
						if (data.length < 1) {
							require(["ctd/widget/MessageBox"], function(MessageBox){
								MessageBox.info("你还没有加入任何项目, 请联系项目管理者。");
							});
						} else {
							require(["dojo/store/Memory"], function(Memory){
								var projStore = new Memory({
									data: data
								});
	
								projectSelect.store = projStore;

								require(["ctd/request/user"], function(user){
									user.getDefaultProject(function(project) {
										var currentProject = (typeof project == 'string' ? data[0] : project);
										projectSelect.setValue(currentProject.id);
									});
								});
							});
						}
					});
				});
			});
		},

		_createMemberList: function() {
			var scope = this;
			require(["dijit/form/FilteringSelect"], function(FilteringSelect) {
				var memberSelect = new FilteringSelect({
					id: "memberSelect",
					name: "member",
					searchAttr: "userid",
					onChange: function(projectid){
						scope._MemberChangeHandler(this, projectid);
					}
				}, "memberSelect");

				scope._oMemberSelector = memberSelect;
			});
		},

		_relayout: function() {
			var ctdProj = dojo.query(".ctdViewProj")[0];
			dojo.place("gridView_filter", ctdProj, "after");
			dojo.place("memberFilter", "gridView_filter", "first");
			dojo.place("projFilter", "gridView_filter", "first");
			dojo.place("gridView_length", "gridView", "after");
			dojo.place("gridView_paginate", "gridView_info", "before");
		},
		
		_disableView: function(disable) {
			addRecord.setDisabled(disable);
			delRecord.setDisabled(disable);
			modifyRecord.setDisabled(disable);
			downloadRecord.setDisabled(disable);
		},

		_bindEvents: function() {
			addRecord.on("click", dojo.hitch(this, this._AddRecordHandler));
			delRecord.on("click", dojo.hitch(this, this._DelRecordHandler));
			modifyRecord.on("click", dojo.hitch(this, this._ModifyRecordHandler));
			downloadUserRecord.on("click", dojo.hitch(this, this._DownloadUserRecordHandler));
			downloadProjectRecord.on("click", dojo.hitch(this, this._DownloadProjectRecordHandler));
		},

		_ProjectChangeHandler: function(context, projectid) {
			oSelectedProject = context.item;
			if (context.item) {
				// 更新成员列表
				var scope = this;
				require(["ctd/request/project"], function(project) {
					project.getProjectMembers(oSelectedProject.id, function(data) {
						if (data.length < 1) {
							require(["ctd/widget/MessageBox"], function(MessageBox){
								MessageBox.info("该项目中还没有任何成员，请确认。");
							});
						} else {
							require(["dojo/store/Memory"], function(Memory){
								scope._oMemberSelector.store =new Memory({
									data: data,
									idProperty: "userid"
								});
								scope._oMemberSelector.setValue("");
								scope._oMemberSelector.setValue(ENV.userEnv.userID);
							});
						}
					});
				});
			} else {
				this._oRecordGrid.fnClearTable();
			}

			this._disableView(context.item == null || context.item == undefined);
		},

		_MemberChangeHandler: function(context, projectid) {
			oSelectedMember = context.item;
			if (context.item) {
				// 更新加班记录
				var url = dojo.replace(URL_RECORD_DATA_FOR_MEMBER, {projectid: oSelectedProject.id, usrid: oSelectedMember.userid});
				this._oRecordGrid.fnReloadData(url, function(d) {
					this.fnSelectRows('tr:first-child', _aSelectedRecord);
				});
			} else {
				this._oRecordGrid.fnClearTable();
			}

			var disable = context.item == null || context.item == undefined || ENV.userEnv.userID !=  oSelectedMember.userid;
			this._disableView(disable);
		},

		_AddRecordHandler: function(e) {
			var scope = this;
			require(["ctd/widget/RecordAddDialog"], function(RecordAddDialog) {
				var dialog = new RecordAddDialog({title: "添加加班记录"});
				dialog.execute = function(formContents) {
					require(["ctd/request/userRecord", "ctd/utils/dateUtil"], function(userRecord, dateUtil) {
						userRecord.addRecord(oSelectedProject.id, oSelectedMember.userid, formContents, function(data) {
							require(["ctd/widget/MessageBox"], function(MessageBox){
								// 更新加班记录
								var url = dojo.replace(URL_RECORD_DATA_FOR_MEMBER, {projectid: oSelectedProject.id, usrid: oSelectedMember.userid});
								scope._oRecordGrid.fnReloadData(url, function(d) {
									this.fnSelectRows('td:contains(' + dateUtil.formatDate(formContents.workDate) + ')', _aSelectedRecord);
								});
								MessageBox.success("添加加班记录成功。");
							});
						});
					});
				};

				parser.parse();
				scope._oAddRecordDialog = dialog;
			});

			this._oAddRecordDialog.show();
			this._oAddRecordDialog.set('projectId', oSelectedProject.id);
			this._oAddRecordDialog.set('projectName', oSelectedProject.name);
			this._oAddRecordDialog.set("record", _aSelectedRecord[0]);
		},

		_DelRecordHandler: function(e) {
			var scope = this;
			require(["ctd/widget/MessageBox"], function(MessageBox){
				MessageBox.confirm("该项加班记录将被删除，删除后再次添加需要重新申请，确定要删除吗？", {
					fnConfirmClick: function() {
						require(["ctd/request/userRecord", "ctd/utils/dateUtil"], function(userRecord){
							userRecord.delRecord(oSelectedProject.id, 
												 oSelectedMember.userid,
												 _aSelectedRecord[0].workDate,
												 function(data) {
								// 更新加班记录
								var url = dojo.replace(URL_RECORD_DATA_FOR_MEMBER, {projectid: oSelectedProject.id, usrid: oSelectedMember.userid});
								scope._oRecordGrid.fnReloadData(url, function(d) {
									this.fnSelectRows('tr:first-child', _aSelectedRecord);
								});

								MessageBox.success("删除加班记录成功。");
							});
						});
					}
				});
			});
		},

		_ModifyRecordHandler: function(e) {
			var scope = this;
			require(["ctd/widget/RecordModifyDialog"], function(RecordModifyDialog) {
				var dialog = new RecordModifyDialog({title: "修改加班记录"});
				dialog.execute = function(formContents) {
					require(["ctd/request/userRecord", "ctd/utils/dateUtil"], function(userRecord, dateUtil) {
						userRecord.updateRecord(oSelectedProject.id,
												oSelectedMember.userid,
												_aSelectedRecord[0].workDate,
												formContents,
												function(data) {
							require(["ctd/widget/MessageBox"], function(MessageBox){
								// 更新加班记录
								var url = dojo.replace(URL_RECORD_DATA_FOR_MEMBER, {projectid: oSelectedProject.id, usrid: oSelectedMember.userid});
								scope._oRecordGrid.fnReloadData(url);
								MessageBox.success("修改加班记录成功。");
							});
						});
					});
				};

				parser.parse();
				scope._oModifyRecordDialog = dialog;
			});

			this._oModifyRecordDialog.show();
			this._oModifyRecordDialog.set('projectId', oSelectedProject.id);
			this._oModifyRecordDialog.set('projectName', oSelectedProject.name);
			this._oModifyRecordDialog.set("record", _aSelectedRecord[0]);
		},

		_DownloadUserRecordHandler: function(e) {
			require(["ctd/request/userRecord"], function(userRecord) {
				userRecord.downloadRecord(oSelectedProject.id, oSelectedMember.userid);
			});
		},

		_DownloadProjectRecordHandler: function(e) {
			require(["ctd/request/userRecord"], function(userRecord) {
				userRecord.downloadRecord(oSelectedProject.id);
			});
		}
	});

	return CTDView;
});