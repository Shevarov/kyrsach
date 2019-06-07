<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>
    <#if user?? >
        <#if user.getId()==userId || isAdmin >

<form action="/composition" method="post">

    <div>
        Title
        <input type="text" name="title" value="${composition.title}" ">
    </div>

    <div>
        Genre
        <input type="text" name="genre" value="${composition.genre}"">
    </div>

    <div>
        Short Description
        <input type="text" name="shortDescription" value="${composition.shortDescription}"">
    </div>

    <div>
        Tags
        <input type="text" name="tags" value="${composition.tag}"">
    </div>
    <input type="hidden" value="${composition.id}" name="compositionId">
    <input type="hidden" value="${user.id}" name="userId">
    <input class="btn btn-primary btn-lg px-5" type="submit" id="sendbtn" name="submit" value="Save"/>

<#if listOfChapters??>
<div class="pt-5">
    <input class="btn btn-primary btn-lg px-5" type="submit" id="sendbtn" name="submit" value="Create new Chapter"/>
</div>
    <#list listOfChapters as chapter>
    <div>
        <a href="/chapter/${user.id}/${composition.id}/${chapter.id}">Chapter number ${chapter.id}</a>
    </div>

    </#list>
</#if>
</form>
        </#if>
    </#if>
</@c.page>