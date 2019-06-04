<#assign
    know = Session.SPRING_SECURITY_CONTEXT??
>

<#if know>
    <#assign
        User = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name=User.getUsername()
        isAdmin = User.getAuthorities()?seq_contains('ADMIN')
        userId= User.getId()
    >
<#else > <#assign
name="unknown"
isAdmin=false
userId=-1
>
</#if>