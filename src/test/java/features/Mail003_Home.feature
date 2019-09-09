#language: zh-CN
@home
功能: 邮箱首页

背景: 在门户中选择首页Tab
假如 点击首页Tab
那么 成功打开首页Tab

@homeUnreadMail
场景: 进入用户概览的未读邮件
假如 在用户概览处点击未读邮件
那么 成功打开未读邮件Tab并正常显示
	|overviewUnreadMailTab|
	|未读邮件|

@homeTodoMail
场景: 进入用户概览的待办邮件
假如 在用户概览处点击待办邮件
那么 成功打开待办邮件Tab并正常显示
	|overviewTodoMailTab|
	|待办邮件|

@homeContactMail
场景: 进入用户概览的联系人邮件
假如 在用户概览处点击联系人邮件
那么 成功打开联系人邮件Tab并正常显示
	|overviewContactMailTab|
	|联系人邮件|

@homeSafetyDegree
场景: 进入用户概览的安全度
假如 在用户概览处点击安全度
那么 成功打开设置Tab并正常显示邮箱安全设置内容
	|overviewSafetyDegreeSign|
	|邮箱健康指数|

@homeLoginProtect
场景: 进入用户概览的登录保护
假如 在用户概览处点击登录保护
那么 成功打开设置Tab并正常显示邮箱登录二次验证内容
	|overviewLoginProtectSign|
	|邮箱登录二次验证|

@homeDailyLife
场景: 进入用户概览的每日生活
假如 在用户概览处点击每日生活
那么 成功打开严选每日推荐Tab并正常显示广告推荐内容
	|overviewDailyLifeTab|
	|严选每日推荐|