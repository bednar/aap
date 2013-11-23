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
> Accept: ${model.consumes?join(" ")}
<#--
Print HTTP Responeses
-->
<#list operation.responses?keys as responseCode>
< ${responseCode}
< Content-Type: ${model.produces?join(", ")}
${operation.responses[responseCode]}
<#if responseCode_has_next>+++++</#if>
</#list>
</#list>