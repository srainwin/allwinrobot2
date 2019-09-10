# allwinrobot2
BDD行为驱动的自动化测试  
java + cucumber + selenium + sikuli + maven + testng + AllureReport  

## 主要工具
java-jdk8:主编程语言  
maven：项目管理构建工具  
testng：单元测试框架  
cucumber：bdd行为驱动开发框架  
selenium：web自动化测试工具  
sikuli：基于图像识别自动化测试工具  
allure：测试报告工具  
log4j：日志管理  
jsonpath：json解析页面对象库  

## 主要功能
POM模式设计维护用例  
cucumber业务故事与代码分层协作方式进行自动化测试编写  
cucumber自带多种方式数据驱动功能  
selenium常用api封装提供调用ui自动化操作  
sikuli常用api封装提供调用图像识别自动化操作  
jdbc连接数据库操作  
可调用autoit的exe脚本进行windows自动化操作  
提供选择ie、chrome、firefox和ghost浏览器进行测试  
支持并行分布式执行用例  
添加cookies免登录  
日志记录用例运行信息  
测试上下文内容共享  
提供美观步级的测试报告  
失败用例可截图至报告  
失败用例可重跑  

# 框架分层
* src/main/java  
  * browser  
  	* SelectLocalBrowser.java：选择本地浏览器驱动  
  	* SelectRemoteBrowser.java：选择远程浏览器驱动  
  * cucumber  
  	* CustomContext.java：测试上下文-自定义，放到TestContext中共享  
  	* PageContext.java：测试上下文-POM模式的页面，放到TestContext中共享  
  	* TestContext.java：测试上下文共享，每个故事代码实现类添加构造方法带TestContext形参  
  * database  
  	* JdbcUtil.java：jdbc数据库操作封装  
  * log  
	* LogConfiguration.java：日志初始化配置  
  * selenium  
  	* SeleniumUtil.java：selenium api封装  
  * sikuli  
  	* SikuliUtil.java：sikuli api封装  
* src/main/resources  
  * config  
  	* baseContext.properties：程序运行基础参数，放到TestContext中共享  
  	* customContext.properties：自定义参数，与CustomContext一起配置放到TestContext中共享  
  	* driver.properties：浏览器驱动路径参数配置，与SelectLocalBrowser和SelectRemoteBrowser一起配置  
  	* cookies.txt：由SeleniumUtil类的cookiesSaveInFile方法保存浏览器cookies信息到此，再由addcookies方法获取添加cookies  
  * driver：浏览器驱动文件  
* src/test/java  
  * pages：json形式的页面元素定位集  
  * features：cucumber-feature形式的业务故事集  
  * featuresteps：cucumber-step形式的业务故事代码实现集，feature文件名字与step脚本名要相同  
  * runners  
  	* BaseRun.java：基础运行的run，含testng的Before和After，每个run必须继承此类  
  	* TestRun.java：测试运行的run  
  	* ReRun.java：失败场景重跑的run  
  	* testng.xml：testng配置文件  
* src/test/resources   
  * autoitScripts:存放autoit的exe脚本  
  * sikuliImages:存放sikuli进行图像识别所需的png图像文件  
* target  
  * result  
    * allure-results:存放allure测试报告  
    * cucumber-reports:存放cucumber测试报告  
    * logs:存放每天用例运行日志  
    * maven-testng-report:存放testng自带的测试报告  
    * rerun.txt：失败场景需重跑记录  

## pom说明
pom.xml：默认使用了nexus私服仓库  
pom2.txt：使用nexus私服仓库的配置信息，需要时复制到pom.xml  
pom3.txt：不使用nexus私服仓库配置信息，需要时复制到pom.xml  

## 多线程执行说明
自定义线程数要在pom文件的maven-surefire-plugin里设置dataproviderthreadcount属性值，不设置时默认为10个线程  

## selenium grid分布式测试使用说明
(1)准备多台服务器并能互相网络访问，vm虚拟机要用桥接网络方式，并每台机正确安装各类浏览器  

(2)下载grid包  
selenium-server-standalone-3.141.59.jar  

(3)启动hub节点为控制端（hub机执行），然后访问http://localhost:4444/grid/console  
java -jar selenium-server-standalone-3.141.59.jar -role hub  

(4)建立node节点连接（node机执行），然后刷新访问http://localhost:4444/grid/console  
java -Dwebdriver.chrome.driver="D:/snc/workspace2/allwinrobot2/src/main/resources/driver/chromedriver.exe" -Dwebdriver.gecko.driver="D:/snc/workspace2/allwinrobot2/src/main/resources/driver/geckodriver.exe" -jar selenium-server-standalone-3.141.59.jar -role node -host 192.168.1.101 -hub http://192.168.1.100:4444/grid/register -browser browserName=chrome,seleniumProtocol=WebDriver,maxInstances=5,platform=WINDOWS -browser browserName=firefox,seleniumProtocol=WebDriver,maxInstances=5,platform=WINDOWS  

