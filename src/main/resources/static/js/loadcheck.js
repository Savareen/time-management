$(document).ready( function () {
            if ($("#position").val() == 'STUDENT') {
                $("#group-id").removeAttr("disabled");
                $("#group-id").focus();
            } else {
                $("#group-id").attr("disabled", "disabled");
            }
    });