// JavaScript Document
define([
	"dojo/_base/declare", 
	"dojo/parser", 
	"ctd/widget/CTDWidget",
	"dojo/text!./templates/Setting.html"
], function(declare, parser, CTDWidget, template){

	var URL_PROJECT_DATA = "project/managed";

	var URL_PROJECT_MEMBER_DATA = "project/{projectid}/member";

	var _aSelectedMember = [];

	var Manager = declare("ctd.widget.Setting", [CTDWidget], {

		templateString: template,

		aSelectedProj: [],

		_oProjGrid: undefined, /* project data table */

		_projAddDialog: undefined, /* add project dialog */
		
		_projModifyDialog: undefined, /* modify project dialog */

		startup: function() {
			this.inherited(arguments);

			var outterScope = this;
			require(["jquery/dataTables"], function(dataTables){
				var oProjGrid = outterScope._createProjDataGrid();
				
				oProjGrid.fnReloadData(URL_PROJECT_DATA, function(d) {
					this.fnSelectRows('tr:first-child', outterScope.aSelectedProj);
				});

				$(".dataTables_scrollBody").height("");
				$(".dataTables_scrollHeadInner").width("100%");
				$(".dataTable").width("100%");
				$(window).resize(function() {
					$(".projView .dataTables_scrollHeadInner .dataTable").width($(".projView .dataTables_scrollBody .dataTable").width());
				});
				
				outterScope._oProjGrid = oProjGrid;
			});

			parser.parse();
			this._bindEvents();
		},

		destroyRecursive: function(preserveDom) {
			this.inherited(arguments);
			this._destroyWidgetRecursive(this._projAddDialog);
			this._destroyWidgetRecursive(this._projModifyDialog);
		},

		_bindEvents: function() {
			addProj.on("click", dojo.hitch(this, this._AddProjHandler));
			delProj.on("click", dojo.hitch(this, this._DeleteProjHandler));
			modifyProj.on("click", dojo.hitch(this, this._ModifyProjHandler));

			this._linkItemHash(this.memberManageItem, "member");
			this._linkItemHash(this.taskManageItem, "task");
		},

		hashChangedHandler: function(changedHash) {
			require(["dojo/io-query"], dojo.hitch(this, function(ioQuery) {
				var obj = ioQuery.queryToObject(changedHash);
				if (obj.sTab == undefined || obj.sTab == "member") {
					this._createMemberView();
				} else if (obj.sTab == "task") {
					this._createTaskView();
				}
			}));
		},

		_linkItemHash: function (itemNode, sTab) {
			// add event for attached dom node.
			require(["dojo/on"], dojo.hitch(this, function(on){
				on(itemNode, "click", dojo.hitch(this, function(e) {
					this._hashToStab(sTab);
				}));
			}));
		},

		_hashToStab: function (sTab) {
			require(["dojo/hash", "dojo/io-query"], dojo.hitch(this, function(hash, ioQuery) {
				var obj = ioQuery.queryToObject(hash());
				obj.sTab = sTab;
				hash(ioQuery.objectToQuery(obj));
			}));
		},

		_createProjDataGrid: function() {

			var scope = this;
			var oProjGrid = $('#projTable').dataTable({
				"bProcessing": true,
				//"sAjaxSource": adjaxUrl,
				"sAjaxDataProp": function(data, type, extra) {
					return data;
				},

				"aoColumns": [
					{ "mData": "id" },
					{ "mData": "name" },
					{ "mData": "openDate" }
				],

				"bFilter": false,
				"bPaginate": false,
				"bInfo": false,
				"sScrollY": "200px", /* just enable the vertical scroll. */
				"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
					$(nRow).click(function(e) {
						var index = jQuery.inArray(aData, scope.aSelectedProj);
						if (index < 0) {
							oProjGrid.fnSelectRows(this, scope.aSelectedProj);
						}
					});
				}
			});
			
			oProjGrid.fnSelectCallback =  dojo.hitch(this, function( selectedData ) {				
				delProj.setDisabled(selectedData.length < 1);
				modifyProj.setDisabled(selectedData.length < 1);
				// 回调
				this._onProjectSelectChange(selectedData);
			});

			return oProjGrid;
		},

		_onProjectSelectChange: function (selectedData) {
			this.onProjectSelectChange(selectedData);
		},

		onProjectSelectChange: function (selectedData) {
			// body...
		},

		_createMemberView: function() {
			require(["ctd/widget/MemberManage"], dojo.hitch(this, function(MemberManage){
				this._selectItem(this.memberManageItem);
				var memberManage = new MemberManage({
					settingView: this
				});
				this._setContentWidget(memberManage, this.settingTabContent);
			}));
		},

		_createTaskView: function() {
			require(["ctd/widget/TaskManage"], dojo.hitch(this, function(TaskManage){
				this._selectItem(this.taskManageItem);
				var taskManage = new TaskManage({
					settingView: this
				});
				this._setContentWidget(taskManage, this.settingTabContent);
			}));
		},

		_selectItem: function(itemDomNode) {
			dojo.query(".settingTabItem").removeClass("settingTabItemSelected");
			dojo.query(itemDomNode).addClass("settingTabItemSelected");
		},

		_AddProjHandler: function(e) {
			var scope = this;
			var dialog;
			require(["ctd/widget/ProjAddDialog"], function(ProjAddDialog){
				dialog = new ProjAddDialog({
					title: "添加项目"
				});

				dialog.execute = function(formContents) {
					require(["ctd/request/project"],
						function(project){
							project.createProject(formContents, function(data){
								require(["ctd/widget/MessageBox"], function(MessageBox){
									scope._oProjGrid.fnReloadData(URL_PROJECT_DATA, function(d) {
										scope._oProjGrid.fnSelectRows('td:contains(' + formContents.id + ')', scope.aSelectedProj);
									});

									MessageBox.success("添加项目信息成功。");
								});
							});
						}
					);
				};
			});

			this._projAddDialog = dialog;
			parser.parse();

			this._projAddDialog.show();
		},

		_DeleteProjHandler: function(e) {
			var project = this.aSelectedProj[0], scope = this;
			require(["ctd/widget/MessageBox"], function(MessageBox){
				MessageBox.confirm("该项目的所有数据都将被删除，确定要删除吗？", {
					fnConfirmClick: function() {
						require(["ctd/request/project"], function(project){
							project.deleteProject(project.id, function(data) {
								scope._oProjGrid.fnReloadData(URL_PROJECT_DATA, function(d) {
									scope._oProjGrid.fnSelectRows('tr:first-child', scope.aSelectedProj);
								});

								MessageBox.success("删除项目信息成功。");
							});
						});
					}
				});
			});
		},

		_ModifyProjHandler: function(e) {
			var scope = this, project = this.aSelectedProj[0];
			var dialog;
			require(["ctd/widget/ProjModifyDialog"], function(ProjModifyDialog){
				dialog = new ProjModifyDialog({
					title: "修改项目信息"
				});

				dialog.execute = function(formContents) {
					require(["ctd/request/project"], function(project){
						project.deleteProject(scope.aSelectedProj[0].id, formContents, function(data) {
							scope._oProjGrid.fnReloadData(URL_PROJECT_DATA, function(d) {
								scope._oProjGrid.fnSelectRows('tr:first-child', scope.aSelectedProj);
							});

							MessageBox.success("删除项目信息成功。");
						});
					});
				};
			});

			this._projModifyDialog = dialog;
			parser.parse();

			this._projModifyDialog.set('projectId', project.id);
			this._projModifyDialog.set('projectName', project.name);
			this._projModifyDialog.set('manager', project.manager);
			this._projModifyDialog.set('openDate', project.openDate);
			this._projModifyDialog.show();
		}

	});

	return Manager;
});