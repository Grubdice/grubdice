<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<body>

<h1>OpenID Sample Home Page</h1>

<p>
    Welcome <sec:authentication property='principal.username'/>!
</p>

<h3>Technical Information</h3>

<p>
    Your principal object is....: <%= request.getUserPrincipal() %>
</p>

<p><a href="/logout">Logout</a>
</body>
</html>
