package testrunners;

import java.io.FileInputStream;
import java.util.Properties;

public class test {

	public static void main(String[] args) {
		try{
			Properties prop = new Properties();
			FileInputStream fileInput = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config/extent.properties");
			prop.load(fileInput);
			System.out.println(prop.keySet());
			for(Object key:prop.keySet()){
				System.out.println(key.toString() + "=" + prop.get(key));
			}
		}catch(Exception e){
			
		}

	}

}
