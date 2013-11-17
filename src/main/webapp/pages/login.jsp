<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<meta charset="utf-8">
<title>where-is-my-mobile</title>
<link rel="shortcut icon" href="<c:url value="resources/images/mda.ico" />" />
<link href="<c:url value="/resources/bootstrap/css/bootstrap.css"/>" rel="stylesheet">
<link href="<c:url value="resources/css/custom.css" />" rel="stylesheet">
<script type="text/javascript" src="<c:url value="resources/bootstrap/js/bootstrap.js"/>"></script>
</head>
<body>
	<div class="welcome">
	    <c:if test="${not empty errorMessage}">
	      <div class="alert alert-warning">${errorMessage}</div>
	    </c:if>
		<form:form action="login" method="POST" modelAttribute="login">
			<table>
				<tr>
					<td><label for="deviceId" class="col-sm-5 control-label">Device
							Id</label> <form:input type="text" class="col-xs-6" id="deviceId"
						placeholder="Device no" path="deviceId"></form:input>
						</td>
						<td><form:errors path="deviceId" cssClass="label label-danger" /></td>
				</tr>
				<tr>
					<td><label for="password" class="col-sm-5 control-label">Password</label>
						<form:input type="password" class="col-xs-6" id="password"
						placeholder="Password" path="password"></form:input>
				    </td>
				    <td><form:errors path="password" cssClass="label label-danger" /></td>
				</tr>
				<tr>
					<td>
						<button type="submit" class="btn btn-primary">Sign in</button>
						<label> <form:checkbox path="rememberMe"></form:checkbox> Remember me
							</label>
					</td>
				</tr>
			</table>
		</form:form>
	</div>
</body>
</html>
