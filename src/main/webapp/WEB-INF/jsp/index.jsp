<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${systemName}</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, "Microsoft YaHei", sans-serif;
            color: #1f2937;
            background: #f3f4f6;
        }

        .page {
            max-width: 960px;
            margin: 48px auto;
            padding: 0 24px;
        }

        .header {
            padding: 28px 32px;
            color: #ffffff;
            background: #0f766e;
            border-radius: 6px;
        }

        .header h1 {
            margin: 0 0 10px;
            font-size: 28px;
            font-weight: 600;
        }

        .header p {
            margin: 0;
            line-height: 1.7;
        }

        .modules {
            margin-top: 24px;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 14px;
        }

        .module {
            min-height: 82px;
            padding: 18px 20px;
            background: #ffffff;
            border: 1px solid #e5e7eb;
            border-radius: 6px;
            box-sizing: border-box;
        }

        .module strong {
            display: block;
            margin-bottom: 10px;
            font-size: 16px;
        }

        .module span {
            color: #6b7280;
            font-size: 13px;
        }

        .module a {
            color: #0f766e;
            text-decoration: none;
        }

        .footer {
            margin-top: 24px;
            color: #6b7280;
            font-size: 13px;
        }
    </style>
</head>
<body>
<main class="page">
    <section class="header">
        <h1>${systemName}</h1>
        <p>传统 Maven SSM Web 项目骨架已就绪。当前阶段仅提供首页入口和基础配置，业务模块将在后续阶段逐步实现。</p>
    </section>

    <section class="modules" aria-label="功能模块">
        <article class="module">
            <strong><a href="${pageContext.request.contextPath}/params">系统参数维护</a></strong>
            <span>维护 tb001 系统参数</span>
        </article>
        <article class="module">
            <strong><a href="${pageContext.request.contextPath}/units/open">单位开户</a></strong>
            <span>新增缴存单位并生成单位账号</span>
        </article>
        <article class="module">
            <strong><a href="${pageContext.request.contextPath}/persons/open">个人开户</a></strong>
            <span>新增个人账户并生成个人账号</span>
        </article>
        <article class="module">
            <strong>单位资料修改</strong>
            <span>待实现</span>
        </article>
        <article class="module">
            <strong>个人资料修改</strong>
            <span>待实现</span>
        </article>
        <article class="module">
            <strong>单位信息查询</strong>
            <span>待实现</span>
        </article>
        <article class="module">
            <strong>个人信息查询</strong>
            <span>待实现</span>
        </article>
    </section>

    <p class="footer">运行环境建议：JDK 25 构建，Java 17 兼容字节码，Tomcat 9，MySQL 9.5。</p>
</main>
</body>
</html>
