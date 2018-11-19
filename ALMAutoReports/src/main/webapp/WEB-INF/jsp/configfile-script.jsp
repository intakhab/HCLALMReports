<script type="text/javascript">
var textVal;

function confirmMsg(msg){
	
	var message="<i class='fa fa-warning aria-hidden='true' style='font-size:35px;color:red'></i>&nbsp;&nbsp;<span class='error'>You have changed "+msg+" timing !!! <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; HCLALM will be re-started automcatically to get the latest value!<span>";
	   $('<div></div>').appendTo('body').html('<div><h6>'+message+'</h6></div>')
	    .dialog({
	        modal: true, 
	        title: 'Confirmation',
	        zIndex: -1, 
	        autoOpen: true,
	        width: 'auto', 
	        resizable: false,
	        buttons: {
	            Yes: function () {
	            	$("#cronHit").val(true);
	                document.getElementById("configForm").submit();
	            },
	            No: function (){
	            	//ob.val(textVal);
	                $(this).dialog("close");
	            }
	        }
	    });
	
}
/****
 * This will fire cron error
 */
function cronError(){
	var message="<i class='fa fa-info-circle' aria-hidden='true' style='font-size:35px;color:red'></i>&nbsp;&nbsp;Input Cron Value is incorrect Please put one sapce in between characters like.. * * * * * *  ";
	   $('<div></div>').appendTo('body').html('<div><h6>'+message+'</h6></div>')
	    .dialog({
	        modal: true, 
	        title: 'Information ',
	        zIndex: -1,
	        width: 'auto', 
			cache : false,
	        autoOpen: true,
	        buttons: { 
	           OK: function () {
                $(this).dialog("close");

               }
	        }
	    });
	
}

/***
 * Only consider number
 */
function validateNumber(event) {
    var key = window.event ? event.keyCode : event.which;
    if (event.keyCode === 8 || event.keyCode === 46) {
        return true;
    } else if ( key < 48 || key > 57 ) {
        return false;
    } else {
    	return true;
    }
};		
function chckTab(id){
	$("#fileConfigId1").removeClass("active");
	$("#almConfigId1").removeClass("active");
	$("#"+id).addClass("active");
	$("#tabId").val(id);
 };

$(document).ready(function() {
    $('.dig').keypress(validateNumber);//Number consider
    //Cron validation fire
	$(".msgc" ).change(function() {
        var idx=$(this).attr("id");
		$.ajax({
			type : "GET",
			url : 'cronvalidator?param='+$(this).val(),
			cache : false,
			async : true,
			success : function(data) {
				if(data==false){
					$("#"+idx).css("background-color", "red");
					 cronError();
	                 $(":submit").attr("disabled", true);
				}else{
				    $(":submit").removeAttr("disabled");
					$("#"+idx).css("background-color", "white");
				}
	             
			}
		});
	});
	$("#tab2").addClass("active");
	$("#tab1").removeClass("active");
	$("#re-loadId").click(function(e) {
		window.location.href="configfile";

	});
    $("#emailCheckedId").click(function(e) {
		if ($('#emailCheckedId').prop('checked')) {
			$('#emailCheckedTextAreaId').show();
			$('#toWhomEmailId').prop('required',true);
		} else {
			$('#emailCheckedTextAreaId').hide();
			$('#toWhomEmailId').val("");
		    $('#toWhomEmailId').prop('required',false);
		}
	});
    
    $("#autoPilotId").click(function(e) {
		if ($('#autoPilotId').prop('checked')) {
			$('#isAutoPilotDiv').show();
			$('#autoPilotCronId').prop('required',true);
		} else {
			$('#isAutoPilotDiv').hide();
		    $('#autoPilotCronId').prop('required',false);
		}
	});
    
    
    $("#enableAlmReportId").click(function(e) {
		if ($('#enableAlmReportId').prop('checked')) {
			$('#isAlmDiv').show();
			$('#almConfigG1Id').prop('required',true);
			$('#almConfigG2Id').prop('required',true);
		} else {
			$('#isAlmDiv').hide();
			$('#almConfigG1Id').prop('required',false);
			$('#almConfigG1Id').prop('required',false);
		}
	});
    
    
    
    
    $('#submitButtonId').click(function () {
        $('input:invalid').each(function () {
            // Find the tab-pane that this element is inside, and get the id
            var $closest = $(this).closest('.tab-pane');
            var id = $closest.attr('id');
            // Find the link that corresponds to the pane and have it show
            $('.nav a[href="#' + id + '"]').tab('show');
            $("#fileConfigId1").removeClass("active");
			$("#almConfigId1").removeClass("active");
			$("#"+id+"1").addClass("active");
				// Only want to do it once
				return false;
			});
		});
});//End

