<#import "parts/common.ftl" as c>
<@c.page>

<form name="tabtext" method="post" action="/users">

    <input class="btn btn-primary btn-lg px-5 " type="submit" id="sendbtn" name="submit" value="Block"/>

    <input class="btn btn-primary btn-lg px-5 ml-5" type="submit" id="sendbtn" name="submit" value="Unlock"/>

    <input class="btn btn-primary btn-lg px-5 ml-5" type="submit" id="sendbtn" name="submit" value="Delete"/>

    <div class=" form-text  mt-3"><h4>List of users</h4></div>
    <div class="  mt-3">
        <table  border="0" class="table table-striped">
            <thead>
            <tr >
                <th scope="col"><label>
                    <input class="checkbox1 " id="checkbox" type="checkbox" name="checkbox" onclick="checking(this)" >
                    Select all</label>
                </th>
                <th scope="col">ID</th>
                <th scope="col">Username</th>
                <th scope="col">Status</th>
                <th scope="col">Role</th>
            </tr>
            </thead>
            <tbody>
    <#list listOfUsers as listOfUser>
    <tr>
        <td width="120" >
            <div>
                <input class="checkbox" type="checkbox" name="checkboxes" id="${listOfUser.id}" value="${listOfUser.id}" >
            </div>
        </td>
        <td width="50">${listOfUser.id}</td>
        <td width="150">${listOfUser.username}</td>
        <td width="150">${listOfUser.status}</td>
        <td width="150">
            <input type="checkbox" name="${role}" onclick="checking1(this)" id="${listOfUser.id}" ${listOfUser.roles?seq_contains(role)?string("checked","")}  >${role}
        </td>
        <td ><a href="/users/${listOfUser.id}">Personal page</a></td>
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

        function checking1(button) {
            let userObj;
            if(button.checked===true){
                userObj = {
                    "checked": "true",
                    "id": button.id
                };

            }
            else {
                 userObj = {
                    "checked": "false",
                    "id":button.id
                }
            }
            var url = "/test/post";
            $.ajax({
                url: url,
                method: "post",
                data: userObj,
                success: function() {
                    $(document).ajaxStop(function(){
                        window.location.reload();
                    });
                }
            });

        }
    </script>
</form>
</@c.page>