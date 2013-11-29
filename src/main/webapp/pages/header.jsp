<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="header">
    <div class="dropdown userDetails">
        Welcome: <a id="userDetailsDropDown" role="button" data-toggle="dropdown" href="#">${user.email} <span class="caret"></span></a>
        <ul class="dropdown-menu" role="menu" aria-labelledby="userDetailsDropDown">
  			<li role="presentation">
				<a href="account" tabindex="-1" role="menuitem">my account</a>
			</li>
  			<li class="divider" role="presentation"></li>
    		<li role="presentation">
				<a href="logout" tabindex="-1" role="menuitem">logout</a>
			</li>
  		</ul>
    </div>
  
</div>