//
function isCronChange(){
	var changeFound=false;
	
	if($("#autoPilotTimeId").val()!=$("#autoPilotCronId").val()){
		changeFound=true; 
		confirmMsg(" Auto Pilot ")
	}
	if($("#almReportsGroupId1").val()!=$("#almConfigG1Id").val()){
		changeFound=true; 
		confirmMsg(" ALM Report ")
	}
	
	if($("#almConfigG2Id").val()!=$("#almReportsGroupId2").val()){
		changeFound=true; 
		confirmMsg(" ALM Report ")
	}
	
   return changeFound;
}
/***
 * 
 */
$( "#configForm" ).submit(function( event ) {
	   event.preventDefault();
	   if(isCronChange()){
		   return;
	   }
   var message="<i class='fa fa-info' aria-hidden='true' style='font-size:35px;color:red'></i>&nbsp;&nbsp;Are you sure want to save the Settings configuration?";
	     $('<div></div>').appendTo('body').html('<div><h6>'+message+'</h6></div>')
    .dialog({
        modal: true, title: 'Confirmation', zIndex: 10000, autoOpen: true,
        width: 'auto', 
        resizable: false,
        buttons: {
            Yes: function () {
                $(this).dialog("close");
            	$("#cronHit").val(false);
                document.getElementById("configForm").submit();

            },
            No: function () {                           		                      
                $(this).dialog("close");
            }
        },
        close: function (event, ui) {
            $(this).remove();
        }
    });
  });
  //
$(window).bind("load", function() {
	if($("#cronHit").val()=="true"){
		restart();
	}
	var tt='${param.tabId}';
	if(tt!=""){
		var tabsId= tt.substring(0, tt.length - 1);
        $("a[href='#"+tabsId+"']").trigger("click");
	}
	//
	
	if($("#checkBoxId").val().trim()==true || $("#checkBoxId").val().trim()=="true"){
	     $('#emailCheckedTextAreaId').show();
	     $('#emailCheckedId').prop('checked', true);
		 $('#toWhomEmailId').prop('required',true);

	}else{
   	     $('#emailCheckedTextAreaId').hide();
   	     $('#emailCheckedId').prop('checked' ,false)
		 $('#toWhomEmailId').prop('required',false);
	}
	
	if($("#autoPilotId").val()=="true"){
		if ($('#autoPilotId').prop('checked')) {
			$('#isAutoPilotDiv').show();
			$('#autoPilotCronId').prop('required',true);
		} else {
			$('#isAutoPilotDiv').hide();
		    $('#autoPilotCronId').prop('required',false);
		}
	}
	
	if($("#enableAlmReportId").val()=="true"){
		if ($('#enableAlmReportId').prop('checked')) {
			$('#isAlmDiv').show();
			$('#almConfigG1Id').prop('required',true);
			$('#almConfigG2Id').prop('required',true);
		} else {
			$('#isAlmDiv').hide();
		    $('#almConfigG1Id').prop('required',false);
		    $('#almConfigG2Id').prop('required',false);
		}
	}

}); 
</script>