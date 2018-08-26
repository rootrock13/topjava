var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

function add() {
    var $mealForm = $("#detailsForm");
    $mealForm.find(":input").val("");
    $mealForm.find("#calories").val(500);
    $("#editRow").modal();
}

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
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
                "desc"
            ]
        ]
    });
    makeEditable();
});