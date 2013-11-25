<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<link rel="shortcut icon" href="<c:url value="resources/images/mda.ico" />" />
<link href="<c:url value="resources/bootstrap/css/bootstrap.css"/>" rel="stylesheet">
<link href="<c:url value="resources/css/custom.css" />" rel="stylesheet">

<script type="text/javascript" src="<c:url value="resources/bootstrap/js/bootstrap.js"/>"></script>
</head>
<body>
    <div class="container">
    	<tiles:insertAttribute name="header" ignore="true" />
    	<tiles:insertAttribute name="body" ignore="true" />
    	<tiles:insertAttribute name="footer" ignore="true" />
    </div>
</body>
</html>