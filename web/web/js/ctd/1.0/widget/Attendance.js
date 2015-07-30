// JavaScript Document
define([
	"dojo/_base/declare", 
	"dojo/parser", 
	"ctd/widget/CTDWidget",
	"dojo/text!./templates/Attendance.html"
], function(declare, parser, CTDWidget, template){

	var Manager = declare("ctd.widget.Attendance", [CTDWidget], {

		templateString: template,

		previousPunchTime: undefined,

		currentTime: undefined,

		currentProject: undefined,

		_punchClickHandler: function(e) {
			//addRecord : function(projectid, record, resultCallback, faultCallback)
			require(["ctd/utils/dateUtil"], dojo.hitch(this, function(dateUtil) {
				var interval = this.currentTime - this.previousPunchTime;
				if (isNaN(interval) || interval > 180000) { // 三分钟以内不能进行两次打卡。
					require(['ctd/request/userRecord'], dojo.hitch(this, function(userRecord) {
						userRecord.punchRecord(this.currentProject.id, dojo.hitch(this, function(data) {
							this._displayPunchRecord(this.currentProject);
						}));
					}));
				} else {
					require(["ctd/widget/MessageBox"], function(MessageBox){
						MessageBox.warning("三分钟以内不能连续打卡两次。");
					});
				}
			}));
		},

		_closeClickHandler: function(e) {
			window.location = "logout";
		},

		_refreshTime: function(callback) {
			var scope = this;
			require(["ctd/request/env"], function(env) {
				env.getTime(function(date) {
					callback.call(scope, date);
					scope.currentTime = date.getTime();
					require(["ctd/utils/dateUtil"], function(dateUtil) {
						var dateStr = dateUtil.formatDate(date, {
							datePattern: "yyyy年MM月dd日 EEEE",
							formatLength: "full", 
							locale: "zh"
						});

						var timeStr = dateUtil.formatTime(date);
						var time = {
							dateSpan: document.getElementById('dateSpan'),
							timeSpan: document.getElementById('timeSpan'),
							hours : parseInt(timeStr.substring(0,2),10),
							minutes : parseInt(timeStr.substring(3,5),10),
							seconds : parseInt(timeStr.substring(6,8),10)
						};

						time.dateSpan.innerHTML = dateStr;

						function refreshTime(){
							scope.currentTime += 1000;
							time.seconds++;
							if(time.seconds == 60){
								time.seconds = 0;
								time.minutes++;
							}
							if(time.minutes == 60){
								time.minutes = 0;
								time.hours++;
							}
							var showHours = time.hours;
							var showMinutes = time.minutes;
							var showSeconds = time.seconds;
							if(time.hours<10) showHours = "0"+showHours;
							if(time.minutes<10) showMinutes = "0"+showMinutes;
							if(time.seconds<10) showSeconds = "0"+showSeconds;
							time.timeSpan.innerHTML = showHours + ":" + showMinutes + ":" + showSeconds;
							setTimeout(refreshTime,1000);
						}

						refreshTime();
					});

				});
			});
		},

		_prepareProjectSelector: function(callback) {

			var fnProjects =  dojo.hitch(this,  function(project) {
				var resultHandler = dojo.hitch(this, function(data) {
					if (data.length < 1) {
						require(["ctd/widget/MessageBox"], function(MessageBox){
							MessageBox.info("你还没有加入任何项目, 请联系项目管理者。");
						});
					} else {
						var fnDefaultProject = dojo.hitch(this, function(user){
							user.getDefaultProject(dojo.hitch(this, function(project) {
								this.currentProject = (typeof project == 'string' ? data[0] : project);
								callback.call(this, this.currentProject);
								this.projectSelectHolder.innerHTML = this.currentProject.name;
								this._createProjectSelector(data);
							}));
						});

						require(["ctd/request/user"], fnDefaultProject);
					}
				});

				project.getProjects(resultHandler);
			});

			require(["ctd/request/project"], fnProjects);
		},

		_displayPunchRecord: function(project) {
			require(["ctd/request/userRecord", "ctd/utils/dateUtil"], dojo.hitch(this, function(userRecord, dateUtil) {
				userRecord.getTodayRecord(project.id, dojo.hitch(this, function(data) {
					var firstPunchNode = dojo.byId("firstPunch"),
						lastPunchNode = dojo.byId("lastPunch");
					var beginTime = dojo.getObject("actual.beginTime", false, data);
					if (beginTime == null) {
						firstPunchNode.parentNode.style.display = "none";
						lastPunchNode.parentNode.style.display = "none";
						dojo.create("div", { 
							"class":"attendanceRecordItem neverPunched", 
							innerHTML:"还没有打过卡……"
						}, firstPunchNode.parentNode, "before");
					} else {
						dojo.query(".neverPunched").style("display", "none");
						firstPunchNode.innerHTML = beginTime;
						firstPunchNode.parentNode.style.display = "";

						var endTime = dojo.getObject("actual.endTime", false, data);
						var currentTimeStr = data.workDate + " " + (endTime == null ? beginTime : endTime);
						this.previousPunchTime = dateUtil.parseDate(currentTimeStr, {
							selector: "full",
							datePattern: "yyyy-MM-dd",
							timePattern: "HH:mm:ss"
						}).getTime();
						//this.previousPunchTime += dateUtil.parseTime((endTime == null ? beginTime : endTime)).getTime();// convert to Date object
						lastPunchNode.innerHTML = endTime;
						lastPunchNode.parentNode.style.display = (endTime == null ? "none" : "");
					} 
				}));
			}));
		},

		_createProjectSelector: function(data) {
			var fn = dojo.hitch(this, function(InlineEditBox, FilteringSelect, Memory) {
				var eb = new InlineEditBox({
					editor: FilteringSelect
				}, this.projectSelectHolder);

				var fnEdit = dojo.hitch(eb, eb.edit);
				var scope = this;
				eb.edit = function() {
					fnEdit();
					var selector = this.wrapperWidget.editWidget;
					if (selector.store.data.length == 0) {
						selector.store = new Memory({
							data: data
						});
						selector.setValue(scope.currentProject.id);
						selector.onChange = function(projectid) {
							scope.currentProject = this.item;
							require(["ctd/request/user"], function(user) {
								user.setDefaultProject(scope.currentProject, function(data) {
									scope._displayPunchRecord(scope.currentProject);
								});
							});
						};
					}
				};
			});

			require(["dijit/InlineEditBox", "dijit/form/FilteringSelect", "dojo/store/Memory"], fn);	
		},

		_showRecordViewClick: function(e) {
			require(["dojo/hash"], function(hash) {
				hash("records");
			});
		},

		startup: function() {
			this.inherited(arguments);
			parser.parse();

			this._refreshTime(function(date) {
				this._prepareProjectSelector(this._displayPunchRecord);
			});

			punch.on("click", dojo.hitch(this, "_punchClickHandler"));
			close.on("click", dojo.hitch(this, "_closeClickHandler"));

			require(["dojo/on"], dojo.hitch(this, function(on){
				on(this.recordNoticeHolder, "click", dojo.hitch(this, function(e){
					this._showRecordViewClick(e);
				}));
			}));
		}
	});

	return Manager;
});