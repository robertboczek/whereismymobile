<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="resetPassword well">
    <table class="resetPasswordTable">
    <tr>
      <td>
        <h2>Reset password:</h2>
      </td>
	</tr>
	</table>
	<form:form action="signup" method="post" modelAttribute="resetPassword">
		<table class="resetPasswordFormTable">
		    <tr>
				<c:if test="${not empty errorMessage}">
					<div class="alert alert-warning">${errorMessage}</div>
				</c:if>
		    </tr>
			<tr>
				<td><label for="accountId" class="col-sm-5 control-label">Email*
						</label> <form:input type="text" class="col-xs-6" id="accountId"
						placeholder="email" path="accountId"></form:input></td>
				<td><form:errors path="accountId" cssClass="formError" /></td>
			</tr>
			<tr>
				<td class="required">* Required field</td>
				<td></td>
			</tr>
			<tr>
				<td>
				    <div class="resetPasswordButtonsRow">
						<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench"></span> Reset</button> or 
						<a href="login">Login page</a>
					</div> 
				</td>
			</tr>
			<tr></tr>
		</table>
	</form:form>
</div>
