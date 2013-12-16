<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${resetResult}">
	<script>
	$(document).ready(function() {  
    	window.setInterval(function() {
    		var counter = $("#counter");
    		var timeLeft = counter.html();                                
        	if(eval(timeLeft) == 0){
           		window.location= ("http://whereismymobile.com/login");                 
        	}else{              
           		counter.html(eval(timeLeft)- eval(1));
        	}
    	}, 1000); 
	});   
	</script>
</c:if>

<div class="resetPassword well">
    <table class="resetPasswordTable">
    <tr>
      <td>
        <h2>Reset password:</h2>
      </td>
	</tr>
	</table>
	<form:form action="resetPassword" method="post" modelAttribute="resetPassword">
		<table class="resetPasswordFormTable">
		    <tr>
		    	<c:if test="${not empty infoMessage}">
					<div class="alert alert-info">${infoMessage}</div>
				</c:if>
				<c:if test="${not empty errorMessage}">
					<div class="alert alert-warning">${errorMessage}</div>
				</c:if>
				<c:if test="${resetResult}">
					<div class="alert alert-info">
						Password successfully changed. Redirecting to login page in <span id="counter">5</span> seconds.
					</div>
				</c:if>
		    </tr>
		    <c:if test="${empty resetPassword.code}">
				<tr>
					<td><label for="accountId" class="col-sm-5 control-label">Email*
							</label> <form:input type="text" class="col-xs-6" id="accountId"
							placeholder="email" path="accountId"></form:input></td>
					<td><form:errors path="accountId" cssClass="formError" /></td>
				</tr>
			</c:if>
			<c:if test="${not empty resetPassword.code}">
			   
				<tr>
					<td><label for="accountId" class="col-sm-5 control-label">Email*
							</label> <form:input type="text" class="col-xs-6" id="accountId"
							placeholder="email" path="accountId" readonly="true"></form:input></td>
					<td><form:errors path="accountId" cssClass="formError" /></td>
					<form:hidden id="code" path="code"></form:hidden>
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
			</c:if>
			<tr>
				<td class="required">* Required field</td>
				<td></td>
			</tr>
			<tr>
				<td colspan="2">
				    <div class="resetPasswordButtonsRow">
						<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench"></span> Reset</button>
						<a href="login"><button type="button" class="btn">Login page</button></a>
					</div> 
				</td>
			</tr>
			<tr></tr>
		</table>
	</form:form>
</div>
