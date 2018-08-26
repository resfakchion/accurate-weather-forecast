$(document).ready(function () {

    $("#file").on("change",function (e) {

        var filename = e.target.value.split('\\').pop();
        if (filename.length < 30 && window.names.indexOf(filename) === -1) {
            $("#load_file_button").attr("disabled",false).css("background-color","#4CAF50");
            $("#label_span").text(filename);
        }
        else {
            $("#label_span").text("Выбрать файл");
            $("#load_file_button").attr("disabled",true).css("background-color","#797C72");

            if (filename.length >= 30) {
                alert("Слишком длиное имя файла")
            }
            else {
                alert("Этот файл уже загружен")
            }
        }
    });
});

