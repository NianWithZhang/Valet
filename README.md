# Valet
for Software Engineering and Mobile Application Development courses





##### 逆地理编码

http://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding-abroad#service-page-anchor-1-0

[GET] http://api.map.baidu.com/geocoder/v2/?location=38.418651,114.645415&output=json&ak=uNrkc1iislhkWLNcEHGS64ycQumvnvoX

百度地图Key uNrkc1iislhkWLNcEHGS64ycQumvnvoX

##### 获取天气 短信验证码

[GET] http://v.juhe.cn/weather/index?cityname=石家庄市&key=ac3acab2283aa7080a37c17cd722b5d5

[GET] http://v.juhe.cn/sms/send?mobile=18117168675&tpl_id=118938&tpl_value=%23code%23=123456&key=7ba5aaf1d1c49d547852e71b848bd1fe 

https://www.juhe.cn/myData



##### 淘宝图片搜索

[POST] https://s.taobao.com/image (Body中附带form-data imgfile:搜索的图片)

Response中name作为搜索页面tfsid

https://s.taobao.com/search?tfsid=TB11nL8tgDqK1RjSZSyXXaxEVXa&app=imgsearch

