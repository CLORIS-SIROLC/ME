# 本文件用于json文件读入 数据划分
import json
from sklearn.model_selection import train_test_split
import os
from sklearn.utils import shuffle

# 存放数据的文件夹的路径
out_folder_path = "train_data"
# json文件
json_path = 'char_dict.json'


# 读取json文件
def read_json():
    # 用字典存储JSON数据
    with open(json_path, 'r') as file:
        json_str = file.read()
        character_dict = json.loads(json_str)
    # # 输出字典中的部分键值对
    # count = 0
    # for key, value in character_dict.items():
    #     if 1000 < count <= 1030:
    #         print(f"键: {key}, 值: {value}")
    #         count += 1
    #     else:
    #         count += 1
    #     # 输出：       键: 1027, 值: 完
    return character_dict


def split_test_train():
    # 存储图片路径数据和标签数据
    image_train = []
    image_test = []
    label_train = []
    label_test = []
    for folder_name in os.listdir(out_folder_path):
        image_data = []  # 存储这一个字的图像数据
        label_data = []  # 存储这一个字的标签数据
        for filename in os.listdir(out_folder_path + '/' + folder_name):
            if filename.endswith(".png"):
                image_path = out_folder_path + '/' + folder_name + '/' + filename
                image_data.append(image_path)  # 实际存储图片路径
                label_data.append(folder_name)

        # 训练集 : 测试集 = 8 : 2     随机数种子最初设为27，后改为18
        temp_image_train, temp_image_test, temp_label_train, temp_label_test = train_test_split(
            image_data, label_data, test_size=0.2, random_state=18)
        # 存储划分后的数据
        image_train += temp_image_train
        image_test += temp_image_test
        label_train += temp_label_train
        label_test += temp_label_test

    # 打乱数据和标签，随机数种子分别为27，28
    test_images, test_labels = shuffle(image_test, label_test, random_state=27)
    train_images, train_labels = shuffle(image_train, label_train, random_state=28)

    print("训练标签数量：" + str(len(train_labels)) + "   训练图片数量：" + str(len(train_images)))
    print("测试标签数量：" + str(len(test_labels)) + "   测试图片数量：" + str(len(test_images)))

    # 新建文件夹名字为doc
    folder_name = "doc"
    os.makedirs(folder_name, exist_ok=True)

    # 保存测试数据和标签到txt文件
    with open('doc/test_images.txt', 'w') as file:
        for image in test_images:
            file.write(str(image) + '\n')
    with open('doc/test_labels.txt', 'w') as file:
        for label in test_labels:
            file.write(str(label) + '\n')
    with open('doc/train_images.txt', 'w') as file:
        for image in train_images:
            file.write(str(image) + '\n')
    with open('doc/train_labels.txt', 'w') as file:
        for label in train_labels:
            file.write(str(label) + '\n')


# 本文件readin.py的所有操作：
def read_in():
    character_dict = read_json()  # json文件读入
    print("············json文件读入完成············")
    split_test_train()  # 划分数据集
    print("············数据集划分完成············")
    return character_dict
