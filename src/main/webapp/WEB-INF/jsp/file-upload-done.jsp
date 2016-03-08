<!DOCTYPE html>
<%@ page isELIgnored="false" %>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Service Invoker</title>
  <link rel="stylesheet" href="/static/css/style.css">
  <!--[if lt IE 9]><script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
    <script type="text/javascript" src="/static/js/jquery-2.1.0.min.js"></script>
  
</head>

<body>
  <div class="container">
    <section class="register">
      <h1>Upload result</h1>
      ${valid}
      
    </section>
    <div id="attckStatus" style="color: #FFF;">
    <div id="attackinnr"></div>
      <div class="ajaxLoader2" style="display: none; margin: 0 700px; bottom:0px;"><img style="" src="/static/images/ajax-loader2.gif"> </div>
    
    </div>
  </div>
  
  <div class="ajaxLoader" style="display: none; position: absolute; margin: 0 649px; top:400px;"><img style="" src="/static/images/ajax-loader.gif"> </div>
  
  <script type="text/javascript">
$(function() {
	$("#userServiceInvokeButton").click(function(){
		
		$(".ajaxLoader").show();
		$.ajax({
			url : $("#userServiceUrl").val(),
			type : "get",
			data : '',
			dataType : "json",
			success : function(data) {
				if (data.error == undefined) {
					$("#userResponse").val(JSON.stringify(data));

				} else {
					$("#userResponse").val(JSON.stringify(data));		
					$(".loginLink").show();
				}
				$(".ajaxLoader").hide();

			},
			error : function() {
				alert("Error At Server");
				$(".ajaxLoader").hide();

			}
		});
	});
	
	
	
	$("#attackerButton").click(function(){
		var r = confirm("This will Create DDOS Attack on server !!!, The Current Logged in user will be blocked. Are you sure to create DDOS Attack with current user??");
		if (r == true)
		  {
		  createDDOSAttack($(this).data("id"),$(this).data("accessToken"))
		  }
		else
		  {
		  }
	});
	
	
	
});


			function createDDOSAttack(id, accessToken) {

				$(".ajaxLoader2").show();

				makeAjaxCall(1);


			}

			function makeAjaxCall(cur) {
				var resp;
				var index=cur+1;
				$.ajax({
					url : $("#userServiceUrl").val(),
					type : "get",
					data : '',
					dataType : "json",					
					success : function(data) {
						$(".ajaxLoader2").focus();
						$("html, body").animate({ scrollTop: $(document).height() }, 1000);
						if (data.error == undefined) {
							$("#attackinnr").append("<br>" +"--------------"+"<br>"+"Request Number "+index+"<br>"+JSON.stringify(data)+"<br><br>");
							makeAjaxCall(index);
						} else {
							alert("User is blocked on request "+index);
							$(".ajaxLoader2").hide();
							return false;
						}

					},
					error : function() {
						alert("Error At Server");
						$(".ajaxLoader2").hide();

					}

				});
				return resp;
			}
		</script>

</body>
</html>