(5)testng.xml/debug.xml填写isRemote和huburl参数值  

(6)使用分布式执行上传文件用例时注意每台机子都要存放相同路径的上传文件  

(7)当hub节点同时也为node节点的时候，启动的浏览器会置底没显示，也就是会被其他已打开的应用遮挡，因此建议运行程序时先把所有应用关闭或最小化，除非浏览器被遮挡运行也不影响功能操作  

(8)当开启selenium grid分布式时，不一定要开启sikuli vnc分布式，除非有使用图像识别的用例运行

## sikuli vnc分布式测试使用说明
(1)准备多台服务器并能互相网络访问  

(2)每台服务器上安装vncserver并启动，options的security要配置prefer on和vnc password  

(3)testng.xml/debug.xml填写isVNC和vncPassword参数值  

(4)testng.xml/debug.xml配置的并发数不能大于分布式服务器总数，因为一个sikuli的screen只对应一个屏幕、鼠标和键盘做操作   

(5)建议每台服务器屏幕分辨率与开发环境屏幕分辨率一致，否则会很容易报错findfailed找不到图像，另外图片的大小、宽高、色差、像素强度等等不一致也容易报错findfailed找不到图像，即同一个图像在不同渲染场景容易findfailed，这是当前sikuli的缺点  

(6)对于上一点使用说明，sikuli作者暂未提供解决方案，查找官网问题论坛中发现有提出过同样问题，作者回复建议是使用image图像集存放多个来自不同渲染环境的目标区域图像，然后迭代访问image图像集的图像直到其中一个找成功即可，作者另一个建议就是利用第三方开源命令行工具ImageMagick进行图像处理但难度较高，本框架决定接受使用第一个建议并封装方法到SikuliUtil.java的pickOneImage(String... images)方法  

(7)当开启sikuli vnc分布式测试时，也必须开启selenium grid分布式测试，因为vnc使用的ip是从selenium grid的session里来获取的  

(8)sikuli输入文字时注意当前输入法的影响  

(9)sikuli的type方法只能输入英文，输入中文用paste方法，但vnc时paste方法输入中文经常会乱码，不稳定，已向作者提出问题未能解决  

(10)若vnc远程机是node节点而不是本地hub节点，则paste方法输入的是系统原来粘贴板的内容，已向作者提出问题，但作者回复表示无能为力（I have no idea and no experience with VNC, sorry.）  

(11)鉴于8、9、10三点问题的严重性，建议测试数据输入需求均使用英文，否则sikuli vnc分布式无法处理  

(12)建议需要使用图像识别功能但有涉及中文输入的用例单独一个testng.xml测试套件在本地运行而不用远程vnc  

## allure测试报告使用说明
(1)测试脚本运行后，allure只会产生一个allure-result目录文件，该目录文件是不能直接查看报告的  

(2)查看allure报告方法一，使用jenkins的allure report插件  

(3)查看allure报告方法二，本地安装allure命令行工具allure-commandline，下载解压allure-commandline包后配置bin目录到系统path变量中，然后cmd运行命令生成html报告，例如：allure generate D:\allure-results -o D:\allure-results\html，最后用firefox打开index.html文件，记得用firefox，其他浏览器会存在跨域协议问题（跨域请求仅支持协议：http, data, chrome, chrome-extension, https, chrome-extension-resource），但是Filefox支持file协议下的AJAX请求  

(4)查看allure报告方法三，同样安装allure命令行工具，cmd运行命令如下：allure serve D:\allure-results，然后会自动用默认浏览器打开这个网页http://192.168.175.1:49081/index.html，同样需要复制到firefox浏览器才可查看得当  

## 实际工作流说明
(1)确定基础运行必要参数：/allwinrobot2/src/main/resources/config/baseContext.properties  

(2)确定feature故事集和测试数据表：/allwinrobot2/src/test/java/features  

(3)确定page页面元素集：/allwinrobot2/src/test/java/pages  

(4)确定每个页面元素集的元素定位值(如果产品未有成品可以放到有产品成品后再做)：/allwinrobot2/src/test/java/pages  
（基础共享变量已固定， page共享自动获取，这两个不需调整，只需调整自定义的共享变量）  

(5)确定step故事步骤脚本集并且每个脚本添加测试上下文共享的构造方法：/allwinrobot2/src/test/java/steps  

(6)确定测试上下文共享内容的调整：/allwinrobot2/src/main/resources/config/customContext.properties  

(7)确定runner脚本并配置testng.xml：/allwinrobot2/src/test/java/runners  

(8)调试脚本完成  

## 附：cucumber与BDD指导
(1)不管同一个feature还是多个feature，scenario里的步骤定义都不允许重复，若非要重复的话需要把步骤定义的函数名改为不重复，否则出现cucumber.runtime.DuplicateStepDefinitionException异常  

