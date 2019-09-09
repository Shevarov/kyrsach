<#import "parts/common.ftl" as c>

<@c.page>
<h2>Mordor - technogenic civilization, tarnished by the winners</h2>
<#if listOfCompositions??>
<div>
    <p>
        <h4>
            Latest updated works:
        </h4>
    </p>
    <#list listOfCompositions as composition>
    <a  href="/readComposition/${composition.id}">${composition.title}</a><br>
    </#list>
</div>
</#if>
<div>
    <p>
    <h4>
        Most popular composition:
    </h4>
    </p>
</div>
</@c.page>