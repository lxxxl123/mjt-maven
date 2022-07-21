<html>
<head>
    <title>Welcome!</title>
</head>
    <body>
        <h1>Welcome ${user}!</h1>
        <#if error??><div>123${error}/div></#if>
        <p>Our latest product:
            <a href="${latestProduct.url}">${latestProduct.name}</a>!
    </body>
</html>