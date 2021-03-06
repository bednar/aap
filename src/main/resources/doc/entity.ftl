<#-- @ftlvariable name="model" type="com.github.bednar.aap.model.entity.EntityModel" -->
--
Resource: ${model.shortDescription}
${model.description}
### Properties
<#list model.properties as property>
    <#assign name = property.name>
    <#assign type = property.type.simpleName?lower_case>
    <#assign desc = property.shortDescription>
    <#assign colors = property.restrictions>
- `${name}[${type} ${colors?join(", ")}]` - _${desc}_
</#list>
--
