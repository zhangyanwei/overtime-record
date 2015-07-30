// JavaScript Document
define([
	"dojo/_base/declare", 
	"dojo/parser", 
	"ctd/widget/CTDWidget",
	"dijit/layout/ContentPane",
	"dojo/text!./templates/MemberManage.html"
], function(declare, parser, CTDWidget, ContentPane, template){

	var URL_PROJECT_MEMBER_DATA = "project/{projectid}/member";

	var _aSelectedMember = [];

	var Manager = declare("ctd.widget.MemberManage", [CTDWidget, ContentPane], {

		templateString: template,

		settingView: undefined, /* project Setting view */

		_oMemberGrid: undefined, /* project member data table */

		_memberAddDialog: undefined, /* add member dialog */

		startup: function() {
			this.inherited(arguments);

			var outterScope = this;
			require(["jquery/dataTables"], function(dataTables){
				var oMemberGrid = outterScope._createMemberDataGrid();

				$(".dataTables_scrollBody").height("");
				$(".dataTables_scrollHeadInner").width("100%");
				$(".dataTable").width("100%");
				$(window).resize(function() {
					$(".projView .dataTables_scrollHeadInner .dataTable").width($(".projView .dataTables_scrollBody .dataTable").width());
				});

				outterScope._oMemberGrid = oMemberGrid;
			});

			parser.parse();
			this._bindEvents();
			this._initData();
		},

		destroyRecursive: function(preserveDom) {
			this.inherited(arguments);
			this._destroyWidgetRecursive(this._memberAddDialog);
		},

		_bindEvents: function() {
			addMember.on("click", dojo.hitch(this, this._AddMemberHandler));
			delMember.on("click", dojo.hitch(this, this._DeleteMemberHandler));
			this.addSignal(this.settingView.on("ProjectSelectChange", dojo.hitch(this, this._projectSelectChangeHandler)));
		},

		_initData: function() {
			this._projectSelectChangeHandler(this.settingView.aSelectedProj);
		},

		_projectSelectChangeHandler: function(selectedData) {
			if (selectedData.length > 0) {
				var url = dojo.replace(URL_PROJECT_MEMBER_DATA, {"projectid": selectedData[0].id});
				this._oMemberGrid.fnReloadData(url);
			}

            addMember.setDisabled(selectedData.length < 1);
		},

		_createMemberDataGrid: function() {

			var oMemberGrid = $('#memberTable').dataTable({
				"bProcessing": true,
				//"sAjaxSource": adjaxUrl,
				"sAjaxDataProp": function(data, type, extra) {
					return data;
				},

				"aoColumns": [
					{ "mData": "userid" },
					{ "mData": "userName" },
					{ "mData": "addDate" }
				],

				"bFilter": false,
				"bPaginate": false,
				"bInfo": false,
				"sScrollY": "200px", /* just enable the vertical scroll. */
				"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
					$(nRow).click(function(e) {
						var index = jQuery.inArray(aData, _aSelectedMember);
						if (index < 0) {
							oMemberGrid.fnSelectRows(this, _aSelectedMember);
						}
					});
				}
			});

			oMemberGrid.fnReloadResultCallback = function(data) {
				delMember.setDisabled(true);
			};

			oMemberGrid.fnSelectCallback =  function( selectedData ) {
				delMember.setDisabled(selectedData.length < 1);
			};
			
			return oMemberGrid;
		},

		_AddMemberHandler: function(e) {
			var scope = this;
			var dialog;
			require(["ctd/widget/MemberAddDialog"], function(MemberAddDialog){
				dialog = new MemberAddDialog({
					title: "添加项目成员",
				});

				dialog.execute = function(formContents) {
					require(["ctd/request/project"], function(project){
						project.addMember(scope.settingView.aSelectedProj[0].id, formContents, function(data){
							require(["ctd/widget/MessageBox"], function(MessageBox){
								var url = dojo.replace(URL_PROJECT_MEMBER_DATA, {"projectid": scope.settingView.aSelectedProj[0].id});
								scope._oMemberGrid.fnReloadData(url);
								MessageBox.success("添加项目成员成功。");
							});
						});
					});
				};
			});

			this._memberAddDialog = dialog;
			parser.parse();

			this._memberAddDialog.set('projectId', this.settingView.aSelectedProj[0].id);
			this._memberAddDialog.set('projectName', this.settingView.aSelectedProj[0].name);
			this._memberAddDialog.show();
		},

		_DeleteMemberHandler: function(e) {
			var selectedProj = this.settingView.aSelectedProj[0], member = _aSelectedMember[0], scope = this;
			require(["ctd/widget/MessageBox"], function(MessageBox){
				MessageBox.confirm("该成员关联的所有加班数据都将被删除，确定要删除吗？", {
					fnConfirmClick: function() {
						require(["ctd/request/project"], function(project){
							project.deleteMember(selectedProj.id, member.userid, function(data) {
								var url = dojo.replace(URL_PROJECT_MEMBER_DATA, {"projectid": selectedProj.id});
								scope._oMemberGrid.fnReloadData(url, function(d) {
									scope._oMemberGrid.fnSelectRows('tr:first-child', _aSelectedMember);
								});

								MessageBox.success("删除项目成员成功。");
							});
						});
					}
				});
			});
		}
	});

	return Manager;
});