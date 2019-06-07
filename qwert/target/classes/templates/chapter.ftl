<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>
    <#if user?? >
        <#if user.getId()==userId || isAdmin >
<link rel="stylesheet" href="/static/style.css">
<form action="/chapter" method="post" enctype="multipart/form-data">

    <div>
        Chapter Name
        <input type="text" name="chapterName" value="${chapter.chapterName}" ">
    </div>

    <div id="drop-area">
            <p>Upload multiple files with the file dialog or by dragging and dropping images onto the dashed region</p>
            <input type="file" name="fileName" id="fileElem" multiple accept="image/*" onchange="handleFiles(this.files)">
            <label class="button" for="fileElem">Select some files</label>
        <div id="gallery"></div>
    </div>
    <div id="gallery">
    <#if chapter.fileName??>
        <img src="/img/${chapter.fileName}">
    </#if>
    </div>
    <div>
        Text
        <input type="text" name="text" value="${chapter.text}"">
    </div>

    <input type="hidden" value="${chapter.id}" name="chapterId">
    <input type="hidden" value="${user.id}" name="userId">
    <input type="hidden" value="${composition.id}" name="compositionId">
    <input class="btn btn-primary btn-lg px-5" type="submit" id="sendbtn" name="submit" value="Save"/>




    <script>
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
            let dt = e.dataTransfer
            let files = dt.files
            handleFiles(files)
        }
        function handleFiles(files) {
            files = [...files]
            previewFile( files[0])
        }
        let img1=null;
        function previewFile(file) {
            let reader = new FileReader()
            reader.readAsDataURL(file)
            reader.onloadend = function (oldChild) {
                let img = document.createElement('img')
                img.src = reader.result
                if (img1!=null)
                document.getElementById('gallery').removeChild(img1)
                document.getElementById('gallery').appendChild(img)
                img1=img;
            }
        }

    </script>
</form>
        </#if>
    </#if>
</@c.page>