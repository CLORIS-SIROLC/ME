import joblib
import numpy as np
from matplotlib import pyplot as plt
from sklearn.ensemble import AdaBoostClassifier, GradientBoostingClassifier
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier

from readin import data_preprocess


def choose_model(X_train, X_test, y_train, y_test):
    D_tree = DecisionTreeClassifier()
    D_tree.fit(X_train, y_train)
    # 计算准确率
    train_score_D = D_tree.score(X_train, y_train)
    test_score_D = D_tree.score(X_test, y_test)

    print("CART模型训练集准确率： " + str(train_score_D))
    print("CART模型测试集准确率： " + str(test_score_D))
    print()

    # 创建AdaBoost分类器，基学习器为决策树
    AdaBoost_tree = AdaBoostClassifier()
    AdaBoost_tree.fit(X_train, y_train)

    # 计算准确率
    train_score_A = AdaBoost_tree.score(X_train, y_train)
    test_score_A = AdaBoost_tree.score(X_test, y_test)
    print("AdaBoost模型训练集准确率： " + str(train_score_A))
    print("AdaBoost模型测试集准确率： " + str(test_score_A))
    print()

    # 创建GradientBoosting分类器，基学习器为决策树
    GradientBoosting_tree = GradientBoostingClassifier()
    GradientBoosting_tree.fit(X_train, y_train)

    # 计算准确率
    train_score_G = GradientBoosting_tree.score(X_train, y_train)
    test_score_G = GradientBoosting_tree.score(X_test, y_test)
    print("GradientBoosting模型训练集准确率： " + str(train_score_G))
    print("GradientBoosting模型测试集准确率： " + str(test_score_G))
    print()

    data = [[train_score_D, train_score_A, train_score_G], [test_score_D, test_score_A, test_score_G]]
    colors = ['blue', 'green']
    bar_width = 0.35  # 柱状图宽度
    index = np.array(range(len(data[0])))  # 柱状图索引
    # 绘制训练集准确率柱状图
    plt.bar(index, data[0], bar_width, color=colors[0], label='Train')

    # 绘制测试集准确率柱状图
    plt.bar([i + bar_width for i in index], data[1], bar_width, color=colors[1], label='Test')

    # 添加标签和标题
    plt.xlabel('Model')
    plt.ylabel('Score')
    plt.title('')
    plt.xticks([i + bar_width / 2 for i in index], ['CART', 'AdaBoost', 'GradientBoosting'])
    plt.legend()

    # 显示柱状图
    plt.show()

    return D_tree


# 按间距中的绿色按钮以运行脚本。
if __name__ == '__main__':
    data = data_preprocess()  # 数据读入与数据清洗，处理后数据存入dataset.csv并返回data列表
    target = [row[8] for row in data]
    data = [row[:8] for row in data]
    target = np.array(target)
    data = np.array(data)

    X_train, X_test, y_train, y_test = train_test_split(data, target, test_size=0.3, random_state=32)

    # # 筛选出最合适的模型
    # D_tree = choose_model(X_train, X_test, y_train, y_test)

    # 创建决策树分类器
    D_tree = DecisionTreeClassifier(max_depth=6, min_samples_split=7, min_samples_leaf=3, ccp_alpha=0.0015)
    D_tree.fit(X_train, y_train)
    # train_score = D_tree.score(X_train, y_train)
    # test_score = D_tree.score(X_test, y_test)
    # print("模型训练集准确率： " + str(train_score))
    # print("模型测试集准确率： " + str(test_score))
    # 保存模型
    joblib.dump(D_tree, 'tree.pkl')

    # 绘制决策树
    plt.rcParams['savefig.dpi'] = 200  # 图片像素
    plt.rcParams['figure.dpi'] = 200  # 分辨率
    plt.figure(figsize=(20, 10))  # 设置图片大小
    from sklearn import tree

    tree.plot_tree(D_tree, filled=True)
    # 保存图片
    plt.savefig('tree.png')  # 保存为PNG格式图片

    # # 计算ccp路径
    # pruning_path = D_tree.cost_complexity_pruning_path(X_train, y_train)
    # # 打印结果
    # print("\nCCP路径：")
    # print("ccp_alphas:", pruning_path['ccp_alphas'])
    # print("impurities:", pruning_path['impurities'])
    #

    # all_train_score = []
    # all_test_score = []
    # # 确定max_depth= 6
    # for i in range(10):
    #     # 创建决策树分类器
    #     clf = DecisionTreeClassifier(max_depth=i + 1)
    #     clf.fit(X_train, y_train)
    #     # 在测试集上进行预测
    #     y_pred = clf.predict(X_test)
    #     # 计算准确率
    #     train_score = clf.score(X_train, y_train)
    #     all_train_score.append(train_score)
    #     test_score = clf.score(X_test, y_test)
    #     all_test_score.append(test_score)
    #     print("当 max_depth = {0} 时   ".format(i + 1) + "train accuracy:{0}\ttest accuracy:{1}".format(train_score, test_score))
    # 绘制曲线图
    # x_range = np.array(range(1, 11))
    # plt.plot(x_range, all_train_score, label='train')
    # plt.plot(x_range, all_test_score, label='test')
    # plt.xlabel('max_depth')
    # plt.ylabel('Accuracy')
    # plt.title('Accuracy Curve')
    # plt.legend()
    # plt.show()

    # # 确定min_samples_split= 7
    # all_train_score = []
    # all_test_score = []
    # for i in range(10):
    #     # 创建决策树分类器
    #     clf = DecisionTreeClassifier(min_samples_split=i + 2)
    #     clf.fit(X_train, y_train)
    #     # 在测试集上进行预测
    #     y_pred = clf.predict(X_test)
    #     # 计算准确率
    #     train_score = clf.score(X_train, y_train)
    #     all_train_score.append(train_score)
    #     test_score = clf.score(X_test, y_test)
    #     all_test_score.append(test_score)
    #     print("当 min_samples_split = {0} 时   ".format(i + 1) + "train accuracy:{0}\ttest accuracy:{1}".format(train_score,
    #                                                                                                     test_score))
    # # 绘制曲线图
    # x_range = np.array(range(2, 12))
    # plt.plot(x_range, all_train_score, label='train')
    # plt.plot(x_range, all_test_score, label='test')
    # plt.xlabel('min_samples_split')
    # plt.ylabel('Accuracy')
    # plt.title('Accuracy Curve')
    # plt.legend()
    # plt.show()

    # # 确定min_samples_leaf= 3
    # all_train_score = []
    # all_test_score = []
    # for i in range(10):
    #     # 创建决策树分类器
    #     clf = DecisionTreeClassifier(min_samples_leaf=i + 1)
    #     clf.fit(X_train, y_train)
    #     # 在测试集上进行预测
    #     y_pred = clf.predict(X_test)
    #     # 计算准确率
    #     train_score = clf.score(X_train, y_train)
    #     all_train_score.append(train_score)
    #     test_score = clf.score(X_test, y_test)
    #     all_test_score.append(test_score)
    #     print("当 min_samples_leaf = {0} 时   ".format(i + 1) + "train accuracy:{0}\ttest accuracy:{1}".format(
    #         train_score,
    #         test_score))
    #     # 绘制曲线图
    # x_range = np.array(range(2, 12))
    # plt.plot(x_range, all_train_score, label='train')
    # plt.plot(x_range, all_test_score, label='test')
    # plt.xlabel('min_samples_leaf')
    # plt.ylabel('Accuracy')
    # plt.title('Accuracy Curve')
    # plt.legend()
    # plt.show()
