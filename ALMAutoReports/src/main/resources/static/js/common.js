$(function(){
	$('input[type="text"]').focus(function() {
		$(this).addClass("focus");
	});

	$('input[type="text"]').blur(function() {
		$(this).removeClass("focus");
	});
	
	$(".reveal").mousedown(function() {
	    $(".pwd").replaceWith($('.pwd').clone().attr('type', 'text'));
	})
	.mouseup(function() {
		$(".pwd").replaceWith($('.pwd').clone().attr('type', 'password'));
	})
	.mouseout(function() {
		$(".pwd").replaceWith($('.pwd').clone().attr('type', 'password'));
	});
	
});

$(document).ready(function() {
	$(":input").tooltip();
	$(':input').keypress(function(event) {
	    if (event.keyCode == 13) {
	        event.preventDefault();
	    }
	});

});

// calls action refreshing the partial
function refreshPartial(urlStr) {
	$.ajax({
		type : "GET",
		url : urlStr,
		cache : false,
		async : true,
		dataType : "html",
		success : function(data) {
			$("#tableDivId").html(data);

		}
	});

}

function restart(){
	
	$.ajax({
		type : "GET",
		url : 'restartContext',
		cache : false,
		async : true,
		success : function(data) {
			
			if(data=='OK'){
				$("#myModal").show();
				setInterval(function(){ 
						call();
		       }, 1000);
			
			}
             
		},
		error: function (jqXHR, exception){
			if (jqXHR.status === 0) {
                alert('Not connect.n Verify Network.');
            } else if (jqXHR.status == 404) {
                alert('Requested page not found. [404]');
            } else if (jqXHR.status == 500) {
                alert('Internal Server Error [500].');
            } else if (exception === 'parsererror') {
                alert('Requested JSON parse failed.');
            } else if (exception === 'timeout') {
                alert('Time out error.');
            } else if (exception === 'abort') {
                alert('Ajax request aborted.');
            } else {
                alert('Uncaught Error.n' + jqXHR.responseText);
            }
		}
	});
}


function call() {
	(function poll() {
		   setTimeout(function() {
		       $.ajax({ 
		    	   url: "run",
		    	   success: function(data) {
		    	   if(data=='OK'){
						window.location.href="/";
					}
		       }, complete: poll });
		    }, 30000);
		})();
	
}

//width: 600,  height: 500,  modal: true,
function restoreUsers() {

	var message = "<i class='fa fa-info' aria-hidden='true' style='font-size:35px;color:red'></i>&nbsp;&nbsp;Are you sure want to restore users database?";
	$('<div></div>').appendTo('body').html(
			'<div><h6>' + message + '</h6></div>').dialog({
		modal : true,
		title : 'Confirmation',
		zIndex : 10000,
		autoOpen : true,
		width : 'auto',
		cache : false,
		resizable : false,
		buttons : {
			Yes : function() {
				$(this).dialog("close");
				window.location.href = "restoreuser";
			},
			No : function() {
				$(this).dialog("close");
			}
		},
		close : function(event, ui) {
			$(this).remove();
		}
		
	});
}
