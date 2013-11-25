<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="fb-root"></div>
<div class="welcome">
	<c:if test="${not empty errorMessage}">
		<div class="alert alert-warning">${errorMessage}</div>
	</c:if>
	<form:form action="login" method="POST" modelAttribute="login">
		<table>
			<tr>
				<td><label for="email" class="col-sm-5 control-label">Email
						Id</label> <form:input type="text" class="col-xs-6" id="email"
						placeholder="Email" path="email"></form:input></td>
				<td><form:errors path="email" cssClass="label label-danger" /></td>
			</tr>
			<tr>
				<td><label for="password" class="col-sm-5 control-label">Password</label>
					<form:input type="password" class="col-xs-6" id="password"
						placeholder="Password" path="password"></form:input></td>
				<td><form:errors path="password" cssClass="label label-danger" /></td>
			</tr>
			<tr>
				<td>
					<button type="submit" class="btn btn-primary">Sign in</button> 
				    <a href="http://www.facebook.com/dialog/oauth/?client_id=374675276002381&redirect_uri=http://ec2-50-16-158-177.compute-1.amazonaws.com:8080/whereismymobile/fbLogin&scope=email,read_friendlists&state=RANDOM_NUMBER">
	  			        <img src="resources/images/loginWithFb.png" style="cursor:pointer;"/>
	  			    </a>
				</td>
			</tr>
		</table>
	</form:form>
</div>
