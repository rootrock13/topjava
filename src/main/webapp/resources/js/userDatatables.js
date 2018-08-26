var ajaxUrl = "ajax/admin/users/";
var datatableApi;

function add() {
    $("#detailsForm").find(":input").val("");
    $("#editRow").modal();
}

function enable(cb) {
    console.log("User with id = " + cb.closest('tr').id + (cb.checked ? " enabled!" : " disabled!"));
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