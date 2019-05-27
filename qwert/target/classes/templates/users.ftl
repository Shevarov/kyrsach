<#import "parts/common.ftl" as c>

<@c.page>

<form name="tabtext" method="post" action="/users">

    <input class="btn btn-primary btn-lg px-5 " type="submit" id="sendbtn" name="submit" value="Block"/>

    <input class="btn btn-primary btn-lg px-5 ml-5" type="submit" id="sendbtn" name="submit" value="Unlock"/>

    <input class="btn btn-primary btn-lg px-5 ml-5" type="submit" id="sendbtn" name="submit" value="Delete"/>

    <div class=" form-text  mt-3"><h4>List of users</h4></div>
    <div class="  mt-3">
        <table   >
            <thead>
            <tr >
                <th><label>
                    <input class="checkbox1 " id="checkbox" type="checkbox" name="checkbox" onclick="checking(this)" >
                    Select all</label>
                </th>
                <th>ID</th>
                <th>Username</th>
                <th>Status</th>
                <th>Role</th>
            </tr>
            </thead>
            <tbody>
    <#list listOfUsers?ifExists as listOfUser>
    <tr>
        <td width="100">
            <div>
                <input class="checkbox" type="checkbox" name="checkboxes" id="${listOfUser.id}" value="${listOfUser.id}" >
            </div>
        </td>
        <td width="50">${listOfUser.id}</td>
        <td width="150">${listOfUser.username}</td>
        <td width="150">${listOfUser.status}</td>
        <td width="150"> <#list listOfUser.roles as role>${role}<#sep>, </#list></td>
        <td ><a href="/users/${listOfUser.id}">edit</a></td>
    </tr>
    <#else >
No users
    </#list>
            </tbody>
        </table>
    </div>
    <script>
        function checking(button) {
            let checkboxes = document.getElementsByClassName('checkbox');
            for (let index = 0; index < checkboxes.length; index++) {
                if(button.checked===true)
                    checkboxes[index].checked=true;
                else
                    checkboxes[index].checked=false;
            }
        }
    </script>
</form>
</@c.page>