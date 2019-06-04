<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>
    <#if user?? >
    <#if user.getId()==userId || isAdmin>
User editor

<form action="/userEdit" method="post">

    <div>
        <label>
            <input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked","")}>${role}
        </label>

    </div>
    <input type="hidden" value="${user.id}" name="userId">
    <input class="btn btn-primary btn-lg px-5" type="submit" id="sendbtn" name="submit" value="Save"/>

</form>
    </#if>
    </#if>
</@c.page>