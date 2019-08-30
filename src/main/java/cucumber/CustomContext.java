package cucumber;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XWR
 * @Description 自定义测试上下文，配合Context.java枚举类，把自定义测试上下文的内容名字以枚举形式存放到Context.java枚举里，
 * 				本类负责设置和获取测试上希望枚举内容，
 * 				最后在 TextContext.java中包含本类，以便可以使用Pico-Container库在所有Cucumber步骤中共享自定义的测试上下文。
 */
public class CustomContext {
	private  Map<String, Object> customContextMap;

	// new一个HasMap对象，它将自定义枚举的测试上下文信息存储在键值对中。键类型为String，Value可以是任何对象类型。
    public CustomContext(){
    	customContextMap = new HashMap<>();
    }
    
    // 设置/传进某上下文内容的值
    public void putContext(Context key, Object value) {
    	customContextMap.put(key.toString(), value);
    }
    
    // 获取某上下文内容的值
    public Object getContext(Context key){
        return customContextMap.get(key.toString());
    }

    // 检查是否存在某上下文内容
    public Boolean isContains(Context key){
        return customContextMap.containsKey(key.toString());
    }
}
