var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

function clearFilter() {
    $("#filterForm").find(":input").val("");
    updateTable();
}


function updateTable() {
    var filterForm = $("#filterForm");
    var request = $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: filterForm.serialize()
    });
    request.done(function (data) {
        fillTableWithData(data);
        successNoty("Table was updated successfully!");
    })

}

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