# 项目描述
本项目是入门级基于Springboot开发的限流工具starter，开箱即用。提供限流兜底扩展点和业务异常处理扩展点，便于自定义降级逻辑。
## 限流模式
1. 立即拒绝：采用滑动窗口算法，精准限流
2. 排队等待：采用漏桶算法，能设置超时时间，进行请求排队等待限流。
# 使用方式
1. 打jar包到本地
![在这里插入图片描述](https://img-blog.csdnimg.cn/7b11def6181c48c1928ae40ce35923ac.png)

2. 引入依赖

```java
        <dependency>
            <groupId>com.hiwei</groupId>
            <artifactId>valve-springboot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```

3. 对限流接口进行注解标记

```java
    @GetMapping("/valve")
    @ValveResource(valveType = ValveType.REJECT,value = 5,blockHandlerClass = {BlockHandler.class})
    public String valve(){
        return "正常";
    }
```

## 自定义
1. BlockExceptionHandler
```java
public class BlockHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest res, HttpServletResponse response, BlockException e) throws Exception {
        //返回json数据
        response.setStatus(500);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), "服务限流了");
    }
}
```
2. FallBackHandler

```java
public class MyBlockHandler implements FallBackHandler{
    @Override
    public void handle(HttpServletRequest res, HttpServletResponse response, BlockException e) throws Exception {
        //返回json数据
        response.setStatus(500);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), "服务异常了");
    }
}
```