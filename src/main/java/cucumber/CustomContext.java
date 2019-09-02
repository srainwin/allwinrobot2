package cucumber;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author XWR
 * @Description 自定义测试上下文，原理是把自定义测试上下文的内容key-value形式存放到customContext.properties里进行读写，
 * 				最后在 TextContext.java中包含本类对象，以便可以使用Pico-Container库在所有Cucumber步骤中共享自定义测试上下文。
 * 注意：多线程的时候，用例之间读写自定义共享变量可能会混乱，例如用例1还没新增共享变量但用例2已经在读取这个共享变量了。
 * 	        另外，上一个写自定义共享变量的场景运行失败也会导致下一个读自定义共享变量的场景运行失败，因此建议不要跨场景使用自定义共享变量，减少耦合性。
 */
public class CustomContext {
	private Logger logger = Logger.getLogger(CustomContext.class.getName());
	private Properties customContextPro;
	private String customContextProFile;
	private FileReader fr;
	private FileWriter fw;

    /**
     * new一个Properties对象，它将自定义枚举的测试上下文信息存储在键值对文件中。
     */
    public CustomContext(){
    	try{
    		customContextPro = new Properties();
    		customContextProFile = System.getProperty("user.dir") + "/src/main/resources/config/customContext.properties";
    		fr = new FileReader(customContextProFile);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
    
    /**
     * @Description 添加(非覆盖)自定义测试上下文共享变量
     * @param key
     * @param value
     */
    public void addContext(String key, String value){
    	try{
    		customContextPro.load(fr);
    		if( isExistContext(key) ){
    			logger.error("添加的测试上下文共享变量" + key + "已存在，无需添加");
    		}else{
            	customContextPro.put(key, value);
        		fw = new FileWriter(customContextProFile);
            	customContextPro.store(fw, "Custom Test Context");
            	logger.info("添加测试上下文共享变量" + key + "成功");
    		}
    	}catch(Exception e){
    		logger.error("添加测试上下文共享变量" + key + "失败");
    	}
    }
    
    /**
     * @Description 添加(覆盖)自定义测试上下文共享变量
     * @param key
     * @param value
     */
    public void addCoverContext(String key, String value){
    	try{
    		customContextPro.load(fr);
    		if( isExistContext(key) ){
    			logger.warn("添加的测试上下文共享变量" + key + "已存在，进行覆盖添加");
    			customContextPro.remove(key);
    		}
            customContextPro.put(key, value);
        	fw = new FileWriter(customContextProFile);
            customContextPro.store(fw, "Custom Test Context");
            logger.info("添加覆盖测试上下文共享变量" + key + "成功");
    	}catch(Exception e){
    		logger.error("添加覆盖测试上下文共享变量" + key + "失败");
    	}
    }
    
    /**
     * @Description 删除自定义测试上下文共享变量
     * @param key
     */
    public void delContext(String key){
    	try{
    		customContextPro.load(fr);
    		if( !isExistContext(key) ){
    			logger.error("删除的测试上下文共享变量" + key + "不存在，无需删除");
    		}else{
            	customContextPro.remove(key);
        		fw = new FileWriter(customContextProFile);
            	customContextPro.store(fw, "Custom Test Context");
            	logger.info("删除测试上下文共享变量" + key + "成功");
    		}
    	}catch(Exception e){
    		logger.error("删除测试上下文共享变量" + key + "失败");
    	}
    }
    
	/**
	 * @Description 修改自定义测试上下文共享变量
	 * @param key
	 * @param value
	 */
	public void setContext(String key, String value){
		try{
			customContextPro.load(fr);
			if( isExistContext(key) ){
				customContextPro.setProperty(key, value);
			}
    		fw = new FileWriter(customContextProFile);
        	customContextPro.store(fw, "Custom Test Context");
        	logger.info("修改测试上下文共享变量" + key + "成功");
    	}catch(Exception e){
    		logger.error("修改测试上下文共享变量" + key + "失败");
    	}
	}
	
	/**
	 * @Description 获取自定义测试上下文共享变量
	 * @param key
	 * @return
	 */
	public Object getContext(String key){
		Object obj = null;
		try{
			customContextPro.load(fr);
			if( isExistContext(key) ){
		    	obj = customContextPro.get(key);
		    	logger.info("获取测试上下文共享变量" + key + "成功");
			}else{
				logger.error("获取的测试上下文共享变量" + key + "不存在，无需获取");
			}
    	}catch(Exception e){
    		logger.error("获取测试上下文共享变量" + key + "失败");
    	}
		return obj;
	}
	
	/**
	 * @Description 判定自定义测试上下文共享变量是否存在
	 * @param key
	 */
	public boolean isExistContext(String key){
		boolean boo = false;
		try{
			customContextPro.load(fr);
			boo = customContextPro.containsKey(key);
	    	logger.info("判定测试上下文共享变量" + key + "是否存在成功");
    	}catch(Exception e){
    		logger.error("判定测试上下文共享变量" + key + "是否存在失败");
    	}
		return boo;
	}
	
	/**
	 * @Description 清理所有自定义测试上下文共享变量
	 */
	public void cleanContext(){
		try{
			customContextPro.load(fr);
			customContextPro.clear();
    		fw = new FileWriter(customContextProFile);
        	customContextPro.store(fw, "Custom Test Context");
	    	logger.info("清理所有自定义测试上下文共享变量成功");
    	}catch(Exception e){
    		logger.error("清理所有自定义测试上下文共享变量失败");
    	}
	}
}
