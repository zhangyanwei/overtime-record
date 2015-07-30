define(["dojo/date/locale"], function(locale){

	var DateUtil = {
		formatDate: function(/* Date */date, /* Object */opts) {
			opts = dojo.mixin({
				selector: "date",
				datePattern: "yyyy-MM-dd"
			}, opts);

			return locale.format(date, opts);
		},

		/**
		*@return string time.
		*/
		formatTime: function(/* Date, string */time, /* Object */opts) {
			opts = dojo.mixin({
				srcPattern: "HH:mm:ss",
				selector: "time",
				timePattern: "HH:mm:ss"
			}, opts);

			if (typeof time == "string") {
				time = this.parseTime(time, {
					timePattern: opts.srcPattern
				});
			}

			return time != null ? locale.format(time, opts) : "";
		},

		/**
		*@return Date time
		*/
		parseTime: function(/* string */time, /* Object */opts) {
			opts = dojo.mixin({
				selector: "time",
				timePattern: "HH:mm:ss"
			}, opts);

			return locale.parse(time, opts);
		},

		/**
		*@return Date time
		*/
		parseDate: function(/* string */date, /* Object */opts) {
			opts = dojo.mixin({
				selector: "date",
				datePattern: "yyyy-MM-dd"
			}, opts);

			return locale.parse(date, opts);
		},

		/**
		*@return time interval with string format.
		*/
		timeInterval: function(/* string */sStartTime, /* string */sEndTime) {
			if (sStartTime == null || sEndTime == null) {
				return "";
			}

			var endTime = this.parseTime(sEndTime),
				beginTime = this.parseTime(sStartTime),
				iInterval = endTime.getTime() - beginTime.getTime(),
				oInterval = new Date();
			oInterval.setTime(oInterval.getTimezoneOffset() * 60000 + iInterval);
			return this.formatTime(oInterval, {
				timePattern: "HH:mm"
			}); // return string pattern
		}
	};

	return DateUtil;
});
