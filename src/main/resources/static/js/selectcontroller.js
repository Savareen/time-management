 $(function () {
        $("#position").change(function () {
            if ($(this).val() == 'STUDENT') {
                $("#group-id").removeAttr("disabled");
                $("#group-id").focus();
            } else {
                $("#group-id").attr("disabled", "disabled");
            }
        });
    });