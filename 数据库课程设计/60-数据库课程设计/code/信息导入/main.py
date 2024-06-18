import io
import sys
from PySide2.QtWidgets import QLabel, QFileDialog, QApplication, QDateEdit, QMainWindow, QTableWidgetItem, QPushButton, QPlainTextEdit, QMessageBox
from PySide2 import QtCore, QtWidgets
from PySide2.QtUiTools import QUiLoader
from PySide2.QtCore import Qt, QDate, QByteArray
from PySide2.QtGui import QImage, QPixmap
import pymssql
from datetime import date, datetime
import pandas as pd
from pandas import read_excel
from sqlalchemy import create_engine

# 创建 SQL Server连接
servername = "LAPTOP-07LLA2EL\MSSQLSERVER2022"
user = "sa"
password = "12345678"
database = "DataBase"
engine = create_engine(f"mssql+pymssql://{user}:{password}@{servername}/{database}")

class UI:
    def __init__(self):
        self.ui = QUiLoader().load('主界面.ui')
        self.ui.button.clicked.connect(self.Clac2go)
        self.ui.button1.clicked.connect(self.Clac2thing1)
        self.ui.button2.clicked.connect(self.Clac2thing2)
        self.ui.button3.clicked.connect(self.Clac2thing3)
        self.ui.button4.clicked.connect(self.Clac2thing4)
        self.ui.button5.clicked.connect(self.Clac2thing5)
        self.ui.button6.clicked.connect(self.Clac2thing6)
        self.ui.button7.clicked.connect(self.Clac2thing7)
        self.ui.button8.clicked.connect(self.Clac2thing8)

    def Clac2thing1(self):
        file_path, _ = QFileDialog.getOpenFileName(self.ui, '选择文件', '', filter='Microsoft Excel 工作表 (*.xlsx)')
        self.ui.text1.setText(file_path)

    def Clac2thing2(self):
        file_path, _ = QFileDialog.getOpenFileName(self.ui, '选择文件', '', filter='Microsoft Excel 工作表 (*.xlsx)')
        self.ui.text2.setText(file_path)

    def Clac2thing3(self):
        file_path, _ = QFileDialog.getOpenFileName(self.ui, '选择文件', '', filter='Microsoft Excel 工作表 (*.xlsx)')
        self.ui.text3.setText(file_path)

    def Clac2thing4(self):
        file_path, _ = QFileDialog.getOpenFileName(self.ui, '选择文件', '', filter='Microsoft Excel 工作表 (*.xlsx)')
        self.ui.text4.setText(file_path)

    def Clac2thing5(self):
        file_path, _ = QFileDialog.getOpenFileName(self.ui, '选择文件', '', filter='Microsoft Excel 工作表 (*.xlsx)')
        self.ui.text5.setText(file_path)

    def Clac2thing6(self):
        file_path, _ = QFileDialog.getOpenFileName(self.ui, '选择文件', '', filter='Microsoft Excel 工作表 (*.xlsx)')
        self.ui.text6.setText(file_path)

    def Clac2thing7(self):
        file_path, _ = QFileDialog.getOpenFileName(self.ui, '选择文件', '', filter='Microsoft Excel 工作表 (*.xlsx)')
        self.ui.text7.setText(file_path)

    def Clac2thing8(self):
        file_path, _ = QFileDialog.getOpenFileName(self.ui, '选择文件', '', filter='Microsoft Excel 工作表 (*.xlsx)')
        self.ui.text8.setText(file_path)

    def Clac2go(self):
        excel_file1 = self.ui.text1.text()
        excel_file2 = self.ui.text2.text()
        excel_file3 = self.ui.text3.text()
        excel_file4 = self.ui.text4.text()
        excel_file5 = self.ui.text5.text()
        excel_file6 = self.ui.text6.text()
        excel_file7 = self.ui.text7.text()
        excel_file8 = self.ui.text8.text()
        if not excel_file1:
            QMessageBox.about(self.ui, '提示', '存在未输入的文件')
            return
        if not excel_file2:
            QMessageBox.about(self.ui, '提示', '存在未输入的文件')
            return
        if not excel_file3:
            QMessageBox.about(self.ui, '提示', '存在未输入的文件')
            return
        if not excel_file4:
            QMessageBox.about(self.ui, '提示', '存在未输入的文件')
            return
        if not excel_file5:
            QMessageBox.about(self.ui, '提示', '存在未输入的文件')
            return
        if not excel_file6:
            QMessageBox.about(self.ui, '提示', '存在未输入的文件')
            return
        if not excel_file7:
            QMessageBox.about(self.ui, '提示', '存在未输入的文件')
            return
        if not excel_file8:
            QMessageBox.about(self.ui, '提示', '存在未输入的文件')
            return

        df = pd.read_excel(excel_file1)
        # 将数据导入到 SQL Server
        table_name = "类别"  # 将此替换为你需要导入到的表名
        df.to_sql(table_name, engine, index=False, if_exists="append")

        df = pd.read_excel(excel_file2)
        # 将数据导入到 SQL Server
        table_name = "产品"  # 将此替换为你需要导入到的表名
        df.to_sql(table_name, engine, index=False, if_exists="append")

        df = pd.read_excel(excel_file3)
        # 将数据导入到 SQL Server
        table_name = "供应商"  # 将此替换为你需要导入到的表名
        df.to_sql(table_name, engine, index=False, if_exists="append")

        df = pd.read_excel(excel_file4)
        # 将数据导入到 SQL Server
        table_name = "客户"  # 将此替换为你需要导入到的表名
        df.to_sql(table_name, engine, index=False, if_exists="append")

        df = pd.read_excel(excel_file5)
        df['雇员备注'] = df['雇员备注'].astype(str)
        # 将数据导入到 SQL Server
        table_name = "雇员"  # 将此替换为你需要导入到的表名
        df.to_sql(table_name, engine, index=False, if_exists="append")

        df = pd.read_excel(excel_file6)
        # 将数据导入到 SQL Server
        table_name = "供应商-产品关系"  # 将此替换为你需要导入到的表名
        df.to_sql(table_name, engine, index=False, if_exists="append")

        df = pd.read_excel(excel_file7)
        # 将数据导入到 SQL Server
        table_name = "订单明细"  # 将此替换为你需要导入到的表名
        df.to_sql(table_name, engine, index=False, if_exists="append")

        df = pd.read_excel(excel_file8)
        # 将数据导入到 SQL Server
        table_name = "订单"  # 将此替换为你需要导入到的表名
        df.to_sql(table_name, engine, index=False, if_exists="append")

        QMessageBox.about(self.ui, '提示', '动作完成')

