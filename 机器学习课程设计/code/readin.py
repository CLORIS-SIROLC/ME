import csv
import os

import pandas as pd
from matplotlib import pyplot as plt

# 处理后数据存储的文件名
csv_file = 'dataset.csv'


def data_preprocess():
    data = []  # 存储未经过处理的原始数据
    with open('depression_anxiety_data.csv', 'r') as file:
        reader = csv.reader(file)
        for row in reader:
            data.append(row)
        # print(data[9][8])
        print('------------------文件 depression_anxiety_data.csv 读取成功------------------')

    # 将data转换为DataFrame对象，除去第一行
    data = pd.DataFrame(data[1:])
    # 将所有值为Not Availble的单元格替换为Normal（bmi的具体数值为0时显示Not Availble）
    data.replace('Not Availble', 'Normal', inplace=True)

    data = data.replace("NA", pd.NA)  # 将"NA"替换为pandas中的NA值
    data.dropna(how='any', inplace=True)  # 删除所有包含NA值的行

    # 将none全部"none"值替换为"None-minimal"可能没有抑郁症状、焦虑症状
    data = data.replace("none", "None-minimal")

    # # 统计类别分布
    # classes = data[3].value_counts().sort_index()  # 按照index大小排序
    # # classes = data[1].astype(int).value_counts().sort_index()# 按照int型index大小排序
    # # classes = data[7].value_counts().sort_values(ascending=False)  # 按照数值大小排序
    # print()
    # # 创建柱状图
    # plt.bar(classes.index, classes.values)
    # plt.xlabel('Class')
    # plt.ylabel('Count')
    # plt.title('Gender Class Distribution')
    # plt.show()

    # 将所有值为male的单元格替换为0,female替换为1
    data.replace('male', 0, inplace=True)
    data.replace('female', 1, inplace=True)
    # 将所有值为FALSE的单元格替换为0,TRUE替换为1
    data.replace('FALSE', 0, inplace=True)
    data.replace('TRUE', 1, inplace=True)
    # who_bmi的等级
    data.replace('Underweight', 0, inplace=True)
    data.replace('Normal', 1, inplace=True)
    data.replace('Overweight', 2, inplace=True)
    data.replace('Class I Obesity', 3, inplace=True)
    data.replace('Class II Obesity', 4, inplace=True)
    data.replace('Class III Obesity', 5, inplace=True)
    # depression_severity抑郁情况的严重程度，焦虑症状严重程度
    data[7].replace('None-minimal', 0, inplace=True)
    data[7].replace('Mild', 1, inplace=True)
    data[7].replace('Moderate', 2, inplace=True)
    data[7].replace('Moderately severe', 3, inplace=True)
    data[7].replace('Severe', 4, inplace=True)
    # depression_severity抑郁情况的严重程度
    data[7].replace('None-minimal', 0, inplace=True)
    data[7].replace('Mild', 1, inplace=True)
    data[7].replace('Moderate', 2, inplace=True)
    data[7].replace('Moderately severe', 3, inplace=True)
    data[7].replace('Severe', 4, inplace=True)
    # anxiety_severity焦虑情况严重程度
    data[13].replace('None-minimal', 0, inplace=True)
    data[13].replace('Mild', 1, inplace=True)
    data[13].replace('Moderate', 2, inplace=True)
    data[13].replace('Severe', 3, inplace=True)
    # 将data转换为列表
    data = data.values.tolist()
    for i in range(len(data)):
        if int(data[i][6]) >= 8 and int(data[i][18]) == 1:
            data[i][8] = 1
        elif int(data[i][3]) == 0 and 10 <= int(data[i][12]) <= 12:
            data[i][14] = 0
        elif int(data[i][6]) >= 15:
            data[i][9] = 1
            data[i][18] = 1
        elif int(data[i][3]) == 1 and 12 <= int(data[i][6]) <= 14:
            data[i][8] = 0

    # 将data转换为DataFrame对象
    data = pd.DataFrame(data[:])

    data = data.drop(data.columns[0], axis=1)  # 除去第0列数据（id号）
    data = data.drop(data.columns[4 - 1], axis=1)  # 除去第4列数据（bmi的具体数值）
    data = data.drop(data.columns[6 - 2], axis=1)  # 除去第6列数据（PHQ的具体数值）
    data = data.drop(data.columns[10 - 3], axis=1)  # 除去第10列数据（抑郁症诊断）
    data = data.drop(data.columns[11 - 4], axis=1)  # 除去第11列数据（抑郁症治疗）
    data = data.drop(data.columns[12 - 5], axis=1)  # 除去第12列数据（GAD的具体数值）
    data = data.drop(data.columns[15 - 6], axis=1)  # 除去第15列数据（焦虑症诊断）
    data = data.drop(data.columns[16 - 7], axis=1)  # 除去第16列数据（焦虑症治疗）
    data = data.drop(data.columns[17 - 8], axis=1)  # 除去第17列数据（epworth的具体数值）

    # 将data转换为列表
    data = data.values.tolist()

    for i in range(len(data)):
        if int(data[i][5]) == 0:
            if int(data[i][8]) == 0:
                data[i].append(0)  # 无焦虑倾向无抑郁倾向
            elif int(data[i][8]) == 1:
                data[i].append(1)  # 有焦虑倾向无抑郁倾向
            else:
                print("error")
        elif int(data[i][5]) == 1:
            if int(data[i][8]) == 0:
                data[i].append(2)  # 无焦虑倾向有抑郁倾向
            elif int(data[i][8]) == 1:
                data[i].append(3)  # 有焦虑倾向有抑郁倾向
            else:
                print("error")
        else:
            print("error")

    # 将data转换为DataFrame对象
    data = pd.DataFrame(data[:])
    data = data.drop(data.columns[8], axis=1)  # 除去第8列数据（是否有抑郁倾向）
    data = data.drop(data.columns[5], axis=1)  # 除去第5列数据（是否有焦虑倾向）
    # 将data转换为列表
    data = data.values.tolist()
    # 将data转换为DataFrame对象
    data = pd.DataFrame(data[:])

    data.to_csv(csv_file, index=False)
    print(f'------------------文件 {csv_file} 创建成功------------------')

    # 将data转换为列表
    data = data.values.tolist()
    return data
