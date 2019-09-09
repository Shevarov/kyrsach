<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>

<link rel="stylesheet" href="/static/style.css">
    <div>
        Title:
        <span>${composition.title}</span>
    </div>
    <div>
        Genre:
        <span>${composition.genre}</span>
    </div>
    <div>
        Short Description:
        <span> ${composition.shortDescription}</span>
    </div>
    <div>
        Tags:
        <span><#list composition.tages as tag >${tag.tagName}<#sep >, </#list></span>
    </div>
    <#if listOfChapters??>
<ol>
        <#list listOfChapters as chapter>
            <li class="container">
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
                <#if User??>
                    <div class="row">
                        <a  href="/readComposition/#{chapter.id}/like">
                            <#if chapter.meLiked>
                                <i class="fas fa-heart"></i>
                            <#else >
                                <i class="far fa-heart"></i>
                            </#if>
                            ${chapter.likes}
                        </a>
                    </div>
                </#if>
            </li>
        </#list>
</ol>
    </#if>

<form method="post" action="/readComposition">
<#if User??>

    <div class="form-row">
        <div class="form-group col-7">
            <input autocomplete="off" type="text" class="form-control ${(commentaryError??)?string('is-invalid','')}" name="commentary" placeholder="Input your comment"
                   value="<#if comment??>${comment.commentary}</#if>">
            <#if commentaryError??>
            <div class="invalid-feedback">
                ${commentaryError}
            </div>
            </#if>
        </div>
        <div class="col">
            <input type="submit" class="btn btn-primary ml-2" id="sendbtn" name="submit" value="Add comment">

            <input type="hidden" value="${composition.id}" name="compositionId">
        </div>
    </div>
    <#if listOfComments??>
    <div id="comments1">
        <ol id="comments4">
    <#list listOfComments as comments>
        <li >
            <div class="card my-3" style="width: 18rem;">
                ${comments.commentary}<br>
                <div class="card-footer text-muted">
                    Author: <strong>${comments.getAuthorName()}</strong><br>
                </div>

            </div>
        </li>
    </#list>
        </ol>
    </div>
    </#if>
</#if>
</form>
<script>
    var sizeComm=${sizeOfComments};
    let userObj;
    <#if User??>
    var url = "/update/comment";
    setInterval(function () {
        userObj = {
            "compositionId": "${composition.id}",
            "sizeOfComments": sizeComm
        };
        $.ajax({
            url: url,
            method: "post",
            data: userObj,
            success: function( data ) {
                if(data) {
                    $.map($.parseJSON(data), function (item) {
                        var newLi = document.createElement('li');
                        var li=document.getElementById('comments4');
                        newLi.innerHTML = '<div class="card my-3" style="width: 18rem">'+
                            '<span>'+item.commentary+'</span>' +
                                '<br>'+'<div class="card-footer text-muted">Author:'+
                        '<strong>'+item.authorName+'</strong>'+'</div>'+'</div>';
                        li.appendChild(newLi);
                        sizeComm=String(+sizeComm+1);
                    });

                }
            },
            error:function(data,status,er) {

            }
        });
    }, 5000);
    </#if>
</script>
</@c.page>