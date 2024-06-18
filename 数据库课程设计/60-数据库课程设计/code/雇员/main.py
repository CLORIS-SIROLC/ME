import io
import sys
from PySide2.QtWidgets import QLabel, QFileDialog, QApplication, QDateEdit, QMainWindow, QTableWidgetItem, QPushButton, QPlainTextEdit, QMessageBox
from PySide2 import QtCore, QtWidgets
from PySide2.QtUiTools import QUiLoader
from PySide2.QtCore import Qt, QDate, QByteArray
from PySide2.QtGui import QImage, QPixmap
import pymssql
from datetime import date, datetime

import binascii

servername = "LAPTOP-07LLA2EL\MSSQLSERVER2022"
port = 1433
user = "sa"
password = "12345678"
database = "DataBase"
charset = "GBK"
connect = pymssql.connect(servername, user, password, database, charset)

class UI:
    def __init__(self):
        if connect:
            print("链接成功")
        # 登录ui初始化
        self.setinui = QUiLoader().load('登录.ui')                    # 装载ui文件
        self.setinui.buttonConn.clicked.connect(self.Clac2Conn)         # 按钮登录信号链接
        self.setinui.button2Gright.clicked.connect(self.Clac2getID)
        self.setinui.button2Gphoto.clicked.connect(self.Clac2getphoto)
        self.setinui.button2Grekey.clicked.connect(self.Clac2rekey)

        self.setinui.text2Gjdate.setDate(QDate.currentDate())

        # 登录ui初始化
        self.mainui = QUiLoader().load('主界面.ui')  # 装载ui文件
        self.mainui.button2DRig.clicked.connect(self.Clac2Dupdate)  # 按钮取消信号链接
        # self.mainui.TabInfbut2right.clicked.connect(self.Clac2update)  # 按钮取消信号链接
        self.mainui.button2f5.clicked.connect(self.Clac2f5)

        # 不允许修改table表中的单元值
        self.mainui.tabDInf.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)

    # 按钮登录信号处理函数
    def Clac2Conn(self):
        self.UserName = self.setinui.textUser.text()  # 获取用户名
        self.KeyWord = self.setinui.textPwd.text()  # 获取密码
        cursor = connect.cursor()
        sql_select = "SELECT 雇员ID, 雇员登陆密码 FROM 雇员"
        cursor.execute(sql_select)
        results = cursor.fetchall()
        sql_select = "SELECT COUNT(*) FROM 雇员"
        cursor.execute(sql_select)
        flag = cursor.fetchall()
        flag = int(flag[0][0])
        for result in results:
            if int(self.UserName) == result[0] and str(self.KeyWord) == result[1]:
                QMessageBox.about(self.setinui, "提示", "登陆成功!\n欢迎回来!")  # 弹窗
                self.setinui.close()
                self.tab2inf()
                self.mainui.show()
                break
            else:
                flag = flag - 1
                continue
        if not flag:
            QMessageBox.about(self.setinui, "提示", "雇员ID或密码错误, 请重试")  # 弹窗

    def Clac2getID(self):
        if not self.setinui.text2Gfname.text():
            QMessageBox.about(self.setinui, "提示", "姓氏不可为空\n请重新输入!")  # 弹窗
            return
        if not self.setinui.text2Glname.text():
            QMessageBox.about(self.setinui, "提示", "名字不可为空\n请重新输入!")  # 弹窗
            return
        if not self.setinui.text2Gjob.text():
            QMessageBox.about(self.setinui, "提示", "职务不可为空\n请重新输入!")  # 弹窗
            return
        if not self.setinui.text2Gbdate.text():
            QMessageBox.about(self.setinui, "提示", "出生日期不可为空\n请重新输入!")  # 弹窗
            return
        if not self.setinui.text2Gjdate.text():
            QMessageBox.about(self.setinui, "提示", "雇用日期不可为空\n请重新输入!")  # 弹窗
            return
        if not self.setinui.texte2Gaddr.text():
            QMessageBox.about(self.setinui, "提示", "详细地址不可为空\n请重新输入!")  # 弹窗
            return
        if not self.setinui.text2Gkey.text():
            QMessageBox.about(self.setinui, "提示", "密码不可为空\n请重新输入!")  # 弹窗
            return
        if not self.setinui.text2Gkey2.text():
            QMessageBox.about(self.setinui, "提示", "确认密码不可为空\n请重新输入!")  # 弹窗
            return

        cursor = connect.cursor()
        query = "select max(雇员ID) from 雇员"
        cursor.execute(query)
        results = cursor.fetchall()
        GID = int(results[0][0]) + 1
        Gfname = self.setinui.text2Gfname.text()
        Glname = self.setinui.text2Glname.text()
        GJob = self.setinui.text2Gjob.text()
        Ghname = self.setinui.text2Ghname.text()
        Gbdate = self.setinui.text2Gbdate.date()
        Gbdate = Gbdate.toPython()
        Gbdate = datetime.combine(Gbdate, datetime.min.time())
        Gjdate = self.setinui.text2Gjdate.date()
        Gjdate = Gjdate.toPython()
        Gjdate = datetime.combine(Gjdate, datetime.min.time())
        GAddr = self.setinui.texte2Gaddr.text()
        GCountry = self.setinui.text2Gcountry.text()
        Gcity = self.setinui.text2Gcity.text()
        Garea = self.setinui.text2Garea.text()
        Gecode = self.setinui.text2Gecode.text()
        Gphone = self.setinui.text2Gphone.text()
        Gphone2 = self.setinui.text2Gphone2.text()
        Gphotoload = self.setinui.text2Gphoto.text()
        if Gphotoload:
            with open(Gphotoload, 'rb') as file:
                image_binary_data = file.read()
            hex_image_data = binascii.hexlify(image_binary_data).decode()
            if len(hex_image_data) > 510:
                Gphoto = hex_image_data[:510]
            else:
                Gphoto = hex_image_data
        Gother = self.setinui.text2Gother.text()
        GupID = self.setinui.text2GupID.text()
        Gkey = self.setinui.text2Gkey.text()
        Gkey2 = self.setinui.text2Gkey2.text()

        if not Gkey == Gkey2:
            QMessageBox.about(self.setinui, "提示", "确认密码与密码不相同\n请重新输入!")  # 弹窗
            return

        data = (GID, Gfname, Glname, GJob, Ghname, Gbdate,  Gjdate, GAddr, GCountry, Gcity, Garea, Gecode, Gphone, Gphone2, Gphoto, Gother, GupID, Gkey)
        cursor = connect.cursor()
        sql_insert = "INSERT INTO 雇员 (雇员ID, 雇员姓氏, 雇员名字, 雇员职务, 雇员尊称, 雇员出生日期, 雇员雇佣日期, 雇员详细地址, 雇员所在国家, 雇员所在城市, 雇员所在地区, 雇员所在地邮政编码, 雇员家庭电话, 雇员电话分机, 雇员照片, 雇员备注, 雇员上级, 雇员登陆密码) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
        cursor.execute(sql_insert, data)
        connect.commit()
        cursor.close()
        cursor = connect.cursor()
        QMessageBox.about(self.setinui, '提示', '注册成功, 你的ID号是' + str(GID))

    def Clac2getphoto(self):
        file_path, _ = QFileDialog.getOpenFileName(self.setinui, '选择文件', '', filter='Images (*.jpg *.jpeg)')
        self.setinui.text2Gphoto.setText(file_path)

    def Clac2rekey(self):
        if self.setinui.text2GmyID.text():
            GID = self.setinui.text2GmyID.text()
            cursor = connect.cursor()
            query = "select 雇员登陆密码 from 雇员 where 雇员ID = %s"
            cursor.execute(query, (GID,))
            results = cursor.fetchall()
            if results:
                query = "update 雇员 set 雇员登陆密码 = '12345678' where 雇员ID = %s"
                cursor.execute(query, (GID,))
                connect.commit()
                cursor.close()
                QMessageBox.about(self.setinui, "提示", "重置密码成功\n现在密码为:12345678")  # 弹窗
            else:
                QMessageBox.about(self.setinui, "提示", "不存在的ID号码\n请重新输入!")  # 弹窗

    def Clac2f5(self):
        self.tab2inf()

    # 界面数据更新函数
    def tab2inf(self):
        self.mainui.dataRig.setDate(QDate.currentDate())
        cursor = connect.cursor()
        sql_select = "SELECT * FROM 雇员 where 雇员ID = " + self.UserName
        cursor.execute(sql_select)
        results = cursor.fetchall()
        result = results[0]
        row = 0
        for res in result[1:]:
            self.mainui.Tab2GInf.setItem(row, 0, QTableWidgetItem(str(res)))
            row = row + 1
        hex_date = result[14]
        byte_data = QByteArray.fromHex(hex_date.encode())
        image = QImage.fromData(byte_data)
        pixmap = QPixmap.fromImage(image)
        label = QLabel()
        label.setPixmap(pixmap)
        label.setScaledContents(True)
        label.setAlignment(Qt.AlignCenter)
        self.mainui.Tab2GInf.setCellWidget(13, 0, label)

        # 生成Tabwidget的行号和列号
        cursor = connect.cursor()
        query = "select * from 订单 where 订单货款确认日期 is null"
        cursor.execute(query)
        resluts = cursor.fetchall()
        cursor.close()
        self.mainui.tabDInf.setRowCount(len(resluts))
        for i, row in enumerate(resluts):
            for j, cell in enumerate(row):
                self.mainui.tabDInf.setItem(i, j, QTableWidgetItem(str(cell)))

    def Clac2Dupdate(self):
        qdate = self.mainui.dataRig.date()
        py_date = qdate.toPython()
        qdatetime = datetime.combine(py_date, datetime.min.time())
        text_from_line_edit = self.mainui.lineID.text()
        cursor = connect.cursor()
        query = "UPDATE 订单 SET 订单贷款确认日期 = %s WHERE 订单ID = %s"
        cursor.execute(query, (qdatetime, text_from_line_edit))
        connect.commit()
        cursor.close()
        QMessageBox.about(self.mainui, "提示", "确认成功")  # 弹窗
        self.tab2inf()       # 更新数据



if __name__ == "__main__":
    app = QApplication([])

    ui = UI()
    ui.setinui.show()
    app.exec_()