<#import "parts/common.ftl" as c>

<@c.page>
<link rel="stylesheet" href="/static/style.css">
    <div>
        Title:${composition.title}
    </div>

    <div>
        Genre:
        ${composition.genre}
    </div>

    <div>
        Short Description:
        ${composition.shortDescription}
    </div>

    <div>
        Tags:
        ${composition.tag}
    </div>

    <#if listOfChapters??>

        <#list listOfChapters as chapter>
        <div>
            Chapters №
        </div>
    <div> Chapter:
        ${chapter.chapterName}
    </div>
     <div id="gallery">
         <#if chapter.fileName??>
             <img src="/img/${chapter.fileName}">
         </#if>
     </div>
        <div> Text:
            ${chapter.text}
        </div>

        </#list>
    </#if>
</@c.page>