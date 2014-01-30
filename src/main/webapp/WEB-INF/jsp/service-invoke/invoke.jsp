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
      <h1>Service Invoker </h1>
      <form method="post" action="/register/add">
      <div class="reg_section personal_info">
      <h3>User OAuth Information</h3>
      <label>
          Logged In User Id :
        </label>
      <input type="text" name="name" value="${userId}" placeholder="Your Desired Username">
      <label>
          Logged In User AccessToken :
        </label>
      <input type="text" name="email" value="${accessToken}" placeholder="Your E-mail Address">
      </div>   
      <div class="reg_section password reg_section personal_info">
         <h3>User Detail Service URL</h3>
        
            <input type="text" name="url" value="http://localhost:8080/user/${userId}?access_token=${accessToken}&userId=${userId}" placeholder="" id="userServiceUrl">      
      </div>
      <div class="reg_section password">
      <h3>Response </h3>
      
      <textarea name="address" id="userResponse">Response Will Appear here</textarea>
      </div>   
      <p class="terms">
        <label>
          
        </label>
      </p>
      <p class="submit"><input type="button" name="commit" value="Invoke Service" id="userServiceInvokeButton"></p>
      
      
      <br>
      <br>
       <div class="reg_section password reg_section personal_info">
               <h3>User Sessions Detail Service URL</h3>

            <input type="text" name="url" value="http://localhost:8080/session-details/" placeholder="" id="sessionServiceUrl">      
      </div>
      <div class="reg_section password">
      <h3>Response </h3>
      
      <textarea name="address" id="sessionResponse">Response Will Appear here</textarea>
      </div>   
      <p class="terms">
        <label>
          
        </label>
      </p>
      <p class="submit"><input type="button" name="commit" value="Invoke Service" id="sessionServiceInvokeButton"></p>
      </form>
    </section>
  </div>
  
  <script type="text/javascript">
$(function() {
	$("#userServiceInvokeButton").click(function(){
		
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
				}

			},
			error : function() {
				alert("Error At Server");
			}
		});
	});
	
	
});
</script>

</body>
</html>