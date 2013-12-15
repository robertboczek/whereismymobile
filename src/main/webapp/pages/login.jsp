<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="login well">
    <table class="loginLabelTable">
    <tr>
      <td>
        <h2>Log in</h2>
      </td>
      <td class="loginWithFb">
        <a href="http://www.facebook.com/dialog/oauth/?client_id=374675276002381&redirect_uri=http://ec2-50-16-158-177.compute-1.amazonaws.com:8080/whereismymobile/fbLogin&scope=email,read_friendlists&state=RANDOM_NUMBER">
	  	    <img src="resources/images/fbLogin.png"/>
	  	</a>
	  </td>
	</tr>
	</table>
	<form:form action="login" method="POST" modelAttribute="login">
		<table class="loginFormTable">
		    <tr>
				<c:if test="${not empty errorMessage}">
					<div class="alert alert-warning">${errorMessage}</div>
				</c:if>
		    </tr>
			<tr>
				<td><label for="email" class="col-sm-5 control-label">Email
						</label> <form:input type="text" class="col-xs-6" id="email"
						placeholder="Email" path="email"></form:input></td>
				<td><form:errors path="email" cssClass="formError" /></td>
			</tr>
			<tr>
				<td><label for="password" class="col-sm-5 control-label">Password</label>
					<form:input type="password" class="col-xs-6" id="password"
						placeholder="Password" path="password"></form:input></td>
				<td><form:errors path="password" cssClass="formError" /></td>
			</tr>
			<tr>
				<td>
				    <div class="loginButtonRow">
				        <div>
							<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-arrow-right"></span> Log in</button> or 
							<a href="signup">Sign up</a>
							<c:if test="${not empty errorMessage}">
						  		<a href="resetPassword">Forgot your password?</a>
							</c:if>
						</div>
					</div> 
				</td>
			</tr>
			<tr></tr>
		</table>
	</form:form>
</div>
