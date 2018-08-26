$(document).ready(function () {

    getDataArray();

    $("#filename_list_input").keyup(function () {
        $("#filename_list").empty();
        var str = $("#filename_list_input").val();
        if(str.length > 3){
            window.names.forEach(function (elem) {
                if (elem.indexOf(str) + 1){
                    $("#filename_list").append("<option value='" + elem + "'/>");
                }
            });
        }
    });
});

var data = [];
var names = [];
function getDataArray() {
    $.get("http://localhost:8080/files",function (data) {
        data.forEach(function (elem) {
            window.data.push(elem);
            window.names.push(elem.name);
        });
    })
}
