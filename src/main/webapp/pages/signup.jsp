<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="newUser well">
    <table class="registerNewUserTable">
    <tr>
      <td>
        <h2>Register new user:</h2>
      </td>
	</tr>
	</table>
	<form:form action="signup" method="post" modelAttribute="newUser">
		<table class="newUserFormTable">
		    <tr>
				<c:if test="${not empty errorMessage}">
					<div class="alert alert-warning">${errorMessage}</div>
				</c:if>
		    </tr>
			<tr>
				<td><label for="email" class="col-sm-5 control-label">Email*
						</label> <form:input type="text" class="col-xs-6" id="email"
						placeholder="email" path="email"></form:input></td>
				<td><form:errors path="email" cssClass="formError" /></td>
			</tr>
			<tr>
				<td><label for="password" class="col-sm-5 control-label">Password*</label>
					<form:input type="password" class="col-xs-6" id="password"
						placeholder="password" path="password"></form:input></td>
				<td><form:errors path="password" cssClass="formError" /></td>
			</tr>
			<tr>
				<td><label for="repeatPassword" class="col-sm-5 control-label">Repeat password*</label>
					<form:input type="password" class="col-xs-6" id="repeatPassword"
						placeholder="repeat password" path="repeatPassword"></form:input></td>
				<td><form:errors path="repeatPassword" cssClass="formError" /></td>
			</tr>
			<tr>
				<td><label for="firstName" class="col-sm-5 control-label">First name*
						</label> <form:input type="text" class="col-xs-6" id="firstName"
						placeholder="first name" path="firstName"></form:input></td>
				<td><form:errors path="firstName" cssClass="formError" /></td>
			</tr>
			<tr>
				<td><label for="lastName" class="col-sm-5 control-label">Last name*
						</label> <form:input type="text" class="col-xs-6" id="lastName"
						placeholder="last name" path="lastName"></form:input></td>
				<td><form:errors path="lastName" cssClass="formError" /></td>
			</tr>
			<tr>
				<td class="required">* Required field</td>
				<td></td>
			</tr>
			<tr>
				<td colspan="2">
				    <div class="newUserButtonsRow">
						<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-disk"></span> Create</button> or 
						<a href="login"><button type="button" class="btn">Login page</button></a>
					</div> 
				</td>
			</tr>
			<tr></tr>
		</table>
	</form:form>
</div>
