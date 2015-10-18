Timeline = {
	init : function() {
		this.loadTimeline();
	},

	loadTimeline : function() {
		var _self = this;
		$.ajax({
			url : Config.service + "api/timeline",
			success : function(data) {
				if (data.status == 0) {
					if (data.data.length > 0) {
						Timeline.renderTimeline(data.data);
						localStorage.setItem("timeline.last",
								data.data[0].createddate);
					}
				}
				setInterval(function() {
					_self.update();
				}, 2000);
			}
		});
	},

	update : function() {
		var last = localStorage.getItem("timeline.last");
		$.ajax({
			url : Config.service + "api/timeline",
			method : "POST",
			data : {
				last : last
			},
			success : function(data) {
				if (data.status == 0) {
					if (data.data.length > 0) {
						localStorage.setItem("timeline.last",
								data.data[0].createddate);
						Timeline.updateTimeline(data.data);
					}
				}
			}
		});
	},

	updateTimeline : function(data) {
		for (var i = 0; i < data.length; i++) {
			data[i].message = App.normalize(data[i].message);
			var html = tmpl("message", data[i]);
			$('#content').prepend(html);
		}
	},

	renderTimeline : function(data) {
		for (var i = 0; i < data.length; i++) {
			data[i].message = App.normalize(data[i].message);
			var html = tmpl("message", data[i]);
			$('#content').append(html);
		}
	},

};
$(document).ready(function() {
	Timeline.init();
});