Search = {

    init: function () {
        this.initSearchBox();
    },

    doSearch: function (obj) {
        $('#content').html('');
        var _self = this;
        var val = obj.val();
        if (val == '') {
            return;
        }
        if (val.startsWith("@")) {
            if (this.ajax) {
                this.ajax.abort();
            }
            this.ajax = $.ajax({
                url: Config.service + Config.search + obj.val().substr(1),
                success: function (data) {
                    if (data.status == 0) {
                        _self.renderSearch(data.data);
                    }
                }
            });
        } else {
            if (this.ajax) {
                this.ajax.abort();
            }
            this.ajax = $.ajax({
                url: Config.service + Config.searchMessage + obj.val(),
                success: function (data) {
                    if (data.status == 0) {
                        _self.renderSearch(data.data);
                    }
                }
            });
        }
    },

    renderSearch: function (data) {
        var resultObj = $('#searchResult');
        resultObj.html('');

        // for (var i = 0; i < data.users.length; i++) {
        // resultObj.append(tmpl("searchResultUser", data.users[i]));
        // }
        // for (var i = 0; i < data.tags.length; i++) {
        // resultObj.append(tmpl("searchResultTag", data.tags[i]));
        // }

        for (var i = 0; i < data.length; i++) {
            data[i].message = App.normalize(data[i].message);
            var html = tmpl("message", data[i]);
            $('#content').append(html);
        }
    },

    initSearchBox: function () {
        $(".top-bar").load("include/search-box.html");
    }

};
//setInterval(function () { alert("Hello") }, 3000);

$(document).ready(function () {
    // setInterval(Search.doSearch($('#inputSearch')), 1000);
    // Search.init();
});