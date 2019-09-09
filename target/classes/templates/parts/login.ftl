<#macro login path,name,isRegistrationForm>
<form action="${path}" method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> User Name :</label>
        <div class="col-sm-4">
             <input type="text" name="username" class="form-control ${(usernameError??)?string('is-invalid','')}" placeholder="User name"
                    value="<#if user??>${user.username}</#if>" autocomplete="off"/>
            <#if usernameError??>
            <div class="invalid-feedback">
                ${usernameError}
            </div>
            </#if>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Password:</label>
        <div class="col-sm-4">
            <input  autocomplete = "off" type="password" name="password"  class="form-control ${(passwordError??)?string('is-invalid','')}" placeholder="Password"/>
             <#if passwordError??>
            <div class="invalid-feedback">
                ${passwordError}
            </div>
             </#if>
        </div>
    </div>
    <#if isRegistrationForm>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Email:</label>
        <div class="col-sm-4">
            <input  autocomplete = "off" type="email" name="email"  class="form-control ${(emailError??)?string('is-invalid','')}" placeholder="some@some.com" value="<#if user??>${user.email}</#if>"/>
         <#if emailError??>
            <div class="invalid-feedback">
                ${emailError}
            </div>
         </#if>
        </div>
    </div>
    </#if>
    <button class="btn btn-primary px-5" type="submit" >${name?ifExists}</button>

</form>
</#macro>

<#macro logout>
     <form action="/logout" method="post">
         <button class="btn btn-primary" type="submit">Log Out</button>
     </form>
</#macro>