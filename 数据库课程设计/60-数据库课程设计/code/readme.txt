在使用四个界面时，请更改每个子文件夹下对应的"main.py"的数据库登录信息。

servername = "LAPTOP-07LLA2EL\MSSQLSERVER2022"
port = 1433
user = "sa"
password = "12345678"
database = "DataBase"
charset = "GBK"
connect = pymssql.connect(servername, user, password, database, charset)

即更改servername,port,password为自己电脑上对应的信息。
