# 使用方法
### 使用已经部署在web上的模型
请运行文件**app.py**，点击输出的网址，比如“http://127.0.0.1:8050”，即可进入我设计的网页，正常填写完所有问题后点击“查看结果”即可看到决策树预测后的结果。
### 完整训练一个模型
从0开始训练一个决策树模型，则请运行文件**model.py**。

## 代码解释
```
code
    app.py
    model.py
    readin.py

    dataset.csv
    depression_anxiety_data.csv

    tree.pkl
    tree.png
```
### ①readin.py————用于csv文件读入、数据预处理（数据清洗）、预处理后数据的保存
data_preprocess()———从"depression_anxiety_data.csv"中读入原始数据，对不必要的数据进行删除，对缺失数据进行填充，对各种类型的数据统一整理成int型，并保存在"dataset.csv"中。

最后返回由所有数据（data和label）组成的列表

### ②model.py————测试集和训练集的划分、决策树模型构建、训练、剪枝、输出决策树构造图，保存决策树模型

注销的代码是决策树剪枝的测试代码，最终筛选出最优的决策树构建算法、决策树训练构造参数。 

将训练好的模型以"tree.pkl"形式保存。  

决策树可视化图片以"tree.png"形式保存。  

### ③app.py————程序的web部署
通过dash包进行界面设计、逻辑设计等，全部填写完成后，读取决策树模型"tree.pkl"，进行预测，最终输出的结果返回到web进行显示


