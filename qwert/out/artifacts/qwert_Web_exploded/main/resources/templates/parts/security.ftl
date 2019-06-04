<#assign
    know = Session.SPRING_SECURITY_CONTEXT??
>

<#if know>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name=user.getUsername()
        isAdmin = user.getAuthorities()?seq_contains('ADMIN')
        userId = user.getId()
    >
<#else > <#assign
name="unknown"
isAdmin=false
userId=-1
>


</#if>