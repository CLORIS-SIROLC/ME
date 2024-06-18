请把code文件夹整个拷到**路径无中文**的地方运行。  
# 使用方法
### 载入已训练好的模型
如果是载入模型测试性能请运行文件**load_model.py**，不要动任何其他文件，尤其是model文件夹，因为model文件夹中存有已经训练好的模型。  
千万注意此时的运行配置是"当前文件"，不是"main",如果是main的话会重新建立model文件夹，导致保存好的模型被覆盖。  
（为了以防万一，备份了一份best_model_A.h5，就放在本文档所在的文件夹里，如果不幸运行了main.py，将best_model_A.h5拷进model文件夹就行）
### 完整训练一个模型
看模型的训练过程，则请运行文件**main.py**。

# 代码解释
```
code
    train_data
    dataset
        test
        train
    doc
        new_test_images.txt
        new_train_images.txt
        train_labels.txt
        train_images.txt
        test_labels.txt
        test_images.txt
    model
        best_model_A.h5
        end_model_A.h5
        Model_A_training_metrics.csv
    char_dict.json
    data_preprocess.py
    load_model.py
    mian.py
    mModel.py
    readin.py
```
### ①readin.py————用于json文件读入、数据划分
read_json()——读取json文件  
split_test_train()——验证集与测试集的划分  
读入图片的路径数据、标签数据，划分后将图片路径与对应标签（打乱后）以txt形式存入doc文件夹中
```
    doc
        train_labels.txt
        train_images.txt
        test_labels.txt
        test_images.txt
```
### ②data_preprocess.py————用于数据预处理与处理后数据存储
read_split()——读取上面保存的四个txt文件  
preprocess_image()——传入图像路径，对图像进行处理后返回灰度图  
load_image()——将预处理后的图像存储在dataset文件夹中的test或train文件夹中  
```
    doc
        new_test_images.txt
        new_train_images.txt
```
将新的图像路径以txt形式存入doc文件夹中  
clear_folder()——清除一个文件夹中的所有文件  
load_data()——用于读取上面提到的标签文件，dataset中的图像文件，并转化为tensorflow能处理的张量，便于模型训练

### ③mModel.py————用于定义模型的结构、模型的评估参数
build_model_A()——搭建我自己的CNN  

### ④main.py————主文件
prepare_folder()——所有需要的文件夹的建立（比如dataset、doc、model）  
if \_\_name\_\_ == "\_\_main\_\_"里面按顺序依次是json文件读入、数据划分、数据预处理，数据读入、模型构建、模型训练、训练曲线绘制的代码  
最终会将训练期间数值变化以csv形式存在model文件夹中，并将训练中性能最好的模型、训练完成后的模型分别保存在model文件夹中：
```
    model
        best_model_A.h5
        end_model_A.h5
        Model_A_training_metrics.csv
```
### ⑤load_model.py————用于对已经训练好的模型进行单张图片的测试
model_A_load()——从model文件夹中读出模型文件best_model_A.h5  
model_A_predict()——从路径读取图片，输出模型预测结果