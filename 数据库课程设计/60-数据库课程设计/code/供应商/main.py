from PySide2.QtWidgets import QApplication, QDateTimeEdit, QMainWindow, QTableWidgetItem, QPushButton, QPlainTextEdit, QMessageBox
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
        self.GID = None
        if connect:
            print("连接成功")
        # 登录界面初始化
        self.loginui = QUiLoader().load('登录界面.ui')
        self.loginui.buttonLogin.clicked.connect(self.Clac2login)  # 按钮登录信号链接

        # 主界面初始化
        self.mainui = QUiLoader().load('主界面.ui')
        self.mainui.button_shouye.clicked.connect(lambda: self.mainui.stackedWidget.setCurrentIndex(0))
        self.mainui.button_shopping.clicked.connect(lambda: self.mainui.stackedWidget.setCurrentIndex(1))
        self.mainui.button_dingdan.clicked.connect(lambda: self.mainui.stackedWidget.setCurrentIndex(2))
        self.mainui.button_mine.clicked.connect(lambda: self.mainui.stackedWidget.setCurrentIndex(3))

        self.mainui.button2SSearch.clicked.connect(self.Clac2SSearch)
        self.mainui.button2Ssearchcancel.clicked.connect(self.Clac2Ssearchcancel)
        self.mainui.button2Dsend.clicked.connect(self.Send2Dunsend)
        self.mainui.button2Dsearchsended.clicked.connect(self.Clac2Dsearchsended)
        self.mainui.button2Dssendcancel.clicked.connect(self.Clac2Dssendcancel)
        self.mainui.button2Dsearchall.clicked.connect(self.Clac2DDsearchall)
        self.mainui.button2Dsacancel.clicked.connect(self.Clac2Dsacancel)
        self.mainui.button2f5.clicked.connect(self.Clac2f5)

        # 不允许修改table表中的单元值
        self.mainui.table2Dunsend.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)
        self.mainui.table2Dsended.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)
        self.mainui.table2D.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)

    # 登录信号链接函数
    def Clac2login(self):
        self.UserName = self.loginui.textUser.text()  # 获取用户名
        self.GID = self.UserName
        self.KeyWord = self.loginui.textPwd.text()  # 获取密码
        cursor = connect.cursor()
        sql_select = "SELECT 供应商ID, 供应商登陆密码 FROM 供应商"
        cursor.execute(sql_select)
        results = cursor.fetchall()
        sql_select = "SELECT COUNT(*) FROM 供应商"
        cursor.execute(sql_select)
        flag = cursor.fetchall()
        flag = int(flag[0][0])
        for result in results:
            if int(self.UserName) == result[0] and str(self.KeyWord) == result[1]:
                QMessageBox.about(self.loginui, "提示", "登陆成功!\n欢迎回来!")  # 弹窗
                self.loginui.close()
                self.mainui.show()
                self.table2GInf()
                self.table2Sallshow()
                self.table2Dallshow()
                self.table2Sinfshow()
                break
            else:
                flag = flag - 1
                continue
        if not flag:
            QMessageBox.about(self.loginui, "提示", "供应商ID或密码错误, 请重试")  # 弹窗

    def Clac2f5(self):
        self.table2Dallshow()
        self.table2GInf()
        self.table2Sallshow()
        self.table2Sinfshow()

    # 获取产品所有信息
    def table2Sallshow(self):
        cursor = connect.cursor()
        query = "select 产品ID, 产品名称, 产品库存量, 产品订购量, 产品再订购量, 产品中止 from 产品 "
        cursor.execute(query)
        resluts = cursor.fetchall()
        self.mainui.tableSAll.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.tableSAll.setItem(i, j, QTableWidgetItem(str(cell)))

    def table2Sinfshow(self):
        cursor = connect.cursor()
        query = "select 产品ID, 产品名称, 产品单位数量, 产品单价, 产品中止 from 产品"
        cursor.execute(query)
        results = cursor.fetchall()
        self.mainui.table2SInf.setRowCount(len(results))

        for i in range(len(results)):
            item = OnlyRead_tableWidgetItem(str(results[i][0]))
            self.mainui.table2SInf.setItem(i, 0, item)

        # 设置其他位置的数据
        for i in range(len(results)):
            for j in range(1, len(results[i])):
                item = QTableWidgetItem(str(results[i][j]))
                self.mainui.table2SInf.setItem(i, j, item)

    def table2Dallshow(self):
        self.table2Dunsendshow()
        self.table2Dsendedshow()
        self.table2Dshow()

    def table2Dunsendshow(self):
        self.mainui.data2Dsenddate.setDateTime(QDateTime.currentDateTime())
        cursor = connect.cursor()
        query = "select * from 订单 where 订单发货日期 is null"
        cursor.execute(query)
        resluts = cursor.fetchall()
        self.mainui.table2Dunsend.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.table2Dunsend.setItem(i, j, QTableWidgetItem(str(cell)))

    def table2Dsendedshow(self):
        cursor = connect.cursor()
        query = "select * from 订单 where 订单发货日期 is not null"
        cursor.execute(query)
        resluts = cursor.fetchall()
        self.mainui.table2Dsended.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.table2Dsended.setItem(i, j, QTableWidgetItem(str(cell)))

    def table2Dshow(self):
        cursor = connect.cursor()
        query = "select * from 订单"
        cursor.execute(query)
        resluts = cursor.fetchall()
        self.mainui.table2D.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.table2D.setItem(i, j, QTableWidgetItem(str(cell)))

    def table2GInf(self):
        cursor = connect.cursor()
        query = "SELECT 供应商公司名称, 供应商联系人姓名, 供应商联系人职务, 供应商所在城市, 供应商所在地区, 供应商所在国家, 供应商详细地址, 供应商所在地邮政编码, 供应商电话, 供应商传真, 供应商登陆密码 FROM 供应商 where 供应商ID = %s"
        cursor.execute(query, (self.GID,))
        resluts = cursor.fetchall()
        res = resluts[0]
        self.mainui.text2GCom.setText(res[0])
        self.mainui.text2GName.setText(res[1])
        self.mainui.text2GJob.setText(res[2])
        self.mainui.text2GCity.setText(res[3])
        self.mainui.text2GArea.setText(res[4])
        self.mainui.text2GCountry.setText(res[5])
        self.mainui.text2GAddr.setText(res[6])
        self.mainui.text2Gecode.setText(res[7])
        self.mainui.text2GPhone.setText(res[8])
        self.mainui.text2Gfox.setText(res[9])
        self.mainui.text2GKeyWord.setText(res[10])

    # 根据产品名称搜索商品信息
    def Clac2SSearch(self):
        textofsearch = self.mainui.text2SSearch.text()
        cursor = connect.cursor()
        query = "SELECT * FROM 产品 where 产品名称 = %s"
        self.mainui.tableSAll.setRowCount(0)     # 清除所有行
        cursor.execute(query, (textofsearch, ))
        resluts = cursor.fetchall()
        self.mainui.tableSAll.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.tableSAll.setItem(i, j, QTableWidgetItem(str(cell)))

    def Clac2Ssearchcancel(self):
        self.mainui.text2SSearch.clear()
        self.table2Sallshow()
        self.table2Sallshow()

    # 根据订单ID搜寻已发送订单
    def Clac2Dsearchsended(self):
        textofsearch = self.mainui.text2Dsearchsended.text()
        cursor = connect.cursor()
        query = "SELECT * FROM 订单 where 订单ID = %s and 订单发货日期 is not null"
        cursor.execute(query, (textofsearch,))
        resluts = cursor.fetchall()
        self.mainui.table2Dsended.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.table2Dsended.setItem(i, j, QTableWidgetItem(str(cell)))

    def Clac2Dssendcancel(self):
        self.mainui.text2Dsearchsended.clear()
        self.table2Dsendedshow()

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

    # 订单发货
    def Send2Dunsend(self):
        qdate = self.mainui.data2Dsenddate.dateTime()
        qdatetime = qdate.toPython()
        ID = self.mainui.text2Dunsend.text()
        money = self.mainui.text2Dtranmoney.text()
        money = Decimal(float(money))
        cursor = connect.cursor()
        query = "UPDATE 订单 SET 订单发货日期 = %s, 订单运货费 = %s WHERE 订单ID = %s"
        cursor.execute(query, (qdatetime, money, ID, ))
        connect.commit()
        cursor.close()
        QMessageBox.about(self.mainui, "提示", "发货成功")  # 弹窗
        self.table2Dallshow()  # 更新数据

# 构造处理tablewdiget的类
class OnlyRead_tableWidgetItem(QTableWidgetItem):
    def __init__(self, text):
        super().__init__(text)
        self.setFlags(self.flags() & ~Qt.ItemIsEditable)

def Col1Onlyread(table, row, col):
    # 设置第一列数据
    for i in range(row):
        item = OnlyRead_tableWidgetItem(f"Item {i + 1}")
        table.setItem(i, 0, item)

    # 设置其他位置的数据
    for i in range(row):
        for j in range(1, col):
            item = QTableWidgetItem(f"Item {i + 1}-{j + 1}")
            table.setItem(i, j, item)

if __name__ == "__main__":
    app = QApplication([])

    ui = UI()
    ui.loginui.show()
    app.exec_()