(2)产品迭代增删改测试场景时记得备份feature和step文件，也可以不删除旧场景，但要用tag标记场景以确定运行集  

(3)feature文件中英文关键字对照表，feature首行加#language: zh-CN  
| feature          | "功能"  
| background       | "背景"  
| scenario         | "场景", "剧本"  
| scenario_outline | "场景大纲", "剧本大纲"  
| examples         | "例子"  
| given            | "* ", "假如", "假设", "假定"  
| when             | "* ", "当"  
| then             | "* ", "那么"  
| and              | "* ", "而且", "并且", "同时"  
| but              | "* ", "但是"  
| given (code)     | "假如", "假设", "假定"  
| when (code)      | "当"  
| then (code)      | "那么"  
| and (code)       | "而且", "并且", "同时"  
| but (code)       | "但是"  

(4)feature文件关键字解释:  
Feature：用来描述我们需要测试的功能。  
background：用来描述每个测试场景的背景，相当于当前feature中每个场景的前置条件，background与Scenario同样有Given、When等描述，但前置条件不要过于复杂。  
Scenario: 用来描述测试场景。  
scenario_outline: 也用来描述测试场景，但必须带有example关键字做测试数据。  
Given： 当前场景的前置条件。  
When、and、but: 描述测试步骤触发条件。  
Then: 对步骤结果断言。  

(5)feature文件添加测试数据形式有：  
描述中带引号传数、examples例子和DataTable数据表，其中DataTable可用raw()和asMap()方法在脚本中获取数据表数据; 
引号传数： feature步骤描述中引号内写上对应参数"参数"，且stepDefinition步骤描述用正则表达式匹配参数\"(.*)\";  
examples：会循环场景跑多行数据，但feature步骤描述中必须写上对应参数"<参数>"，且step脚本步骤描述用正则表达式匹配参数\"(.*)\";  
DataTable：要在stepDefinition脚本中自行编写循环取多行数据跑，但步骤描述中不需要写上对应参数;  

examples作用域是scenario，DataTable作用域是scenario中的步骤；  

还有DataTable分为列表数据表和地图数据表，分别如下获取数据：  
列表数据表（默认无表头）：  
public void xxx(DataTable datatable){  
	List<List<String>> data = datatable.asLists();  
	data.get(行数).get(列数);//默认无表头行数从0开始取，若把首行当表头使用则行数从1开始取  
}  
地图数据表（有表头字段名）：  
public void xxx(DataTable datatable){  
	List<Map<String,String>> data = datatable.asMaps();  
	data.get(行数).get("列名");  
}  

(6)featurestep脚本步骤描述与参数：  
描述中可用正则表达式或黄瓜表达式匹配参数（测试数据）  

正则表达式如下：  
用^$符号括起步骤描述，中间通常用\"(.*)\"匹配任意参数，参数为(String dataname)  
举例：  
@Given("^input username is \"(.*)\"$")  
public void input_username_is (String dataname) {}  

黄瓜表达式如下：  
{int}	匹配整数，例如71或-19。  
{float}	匹配浮动，例如3.6，.8或-9.2。  
{word}	匹配没有空格的单词，例如banana（但不是banana split）  
{string}	匹配单引号或双引号字符串，例如"banana split"或'banana split'（但不是banana split）。仅提取引号之间的文本。引号本身被丢弃。  
{} 	匹配任何东西（/.*/）。  
举例：  
@Given("input username is {string}")  
public void input_username_is (String string) {}  

(7)cucumber是按feature文件名字母数字顺序执行的，如果功能之间有依赖的话请设计好feature文件名，行业共识建议feature之间尽量解耦，要求每个场景都是独立可运行的。  

(8)行为驱动开发（BDD）是一种软件开发的协作方法，可以弥合业务和IT之间的通信差距。BDD可帮助团队更准确地沟通需求，及早发现缺陷并生成随时间保持可维护的软件。它可以帮助团队创建整个团队可以理解的业务需求。指定的例子揭示了人们可能甚至不知道的误解。实践BDD的团队专注于预防缺陷而不是发现缺陷。这样可以减少返工，缩短产品上市时间。  

(9)您的Cucumber功能应该推动您的实施，而不是反映它。这意味着应该在实现该功能的代码之前编写Cucumber功能。通俗的说就是在产品代码开发前就要思考测试场景/用例，并且是产品经理、需求经理、开发人员、测试人员和客户都要以研讨会形式参与产品使用场景的思考发掘，最终形成cucumber的feature故事集并反复评审。这是要不同于以往只是测试人员在写测试用例甚至无暇顾及写用例，单纯的经验与探索性测试不足以保障产品质量。  

(10)BDD教程参考官网介绍：  
https://cucumber.io/docs/guides/bdd-tutorial/  
https://cucumber.io/docs/bdd/who-does-what/  