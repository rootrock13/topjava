var ajaxUrl = "ajax/admin/users/";
var datatableApi;

function updateTable() {
    $.get(ajaxUrl, function (data) {
        fillTableWithData(data);
    });
}

function add() {
    $("#detailsForm").find(":input").val("");
    $("#editRow").modal();
}

function enable(cb) {
    var enabled = cb[0].checked;
    var id = cb[0].closest('tr').id;
    var result = "User with id = " + id + " " + (enabled ? "enabled!" : "disabled!");
    console.log(result);
    var request = $.ajax({
        type: "POST",
        url: ajaxUrl + id,
        data: "enabled=" + enabled
    });
    request.done(function () {
        successNoty(result);
        cb.closest("tr").attr("data-userEnabled", enabled);
    });
    request.fail(function () {
        cb.prop("checked", !enabled);
    });
}

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});