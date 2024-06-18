# 本文件用于数据预处理
import os

import tensorflow as tf
from PIL import Image, ImageOps
from keras.preprocessing.image import ImageDataGenerator

target_size = 64
folder_name = "dataset"
save_folder_1 = "dataset/train"
save_folder_2 = "dataset/test"


# 读取划分好的测试数据路径和标签
def read_split():
    with open('doc/test_images.txt', 'r') as file:
        test_images_path = [address.strip() for address in file.readlines()]
    with open('doc/test_labels.txt', 'r') as file:
        test_labels = [str(label.strip()) for label in file.readlines()]
    with open('doc/train_images.txt', 'r') as file:
        train_images_path = [address.strip() for address in file.readlines()]
    with open('doc/train_labels.txt', 'r') as file:
        train_labels = [str(label.strip()) for label in file.readlines()]
    return test_images_path, test_labels, train_images_path, train_labels


# 清除文件夹中的文件
def clear_folder(folder_path):
    # 获取文件夹中的所有文件和子文件夹
    files = os.listdir(folder_path)
    # 遍历文件夹中的文件和子文件夹
    for file_name in files:
        file_path = os.path.join(folder_path, file_name)
        # 如果是文件，则删除
        if os.path.isfile(file_path):
            os.remove(file_path)
        # 如果是文件夹，则递归调用clear_folder函数清除子文件夹中的文件
        elif os.path.isdir(file_path):
            clear_folder(file_path)


# 将预处理后的图像存储在dataset文件夹中
def load_image(train_images_path, test_images_path):
    # 新建文件夹名字为dataset
    os.makedirs(folder_name, exist_ok=True)
    in_folder = os.path.join(folder_name, "test")
    os.makedirs(in_folder, exist_ok=True)  # 在dataset文件夹中创建test文件夹
    clear_folder(in_folder)  # 清除文件夹中原有的文件
    in_folder = os.path.join(folder_name, "train")
    os.makedirs(in_folder, exist_ok=True)  # 在dataset文件夹中创建train文件夹
    clear_folder(in_folder)  # 清除文件夹中原有的文件

    # 用于存储预处理后图片的路径
    new_train_images_path = []
    new_test_images_path = []

    for i in range(len(train_images_path)):
        img = preprocess_image(train_images_path[i])
        file_name = os.path.basename(train_images_path[i])  # 取出图像的文件名
        # 拼接保存的文件路径
        save_path = os.path.join(save_folder_1, file_name)
        # 保存图像
        img.save(save_path)
        img.close()
        new_train_images_path.append(save_folder_1 + '/' + file_name)
        # print(save_path + "存储完毕")

    for j in range(len(test_images_path)):
        img = preprocess_image(test_images_path[j])
        file_name = os.path.basename(test_images_path[j])  # 取出图像的文件名
        # 拼接保存的文件路径
        save_path = os.path.join(save_folder_2, file_name)
        # 保存图像
        img.save(save_path)
        img.close()
        new_test_images_path.append(save_folder_2 + '/' + file_name)
        # print(save_path + "存储完毕")

    os.makedirs("doc", exist_ok=True)
    with open('doc/new_test_images.txt', 'w') as file:
        for image in new_test_images_path:
            file.write(str(image) + '\n')

    with open('doc/new_train_images.txt', 'w') as file:
        for image in new_train_images_path:
            file.write(str(image) + '\n')


# 从路径读入图像，图像预处理，并由RGB转换为灰度图
def preprocess_image(path):
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

    resized_image = ImageOps.equalize(resized_image)  # 应用直方图均衡化

    new_image = Image.new('L', (target_size, target_size), 255)  # 创建白色背景
    left = (target_size - new_width) // 2  # 计算粘贴的起始位置
    right = (target_size - new_height) // 2
    new_image.paste(resized_image, (left, right))  # 将缩放后的图像粘贴到白色背景图像中心
    resized_image.close()  # 关闭图像

    gray_image = new_image.convert("L")  # 转换为灰度图像
    new_image.close()  # 关闭图像
    return gray_image


# 读取图片数据、label数据
def load_data():
    # 读取划分好的测试数据路径和标签
    with open('doc/new_test_images.txt', 'r') as file:
        new_test_images_path = [address.strip() for address in file.readlines()]
    with open('doc/new_train_images.txt', 'r') as file:
        new_train_images_path = [address.strip() for address in file.readlines()]

    with open('doc/train_labels.txt', 'r') as file:
        train_labels = [str(label.strip()) for label in file.readlines()]
    with open('doc/test_labels.txt', 'r') as file:
        test_labels = [str(label.strip()) for label in file.readlines()]
    # 将label由字符串格式改为int类型       比如： '01209'  改为 209
    test_labels = [int(item[-3:]) for item in test_labels]
    train_labels = [int(item[-3:]) for item in train_labels]

    train_image = []
    test_image = []
    for path in new_train_images_path:
        image = tf.io.read_file(path)  # 读出灰度图
        image = tf.image.decode_image(image, channels=1)
        train_image.append(image)
    del new_train_images_path
    for path in new_test_images_path:
        image = tf.io.read_file(path)  # 读出灰度图
        image = tf.image.decode_image(image, channels=1)
        test_image.append(image)
    del new_test_images_path

    # 将数据堆叠为张量
    train_image_tensor = tf.stack(train_image)
    del train_image
    train_label_tensor = tf.stack(train_labels)
    del train_labels
    test_image_tensor = tf.stack(test_image)
    del test_image
    test_label_tensor = tf.stack(test_labels)
    del test_labels

    print("············图片、标签读入完成············")

    # 对训练数据进行增强
    datagen = ImageDataGenerator(
        rotation_range=10,
        width_shift_range=0.2,
        height_shift_range=0.2,
        horizontal_flip=False
    )
    datagen.fit(train_image_tensor)

    print("············数据增强完成············")

    # # 生成增强后的图像
    # augmented_data_generator = datagen.flow(train_image_tensor, batch_size=1)
    # # 打印一张增强后的图像
    # augmented_image = augmented_data_generator.next()[0].astype('int')  # 获取增强后的图像
    # plt.imshow(augmented_image, cmap='gray')
    # plt.axis('off')
    # plt.show()
    # augmented_image = augmented_data_generator.next()[0].astype('int')  # 获取增强后的图像
    # plt.imshow(augmented_image, cmap='gray')
    # plt.axis('off')
    # plt.show()
    # augmented_image = augmented_data_generator.next()[0].astype('int')  # 获取增强后的图像
    # plt.imshow(augmented_image, cmap='gray')
    # plt.axis('off')
    # plt.show()
    return train_label_tensor, train_image_tensor, test_label_tensor, test_image_tensor


# 完整的数据预处理操作
def all_processing():
    # 读取划分好的测试数据路径和标签
    test_images_path, test_labels, train_images_path, train_labels = read_split()
    print("············划分好的测试数据路径和标签读取完成············")
    load_image(train_images_path, test_images_path)
    print("············图片预处理存储完成············")
