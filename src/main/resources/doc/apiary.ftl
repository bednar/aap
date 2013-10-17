<#-- @ftlvariable name="description" type="java.lang.String" -->
<#-- @ftlvariable name="baseURL" type="java.lang.String" -->
<#-- @ftlvariable name="appName" type="java.lang.String" -->
<#-- @ftlvariable name="templates" type="java.util.List<String>" -->
HOST: ${baseURL}

--- ${appName} ---

<#if description?has_content>
---
${description}
---

</#if>
<#list templates as template>
${template}
</#list>