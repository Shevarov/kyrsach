<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>
    <#if user?? >
        <#if user.getId()==userId || isAdmin >
<link rel="stylesheet" href="/static/style.css">
<form action="/chapter" method="post" enctype="multipart/form-data">

    <div>
        Chapter Name
        <input  autocomplete = "off" type="text"  name="chapterName" value="${chapter.chapterName}">
    </div>
    <div id="drop-area" class="my-3">
            <input type="file" name="fileName" id="fileElem"  accept="image/*" onchange="handleFiles(this.files)">
            <label class="button" for="fileElem">Select file</label>
            <div class="clearfix"></div>
        <div id="gallery"></div>
        <div>
            <button id="my_button" type="button" onclick="removeFile(this)">remove</button>
        </div>
    </div>
    <div id="gallery">
    <#if chapter.fileName??>
        <img src="/img/${chapter.fileName}">
        <div id="checkbox">
            <label>
                <input class="checkbox" type="checkbox" name="checkboxes" value="1">
                Delete image?
            </label>
        </div>
    </#if>
    </div>

    <div>
        Text
        <p>
            <textarea autocomplete="off" wrap="soft | hard" maxlength="10000"
                      name="text" placeholder="Write text"
                      cols="150" rows="20">${chapter.text!""}</textarea>
        </p>
    </div>

    <input type="hidden" value="${chapter.id}" name="chapterId">
    <input type="hidden" value="${user.id}" name="userId">
    <input type="hidden" value="${composition.id}" name="compositionId">
    <input class="btn btn-primary btn-lg px-5" type="submit" id="sendbtn" name="submit" value="Save"/>




    <script>

        document.getElementById('my_button').style.visibility = 'hidden';

        let dropArea = document.getElementById('drop-area');
        ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
            dropArea.addEventListener(eventName, preventDefaults, false);
        });
        function preventDefaults (e) {
            e.preventDefault();
            e.stopPropagation();
        }
        ['dragenter', 'dragover'].forEach(eventName => {
            dropArea.addEventListener(eventName, highlight, false);
        });
        ['dragleave', 'drop'].forEach(eventName => {
            dropArea.addEventListener(eventName, unhighlight, false);
        });
        function highlight(e) {
            dropArea.classList.add('highlight');
        }
        function unhighlight(e) {
            dropArea.classList.remove('highlight');
        }

        dropArea.addEventListener('drop', handleDrop, false);

        function handleDrop(e) {
            let dt = e.dataTransfer;
            let files = dt.files;
            handleFiles(files)
        }

        function handleFiles(files) {
            files = [...files];
            previewFile( files[0])
        }
        let img1=null;
        function previewFile(file) {
            let reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onloadend = function () {
                let img = document.createElement('img');
                img.src = reader.result;
                if (img1!=null)
                document.getElementById('gallery').removeChild(img1);
                document.getElementById('gallery').appendChild(img);
                img1=img;
                document.getElementById('my_button').style.visibility = 'visible';
            }
        }

        function removeFile(button) {
            document.getElementById('gallery').removeChild(img1);
            img1=null;
            button.style.visibility='hidden';
            $("#fileElem")[0].value = ''
        }

    </script>
</form>
        </#if>
    </#if>
</@c.page>