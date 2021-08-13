package ziheng.xu.proxytest.starter;

import org.jolokia.converter.object.StringToObjectConverter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.util.Map;

@Component
public class JokoliaStarter implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("==============JolokiaRunner==============");
        Class<?> parserInterface = Class.forName("org.jolokia.converter.object.StringToObjectConverter$Parser");


//        Object instance = (StringToObjectConverter.Parser) c.newInstance();
        Class<StringToObjectConverter> clazz = (Class<StringToObjectConverter>) Class.forName("org.jolokia.converter.object.StringToObjectConverter");
        Field parserMap = clazz.getDeclaredField("PARSER_MAP");

        parserMap.setAccessible(true);
        Map map = (Map)parserMap.get(clazz);
        System.out.println(map);

        Class<?>[] classes = new Class[1];
        classes[0] = parserInterface;
        Object parserInstance = Proxy.newProxyInstance(parserInterface.getClassLoader(), classes, new ProxyListener());

        map.put("LocalDate", parserInstance);
        map.put(LocalDate.class.getName(), parserInstance);
        System.out.println(map);
    }
}

class ProxyListener implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Object result = null;
        if (method.getName().equals("extract")) {
            result = LocalDate.parse((String) args[0]);
        }

        return result;
    }
}