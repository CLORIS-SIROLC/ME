# 如果是载入模型测试性能请运行这个文件，不要动任何其他文件，尤其是model文件夹
# 千万注意运行配置是"当前文件",不是"main",如果是main的话会重新建立model文件夹,导致保存好的模型被覆盖
import matplotlib.pyplot as plt
import numpy as np
import tensorflow as tf
import os
import keras
from PIL import Image, ImageOps

from mModel import recall, f1_score
from readin import read_json

model_file_path = 'model/best_model_A.h5'  # 模型的相对地址
folder_file_path = 'model'  # 模型存储的文件夹相对地址


# 加载训练好的模型
def model_A_load():
    model = None  # 避免model在赋值前被return
    if os.path.exists(folder_file_path):
        if os.path.exists(model_file_path):
            # 必须先定义我自定义的模型评判指标
            keras.utils.get_custom_objects()['recall'] = recall
            keras.utils.get_custom_objects()['f1_score'] = f1_score
            # 加载模型
            model = keras.models.load_model('model/best_model_A.h5')
            # model.summary()
            print("············模型加载完成············")
        else:
            print("模型文件不存在")
    else:
        print("存储模型的文件夹不存在")
    return model


# 从路径读入图像，图像预处理，并由RGB转换为灰度图，再转换为张量
def preprocess_image_modelA(path):
    target_size = 64
    image = Image.open(path)
    # image 是RGB图像
    width, height = image.size
    k = width / height  # 缩放比例
    if width >= height:  # 计算等比例缩放后的图片大小
        new_width = target_size
        new_height = int(target_size / k)
    else:
        new_height = target_size
        new_width = int(target_size * k)
    resized_image = image.resize((new_width, new_height))  # 缩放图像
    image.close()  # 关闭图像
    resized_image = ImageOps.equalize(resized_image)# 应用直方图均衡化
    new_image = Image.new('L', (target_size, target_size), 255)  # 创建白色背景
    left = (target_size - new_width) // 2  # 计算粘贴的起始位置
    right = (target_size - new_height) // 2
    new_image.paste(resized_image, (left, right))  # 将缩放后的图像粘贴到白色背景图像中心
    resized_image.close()  # 关闭图像
    gray_image = new_image.convert("L")  # 转换为灰度图像
    new_image.close()  # 关闭图像
    # 将PIL图像转换为numpy数组
    image = np.array(gray_image)
    image = tf.convert_to_tensor(image)
    image = tf.expand_dims(image, axis=0)  # 添加批量维度
    return image


# 单张图片的预测
def model_A_predict(image_path):
    # 展示这张图片
    image = Image.open(image_path)
    plt.imshow(image)
    plt.axis('off')  # 关闭坐标轴
    plt.show()

    modelA = model_A_load()# 加载模型
    image = preprocess_image_modelA(image_path)# 从image_path种取出图片，进行预处理后返回张量
    predictions = modelA.predict(image)# 使用加载的模型对这张图片进行预测
    predicted_class_index = np.argmax(predictions[0])# 得到可能性最高的预测结果
    class_string = '0' + str(predicted_class_index + 1000)

    character_dict = read_json()  # json文件读入
    class_character = character_dict[str(predicted_class_index + 1000)] # 对照json文件得到中文字
    # 返回在模型中的标签（int）、文字索引（字符串）、单个的中文字
    return int(predicted_class_index), str(class_string), class_character


if __name__ == "__main__":
    print(model_A_predict(r"train_data/01082/7240.png"))
