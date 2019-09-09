<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>
    <#if user?? >
        <#if user.getId()==userId || isAdmin>
<style>
    th {
        cursor: pointer;
    }

    th:hover {
        background: #AAA;
    }
</style>

<form action="/personalPage" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Password:</label>
            <div class="col-sm-4">
                <input  autocomplete = "off" type="password" name="password"  class="form-control" placeholder="Password"/>
            </div>
        </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Email:</label>
        <div class="col-sm-4">
            <input  autocomplete = "off" type="email" name="email"  class="form-control" placeholder="some@some.com" value="${user.email}"/>
        </div>
    </div>
    <input type="hidden" value="${user.id}" name="userId">
    <input class="btn btn-primary btn-lg px-5" type="submit" id="sendbtn1" name="submit" value="Save"/>

    <div class="pt-5">
        <input class="btn btn-primary btn-lg px-5 " type="submit" id="sendbtn2" name="submit" value="Create"/>

        <input class="btn btn-primary btn-lg px-5 ml-5" type="submit" id="sendbtn3" name="submit" value="Redaction"/>

        <input class="btn btn-primary btn-lg px-5 ml-5" type="submit" id="sendbtn4" name="submit" value="Open for read"/>

        <input class="btn btn-primary btn-lg px-5 ml-5" type="submit" id="sendbtn5" name="submit" value="Delete"/>
    </div>

    <div class=" form-text  mt-3"><h4>List of compositions</h4></div>
    <#if listOfCompositions??>
    <div class="  mt-3">
        <table border="1" id="grid" class="table table-striped">
            <thead>
            <tr>
                <th>
                    Select
                </th>
                <th id="id">Number</th>
                <th id="title">Title</th>
                <th id="genre">Genre</th>
                <th id="descrip">Short Description</th>
                <th id="tags">Tags</th>
            </tr>
            <tr class='table-filters'>
                <td

                </td>
                <td>
                    <input type="text"/>
                </td>
                <td>
                    <input type="text"/>
                </td>
                <td>
                    <input type="text"/>
                </td>
                <td>
                    <input type="text"/>
                </td>
                <td>
                    <input type="text"/>
                </td>
            </tr>
            </thead>
            <tbody>
    <#list listOfCompositions as composition>

    <tr class='table-data' onclick="/readComposition/${composition.id}">
        <td width="100">
            <div>
                <input class="checkbox" type="checkbox" name="checkboxes" id="${composition.id}"
                       value="${composition.id}" onclick="checking(this)">
            </div>
        </td>
        <td width="50"></td>
        <td width="200">${composition.title}</td>
        <td width="200">${composition.genre}</td>
        <td width="400">${composition.shortDescription}</td>
        <td width="200"><#list composition.tages as tag >${tag.tagName}<#sep >, </#list></td>
    </tr>
    </#list>
            </tbody>
        </table>
    </div>
    </#if>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
    <script>
        $('#grid tbody tr').each(function(i) {
            var number = i + 1;
            $(this).find('td:eq(1)').text(number);
        });

        function checking(button) {
            let checkboxes = document.getElementsByClassName('checkbox');
            var dop;
            dop = button.checked === true;
            for (let index = 0; index < checkboxes.length; index++) {
                    checkboxes[index].checked=false;
            }
            button.checked = dop === true;
        }

        function sortTable(f,n){
            var rows = $('#grid tbody  tr').get();
            rows.sort(function(a, b) {
                var A = getVal(a);
                var B = getVal(b);
                if(A < B) {
                    return -1*f;
                }
                if(A > B) {
                    return 1*f;
                }
                return 0;
            });
            function getVal(elm){
                var v = $(elm).children('td').eq(n).text().toUpperCase();
                if($.isNumeric(v)){
                    v = parseInt(v,10);
                }
                return v;
            }

            $.each(rows, function(index, row) {
                $('#grid').children('tbody').append(row);
            });
        }
        var f_id = 1;
        var f_title = 1;
        var f_genre = 1;
        var f_descrip = 1;
        var f_tags = 1;
        $("#id").click(function(){
            f_id *= -1;
            var n = $(this).prevAll().length;
            sortTable(f_id,n);
        });
        $("#title").click(function(){
            f_title *= -1;
            var n = $(this).prevAll().length;
            sortTable(f_title,n);
        });
        $("#genre").click(function(){
            f_genre *= -1;
            var n = $(this).prevAll().length;
            sortTable(f_genre,n);
        });
        $("#descrip").click(function(){
            f_descrip *= -1;
            var n = $(this).prevAll().length;
            sortTable(f_descrip,n);
        });
        $("#tags").click(function(){
            f_tags *= -1;
            var n = $(this).prevAll().length;
            sortTable(f_tags,n);
        });

        $('.table-filters input').on('input', function () {
            filterTable($(this).parents('table'));
        });
        function filterTable($table) {
            var $filters = $table.find('.table-filters td');
            var $rows = $table.find('.table-data');
            $rows.each(function (rowIndex) {
                var valid = true;
                $(this).find('td').each(function (colIndex) {
                    if ($filters.eq(colIndex).find('input').val()) {
                        if ($(this).html().toLowerCase().indexOf(
                                $filters.eq(colIndex).find('input').val().toLowerCase()) == -1) {
                            valid = valid && false;
                        }
                    }
                });
                if (valid === true) {
                    $(this).css('display', '');
                } else {
                    $(this).css('display', 'none');
                }
            });
        }
    </script>
</form>
        </#if>
    </#if>
</@c.page>