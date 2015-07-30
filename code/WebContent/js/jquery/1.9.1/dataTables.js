define([
	"./plugins/dataTables/jquery.dataTables"
],function() {
	var DataTable = jQuery.fn.dataTable;
	jQuery.extend(true, DataTable.defaults, {
		"oLanguage": {
			"sLengthMenu": "每页显示 _MENU_ 条记录",
			"sZeroRecords": "还没有任何记录......",
			"sInfo": "共_TOTAL_条记录, 当前页显示了_START_到_END_的记录。",
			"sInfoEmpty": "没有任何记录可显示。",
			"sSearch": "快速过滤: ",
			"oPaginate": {
				"sFirst": "第一页",
				"sLast": "最后一页",
				"sNext": "下一页",
				"sPrevious": "前一页"
			}
			//"sInfoFiltered": "(filtered from _MAX_ total records)"
		}
	});

	var EnchancedDataTable = function(oInit) {

		this.fnReloadData = function(sourceUrl, callback) {
			var scope = this;
			jQuery.getJSON(sourceUrl, function(data) {
				scope.fnClearTable();
				scope.fnAddData(data, true);
				if (callback !== undefined) {
					callback.call(scope, data);
				}

				scope.fnReloadResultCallback && scope.fnReloadResultCallback(data);
			}, function(error) {
				scope.fnReloadFaultCallback && scope.fnReloadFaultCallback(error);
			});
		};
		
		this.fnSelectRows = function(selector, dataArray) {
			dataArray.splice(0, dataArray.length);
			var trNodes = this.$(selector);
			if (trNodes != undefined) {
				var scope = this;
				trNodes.each(function(index){
					var trNode = $(this);
					var trDom = trNode[0];
					if (trDom.nodeName === "TD") {
						trDom = trDom.parentNode;
					}
					
					$("tr", trDom.parentNode).removeClass('row_selected');
					$(trDom).addClass('row_selected');
					var data = scope.fnGetData( trDom );
					dataArray.push(data);
				});
			}
			
			this.fnSelectCallback && this.fnSelectCallback(dataArray);
		};
		
		DataTable.call(this, oInit);

		return this;
	};


	jQuery.extend(true, EnchancedDataTable, DataTable);

	jQuery.fn.dataTable = EnchancedDataTable;

	return EnchancedDataTable;
});