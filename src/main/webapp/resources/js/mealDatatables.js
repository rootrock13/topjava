const ajaxUrl = "ajax/profile/meals/";
let datatableApi;

function updateTable() {
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime",
                "render": function (dateTime, type, row) {
                    if (type === "display") {
                        return formatDateTime(dateTime);
                    }
                    return dateTime;
                }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "",
                "orderable": false,
                "render": renderEditBtn
            },
            {
                "defaultContent": "",
                "orderable": false,
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).attr("data-mealExceed", data.exceed);
        },

        "initComplete": makeEditable
    });

    // https://zzz.buzz/2016/01/13/detect-browser-language-in-javascript/
    var language = navigator.languages && navigator.languages[0] ||
        navigator.language ||
        navigator.userLanguage;

    if (language.substring(0, 2).toLocaleLowerCase() === "ru") {
        $.datetimepicker.setLocale("ru");
    }

    var startDate = $('#startDate');
    var endDate = $('#endDate');

    startDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                maxDate: endDate.val() ? endDate.val() : false
            })
        }
    });

    endDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                minDate: startDate.val() ? startDate.val() : false
            })
        }
    });

    var startTime = $('#startTime');
    var endTime = $('#endTime');

    startTime.datetimepicker({

        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                maxTime: endTime.val() ? endTime.val() : false
            })
        },
        datepicker: false
    });

    endTime.datetimepicker({

        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                minTime: startTime.val() ? startTime.val() : false
            })
        },
        datepicker: false
    });

    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });
});