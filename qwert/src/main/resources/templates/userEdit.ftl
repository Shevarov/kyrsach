<#import "parts/common.ftl" as c>

<@c.page>
User editor

<form action="/userEdit" method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> User Name :</label>
        <div class="col-sm-4">
            <input type="text" name="username" class="form-control" value="${user.username}"/>
        </div>
    </div>
    <#list roles as role>
        <div>
            <label>
                <input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked","")}>${role}
            </label>
        </div>
    </#list>
    <input type="hidden" value="${user.id}" name="userId">
    <input class="btn btn-primary btn-lg px-5" type="submit" id="sendbtn" name="submit" value="Save"/>
</form>
</@c.page>