if __name__ == "__main__":
    app = QApplication([])

    ui = UI()
    ui.ui.show()
    app.exec_()



# # 读取Excel
# excel_file = "类别.xlsx"  # 将此替换为你的Excel文件名
# df = pd.read_excel(excel_file)
#
# # 将数据导入到 SQL Server
# table_name = "类别"  # 将此替换为你需要导入到的表名
# df.to_sql(table_name, engine, index=False, if_exists="append")
#
# # 读取Excel
# excel_file = "产品.xlsx"  # 将此替换为你的Excel文件名
# df = pd.read_excel(excel_file)
#
# # 将数据导入到 SQL Server
# table_name = "产品"  # 将此替换为你需要导入到的表名
# df.to_sql(table_name, engine, index=False, if_exists="append")
#
# # 读取Excel
# excel_file = "供应商.xlsx"  # 将此替换为你的Excel文件名
# df = pd.read_excel(excel_file)
#
# # 将数据导入到 SQL Server
# table_name = "供应商"  # 将此替换为你需要导入到的表名
# df.to_sql(table_name, engine, index=False, if_exists="append")

# # 读取Excel
# excel_file = "客户.xlsx"  # 将此替换为你的Excel文件名
# df = pd.read_excel(excel_file)
#
# # 将数据导入到 SQL Server
# table_name = "客户"  # 将此替换为你需要导入到的表名
# df.to_sql(table_name, engine, index=False, if_exists="append")
# #
# # 读取Excel
# excel_file = "雇员.xlsx"  # 将此替换为你的Excel文件名
# df = pd.read_excel(excel_file)
# df['雇员备注'] = df['雇员备注'].astype(str)
#
# # 将数据导入到 SQL Server
# table_name = "雇员"  # 将此替换为你需要导入到的表名
# df.to_sql(table_name, engine, index=False, if_exists="append")
#
# # 读取Excel
# excel_file = "供应商-产品关系.xlsx"  # 将此替换为你的Excel文件名
# df = pd.read_excel(excel_file)
#
# # 将数据导入到 SQL Server
# table_name = "供应商-产品关系"  # 将此替换为你需要导入到的表名
# df.to_sql(table_name, engine, index=False, if_exists="append")
#
# # 读取Excel
# excel_file = "订单明细.xlsx"  # 将此替换为你的Excel文件名
# df = pd.read_excel(excel_file)
#
# # 将数据导入到 SQL Server
# table_name = "订单明细"  # 将此替换为你需要导入到的表名
# df.to_sql(table_name, engine, index=False, if_exists="append")
#
# # 读取Excel
# excel_file = "订单.xlsx"  # 将此替换为你的Excel文件名
# df = pd.read_excel(excel_file)
#
# # 将数据导入到 SQL Server
# table_name = "订单"  # 将此替换为你需要导入到的表名
# df.to_sql(table_name, engine, index=False, if_exists="append")