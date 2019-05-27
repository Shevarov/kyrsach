<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
<div class="nb-1">
Add new user  </div>
    ${message?ifExists}
<@l.login "/registration","Check in",true/>
</@c.page>