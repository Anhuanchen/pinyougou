<html>
<head>
    <title>freemarker小测试</title>
    <meta charset="utf-8">
</head>
<body>
<#--我是一个注释，不会输出-->
<!--html注释-->
<#include "head.ftl">
${name},你好。<br>
${message}<br>
<#--定义变量-->
<#assign linkman="周先生">
联系人：${linkman}<br>
<#if success=true>
    哎 我出来了
<#else>
    哎 我又回去了
</#if>
<br>
----商品列表----
<br>
<#list goodsList as goods>
    ${goods.name}   ${goods.price}<br>
</#list>
共${goodsList?size}条记录 <br>
<#assign text="{'bank':'工商银行','account':'10102919210201'}">
<#assign data=text?eval>
开户行:${data.bank}   账号:${data.account}<br>
当前日期：${today?date}<br>
当前时间：${today?time}<br>
当前日期+时间：${today?datetime}<br>
日期格式化：${today?string("yyyy年MM月")}<br>
${point?c} <br>
<#if aaa??>
    aaa存在 aaa值：${aaa}
<#else >
    aaa不存在 <br>
</#if>
${aaa!'aaa不存在'}
</body>
</html>