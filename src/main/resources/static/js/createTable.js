$(document).ready(function () {
    $("#show_table_button").click(function () {

        var filename = $("#filename_list_input").val();
        if (names.indexOf(filename) !== -1){
            $.get("http://localhost:8080/fileStat?name="+filename,function (data) {

                data.avgWordInLines = Math.round(data.avgWordInLines * 10)/10;
                var row = "<tr>";
                for (var elem in data){
                    row += "<td>" + data[elem] + "</td>";
                }
                row += "</tr>";

                $("#data_table").find("tr:not(:first)").remove();
                $("#data_table").append(row);
            })
        }
        else {
            $("#data_table").find("tr:not(:first)").remove();
            window.data.forEach(function (obj) {
                obj.avgWordInLines = Math.round(obj.avgWordInLines * 10)/10;
                var row = "<tr>";
                for (var elem in obj){
                    row += "<td>" + obj[elem] + "</td>";
                }
                row += "</row>";

                $("#data_table").append(row);
            })
        }
    });
});
