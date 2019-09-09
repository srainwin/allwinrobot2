#language: zh-CN
@writeLetter
功能: 写信
@sendAttachmentMail
场景: 发送附件邮件
假如 点击写信
当 填写收件人
|addressee|
|helloxwr@163.com|
而且 填写主题
|theme|
|发送邮件测试|
当 关闭附件提示
而且 添加附件并上传
|exeName|
|upload1.exe|
当 填写写信内容
|WritingContent|
|你好！\n欢迎来到我的自动化测试世界！|
而且 点击发送信件
那么 成功发送邮件