# 认证服务器设计

## 目录

<!-- TOC -->

- [标准化团队认证体系架构设计](#认证服务器设计)
    - [版本记录](#版本记录)
    - [目录](#目录)
    - [1.概要说明](#1概要说明)
        - [1.1.标准化团队认证体系说明](#11标准化团队认证体系说明)
        - [1.1 认证服务器作用](#11-认证服务器作用)
        - [1.2 oauth2.0授权类型及授权过程](#12-oauth20授权类型及授权过程)
            - [1.2.1 授权码模式（authorization code）](#121-授权码模式authorization-code)
            - [1.2.2 简化模式（implicit）](#122-简化模式implicit)
            - [1.2.3 密码模式（resource owner password credentials）](#123-密码模式resource-owner-password-credentials)
            - [1.2.4 客户端模式（client credentials）](#124-客户端模式client-credentials)
        - [1.3 认证服务器返回结果](#13-认证服务器返回结果)
        - [1.4 授权服务器进行认证](#14-授权服务器进行认证)
        - [1.5 access_token介绍和使用](#15-access_token介绍和使用)
            - [1.5.1 access_token介绍](#151-access_token介绍)
            - [1.5.1 access_token使用](#151-access_token使用)
        - [1.6 OIDC协议介绍](#16-oidc协议介绍)
            - [1.6.1 OIDC介绍](#161-oidc介绍)
            - [1.6.2 OIDC术语介绍](#162-oidc术语介绍)
            - [1.6.2 id_token介绍](#162-id_token介绍)
            - [1.6.3 id_token使用](#163-id_token使用)
        - [1.7 单点登录(SSO)介绍](#17-单点登录sso介绍)
    - [2.资源服务器设计](#2资源服务器设计)
        - [2.1 资源服务器作用](#21-资源服务器作用)
        - [2.2 资源服务器验证token(check_token)](#22-资源服务器验证tokencheck_token)
    - [3.客户端介绍和需遵守规范](#3客户端介绍和需遵守规范)
        - [3.1 客户端介绍](#31-客户端介绍)
            - [3.1.1 scope](#311-scope)
            - [3.1.2 token过期时间](#312-token过期时间)
        - [3.2 客户端发起oauth2.0认证请求](#32-客户端发起oauth20认证请求)
        - [3.3 客户端解析id_token](#33-客户端解析id_token)
    - [4.oauth2.0及OIDC实现](#4oauth20及oidc实现)
        - [4.1 授权模式](#41-授权模式)
        - [4.2 授权服务器授权流程](#42-授权服务器授权流程)
            - [4.2.1 获取授权码](#421-获取授权码)
            - [4.2.2 通过授权码获取token](#422-通过授权码获取token)
            - [4.2.3 返回结果](#423-返回结果)
            - [4.2.4 客户端响应](#424-客户端响应)
            - [4.2.5 资源服务器响应](#425-资源服务器响应)
    - [5.认证系统使用规范](#5认证系统使用规范)
        - [5.1 授权模式使用规范](#51-授权模式使用规范)
        - [5.2 客户端注册](#52-客户端注册)
        - [5.3 id_token使用原因](#53-id_token使用原因)
        - [5.4 id_token使用规范](#54-id_token使用规范)
        - [5.5 token注销](#55-token注销)
        - [5.6 资源服务器验证token](#56-资源服务器验证token)
        - [5.7 token的scope使用](#57-token的scope使用)
        - [5.8 spring cloud gateway网关鉴权](#58-spring-cloud-gateway网关鉴权)
            - [5.8.1 网关鉴权实现](#581-网关鉴权实现)
            - [5.8.2 网关鉴权流程图](#582-网关鉴权流程图)
            - [5.8.3 示例代码](#583-示例代码)
    - [6.权限](#6权限)
    - [7.oauth2.0安全漏洞防范](#7oauth20安全漏洞防范)
        - [7.1 CSRF跨站请求攻击](#71-csrf跨站请求攻击)
        - [7.2 XSS问题](#72-xss问题)
        - [7.3 redirect_uri问题](#73-redirect_uri问题)

<!-- /TOC -->

## 1.概要说明

### 1.1.标准化团队认证体系说明

> 认证包含的功能范围以及在整个体系中的角色
>
> 区分资源，认证服务
>
> 说明白claim,scope等

### 1.1 认证服务器作用

> 提供哪些功能

* 用户注册
* 客户端注册(包含范围)
* 刷新令牌 涉及到用户授权会有refresh_token
* 撤销令牌
* 检查令牌
* 对客户端的授权，对资源服务器的保护，当未授权的客户端直接访问资源服务器的时候会跳转到认证服务器进行授权，授权成功后客户端获取access_token对资源服务器进行访问。
* 为客户端颁发id_token

### 1.2 oauth2.0授权类型及授权过程

#### 1.2.1 授权码模式（authorization code）

申请授权码

```url
http://localhost:8080/oauth/authorize?client_id=clientapp&redirect_uri=http://localhost:9001/callback&response_type=code&scope=read_userinfo
```

获取授权码

通过用户的授权之后，浏览器重定向到redirect_uri，授权码以code参数的形式在重定向url上面

```url
http://localhost:9001/callback&code=XXXXXXX
```

获取token

```url
http://localhost:8080/oauth/token?code=XXXXXXX&grant_type=authorization_code&redirect_uri=http://localhost:9001/callback&scope=read_userinfo
```

#### 1.2.2 简化模式（implicit）

简化模式也被称为隐式许可类型，客户端运行在浏览器内部，比如使用javascript，response_type参数为token

申请授权

```url
http://localhost:8080/oauth/authorize?client_id=clientapp&redirect_uri=http://localhost:9001/callback&response_type=token&scope=admin&state=abc
```

获取token

在用户approve之后浏览器会跳转到重定向地址并携带token相关信息

```url
http://localhost:9001/callback#access_token=xxxx-xxxxx-xxxx&token_type=bearer&state=abc&expries_in=59
```

获得的access_token会在重定向的url的参数中

#### 1.2.3 密码模式（resource owner password credentials）

密码模式适用于用户对应用程序高度信任的情况。比如是用户操作系统的一部分。认证服务器只有在其他授权模式无法执行的情况下，才能考虑使用这种模式。

获取token

```url
http://localhost:8080/oauth/token?password=123456&grant_type=password&username=lll&scope=admin
```

#### 1.2.4 客户端模式（client credentials）

客户端模式又称为客户端凭据许可类型

没有明确的资源拥有者，或对于客户端来说资源拥有者不可区分。使用客户端的凭据直接向授权服务器获取token，不涉及用户

申请授权token

```url
http://localhost:8080/oauth/token?grant_type=client_credentials&scope=admin
```

### 1.3 认证服务器返回结果

结果携带OIDC的id_token

```json
{
    "access_token": "SampleAccessToken",
    "id_token": "SampleIdToken",
    "token_type": "bearer",
    "expires_in": 3600,
    "refresh_token": "SampleRefreshToken",
    "scope":"read write"
}
```

### 1.4 授权服务器进行认证

目前授权服务器的认证功能遵守OIDC(openID connect)协议，OIDC协议将会增加response_type，id_token

更多内容参考：

[OIDC（OpenId Connect）身份认证（核心部分）](https://www.cnblogs.com/linianhui/p/openid-connect-core.html "With a Title")

[OIDC（OpenId Connect）身份认证（扩展部分）](https://www.cnblogs.com/linianhui/p/openid-connect-extension.html#auto_id_0 "With a Title")

### 1.5 access_token介绍和使用

#### 1.5.1 access_token介绍

access_token被称为访问令牌，携带access_token可以对资源服务器进行访问

在本次设计中access_token不会包含用户信息

#### 1.5.1 access_token使用

1. bearer请求头包含方式 bearer access_token二者之间加空格分隔
2. URL后面携带参数 URL?access_token=xxxxxxx

### 1.6 OIDC协议介绍

#### 1.6.1 OIDC介绍

&emsp;&emsp;OAuth2提供了Access Token来解决授权第三方客户端访问受保护资源的问题；OIDC在这个基础上提供了ID Token来解决第三方客户端标识用户身份认证的问题。OIDC的核心在于在OAuth2的授权流程中，一并提供用户的身份认证信息（ID Token）给到第三方客户端，ID Token使用JWT格式来包装，得益于JWT（JSON Web Token）的自包含性，紧凑性以及防篡改机制，使得ID Token可以安全的传递给第三方客户端程序并且容易被验证。此外还提供了UserInfo的接口，用户获取用户的更完整的信息。

#### 1.6.2 OIDC术语介绍

EU：End User：一个人类用户

RP：Relying Party ,用来代指OAuth2中的受信任的客户端，身份认证和授权信息的消费方

OP：OpenID Provider，有能力提供EU认证的服务（比如OAuth2中的授权服务），用来为RP提供EU的身份认证信息

ID Token：JWT格式的数据，包含EU身份认证的信息

UserInfo Endpoint：用户信息接口（受OAuth2保护），当RP使用Access Token访问时，返回授权用户的信息，此接口必须使用HTTPS

#### 1.6.2 id_token介绍

 Id Token是一个签名的JSON Web Token(JWT)，内容如下：

```json
{
   "iss": "https://server.example.com", #必须
   "sub": "24400320", #必须
   "aud": "s6BhdRkqt3", # 必须
   "nonce": "n-0S6_WzA2Mj",
   "exp": 1311281970, #必须
   "iat": 1311280970, #必须
   "auth_time": 1311280969,
   "acr": "urn:mace:incommon:iap:silver"
}
```

iss = Issuer Identifier：必须。提供认证信息者的唯一标识。一般是一个https的url（不包含querystring和fragment部分）。

sub = Subject Identifier：必须。iss提供的EU的标识，在iss范围内唯一。它会被RP用来标识唯一的用户。最长为255个ASCII个字符。

aud = Audience(s)：必须。标识ID Token的受众。必须包含OAuth2的client_id。

exp = Expiration time：必须。过期时间，超过此时间的ID Token会作废不再被验证通过。

iat = Issued At Time：必须。JWT的构建的时间。

auth_time = AuthenticationTime：EU完成认证的时间。如果RP发送AuthN请求的时候携带max_age的参数，则此Claim是必须的。

nonce：RP发送请求的时候提供的随机字符串，用来减缓重放攻击，也可以来关联ID Token和RP本身的Session信息。

acr = Authentication Context Class Reference：可选。表示一个认证上下文引用值，可以用来标识认证上下文类。

amr = Authentication Methods References：可选。表示一组认证方法。

azp = Authorized party：可选。结合aud使用。只有在被认证的一方和受众（aud）不一致时才使用此值，一般情况下很少使用。

#### 1.6.3 id_token使用

id_token中携带者用户的标识信息，客户端解析id_token之后就可以知道是是谁发起的认证请求，并在客户端显示出来

### 1.7 单点登录(SSO)介绍

假如我们有系统A，系统B，系统C三个系统，这三个系统都有自己的登录界面但是内部用户数据是打通的。

当用户在系统A登陆的时候，在同一浏览器跳转到系统B或者C之后无需再登录。

这里我们通过oauth2.0进行单点登录的实现，客户端使用认证服务器颁发的access_token访问三个系统，当token过期或者退出登录之后token将无效，三个系统将不能被访问，达到单点登录的效果。

## 2.资源服务器设计

### 2.1 资源服务器作用

资源服务器上面存放着资源内容，可以提供给用户进行访问

### 2.2 资源服务器验证token(check_token)

资源服务器在获取token之后首先到认证服务器验证token的有效性

1. 先验证token是否存在

## 3.客户端介绍和需遵守规范

### 3.1 客户端介绍

#### 3.1.1 scope

每个客户端在注册的时候都会注册自己的scopes(可能多个)，resourceIds(可能多个)，当客户端在申请认证的时候会首先验证客户端的scope，如果scope跟注册时候的scope对不上会拒绝认证

scope代表了客户端的访问权限，可以在资源服务器设置不同scope可以访问哪些API

#### 3.1.2 token过期时间

token的过期时间也是客户端的一个属性

### 3.2 客户端发起oauth2.0认证请求

### 3.3 客户端解析id_token

id_token会被RSAwithSHA256私钥加密，公钥加密会通过授权服务器以接口的方式暴露给客户端，客户端拿到公钥之后将jwt解密并解析出来

## 4.oauth2.0及OIDC实现

### 4.1 授权模式

OIDC使用授权码模式

### 4.2 授权服务器授权流程

#### 4.2.1 获取授权码

多个scope参数用空格分隔

```url
http://localhost:9110/oauth/authorize?client_id=008fec3d-c125-409e-9f8d-ef7724ec21df&redirect_uri=http://www.baidu.com&response_type=code&scope=write read&state=abc
```

OIDC协议规范的response_type可能有多中，在我们的授权服务器建设过程中使用code类型即刻

可以加state参数防止跨站请求攻击

#### 4.2.2 通过授权码获取token

```url
http://localhost:9110/oauth/token?client_id=008fec3d-c125-409e-9f8d-ef7724ec21df&grant_type=authorization_code&redirect_uri=http://www.baidu.com&code=8QKsHT
```

#### 4.2.3 返回结果

```json
{
   "access_token": "SlAV32hkKG",
   "token_type": "Bearer",
   "refresh_token": "8xLOxBtZp8",
   "expires_in": 3600,
   "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFlOWdkazcifQ.ewogImlzc
     yI6ICJodHRwOi8vc2VydmVyLmV4YW1wbGUuY29tIiwKICJzdWIiOiAiMjQ4Mjg5
     NzYxMDAxIiwKICJhdWQiOiAiczZCaGRSa3F0MyIsCiAibm9uY2UiOiAibi0wUzZ
     fV3pBMk1qIiwKICJleHAiOiAxMzExMjgxOTcwLAogImlhdCI6IDEzMTEyODA5Nz
     AKfQ.ggW8hZ1EuVLuxNuuIJKX_V8a_OMXzR0EHR9R6jgdqrOOF4daGU96Sr_P6q
     Jp6IcmD3HP99Obi1PRs-cwh3LO-p146waJ8IhehcwL7F09JdijmBqkvPeB2T9CJ
     NqeGpe-gccMg4vfKjkM8FcGvnzZUN4_KSP0aAp1tOJ1zZwgjxqGByKHiOtX7Tpd
     QyHE5lcMiKPXfEIQILVq0pc_E2DzL7emopWoaoZTF_m0_N0YzFC6g6EJbOEoRoS
     K5hoDalrcvRYLSrQAZZKflyuVCyixEoV9GfNQC3_osjzw2PAithfubEEBLuVVk4
     XUVrWOLrLl0nx7RkKU8NXNHq-rvKMzqg",
   "scope":"admin"
  }
```

注：id_token不能作为访问令牌进行资源访问

#### 4.2.4 客户端响应

客户端获取access_token之后可以对资源服务器进行访问

客户端获取到id_token之后解析id_token可以得知当前授权用户是谁

#### 4.2.5 资源服务器响应

客户端使用access_token进行资源访问，资源服务器拦截到token之后对token进行检查校验token的过期时间，token的scope，校验通过后允许访问API

## 5.认证系统使用规范

### 5.1 授权模式使用规范

1. 目前认证系统支持授权码模式和密码模式
2. 团队内部产品均使用密码模式进行认证授权操作
3. 第三方产品接入团队资源服务器要求使用授权码模式进行授权认证
4. 授权码模式将会返回id_token，密码模式不会返回id_token

### 5.2 客户端注册

注册接口

```url
/client/addClient
```

注册客户端需要的数据

```json
{
  "access_token_validity": 60,#token有效时间
  "authorities": [#授权人需要具有的身份
    "admin",
    "user"
  ],
  "authorized_grant_types": [ #授权类型
    "password",
    "refresh_token",
    "authorization_code"
  ],
  "autoapprove": [#哪些scope可以被授权者直接授权 如果为true则表示全部可以直接授权，false为全部不可以直接同意授权
    "read"
  ],
  "client_secret": "123456",#client的密码
  "redirect_uri": [#回调地址
    "http://www.baidu.com"
  ],
  "refresh_token_validity": 100,#refresh有效时长
  "resource_ids": [#资源服务器id
    "system"
  ],
  "scope": [#客户端scope
    "read",
    "wirte"
  ]
}
```

### 5.3 id_token使用原因

在oauth2.0授权完成后，客户端不清楚是谁允许授权，是谁授的权，access_token的受众是谁，这些问题如果不解决，客户端是迷茫的，所以将这些信息都包含在id_token中供客户端获取这些信息。

### 5.4 id_token使用规范

认证服务器的id_token使用jwt格式，id_token的sign值采用的是RSAwith256秘钥非对称加密，解密需要使用公钥

公钥地址：

```url
/.well-known/jwks.json
```

响应数据，这是一个经过jose加密的公钥

```json
{
        "kty": "RSA",
        "e": "AQAB",
        "n": "p2naRuozp2VPk2-cysifAtwmCiHI2KaSeYwnN_OPr317cEKgfU1zuEtULeQJ_dvzAyC-w7vseUY7OyD2RGVzy8pJPENidSXvRw2Q-EY7Uvz1y0RTkiyhSVkktD66x6eSuuH5gu5ilBMPx6TXwR8jHM3S3h8ilD5YjdXLaQ732g8"
    }
```

token解析可以使用java jose工具包进行解析，解析token的时候要验证jwt的签名防止伪造jwt。

### 5.5 token注销

当应用使用完token之后需要将token注销，注销接口:

```json
/oauth/revoke-token
```

### 5.6 资源服务器验证token

认证服务器会开放```/oauth/check_token```接口作为token验证接口，要求在验证的时候需要输入在认证服务器有效的客户端id(clientId)和客户端密码(clientSecret)

```java
    @Primary
    @Bean
    public RemoteTokenServices tokenServices() {
        final RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(String.format("%s/oauth/check_token",host));
        tokenService.setClientId(clientId);
        tokenService.setClientSecret(clientSecret);
        return tokenService;
    }
```

验证示例url：

```url
http://112:222@localhost:9110/oauth/check_token?token=3b26a2c1-7165-4bc2-9c84-29bdc84681ec
```

以下是验证结果：

token有效：

```json
{
    "aud": [
        "travelme",
        "system"
    ],
    "user_name": "xiaoliu",
    "scope": [
        "write"
    ],
    "id_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ4aWFvbGl1IiwiYXVkIjoiMTExIiwic2NvcGUiOlsid3JpdGUiXSwiaXNzIjoiaHR0cDovLzEzOS4yMTkuMTEuMjExOjMwOTIwIiwiZXhwIjoxNTY2MTEwMjE2LCJpYXQiOjE1NjYwMjM4MTYsImF1dGhvcml0aWVzIjpbImFkbWluIl19.IgpEPQaILIqerIJMVthDBy4_UkI_Tixtj9A11hn4YGbNt7b4LvqbcoScG3vHRtAQytQc1tBPKFONnPVIUapsEqrHdA96yVYse2NvcJfa1QJp6brW0I-QjZ_H6bYPAWBhLqkrLBjXp6qMfRbBd2hBBihrU3hGIA8L9bKm5umq6V8",
    "active": true,
    "exp": 1566023876,
    "grantType": "authorization_code",
    "authorities": [
        "admin"
    ],
    "client_id": "111",
    "status": 1
}
```

token过期：

```json
{
    "error": "invalid_token",
    "error_description": "Token has expired"
}
```

### 5.7 token的scope使用

资源服务器会判断哪些scope有什么样的权限spring security帮我们实现scope验证

以下java代码表示/hello路径下面的所有访问，客户端scope必须包含write

```java
@Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/hello/**").access("#oauth2.hasScope('write')";
    }
```

### 5.8 spring cloud gateway网关鉴权

* 网关不设置scope，网关将作为客户端存在于整个oauth2.0体系中(客户端有了clientId和clientSecret客户端将可以访问认证服务器进行验证token)
* 网关增加过滤器过滤请求token，如果token为空或者token无效将不继续走下一个过滤器立链

#### 5.8.1 网关鉴权实现

自定义实现过滤器GlobalFilter和加载顺序Ordered接口，重写filter方法

目前网关拦截逻辑是如果请求头中传输了Bearer token，网关将token拿到认证服务器进行有效性验证验证成功将放行，验证失败将在网关层返回401无访问权限状态码

#### 5.8.2 网关鉴权流程图

![image](pics/网关鉴权流程.png)

#### 5.8.3 示例代码

```java
@Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String accessToken = extractToken(exchange.getRequest());
        if (pathMatcher.match("/travelme/dev/staffLogin", exchange.getRequest().getPath().value())) {
            return chain.filter(exchange);
        }
        //目前token为空时直接放行 之后可以根据接口规则配置路径拦截
        if (accessToken == null) {
            return chain.filter(exchange);
        } else {
            //在资源服务器校验token
            String result = checkToken(host, accessToken);
            try {
                if (result.equals(FAILED)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                } else {
                    return chain.filter(exchange);
                }
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
    }
```

## 6.权限

## 7.oauth2.0安全漏洞防范

### 7.1 CSRF跨站请求攻击

### 7.2 XSS问题

### 7.3 redirect_uri问题
