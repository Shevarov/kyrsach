<#import "parts/common.ftl" as c>

<@c.page>

<#if listOfCompositions??>
<h5>Result of search</h5>
    <#list listOfCompositions as composition>
    <a  href="/readComposition/${composition.id}">Name composition:${composition.title}</a><br>
    </#list>
<#else>
    <h1>Your request not found</h1>
</#if>
</@c.page>