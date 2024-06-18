from PySide2.QtWidgets import QApplication, QMainWindow, QTableWidgetItem, QPushButton, QPlainTextEdit, QMessageBox
from PySide2 import QtCore, QtWidgets
from PySide2.QtUiTools import QUiLoader
from PySide2.QtCore import Qt, QDate, QDateTime
import pymssql
from datetime import date, datetime
from decimal import Decimal

servername = "LAPTOP-07LLA2EL\MSSQLSERVER2022"
port = 1433
user = "sa"
password = "12345678"
database = "DataBase"
charset = "GBK"
connect = pymssql.connect(servername, user, password, database, charset)

class UI:
    def __init__(self):
        self.KID = None
        if connect:
            print("连接成功")
        # 登录界面初始化
        self.loginui = QUiLoader().load('登录界面.ui')
        self.loginui.buttonLogin.clicked.connect(self.Clac2login)  # 按钮登录信号链接
        self.loginui.button2KZright.clicked.connect(self.Clac2getID) # 按钮注册信号链接
        self.loginui.button2Krekey.clicked.connect(self.Clac2reKey)

        self.mainui = QUiLoader().load('主界面.ui')

        self.mainui.button_shopping.clicked.connect(lambda: self.mainui.stackedWidget.setCurrentIndex(0))
        self.mainui.button_dingdan.clicked.connect(lambda: self.mainui.stackedWidget.setCurrentIndex(1))
        self.mainui.button_mine.clicked.connect(lambda: self.mainui.stackedWidget.setCurrentIndex(2))
        self.mainui.button_swappig.clicked.connect(lambda: self.mainui.stackedWidget.setCurrentIndex(3))

        self.mainui.button2GSright.clicked.connect(self.Clac2Dright)
        self.mainui.button2Dright.clicked.connect(self.Clac2Dget)
        self.mainui.button2Dsearchgeted.clicked.connect(self.Clac2Dsearchgeted)
        self.mainui.button2Dsgetcancel.clicked.connect(self.Clac2Dsgetedcancel)
        self.mainui.button2Dsearchall.clicked.connect(self.Clac2DDsearchall)
        self.mainui.button2Dsacancel.clicked.connect(self.Clac2Dsacancel)
        self.mainui.button2f5.clicked.connect(self.Clac2f5)
        self.mainui.button2Kupdate.clicked.connect(self.Clac2update)
        self.mainui.button1.clicked.connect(self.Clac2b1)
        self.mainui.button2.clicked.connect(self.Clac2b2)
        self.mainui.button3.clicked.connect(self.Clac2b3)

        self.mainui.table2Dunget.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)
        self.mainui.table2Dgeted.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)
        self.mainui.table2D.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)

    # 登录信号链接函数
    def Clac2login(self):
        self.UserName = self.loginui.textUser.text()  # 获取用户名
        self.KID = self.UserName
        self.KeyWord = self.loginui.textPwd.text()  # 获取密码
        cursor = connect.cursor()
        sql_select = "SELECT 客户ID, 客户登录密码 FROM 客户"
        cursor.execute(sql_select)
        results = cursor.fetchall()
        sql_select = "SELECT COUNT(*) FROM 客户"
        cursor.execute(sql_select)
        flag = cursor.fetchall()
        flag = int(flag[0][0])
        for result in results:
            if str(self.UserName) == result[0] and str(self.KeyWord) == result[1]:
                QMessageBox.about(self.loginui, "提示", "登陆成功!\n欢迎回来!")  # 弹窗
                self.loginui.close()
                self.mainui.show()
                self.table2KInf()
                self.table2KSinfShow()
                self.table2Dallshow()
                self.Scoreshow()
                break
            else:
                flag = flag - 1
                continue
        if not flag:
            QMessageBox.about(self.loginui, "提示", "客户ID或密码错误, 请重试")  # 弹窗

    def Clac2getID(self):
        cursor = connect.cursor()
        query = "select count(*) from 客户"
        cursor.execute(query)
        results = cursor.fetchall()
        flag = results[0][0]
        if not self.loginui.text2KCom.text():
            QMessageBox.about(self.loginui, "提示", "公司名称不可为空\n请重新输入!")  # 弹窗
            return
        if not self.loginui.text2KName.text():
            QMessageBox.about(self.loginui, "提示", "姓名不可为空\n请重新输入!")  # 弹窗
            return
        if not self.loginui.text2KJob.text():
            QMessageBox.about(self.loginui, "提示", "职务不可为空\n请重新输入!")  # 弹窗
            return
        if not self.loginui.text2KCity.text():
            QMessageBox.about(self.loginui, "提示", "城市不可为空\n请重新输入!")  # 弹窗
            return
        if not self.loginui.text2KArea.text():
            QMessageBox.about(self.loginui, "提示", "地区不可为空\n请重新输入!")  # 弹窗
            return
        if not self.loginui.text2KCountry.text():
            QMessageBox.about(self.loginui, "提示", "国家不可为空\n请重新输入!")  # 弹窗
            return
        if not self.loginui.text2Kecode.text():
            QMessageBox.about(self.loginui, "提示", "邮政编码不可为空\n请重新输入!")  # 弹窗
            return
        if not self.loginui.text2Adrr.text():
            QMessageBox.about(self.loginui, "提示", "详细地址不可为空\n请重新输入!")  # 弹窗
            return
        if not self.loginui.text2Kphone.text():
            QMessageBox.about(self.loginui, "提示", "电话不可为空\n请重新输入!")  # 弹窗
            return
        if not self.loginui.text2KID.text():
            QMessageBox.about(self.loginui, "提示", "客户ID不可为空\n请重新输入!")  # 弹窗
            return
        if not self.loginui.text2Kkey.text():
            QMessageBox.about(self.loginui, "提示", "密码不可为空\n请重新输入!")  # 弹窗
            return

        KID = self.loginui.text2KID.text()
        KCom = self.loginui.text2KCom.text()
        KName = self.loginui.text2KName.text()
        KJob = self.loginui.text2KJob.text()
        KAdrr = self.loginui.text2Adrr.text()
        KCity = self.loginui.text2KCity.text()
        KArea = self.loginui.text2KArea.text()
        KCountry = self.loginui.text2KCountry.text()
        Kecode = self.loginui.text2Kecode.text()
        Kphone = self.loginui.text2Kphone.text()
        Kfox = self.loginui.text2Kfox.text()
        Kkey = self.loginui.text2Kkey.text()

        if len(Kphone) < 10 :
            QMessageBox.about(self.loginui, "提示", "手机号长度应大于等于10个字符\n请重新输入!")  # 弹窗
            return
        if len(KID) < 5:
            QMessageBox.about(self.loginui, "提示", "ID号长度应大于等于5个字符\n请重新输入!")  # 弹窗
            return


        cursor = connect.cursor()
        query = "select 客户ID from 客户"
        cursor.execute(query)
        check_results = cursor.fetchall()
        for res in check_results:
            if KID == res[0]:
                QMessageBox.about(self.loginui, "提示", "该ID号已被注册\n请重新输入!")  # 弹窗
                return

        data = (KID, KCom, KName, KJob, KAdrr, KCity, KArea, KCountry, Kecode, Kphone, Kfox, Kkey)
        cursor = connect.cursor()
        sql_insert = "INSERT INTO 客户 (客户ID, 客户公司名称, 联系人姓名, 联系人职务, 客户详细地址, 客户所在城市, 客户所在地区, 客户所在国家, 客户所在地邮政编码, 客户电话, 客户传真, 客户登录密码) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
        cursor.execute(sql_insert, data)
        connect.commit()
        cursor.close()
        cursor = connect.cursor()
        query = "select count(*) from 客户"
        cursor.execute(query)
        results = cursor.fetchall()
        count = results[0][0]
        if count > flag:
            QMessageBox.about(self.loginui, "提示", "注册成功!")  # 弹窗
        else:
            QMessageBox.about(self.loginui, "提示", "注册失败请检查输入内容!")  # 弹窗

    def Clac2reKey(self):
        if self.loginui.text2KmyID.text():
            KID = self.loginui.text2KmyID.text()
            cursor = connect.cursor()
            query = "select 客户登录密码 from 客户 where 客户ID = %s"
            cursor.execute(query, (KID, ))
            results = cursor.fetchall()
            if results:
                query = "update 客户 set 客户登录密码 = '12345678' where 客户ID = %s"
                cursor.execute(query, (KID, ))
                connect.commit()
                cursor.close()
                QMessageBox.about(self.loginui, "提示", "重置密码成功\n现在密码为:12345678")  # 弹窗
            else:
                QMessageBox.about(self.loginui, "提示", "不存在的ID号码\n请重新输入!")  # 弹窗

    # 下单信号函数
    def table2KSinfShow(self):
        cursor = connect.cursor()
        query = "select 产品ID, 产品名称, 产品单位数量, 产品单价 from 产品"
        cursor.execute(query)
        results = cursor.fetchall()
        self.mainui.table2Kshopping.setRowCount(len(results))

        for i in range(len(results)):
            for j in range(4):
                item = OnlyRead_tableWidgetItem(str(results[i][j]))
                self.mainui.table2Kshopping.setItem(i, j, item)

    def Clac2Dright(self):
        result_list = []
        last_cell_value = None
        first_cell_value = None
        mid_cell_value = None
        sum = 0
        for row in range(self.mainui.table2Kshopping.rowCount()):
            mid_col = 3
            last_column = 4
            if self.mainui.table2Kshopping.item(row, last_column) is not None:
                last_cell_value = self.mainui.table2Kshopping.item(row, last_column).text()
                mid_cell_value = self.mainui.table2Kshopping.item(row, mid_col).text()
                first_cell_value = self.mainui.table2Kshopping.item(row, 0).text()
            if last_cell_value:
                result_list.append((first_cell_value, mid_cell_value, last_cell_value))
            last_cell_value = None
            first_cell_value = None
            mid_cell_value = None

        if result_list:
            text2Dname = self.mainui.text2KName.text()
            text2DAdrr = self.mainui.text2KAddr.text()
            text2Dbtime = datetime.now()
            text2DCountry = self.mainui.text2KCountry.text()
            text2DCity = self.mainui.text2KCity.text()
            text2DArea = self.mainui.text2KArea.text()
            text2Decode = self.mainui.text2Kecode.text()
            cursor = connect.cursor()
            query = "SELECT MAX(订单ID) FROM 订单"
            cursor.execute(query)
            results = cursor.fetchall()
            new_ID = int(results[0][0]) + 1
            data = (new_ID, text2Dbtime,  text2Dname, text2DAdrr, text2DCountry, text2DCity, text2DArea, text2Decode, self.KID)
            cursor = connect.cursor()
            sql_insert = "INSERT INTO 订单 (订单ID, 订单订购日期, 订单货主名称, 订单货主详细地址, 订单货主所在国家, 订单货主所在城市, 订单货主所在地区, 订单货主所在地邮政编码, 客户ID) VALUES (%d, %s, %s, %s, %s, %s, %s, %s, %s)"
            cursor.execute(sql_insert, data)
            sql_update = "update 客户 set 客户总积分 = %s"
            connect.commit()
            cursor.close()

            cursor = connect.cursor()
            for result in result_list:
                money = Decimal(float(result[1]) * int(result[2]))
                sum = sum + round(money, 4)
                data = (new_ID, int(result[0]), int(result[2]), round(money, 4))
                sql_insert = "INSERT INTO 订单明细 (订单ID, 产品ID, 数量, 单项产品总金额) VALUES (%d, %d, %d, %s)"
                cursor.execute(sql_insert, data)
                connect.commit()
            cursor.close()

            QMessageBox.about(self.mainui, "提示", "下单成功, 共花费" + str(sum) + "元")
            cursor = connect.cursor()
            sql_select = "select 客户总积分 from 客户 where 客户ID = %s"
            cursor.execute(sql_select, (self.KID, ))
            results = cursor.fetchall()
            res = results[0][0]
            if res == None:
                res = 0
            sql_update = "update 客户 set 客户总积分 = %s where 客户ID = %s"
            cursor.execute(sql_update, (Decimal(Decimal(res) + sum * 10), self.KID, ))
            connect.commit()
            self.Scoreshow()
            cursor.close()

            # 遍历所有行
            for row in range(self.mainui.table2Kshopping.rowCount()):
                # 删除最后一列中的数据
                self.mainui.table2Kshopping.setItem(row, 4, QTableWidgetItem(""))

    def Clac2Dget(self):
        qdate = self.mainui.datatime2Drig.dateTime()
        qdatetime = qdate.toPython()
        ID = self.mainui.text2DID.text()
        cursor = connect.cursor()
        query = "UPDATE 订单 SET 订单到货日期 = %s WHERE 订单ID = %s"
        cursor.execute(query, (qdatetime, ID,))
        connect.commit()
        cursor.close()
        QMessageBox.about(self.mainui, "提示", "签收成功")  # 弹窗
        self.table2Dallshow()  # 更新数据

    def Clac2f5(self):
        self.table2Dallshow()
        self.table2KSinfShow()
        self.table2KSinfShow()
        self.Scoreshow()

    def table2KInf(self):
        cursor = connect.cursor()
        query = "SELECT 客户公司名称, 联系人姓名, 联系人职务, 客户所在城市, 客户所在地区, 客户所在国家, 客户详细地址, 客户所在地邮政编码, 客户电话, 客户传真, 客户登录密码 FROM 客户 where 客户ID = %s"
        cursor.execute(query, (self.KID,))
        resluts = cursor.fetchall()
        res = resluts[0]
        self.mainui.text2KCom.setText(res[0])
        self.mainui.text2KName.setText(res[1])
        self.mainui.text2KJob.setText(res[2])
        self.mainui.text2KCity.setText(res[3])
        self.mainui.text2KArea.setText(res[4])
        self.mainui.text2KCountry.setText(res[5])
        self.mainui.text2KAddr.setText(res[6])
        self.mainui.text2Kecode.setText(res[7])
        self.mainui.text2KPhone.setText(res[8])
        self.mainui.text2Kfox.setText(res[9])
        self.mainui.text2KKeyWord.setText(res[10])

    def Clac2update(self):
        cursor = connect.cursor()
        query = "select count(*) from 客户"
        cursor.execute(query)
        results = cursor.fetchall()
        flag = results[0][0]
        if not self.mainui.text2KCom.text():
            QMessageBox.about(self.mainui, "提示", "公司名称不可为空\n请重新输入!")  # 弹窗
            return
        if not self.mainui.text2KName.text():
            QMessageBox.about(self.mainui, "提示", "姓名不可为空\n请重新输入!")  # 弹窗
            return
        if not self.mainui.text2KJob.text():
            QMessageBox.about(self.mainui, "提示", "职务不可为空\n请重新输入!")  # 弹窗
            return
        if not self.mainui.text2KCity.text():
            QMessageBox.about(self.mainui, "提示", "城市不可为空\n请重新输入!")  # 弹窗
            return
        if not self.mainui.text2KArea.text():
            QMessageBox.about(self.mainui, "提示", "地区不可为空\n请重新输入!")  # 弹窗
            return
        if not self.mainui.text2KCountry.text():
            QMessageBox.about(self.mainui, "提示", "国家不可为空\n请重新输入!")  # 弹窗
            return
        if not self.mainui.text2Kecode.text():
            QMessageBox.about(self.mainui, "提示", "邮政编码不可为空\n请重新输入!")  # 弹窗
            return
        if not self.mainui.text2KAddr.text():
            QMessageBox.about(self.mainui, "提示", "详细地址不可为空\n请重新输入!")  # 弹窗
            return
        if not self.mainui.text2KPhone.text():
            QMessageBox.about(self.mainui, "提示", "电话不可为空\n请重新输入!")  # 弹窗
            return
        if not self.mainui.text2KKeyWord.text():
            QMessageBox.about(self.mainui, "提示", "密码不可为空\n请重新输入!")  # 弹窗
            return

        KCom = self.mainui.text2KCom.text()
        KName = self.mainui.text2KName.text()
        KJob = self.mainui.text2KJob.text()
        KAdrr = self.mainui.text2KAddr.text()
        KCity = self.mainui.text2KCity.text()
        KArea = self.mainui.text2KArea.text()
        KCountry = self.mainui.text2KCountry.text()
        Kecode = self.mainui.text2Kecode.text()
        Kphone = self.mainui.text2KPhone.text()
        Kfox = self.mainui.text2Kfox.text()
        Kkey = self.mainui.text2KKeyWord.text()

        if len(Kphone) < 10:
            QMessageBox.about(self.loginui, "提示", "手机号长度应大于等于10个字符\n请重新输入!")  # 弹窗
            return

        data = (KCom, KName, KJob, KAdrr, KCity, KArea, KCountry, Kecode, Kphone, Kfox, Kkey, self.KID)
        cursor = connect.cursor()
        sql_insert = "update 客户  set 客户公司名称 = %s, 联系人姓名 = %s, 联系人职务 = %s, 客户详细地址 = %s, 客户所在城市 = %s, 客户所在地区 = %s, 客户所在国家 = %s, 客户所在地邮政编码 = %s, 客户电话 = %s, 客户传真 = %s, 客户登录密码 = %s where 客户ID = %s"
        cursor.execute(sql_insert, data)
        connect.commit()
        cursor.close()
        QMessageBox.about(self.mainui, "提示", "修改成功")  # 弹窗
        self.table2KInf()   # 更新数据

    def Clac2b1(self):
        cursor = connect.cursor()
        query = "select 客户总积分 from 客户 where 客户ID = %d"
        cursor.execute(query, self.KID)
        resluts = cursor.fetchall()
        result = resluts[0][0]
        if result == None:
            result = 0
        else:
            result = Decimal(result)
        if result > 5000:
            result = result - 5000
            QMessageBox.about(self.mainui, "提示", "兑换成功!")  # 弹窗
            cursor = connect.cursor()
            query = "UPDATE 客户 SET 客户总积分 = %s WHERE 客户ID = %s"
            cursor.execute(query, (result, self.KID,))
            connect.commit()
            cursor.close()
        else:
            QMessageBox.about(self.mainui, "提示", "积分不足!!")  # 弹窗
        self.Scoreshow()

    def Clac2b2(self):
        cursor = connect.cursor()
        query = "select 客户总积分 from 客户 where 客户ID = %d"
        cursor.execute(query, self.KID)
        resluts = cursor.fetchall()
        result = resluts[0][0]
        if result == None:
            result = 0
        else:
            result = Decimal(result)
        if result > 5000:
            result = result - 5000
            QMessageBox.about(self.mainui, "提示", "兑换成功!")  # 弹窗
            cursor = connect.cursor()
            query = "UPDATE 客户 SET 客户总积分 = %s WHERE 客户ID = %s"
            cursor.execute(query, (result, self.KID,))
            connect.commit()
            cursor.close()
        else:
            QMessageBox.about(self.mainui, "提示", "积分不足!!")  # 弹窗
        self.Scoreshow()

    def Clac2b3(self):
        cursor = connect.cursor()
        query = "select 客户总积分 from 客户 where 客户ID = %d"
        cursor.execute(query, self.KID)
        resluts = cursor.fetchall()
        result = resluts[0][0]
        if result == None:
            result = 0
        else:
            result = Decimal(result)
        if result > 10000:
            result = result - 10000
            QMessageBox.about(self.mainui, "提示", "兑换成功!")  # 弹窗
            cursor = connect.cursor()
            query = "UPDATE 客户 SET 客户总积分 = %s WHERE 客户ID = %s"
            cursor.execute(query, (result, self.KID,))
            connect.commit()
            cursor.close()
        else:
            QMessageBox.about(self.mainui, "提示", "积分不足!!")  # 弹窗
        self.Scoreshow()

    def table2Dallshow(self):
        self.table2Dungetshow()
        self.table2Dgetedshow()
        self.table2Dshow()

    def table2Dungetshow(self):
        self.mainui.datatime2Drig.setDateTime(QDateTime.currentDateTime())
        cursor = connect.cursor()
        query = "select * from 订单 where 订单到货日期 is null"
        cursor.execute(query)
        resluts = cursor.fetchall()
        self.mainui.table2Dunget.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.table2Dunget.setItem(i, j, QTableWidgetItem(str(cell)))

    def table2Dgetedshow(self):
        cursor = connect.cursor()
        query = "select * from 订单 where 订单到货日期 is not null"
        cursor.execute(query)
        resluts = cursor.fetchall()
        self.mainui.table2Dgeted.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.table2Dgeted.setItem(i, j, QTableWidgetItem(str(cell)))

    def table2Dshow(self):
        cursor = connect.cursor()
        query = "select * from 订单"
        cursor.execute(query)
        resluts = cursor.fetchall()
        self.mainui.table2D.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.table2D.setItem(i, j, QTableWidgetItem(str(cell)))

    def Scoreshow(self):
        cursor = connect.cursor()
        query = "select 客户总积分 from 客户 where 客户ID = %d"
        cursor.execute(query, self.KID)
        resluts = cursor.fetchall()
        result = resluts[0][0]
        if result == None:
            self.mainui.text2Sscore.setText("0")
        else:
            self.mainui.text2Sscore.setText(str(result))
        self.mainui.text2Sscore.show()

    # 根据订单ID搜寻已发送订单
    def Clac2Dsearchgeted(self):
        textofsearch = self.mainui.text2Dsearchsended.text()
        cursor = connect.cursor()
        query = "SELECT * FROM 订单 where 订单ID = %s and 订单到货日期 is not null"
        cursor.execute(query, (textofsearch,))
        resluts = cursor.fetchall()
        self.mainui.table2Dgeted.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.table2Dgeted.setItem(i, j, QTableWidgetItem(str(cell)))

    def Clac2Dsgetedcancel(self):
        self.mainui.text2Dsearchgeted.clear()
        self.table2Dgetedshow()

    def Clac2DDsearchall(self):
        textofsearch = self.mainui.text2Dsearchall.text()
        cursor = connect.cursor()
        query = "SELECT * FROM 订单 where 订单ID = %s"
        cursor.execute(query, (textofsearch,))
        resluts = cursor.fetchall()
        self.mainui.table2D.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.table2D.setItem(i, j, QTableWidgetItem(str(cell)))

    def Clac2Dsacancel(self):
        self.mainui.text2Dsearchall.clear()
        self.table2Dshow()

# 构造处理tablewdiget的类
class OnlyRead_tableWidgetItem(QTableWidgetItem):
    def __init__(self, text):
        super().__init__(text)
        self.setFlags(self.flags() & ~Qt.ItemIsEditable)

def Col1234Onlyread(table, row, col):
    # 设置第一列数据
    for i in range(row):
        for j in range(3):
            item = OnlyRead_tableWidgetItem(f"Item {i + 1}-{j + 1}")
            table.setItem(i, j, item)

    # 设置其他位置的数据
    for i in range(row):
        for j in range(4, col):
            item = QTableWidgetItem(f"Item {i + 1}-{j + 1}")
            table.setItem(i, j, item)

if __name__ == "__main__":
    app = QApplication([])

    ui = UI()
    ui.loginui.show()
    app.exec_()
