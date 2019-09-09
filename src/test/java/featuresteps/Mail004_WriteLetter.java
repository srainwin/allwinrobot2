package featuresteps;

import org.apache.log4j.Logger;
import org.sikuli.script.Region;

import cucumber.PageContext;
import cucumber.TestContext;
import cucumber.api.java.zh_cn.假如;
import cucumber.api.java.zh_cn.当;
import cucumber.api.java.zh_cn.那么;
import io.cucumber.datatable.DataTable;
import selenium.SeleniumUtil;
import sikuli.SikuliUtil;

public class Mail004_WriteLetter {
	Logger logger = Logger.getLogger(Mail004_WriteLetter.class.getSimpleName());
	TestContext testContext;
	PageContext pageContext;
	SeleniumUtil seleniumUtil;
	SikuliUtil sikuliUtil;
	
	public Mail004_WriteLetter(TestContext context) {
		testContext = context;
		pageContext = testContext.getpageContext();
		seleniumUtil = testContext.getseleniumUtil();
		sikuliUtil = testContext.getsikuliUtil();
	}
	
	// 场景: 发送附件邮件
	@假如("^点击写信$")
	public void writeLetterClick() {
	    logger.info("sendAttachmentMail_step1:点击写信");
	    pageContext.setPagefile("homePage.json");
		seleniumUtil.click(pageContext.getElementLocator("writeLetters"));
	}

	@当("^填写收件人$")
	public void inputAddressee(DataTable datatable) {
	    logger.info("sendAttachmentMail_step2:填写收件人");
	    pageContext.setPagefile("writeLettersPage.json");
		String image = sikuliUtil.pickOneImage(pageContext.getImageLocator("addressee1"),pageContext.getImageLocator("addressee2"));
		Region region = sikuliUtil.getImageRight(image, 2, 20);
		String addressee = datatable.asMaps().get(0).get("addressee");
		sikuliUtil.keyboardPasteTextRegion(region, addressee);
	}

	@当("^填写主题$")
	public void inputTheme(DataTable datatable) {
	    logger.info("sendAttachmentMail_step3:填写主题");
		String image = sikuliUtil.pickOneImage(pageContext.getImageLocator("theme1"),pageContext.getImageLocator("theme2"));
		Region region = sikuliUtil.getImageRight(image, 2, 20);
		String theme = datatable.asMaps().get(0).get("theme");
		sikuliUtil.keyboardPasteTextRegion(region, theme);
	}

	@当("^关闭附件提示$")
	public void closeAppendixTips() {
	    logger.info("sendAttachmentMail_step4:关闭附件提示");
		String image = sikuliUtil.pickOneImage(pageContext.getImageLocator("appendixTips1"),pageContext.getImageLocator("appendixTips2"));
		Region region = null;
		if("appendixTips1".equals(image)){
			region = sikuliUtil.getInnerRegion(image, pageContext.getImageLocator("appendixTipsClose1"));
		}else{
			region = sikuliUtil.getInnerRegion(image, pageContext.getImageLocator("appendixTipsClose2"));
		}
		sikuliUtil.mouseClickRegion(region);
	}

	@当("^添加附件并上传$")
	public void addAttachments(DataTable datatable) {
	    logger.info("sendAttachmentMail_step5:添加附件并上传");
	    String exeName = datatable.asMaps().get(0).get("exeName");
	    String autoitFolderPath = testContext.getautoitFolderPath();
	    seleniumUtil.uploadFile(pageContext.getElementLocator("addAttachments"), exeName, autoitFolderPath);
		seleniumUtil.findElementByWait(15, pageContext.getElementLocator("attachProgressOK"));
	}

	@当("^填写写信内容$")
	public void inputWritingContent(DataTable datatable) {
	    logger.info("sendAttachmentMail_step6:填写写信内容");
		String image = sikuliUtil.pickOneImage(pageContext.getImageLocator("writingContent1"),pageContext.getImageLocator("writingContent2"));
		Region region = sikuliUtil.getImageBelow(image, 2, 30);
		sikuliUtil.mouseClickRegion(region);
		String WritingContent = datatable.asMaps().get(0).get("WritingContent");
		sikuliUtil.keyboardPasteTextRegion(region, WritingContent);
	}

	@当("^点击发送信件$")
	public void sendMailClick() {
	    logger.info("sendAttachmentMail_step7:点击发送信件");
		String image = sikuliUtil.pickOneImage(pageContext.getImageLocator("upSendButton1"),pageContext.getImageLocator("upSendButton2"));
		sikuliUtil.mouseClickImage(image, -55, 0, 2);
	}

	@那么("^成功发送邮件$")
	public void assertSendMailSucc() {
	    logger.info("sendAttachmentMail_step8:成功发送邮件");
		String image = sikuliUtil.pickOneImage(pageContext.getImageLocator("sendSucc1"),pageContext.getImageLocator("sendSucc2"));
		sikuliUtil.waitImage(image,20);
	}
}
