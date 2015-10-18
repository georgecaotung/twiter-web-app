App = {

	init : function() {
		var _self = this;
		$("#tmpl").load("include/tmpl.html", function() {
			var user = localStorage.getItem("user");
			if (user != null) {
				user = JSON.parse(user);
				var html = tmpl("usertmpl", user);
				$('.header').html(html);
			}
		});
	},

	doRegister : function() {
		$.ajax({
			url : Config.service + "api/register",
			method : "POST",
			data : {
				email : $('#email').val(),
				password : $('#password').val()
			},
			success : function(data) {
				if (data.status != 0) {
					alert(data.msg);
				} else {
					alert("Register account successfully");
					window.location.href = 'login.html';
				}
			},
			failure : function(data) {
				alert("Register account error");
			}
		});
	},

	login : function() {
		$.ajax({
			url : Config.service + "api/login",
			method : "POST",
			data : {
				email : $('#email').val(),
				password : $('#password').val()
			},
			success : function(data) {
				if (data.status != 0) {
					alert(data.msg);
				} else {
					localStorage.setItem("login.token", data.data.token);
					localStorage
							.setItem("user", JSON.stringify(data.data.user));
					window.location.href = 'timeline.html';
				}
			},
			failure : function(data) {
				alert("Login account error");
			}
		});
	},

	logout : function() {
		localStorage.removeItem("login.token");
		localStorage.removeItem("user");
		$.ajax({
			url : Config.service + "api/logout",
			method : "POST",
			data : {
				token : localStorage.getItem("token")
			},
			success : function(data) {
				if (data.status != 0) {
					alert(data.msg);
				} else {
					window.location.href = 'login.html';
				}
			},
			failure : function(data) {
				alert("Logout account error");
			}
		});
	},

	addMessage : function() {
		$.ajax({
			url : Config.service + "api/postmessage",
			method : "POST",
			data : {
				message : $('#message-input').val(),
				token : localStorage.getItem("login.token")
			},
			success : function(data) {
				if (data.status != 0) {
					alert(data.msg);
					if (data.status == 1) {
						window.location.href = "login.html";
					}
				} else {
					window.location.href = window.location.href;
				}
			},
			failure : function(data) {
				alert("Post message error");
			}
		});
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
	App.init();
});