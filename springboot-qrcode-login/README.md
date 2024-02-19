# 接口测试

## 登录接口

请求地址：

```http
http://localhost:9020/api/login/uname?userid=1&pass=123&device=android1001
```

参数：

1. userid：用户id，写死在类`LoginController` 的静态变量`userMap`。
2. pass：用户密码，写死在类`LoginController` 的静态变量`userMap`。
3. device：设备来源，配置在`application.properties`的属性`my.device`。

返回值：

```json
{
  "msg": "登录成功",
  "code": 20000,
  "data": "5a77f0de1cef44a8929ce8fe293de7e0"
}
```

data为登录token。

## 生成二维码接口

请求地址：

```http
http://localhost:9020/api/qrcode/generate
```

参数：无

返回值：二维码图片



## 轮询二维码状态接口

请求地址：

```http
http://localhost:9020/api/qrcode/query/qrcode_status?qrcode_id=c650f843519a432b93c76f094a3c7da9
```

参数：

1. qrcode_id：包含在二维码中的唯一值。

返回值：

```json
{
  "msg": "二维码等待扫描",
  "code": 10000,
  "data": {
    "codeStatus": "UNUSED",
    "message": "二维码等待扫描",
    "token": null,
    "device": null
  }
}
```

## 扫码登录接口

请求地址：

```http
http://localhost:9020/api/qrcode/scan_qrcode_login?device=android1001&uuid=30f8051f90c0499c9cbb0475bbe28fc7&mobile_token=5a77f0de1cef44a8929ce8fe293de7e0
```

参数：

1. device：设备来源
2. uuid：二维码id
3. mobile_token：设备登录token

返回值：

```json
{
  "msg": "请确认登录",
  "code": 200,
  "data": {
    "codeStatus": "CONFIRMING",
    "message": "二维码扫描成功，等待确认",
    "token": "1a8f67823a0b486f89adf778a3b620f0",
    "device": "android1001"
  }
}
```

token为一次性token，用来确认登录的。

## 确认登录接口

请求地址：

```http
http://localhost:9020/api/qrcode/confirm_qr_login?uuid=30f8051f90c0499c9cbb0475bbe28fc7&once_token=1a8f67823a0b486f89adf778a3b620f0&mobile_token=5a77f0de1cef44a8929ce8fe293de7e0&device=android1001
```

参数：

1. uuid：二维码唯一值
2. once_token：一次性token
3. mobile_token：设备登录token
4. device：设备来源

返回值：

```json
{
  "msg": "扫码登陆成功",
  "code": 200,
  "data": {
    "codeStatus": "CONFIRMED",
    "message": "二维码已确认",
    "token": "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiIxIiwiZXhwIjoxNzEwODUzODgyfQ.B9Al9ZS-vEdB78c1dvG1tITsGJDx4gjMR63a_A4CjTo",
    "device": null
  }
}
```

## 访问系统资源

请求地址：

```http
http://localhost:9020/api/product/findall?token=eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiIxIiwiZXhwIjoxNzEwODUzODgyfQ.B9Al9ZS-vEdB78c1dvG1tITsGJDx4gjMR63a_A4CjTo&device=android
```

参数：

1. token：生成的pc端 jwt token
2. device：设备来源

返回值：

```json
[
  {
    "id": 1,
    "name": "mate 60 pro",
    "price": 10000
  },
  {
    "id": 2,
    "name": "iphone 15 pro max",
    "price": 12000
  },
  {
    "id": 3,
    "name": "s24 ultra",
    "price": 11000
  }
]
```

