<#-- @ftlvariable name="baseURL" type="java.lang.String" -->
<#-- @ftlvariable name="appName" type="java.lang.String" -->
<#-- @ftlvariable name="templates" type="java.util.List<String>" -->
HOST: ${baseURL}

--- ${appName} ---

---
Welcome to the our sample API documentation. All comments can be written in (support [Markdown](http://daringfireball.net/projects/markdown/syntax) syntax)
---

<#list templates as template>
${template}
</#list>