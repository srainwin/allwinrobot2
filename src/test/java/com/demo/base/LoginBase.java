/***
 * @author srainwin
 * @Description 启动浏览器和关闭浏览器，以及提供登录126邮箱参数
*/
package com.demo.base;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import com.demo.utils.LogConfiguration;

public class LoginBase {
	public static WebDriver driver;
	protected String baseurl;
	static Logger logger = Logger.getLogger(LoginBase.class.getName());

	@BeforeClass
	public void setup() {
		try {
			LogConfiguration.initLog(this.getClass().getSimpleName());
			logger.info("正启动浏览器");
			System.setProperty("webdriver.chrome.driver",
					"D:\\Selenium-Java-2.48.2\\chromedriver_win32\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			// driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			logger.info("浏览器启动成功");
			baseurl = "https://mail.126.com";
		} catch (Exception e) {
			logger.error("浏览器不能正常工作，请检查是不是被手动关闭或者其他原因", e);
		}
	}

	@AfterClass
	public void teardown() {
		try {
			driver.quit();
		} catch (Exception e) {
			logger.error("浏览器异常，无法关闭", e);
		}
	}

	/*
	 * @DataProvider(name = "usersFail") public Object[][] usersFail(){ return
	 * new Object[][]{ {"","","请输入帐号"}, {"","123456","请输入帐号"},
	 * {"helloxwr","","请输入密码"}, {"xwr","123456","帐号或密码错误"} };
	 * 
	 * }
	 * 
	 * @DataProvider(name = "usersSucess") public Object[][] usersSucess(){
	 * return new Object[][]{ {"helloxwr","Gmcc_1234"} }; }
	 */

	@DataProvider(name = "testdata")
	public Object[][] testData() {
		try {
			// 数据文件路径，一个测试类对应一个excel数据文件
			String dataname = this.getClass().getSimpleName();
			String datapath = "../src/test/resources/testdata/" + dataname + ".xlsx";
			// 获取工作簿
			InputStream is = new FileInputStream(datapath);
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			// 获取工作表
			XSSFSheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				workbook.close();
				return new Object[][] {};
			}
			// 获取工作表总行数，注意：首行行号是0
			int rowcount = (sheet.getLastRowNum() - sheet.getFirstRowNum()) + 1;
			List<Object[]> list = new ArrayList<Object[]>();
			// 遍历获取每行记录，除了第一行，第一行是数据列名称，所以rownum不从0开始
			for (int rownum = 1; rownum < rowcount; rownum++) {
				XSSFRow row = sheet.getRow(rownum);
				if (row == null) {
					continue;
				}
				// 获取每行记录的总列数
				int cellcount = row.getLastCellNum();
				Object[] cell = new Object[cellcount];
				// 遍历获取每行记录的每一列
				for (int cellnum = 0; cellnum < cellcount; cellnum++) {
					XSSFCell xssfcell = row.getCell(cellnum);
					if (xssfcell == null) {
						continue;
					}
					// 调用getValue方法处理xssfcell获取的多种类型值转化为string类型，并放到一个object一维数组中
					cell[cellnum] = getValue(xssfcell);
				}
				// 存储测试数据，第n行所有列数据存放到lsit列表中第n个Object[]对象
				list.add(cell);
			}
			workbook.close();
			// List<Object[]>数据转化为Object[][]数据，list的总数就是object二维数组中的一维数组总数
			int listcount = list.size();
			Object[][] result = new Object[listcount][];
			// 将list中的object一维数组成为object二维数组中的一维数组
			for (int i = 0; i < listcount; i++) {
				result[i] = list.get(i);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * @Description 用于testData()方法里处理cell的多种类型值转化为string类型
	 * @return
	 */
	private static String getValue(XSSFCell xssfcell) {
		if (xssfcell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(xssfcell.getBooleanCellValue());
		} else if (xssfcell.getCellType() == CellType.NUMERIC) {
			return String.valueOf(xssfcell.getNumericCellValue());
		} else {
			return String.valueOf(xssfcell.getStringCellValue());
		}
	}
}