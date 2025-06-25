# StudentBookingSystem

说明：     

功能：   
    1、学生基本信息的管理，尤其是学生目的地。   
    2、学生定票的基本信息，尤其是价钱和车票目的地   
    3、能实现退票功能   
    4、能进行信息的统计和查询   
    5、操作员管理     
  
环境：   
   IDEA 2016.3   Intellij 
   SQL Server 2014    
   JavaFX   
   
效果截图:

登陆界面:


![](https://github.com/PhoeBe-NanMu/StudentBookingSystem/blob/master/pics/loginIn.PNG)


学生订票-查询已有订单:
![](https://github.com/PhoeBe-NanMu/StudentBookingSystem/blob/master/pics/%E5%AD%A6%E7%94%9F%E8%AE%A2%E7%A5%A8-%E7%9B%B4%E6%8E%A5%E6%9F%A5%E8%AF%A2.PNG)


学生订票-搜索-添加:
![](https://github.com/PhoeBe-NanMu/StudentBookingSystem/blob/master/pics/%E5%AD%A6%E7%94%9F%E8%AE%A2%E7%A5%A8-%E6%90%9C%E7%B4%A2.PNG)


学生订票-添加后查询:
![](https://github.com/PhoeBe-NanMu/StudentBookingSystem/blob/master/pics/%E5%AD%A6%E7%94%9F%E8%AE%A2%E7%A5%A8-%E6%B7%BB%E5%8A%A0%E5%90%8E%E6%9F%A5%E8%AF%A2.PNG)


学生订票-退票后查询:
![](https://github.com/PhoeBe-NanMu/StudentBookingSystem/blob/master/pics/%E5%AD%A6%E7%94%9F%E8%AE%A2%E7%A5%A8-%E9%80%80%E7%A5%A8%E5%90%8E%E6%9F%A5%E8%AF%A2.PNG)


操作员-查找之前：
![](https://github.com/PhoeBe-NanMu/StudentBookingSystem/blob/master/pics/%E6%93%8D%E4%BD%9C%E5%91%98-%E6%9F%A5%E6%89%BE%E4%B9%8B%E5%89%8D.PNG)


操作员-按姓名查找:
![](https://github.com/PhoeBe-NanMu/StudentBookingSystem/blob/master/pics/%E6%93%8D%E4%BD%9C%E5%91%98-%E6%8C%89%E5%A7%93%E5%90%8D%E6%9F%A5%E6%89%BE.PNG)


操作员-查看全部订单:
![](https://github.com/PhoeBe-NanMu/StudentBookingSystem/blob/master/pics/%E6%93%8D%E4%BD%9C%E5%91%98-%E6%9F%A5%E7%9C%8B%E5%85%A8%E9%83%A8%E8%AE%A2%E5%8D%95.PNG)


操作员-完成退票:
![](https://github.com/PhoeBe-NanMu/StudentBookingSystem/blob/master/pics/%E6%93%8D%E4%BD%9C%E5%91%98-%E5%AE%8C%E6%88%90%E9%80%80%E7%A5%A8.PNG)

## 配置（一定要看）
1. 先下载JavaFX SDK， 下载版本： 24.0.1
2. 下载好请打开你的 IDE， 然后点开project structure

3. 之后点开Global respitory，添加你下载好的javafx 24.0.1 -> lib 里面的jar文件进入这里

4. 导入好后鼠标右键你导入好的包，然后选择add modle 把他丢进要运行的程序里面，之后点击apply，然后确定


## 运行配置（一定要看）
1. 点击你现在正在运行的class，然后点击edit configuration

2. 点击add new, 然后选择application

3. 然后在mainclass 选择要运行的文件

4. 然后请在modify 那边选择vm option 添加虚拟选项

5. 然后要添加虚拟选项了
* Macbook ： --module-path /path/to/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml
打个比方我是macbook，我的vm option就是：--module-path /Users/hillzhang/Libraries/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml

***记得path to这个东西是指你电脑里下载的javafx lib 的地址***
* Window ： --module-path "\path\to\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml


6. 点击apply 后运行就可以正常的跑啦