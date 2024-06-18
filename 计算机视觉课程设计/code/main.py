# 如果要从0开始训练就跑这个文件，如果是测试模型性能的话请不要动这个文件！它会清空所有我建立的文件夹！
import os
from keras.callbacks import EarlyStopping, CSVLogger, ModelCheckpoint
from matplotlib import pyplot as plt
import readin
import data_preprocess
import mModel

# 回调函数： 5轮内指标val_accuracy和accuracy不再改善，则停止训练（防止过拟合）
early_stopping_1 = EarlyStopping(monitor='val_accuracy', patience=5, mode='max', verbose=1)
early_stopping_2 = EarlyStopping(monitor='accuracy', patience=5, mode='max', verbose=1)
# 创建一个CSVLogger回调函数，指定ModelA训练过程种的记录文件路径
csv_logger_A = CSVLogger('model/Model_A_training_metrics.csv', separator=',', append=True)
# 定义一个ModelCheckpoint回调函数，监视ModelA的验证集上的准确率，并保存最好的模型
checkpoint_A = ModelCheckpoint(filepath='model/best_model_A.h5', monitor='val_accuracy', save_best_only=True)


# 先把所有需要的文件夹建好
def prepare_folder():
    folder_name = "doc"
    os.makedirs(folder_name, exist_ok=True)
    data_preprocess.clear_folder(folder_name)  # 清除文件夹中原有的文件
    folder_name = "model"
    os.makedirs(folder_name, exist_ok=True)
    data_preprocess.clear_folder(folder_name)  # 清除文件夹中原有的文件
    # 新建文件夹名字为dataset
    folder_name = "dataset"
    os.makedirs(folder_name, exist_ok=True)
    in_folder = os.path.join(folder_name, "test")
    os.makedirs(in_folder, exist_ok=True)  # 在dataset文件夹中创建test文件夹
    data_preprocess.clear_folder(in_folder)  # 清除文件夹中原有的文件
    in_folder = os.path.join(folder_name, "train")
    os.makedirs(in_folder, exist_ok=True)  # 在dataset文件夹中创建train文件夹
    data_preprocess.clear_folder(in_folder)  # 清除文件夹中原有的文件


if __name__ == "__main__":
    prepare_folder()

    # json文件读入，数据集划分
    # character_dict为读出的json文件
    # 数据集划分见doc文件夹
    character_dict = readin.read_in()

    # 数据预处理并存储在dataset文件夹
    data_preprocess.all_processing()

    # 读入完整数据并进行数据增强
    train_label_tensor, train_image_tensor, test_label_tensor, test_image_tensor = data_preprocess.load_data()

    # 构建模型A
    model_A = mModel.build_model_A()
    print("············模型初始化完成············")
    history_A = model_A.fit(
        x=train_image_tensor,
        y=train_label_tensor,
        epochs=50,
        batch_size=64,
        validation_data=(test_image_tensor, test_label_tensor),
        callbacks=[early_stopping_1, early_stopping_2, csv_logger_A, checkpoint_A],
        shuffle=True  # 随机打乱数据
    )
    model_A.save("model/end_model_A.h5")

    train_loss_A = history_A.history['loss']
    val_loss_A = history_A.history['val_loss']
    train_acc_A = history_A.history['accuracy']
    val_acc_A = history_A.history['val_accuracy']
    epochs_A = range(1, len(train_loss_A) + 1)

    # 绘制Accuracy曲线图
    plt.figure(figsize=(10, 5))
    plt.plot(val_acc_A, label='Model A Val Accuracy', marker='o', linestyle='-', color='blue')
    plt.plot(train_acc_A, label='Model A Train Accuracy', marker='o', linestyle='-', color='red')
    plt.title('Accuracy')
    plt.xlabel('Epochs')
    plt.ylabel('Accuracy')
    plt.ylim(0, 1)  # 设置纵轴范围
    plt.legend()
    plt.grid(True)
    plt.show()

    # 绘制Loss曲线图
    plt.figure(figsize=(10, 5))
    plt.plot(val_loss_A, label='Model A Val Loss', marker='o', linestyle='-', color='blue')
    plt.plot(train_loss_A, label='Model A Train Loss', marker='o', linestyle='-', color='red')
    plt.title('Loss')
    plt.xlabel('Epochs')
    plt.ylabel('Loss')
    plt.ylim(0, )  # 设置纵轴范围
    plt.legend()
    plt.grid(True)
    plt.show()

    print("············模型训练完成············")

