<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>
    <#if user?? >
        <#if user.getId()==userId || isAdmin >
<link rel="stylesheet" href="/static/style.css">
    <link href="/static/magicsuggest-min.css" rel="stylesheet">
        <script src="/static/magicsuggest.js"></script>
<form action="/composition" method="post">

    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Title:</label>
        <div class="col-sm-4">
            <input  autocomplete = "off" type="text" class="form-control" name="title" value="${composition.title}" ">
        </div>
    </div>

    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Genre:</label>
        <select id="genreCom" class="form-control col-sm-4" name="genre" style="width: 200px"">
        <option value="Fantasy">Fantasy</option>
        <option value="Erotic">Erotic</option>
        <option value="Comedy">Comedy</option>
        <option value="Drama">Fantasy</option>
        <option value="Action">Erotic</option>
        <option value="Detective">Comedy</option>
        </select>
    </div>
    <div>
        Short Description:
        <p>
            <textarea wrap="soft | hard" autocomplete = "off" maxlength="400" name="shortDescription" placeholder="Write short description" cols="100" rows="6">${composition.shortDescription!""}</textarea>
        </p>
    </div>

    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Tags:</label>
        <select id="tagsCom" class="form-control col-sm-4" name="tags"  style="width: 300px"">
        </select>
    </div>
    <input type="hidden" value="${composition.id}" name="compositionId">
    <input type="hidden" value="${user.id}" name="userId">
    <input class="btn btn-primary btn-lg px-5" type="submit" id="sendbtn" name="submit" value="Save" onclick="loadTag()"/>
    <input type="hidden" value="" name="chapterId" id="chapterID">
    <input type="hidden" value="" name="tagsName" id="tagsID">

    <#if "${composition.id}"!="-1">
<div class="pt-5">
    <input class="btn btn-primary btn-lg px-5" type="submit" id="sendbtn" name="submit" value="Create new Chapter"/>
</div>
    </#if>
        <#if listOfChapters??>
        <div class="my-3">
<ol>
    <#list listOfChapters as chapter>
        <div>
            <li>
                <a href="/chapter/${user.id}/${composition.id}/${chapter.id}">${chapter.chapterName}</a>
                <input type="submit" value="X" name="${chapter.id}" onclick="changeChapterID(this)">
            </li>
        </div>
    </#list>
</ol>
        </#if>
</div>

 <#if listOfChapters??>
                <div id="chapterName"> Chapter:
                </div>
                <div id="gallery">
                    <img src="" id="gallery1" name="gallery1">
                </div>
                <div id="text"> Text:
                </div>
 <div class="my-5">
     <input class="btn btn-primary" type="button" id="prev" value="<" onclick="prevChapter()" >
     <input class="btn btn-primary" type="button" id="next" value=">" onclick="nextChapter()">
 </div>
 </#if>
</form>

        <script >
            var select = document.getElementById('genreCom');
            var chapterName= document.getElementById('chapterName');
            var gallery= document.getElementById('gallery1');
            var text= document.getElementById('text');
            for (i=0;i<select.options.length;i++){
                if(select.options[i].value==="${composition.genre}")
                select.options[i].selected="selected";
            }
            function changeChapterID(button) {
                var chapterId = document.getElementById('chapterID');
                chapterId.value=button.name;
            }

            let size = 0;

            <#if listOfChapters??>
            chapterName.textContent = "${listOfChapters[0].chapterName}";
                <#if listOfChapters[0].fileName??>
                    gallery.src = "/img/${listOfChapters[0].fileName}";
                </#if>
            text.textContent = "${listOfChapters[0].text}";
                <#list listOfChapters as chapter>
                size++;
                </#list>
            document.getElementById('prev').disabled = true;
                if(size===1){
                    document.getElementById('next').disabled = true;
                }
            </#if>
            let num1=0;
            let num = 1;

            function nextChapter() {
            <#if listOfChapters??>
                num++;
                  <#list listOfChapters as chapter>
                    num1++;
                    if (num1 === num) {
                        chapterName.textContent = "${chapter.chapterName}";
                <#if chapter.fileName??>
                    gallery.src = "/img/${chapter.fileName}";
                    <#else >
                gallery.src="";
                </#if>
                        text.textContent = "${chapter.text}";
                    }
                  </#list>
                num1=0;
                if(size-num===0){
                    document.getElementById('next').disabled = true;}
                document.getElementById('prev').disabled = false;
            </#if>
            }
            function prevChapter() {
                <#if listOfChapters??>
                num--;
                  <#list listOfChapters as chapter>
                    num1++;
                    if (num1 === num) {
                        chapterName.textContent = "${chapter.chapterName}";
                <#if chapter.fileName??>
                    gallery.src = "/img/${chapter.fileName}";
                <#else >
                gallery.src="";
                </#if>
                        text.textContent = "${chapter.text}";
                    }
                  </#list>
                num1=0;
                if(num==1)
                    document.getElementById('prev').disabled = true;
                document.getElementById('next').disabled = false;
        </#if>
            }
        </script>

        <script>
            $.ajax({
                url: '/getTags',
                method: "post",
                success: function( data ) {
                    $.map($.parseJSON(data), function (item) {
                        $('#tagsCom').append($('<option>', {
                            value: item.tagName,
                            text : item.tagName
                        }));
                    });
                    $(document).ready(function() {
                        var ms = $('#tagsCom').magicSuggest({
                            expandOnFocus: true,
                            hideTrigger: true,
                            maxSelection: 3,
                            sortOrder: 'name',
                            allowDuplicates: false,
                            autoSelect: false,
                            maxSuggestions: 5
                        });
                        $(ms).on('selectionchange', function(){
                            document.getElementById('tagsID').value=(this.getValue());
                        });

                         <#list composition.tages as tag >
                        ms.setValue(["${tag.tagName}"]);
                         </#list>
                    });
                }
            });
            
        </script>

        <script>

        </script>

        </#if>
    </#if>
</@c.page>