<div class="newUser well">
    <table class="registerNewUserTable">
    <tr>
      <td>
        <h2>Register new user:</h2>
      </td>
	</tr>
	</table>
	<form:form action="signup" method="POST" > <!-- modelAttribute="login" -->
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
				<td><label for="repeatPassword" class="col-sm-5 control-label">Repeat password</label>
					<form:input type="password" class="col-xs-6" id="repeatPassword"
						placeholder="Repeat password" path="password"></form:input></td>
				<td><form:errors path="repeatPassword" cssClass="formError" /></td>
			</tr>
			<tr>
				<td>
				    <div class="loginButtonRow">
						<button type="submit" class="btn btn-primary">Register</button>
					</div> 
				</td>
			</tr>
			<tr></tr>
		</table>
	</form:form>
</div>
