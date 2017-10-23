

function api_register() {
    var name = $("#name").val();
    var password = $("#password").val();
    var repassword = $("#repassword").val();
    var surname = $("#secondame").val();
    var login = $("#username").val();
    var email = $("#email").val();
    var fb = $("#fb").val();
    $.get( "./invoker/business.DB/register", {"name":name, "password":password, "repassword": repassword, "surname": surname, "login": login, "email": email, "fb": fb},
        function( data ) {
        if(data.dto.responseObject){
            alert(data.dto.responseObject);
        }else{
            $(location).attr('href', 'index.html')
        }
    }, "json" );
}

function api_login() {
    var login = $("#login").val();
    var password = $("#password").val();

    $.get( "./invoker/business.DB/login", {"login":login, "password":password},
        function( data ) {
            if(data.dto.responseObject){
                $(location).attr('href', 'index.html')
            }else{
                $(location).attr('href', 'index.html')
            }
        }, "json" );
}

function api_listProblems() {
    $.get( "templates/problem.html", function(date){
        $.template( "problemtemplate", date );


        $.get( "./invoker/business.Classifieds/getClassifieds",
            function( data ) {
                if(data.dto.responseObject){
                    var viewList = $.map(data.dto.responseObject, function (val, i) {
                        var color;
                        if(val.type == "building"){
                            val.type = "build";
                            val.id = "building";
                        }
                        if(val.type == "writing"){
                            val.type = "border_color";
                            val.id = "writing";
                        }
                        if(val.type == "learning"){
                            val.type = "chat";
                            val.id = "comment";
                        }
                        return val;
                    });
                    $.tmpl("problemtemplate", viewList).appendTo("#problems");
                }else{
                    $(location).attr('href', 'index.html')
                }
            }, "json" );



    }, "html")
}



function api_logout() {
    $.get( "./invoker/business.DB/logOut",
        function( data ) {
            $(location).attr('href', 'index.html')
        }, "json" );
}

function api_logged() {
    $.get( "./invoker/business.Profile/getMyProfile",
        function( data ) {
            if(data.dto.responseObject) {
                api_up();
            }else{
                api_upNotLogged();
                // $(location).attr('href', 'index.html');
            }
        }, "json" );
}

function api_loggedRight() {
    $.get( "./invoker/business.Profile/getMyProfile",
        function( data ) {
            if(data.dto.responseObject) {
                $.get( "templates/logout.html", function(date){
                    $.template( "logout", date );
                    $.tmpl("logout", data.dto.responseObject).appendTo("#logged");

                }, "html")
            }else{

            }
        }, "json" );
}

function api_up() {
    $.get( "templates/up.html", function(date){
        $.template( "up", date );

        $.get( "./invoker/business.Profile/getMyProfile",
            function( data ) {
                if(data.dto.responseObject) {
                    $.tmpl("up", data.dto.responseObject).appendTo("#up");
                }else{

                }
            }, "json" );

    }, "html")
}

function api_upNotLogged() {
    $.get( "templates/upNotLogged.html", function(date){
        $.template( "upNotLogged", date );
        $.tmpl("upNotLogged").appendTo("#up");
    }, "html")
}

function toChallenge() {
    $(location).attr('href', 'new_challenge.html');
}

function api_addChallenge() {
    var category = $("#category").val();
    var title = $("#title").val();
    var descriptiom = $("#description").val();

    if(category == "Roboty fizyczne"){
        category = "building";
    }
    if(category == "Zadania Domowe"){
        category = "writing";
    }
    if(category == "Pomoc w nauce"){
        category = "learning";
    }

    $.get( "./invoker/business.Classifieds/addClaaifieds", {"title":title, "text":descriptiom, "type": category},
        function( data ) {
            if(data.dto.responseObject == "jp2gmd"){
                $(location).attr('href', 'index.html');
            }else{
                alert(data.dto.responseObject);
            }
        }, "json" );
}

var help;

function to_Problem() {
    var help = $("#id");
    $(location).attr('href', 'challenge_view.html');
}

function api_showProblem() {
    // alert(help);
    var movies = [
        { title: "healp"}
    ];
    $.get( "templates/classified.html", function(date){
        $.template( "classified", movies );
        $.tmpl("classified").appendTo("#problem");
    }, "html")
}

function my_profile() {
    $.get( "templates/user.html", function(date){
        $.template( "profile", date );

        $.get( "./invoker/business.Profile/getMyProfile",
            function( data ) {
                if(data.dto.responseObject) {
                    $.tmpl("profile", data.dto.responseObject).appendTo("#user");
                }else{

                }
            }, "json" );

    }, "html")


}

function my_stats() {
    $.get( "templates/stats.html", function(date){
        $.template( "stat", date );

        $.get( "./invoker/business.Profile/getMyProfile",
            function( data ) {
                if(data.dto.responseObject) {
                    $.tmpl("stat", data.dto.responseObject).appendTo("#stats");
                }else{

                }
            }, "json" );

    }, "html")
}

function my_problems(){
    $.get( "templates/myProblems.html", function(date){
            $.template( "problem", date );

            $.get( "./invoker/business.Profile/showMyClassifieds",
                function( data ) {
                    if(data.dto.responseObject) {
                        $.tmpl("problem", data.dto.responseObject).appendTo("#myProblems");
                    }else{

                    }
                }, "json" );

        }, "html")}

function specific_problems(category) {

    $.get( "templates/problem.html", function(date){
        $.template( "problemtemplate", date );

        $("#problems").empty();
        $.get( "./invoker/business.Classifieds/getSpecificClassifieds",{"type": category},
            function( data ) {
                if(data.dto.responseObject){
                    var viewList = $.map(data.dto.responseObject, function (val, i) {
                        var color;
                        if(val.type == "building"){
                            val.type = "build";
                            val.id = "building";
                        }
                        if(val.type == "writing"){
                            val.type = "border_color";
                            val.id = "writing";
                        }
                        if(val.type == "learning"){
                            val.type = "chat";
                            val.id = "comment";
                        }
                        return val;
                    });
                    $.tmpl("problemtemplate", viewList).appendTo("#problems");
                }else{
                    $(location).attr('href', 'index.html')
                }
            }, "json" );



    }, "html")
}

function api_rank() {
    $.get( "templates/rank.html", function(date){
        $.template( "rank", date );

        $.get( "./invoker/business.Rank/rank",
            function( data ) {
                if(data.dto.responseObject) {
                    $.tmpl("rank", data.dto.responseObject).appendTo("#rank");
                }else{

                }
            }, "json" );

    }, "html")
}

function api_helped(id, type) {
     var user = $("#" + id).val();
    $.get( "./invoker/business.Profile/helped", {"id":id, "subject":type, "user":user},
        function( data ) {
            if(data.dto.responseObject){
                $(location).attr('href', 'index.html')
            }else{
                $(location).attr('href', 'index.html')
            }
        }, "json" );
}
