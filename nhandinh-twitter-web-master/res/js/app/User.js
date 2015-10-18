User = {
	init : function() {
		this.loadUser();
	},

	loadUser : function() {
		var _self = this;
		var username = this.username = Utils.getQueryVariable("user");
		this.key = "user.last." + username;
		$('#username').html(username);
		$.ajax({
			url : Config.service + "api/users/" + username,
			success : function(data) {
				if (data.status == 0) {
					if (data.data.length > 0) {
						_self.renderTimeline(data.data);
						localStorage.setItem(_self.key,
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
		var _self = this;
		var last = localStorage.getItem(this.key);
		$.ajax({
			url : Config.service + "api/users/" + this.username,
			method : "POST",
			data : {
				last : last
			},
			success : function(data) {
				if (data.status == 0) {
					if (data.data.length > 0) {
						localStorage.setItem(_self.key,
								data.data[0].createddate);
						_self.updateTimeline(data.data);
					}
				}
			}
		});
	},
	
	updateTimeline : function(data) {
		for (var i = 0; i < data.length; i++) {
			data[i].message = this.normalize(data[i].message);
			var html = tmpl("message", data[i]);
			$('#content').prepend(html);
		}
	},

	renderTimeline : function(data) {
		var _self = this;
		for (var i = 0; i < data.length; i++) {
			data[i].message = _self.normalize(data[i].message);
			var html = tmpl("message", data[i]);
			$('#content').append(html);
		}
	},

	normalize : function(msg) {
		var result = msg.match(/\S*#(?:\[[^\]]+\]|\S+)/g);
		if (result != null) {
			for (var i = 0; i < result.length; i++) {
				var find = result[i];
				var re = new RegExp(find, 'g');
				msg = msg.replace(re, '<a class="tag" href="tag.html?tag='
						+ result[i].substr(1) + '">' + result[i] + '</a>');
			}
		}
		return msg;
	}
};
$(document).ready(function() {
	User.init();
});