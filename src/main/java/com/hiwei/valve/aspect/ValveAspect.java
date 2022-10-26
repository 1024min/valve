package com.hiwei.valve.aspect;

import com.hiwei.valve.annotation.ValveResource;
import com.hiwei.valve.exception.BlockException;
import com.hiwei.valve.controller.TrafficShapingController;
import com.hiwei.valve.controller.ValveType;
import com.hiwei.valve.controller.rule.LeakyBucket;
import com.hiwei.valve.controller.rule.SlidingWindow;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ValveAspect {
    private static Map<Method, TrafficShapingController> rules = new HashMap<>();

    @Pointcut("@annotation(com.hiwei.valve.annotation.ValveResource)")
    private void serviceMethod() {
    }

    @Around(value = "serviceMethod()&& @annotation(valveResource)")
    public Object advice(ProceedingJoinPoint point, ValveResource valveResource) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //解析注解
        Signature sig = point.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        Object target = point.getTarget();
        try {
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            TrafficShapingController trafficShapingController = rules.get(currentMethod);
            if(trafficShapingController==null){
                if(valveResource.valveType()== ValveType.REJECT){
                    trafficShapingController = new SlidingWindow(valveResource.value());
                    rules.put(currentMethod,trafficShapingController);
                }else if(valveResource.valveType()== ValveType.QUEUE_WAIT){
                    trafficShapingController = new LeakyBucket(valveResource.value(),valveResource.timeout());
                    rules.put(currentMethod,trafficShapingController);
                }else{
                    throw new RuntimeException("nonsupport valve type:"+valveResource.valveType());
                }
            }
            boolean canPass = trafficShapingController.canPass();
            if (!canPass){
                throw new BlockException();
            }
            return point.proceed(point.getArgs());
        } catch (Exception e) {
            exceptionHandle(request, response, e, valveResource);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * 异常兜底处理
     * @param request
     * @param response
     * @param e
     * @param valveResource
     */
    private void exceptionHandle(HttpServletRequest request, HttpServletResponse response, Exception e, ValveResource valveResource) {
        //反射调用
        Class<?>[] classes;
        if(e instanceof BlockException){
            classes = valveResource.blockHandlerClass();
        }else{
            classes = valveResource.fallbackClass();
        }
        for (Class<?> aClass : classes) {
            try {
                Object handler = aClass.newInstance();
                Method declaredMethod = aClass.getMethod("handle",HttpServletRequest.class,HttpServletResponse.class, BlockException.class);
                declaredMethod.invoke(handler,request,response,e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
