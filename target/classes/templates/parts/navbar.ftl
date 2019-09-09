<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <#--<a class="navbar-brand" href="/">qwert</a>-->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">Home </a>
            </li>
        <#if isAdmin>
            <li class="nav-item">
                <a class="nav-link" href="/users">Users </a>
            </li>
        </#if>
        <#if User??>
            <li class="nav-item">
                <a class="nav-link" href="/users/${userId}">Personal page</a>
            </li>
        </#if>
            <form class="form-inline ml-5" action="/searchText" method="get">
                <input style="width: 400px" autocomplete = "off" class="form-control mr-sm-2 " type="search" placeholder="Search" aria-label="Search" name="searchText">
                <button class="btn btn-outline-success my-2 my-sm-0 "  type="submit">Search</button>
            </form>
        </ul>
        <#if name=="unknown">
        <form action="/login" method="post">
            <button class="btn btn-primary" type="submit">Log in</button>
        </form>
        </#if>
        <#if name!="unknown">
        <div class="navbar-text mr-3" >${name} </div>
        <@l.logout/>
        </#if>
    </div>
</nav>