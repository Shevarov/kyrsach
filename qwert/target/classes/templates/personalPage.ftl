<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>
    <#if user?? >
        <#if user.getId()==userId || isAdmin>

<form action="/personalPage" method="post">

    <div>
        User name
        <input type="text" name="username" value="${user.username}">
    </div>

    <div>
        Email
        <input type="text" name="username" value="${user.email}">
    </div>

    <input type="hidden" value="${user.id}" name="userId">
    <input class="btn btn-primary btn-lg px-5" type="submit" id="sendbtn" name="submit" value="Save"/>

    <div class="pt-5">
        <input class="btn btn-primary btn-lg px-5 " type="submit" id="sendbtn" name="submit" value="Create"/>

        <input class="btn btn-primary btn-lg px-5 ml-5" type="submit" id="sendbtn" name="submit" value="Redaction"/>

        <input class="btn btn-primary btn-lg px-5 ml-5" type="submit" id="sendbtn" name="submit" value="Open for read"/>

        <input class="btn btn-primary btn-lg px-5 ml-5" type="submit" id="sendbtn" name="submit" value="Delete"/>
    </div>

    <div class=" form-text  mt-3"><h4>List of compositions</h4></div>
    <div class="  mt-3">
        <table border="1">
            <thead>
            <tr>
                <th>
                    Select
                </th>
                <th>ID</th>
                <th>Title</th>
                <th>Genre</th>
                <th>Short Description</th>
                <th>Tags</th>
            </tr>
            </thead>
            <tbody>
    <#list listOfCompositions as composition>
    <tr>
        <td width="100">
            <div>
                <input class="checkbox" type="checkbox" name="checkboxes" id="${composition.id}" value="${composition.id}" onclick="checking(this)">
            </div>
        </td>
        <td width="50">${composition.id}</td>
        <td width="200">${composition.title}</td>
        <td width="200">${composition.genre}</td>
        <td width="400">${composition.shortDescription}</td>
        <td width="200">${composition.tag}</td>
    </tr>
    </#list>
            </tbody>
        </table>
    </div>
    <script>
        function checking(button) {
            let checkboxes = document.getElementsByClassName('checkbox');
            var dop;
            dop = button.checked === true;
            for (let index = 0; index < checkboxes.length; index++) {
                    checkboxes[index].checked=false;
            }
            if (dop===true)
            button.checked=true;
            else button.checked=false;
        }
    </script>
</form>
        </#if>
    </#if>
</@c.page>