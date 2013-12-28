<#-- @ftlvariable name="model" type="com.github.bednar.aap.model.api.ApiModel" -->
--
${model.shortDescription}
${model.description}
--

<#list model.operations as operation>
${operation.shortDescription}
<#--
Print Parameters
-->
<#if operation.parameters?has_content>
### Parameters
<#list operation.parameters as parameter>
- `${parameter.name} [${parameter.type.simpleName?lower_case}]` - ${parameter.shortDescription}
</#list>
</#if>
<#--
Print HTTP Method
-->
${operation.httpMethod} ${operation.path}
> Accept: ${operation.consumes?join(" ")}
<#--
Print HTTP Responeses
-->
<#list operation.responses as response>
< ${response.statusCode}
< Content-Type: ${operation.produces?join(", ")}
${response.shortDescription}
<#if response_has_next>+++++</#if>
</#list>
</#list>