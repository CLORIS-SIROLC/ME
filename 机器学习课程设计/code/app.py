import dash
import feffery_antd_components as fac  # fac通用组件库
import joblib
import numpy as np
from dash import html  # dash自带的原生html组件库
from dash.dependencies import Input, Output  # 用于构建应用交互功能的不同角色

# 创建一个全为0的列表
data_1 = np.zeros(5)  # 存储第1部分选项
data_2 = np.zeros(9)  # 存储第2部分选项
data_3 = np.zeros(7)  # 存储第3部分选项
data_4 = -1  # 存储自杀倾向
data_5 = -1  # 存储嗜睡倾向
end = np.zeros(8)  # 存放总的结果（不包含最终结果维）
str_0 = "评估结果"
str_1 = '''恭喜！您没有或只有轻度的焦虑倾向和抑郁倾向，这表明您目前的心理状态良好。\n以下是一些小建议：\n
①保持良好的心态，②注重自我关爱，③寻求平衡，④建立支持系统，⑤学习应对压力，⑥寻找兴趣爱好，⑦关注心理健康，
⑧最重要的是，要珍惜自己的心理健康，认识到它和整体幸福感之间的重要联系。\n祝您一切顺利，保持健康快乐！'''
str_2 = '''出现中重度焦虑倾向是一个需要关注的问题，焦虑是一种常见的心理健康问题，严重的焦虑可能导致持续的紧张和不安感、
心悸和身体不适、恐惧和恐慌、焦虑性思维和行为、身体紧张和肌肉疼痛、睡眠问题等，严重影响个人的日常功能和社交关系，甚至增加自我危害的风险。
因此建议您及时就医，寻求专业心理咨询或治疗。
专业的心理健康专家可以帮助您了解焦虑的原因，提供有效的治疗方案，并指导您应对焦虑情绪。'''
str_3 = '''中重度抑郁倾向是一种常见的心理健康问题，它不仅会影响个人的情绪状态，还会影响到日常生活、学习和人际关系。
抑郁症可能表现为持续的悲伤、消极情绪、失去兴趣和快乐感、自我负罪感、睡眠问题等。
严重的抑郁症可能导致自我伤害甚至自杀的风险。因此建议您及时就医，寻求专业心理咨询或治疗。
专业的心理健康专家可以帮助您了解抑郁的原因，提供有效的治疗方案，并指导您应对抑郁情绪。'''
str_4 = '''出现中重度焦虑倾向和中重度抑郁倾向是一个非常非常需要关注的问题，您可能伴有心悸和身体不适、持续的悲伤、消极情绪、
恐惧和恐慌、焦虑性思维和行为、身体紧张和肌肉疼痛、睡眠问题、失去兴趣和快乐感、自我负罪感等，因此建议您及时就医，寻求专业心理咨询或治疗。'''

list_grade = ['大一', '大二', '大三', '大四']  # 存储年级选项的数组
list_gender = ['男', '女']  # 存储年级选项的数组
list_phq = ['从不', '几天', '大部分时间', '每天']
list_gad = ['从不', '几天', '大部分时间', '每天']

app = dash.Dash(__name__)  # dash 实例化

app.layout = html.Div(  # 创建一个网页
    [
        fac.AntdSpace(
            [
                fac.AntdTitle(
                    '大学生心理健康评估',
                    level=1,
                    type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                    mark=True  # 默认为False,用于设置是否以高亮标黄模式渲染文字内容
                ),
                fac.AntdTitle(
                    '人工智能211 孔涵玥 11521118',
                    level=5,
                    type='secondary',  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                    mark=False  # 默认为False,用于设置是否以高亮标黄模式渲染文字内容
                )
            ],
            style={
                'padding': '10px 50px',  # 上下页边距10像素px，左右50
            }
        ),
        fac.AntdDivider(isDashed=False),  # 放置水平分割线  isDashed=True虚线，False实线
        fac.AntdTitle(
            '本问卷不会涉及任何个人信息，请认真阅读，依照自身情况仔细填写。',
            level=4,
            type='warning',  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
            mark=False,  # 默认为False,用于设置是否以高亮标黄模式渲染文字内容
            style={
                'padding': '10px 50px',  # 上下页边距10像素px，左右50
            }
        ),
        fac.AntdDivider(
            '第一部分',
            innerTextOrientation='left',
            isDashed=True,
            fontWeight='bold',
            fontFamily='KaiTi'
        ),
        # 第一部分
        fac.AntdSpace(
            [
                # 题目1-1
                fac.AntdSpace([
                    fac.AntdText(
                        '1.1 你现在就读的年级是？\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_grade
                        ],
                        id='AntdRadioGroup_1_1',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目1-2
                fac.AntdSpace([
                    fac.AntdText(
                        '1.2 你现在的年龄是（）岁？\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdInputNumber(
                        defaultValue=None,
                        id='AntdInputNumber_1_2',
                        style={
                            'width': 150
                        }
                    )
                ],
                    direction='vertical'
                ),
                # 题目1-3
                fac.AntdSpace([
                    fac.AntdText(
                        '1.3 你的性别是？\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_gender
                        ],
                        id='AntdRadioGroup_1_3',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目1-4
                fac.AntdSpace([
                    fac.AntdText(
                        '1.4 你现在的身高是（）厘米？\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdInputNumber(
                        defaultValue=None,
                        id='AntdInputNumber_1_4',
                        style={
                            'width': 150
                        }
                    )
                ],
                    direction='vertical'
                ),
                # 题目1-5
                fac.AntdSpace([
                    fac.AntdText(
                        '1.5 你现在的体重是（）公斤？\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdInputNumber(
                        defaultValue=None,
                        id='AntdInputNumber_1_5',
                        style={
                            'width': 150
                        }
                    )
                ],
                    direction='vertical'
                ),
            ],
            align='start',
            direction='vertical',
            style={
                'padding': '10px 100px',  # 上下页边距10像素px，左右100
                'width': '100%',
                'backgroundColor': 'rgba(240, 240, 240, 0.5)'
            }

        ),
        fac.AntdDivider(
            '第二部分',
            innerTextOrientation='left',
            isDashed=True,
            fontWeight='bold',
            fontFamily='KaiTi'
        ),
        # 第二部分
        fac.AntdSpace(
            [
                fac.AntdTitle(
                    '在过去两个星期，您有多少时间被以下问题所困扰?',
                    level=5,
                    mark=False,  # 默认为False,用于设置是否以高亮标黄模式渲染文字内容
                    type='secondary',
                ),
                # 题目2-1
                fac.AntdSpace([
                    fac.AntdText(
                        '2.1 做什么事都感到没有兴趣或乐趣\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_phq
                        ],
                        id='AntdRadioGroup_2_1',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目2-2
                fac.AntdSpace([
                    fac.AntdText(
                        '2.2 感到沮丧、消沉或绝望\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_phq
                        ],
                        id='AntdRadioGroup_2_2',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目2-3
                fac.AntdSpace([
                    fac.AntdText(
                        '2.3 入睡困难、很难熟睡或睡太多\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_phq
                        ],
                        id='AntdRadioGroup_2_3',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目2-4
                fac.AntdSpace([
                    fac.AntdText(
                        '2.4 感到疲劳或无精打采\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_phq
                        ],
                        id='AntdRadioGroup_2_4',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目2-5
                fac.AntdSpace([
                    fac.AntdText(
                        '2.5 食欲不振或暴饮暴食\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_phq
                        ],
                        id='AntdRadioGroup_2_5',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目2-6
                fac.AntdSpace([
                    fac.AntdText(
                        '2.6 觉得自己很糟，很失败，让家人失望\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_phq
                        ],
                        id='AntdRadioGroup_2_6',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目2-7
                fac.AntdSpace([
                    fac.AntdText(
                        '2.7 注意很难集中\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_phq
                        ],
                        id='AntdRadioGroup_2_7',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目2-8
                fac.AntdSpace([
                    fac.AntdText(
                        '2.8 行动或说话缓慢到其他人可能已经注意到的程度，或者坐立不安，比平常动作更多\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_phq
                        ],
                        id='AntdRadioGroup_2_8',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目2-9
                fac.AntdSpace([
                    fac.AntdText(
                        '2.9 认为自己最好死去，或有用某种方式伤害自己的想法或行为\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_phq
                        ],
                        id='AntdRadioGroup_2_9',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),

            ],
            align='start',
            direction='vertical',
            style={
                'padding': '10px 100px',  # 上下页边距10像素px，左右100
                'width': '100%',
                'backgroundColor': 'rgba(240, 240, 240, 0.5)'
            }

        ),
        fac.AntdDivider(
            '第三部分',
            innerTextOrientation='left',
            isDashed=True,
            fontWeight='bold',
            fontFamily='KaiTi'
        ),
        # 第三部分
        fac.AntdSpace(
            [
                fac.AntdTitle(
                    '在过去两个星期，您有多少时间被以下问题所困扰?',
                    level=5,
                    mark=False,  # 默认为False,用于设置是否以高亮标黄模式渲染文字内容
                    type='secondary',
                ),
                # 题目3-1
                fac.AntdSpace([
                    fac.AntdText(
                        '3.1 感到紧张、焦虑或不安\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_gad
                        ],
                        id='AntdRadioGroup_3_1',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目3-2
                fac.AntdSpace([
                    fac.AntdText(
                        '3.2 容易被小事惊吓或感到害怕\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_gad
                        ],
                        id='AntdRadioGroup_3_2',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目3-3
                fac.AntdSpace([
                    fac.AntdText(
                        '3.3 感到身体或肌肉紧张\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_gad
                        ],
                        id='AntdRadioGroup_3_3',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目3-4
                fac.AntdSpace([
                    fac.AntdText(
                        '3.4 很难放松下来\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_gad
                        ],
                        id='AntdRadioGroup_3_4',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目3-5
                fac.AntdSpace([
                    fac.AntdText(
                        '3.5 很难控制自己的忧虑\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_gad
                        ],
                        id='AntdRadioGroup_3_5',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),
                # 题目3-6
                fac.AntdSpace([
                    fac.AntdText(
                        '3.6 担心会发生坏事\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_gad
                        ],
                        id='AntdRadioGroup_3_6',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                ),

                # 题目3-7
                fac.AntdSpace([
                    fac.AntdText(
                        '3.7 到坐立不安或难以静坐\n',
                        type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                        mark=None,  # 默认为False,是否以高亮标黄模式渲染文字内容
                        strong=True  # 是否加粗
                    ),
                    fac.AntdRadioGroup(  # 单选框
                        options=[
                            {
                                'label': f'{c}',
                                'value': c
                            }
                            for c in list_gad
                        ],
                        id='AntdRadioGroup_3_7',
                        defaultValue=None
                    )
                ],
                    direction='vertical'
                )

            ],
            align='start',
            direction='vertical',
            style={
                'padding': '10px 100px',  # 上下页边距10像素px，左右100
                'width': '100%',
                'backgroundColor': 'rgba(240, 240, 240, 0.5)'
            }
        ),
        fac.AntdDivider(isDashed=False),
        fac.AntdSpace(
            [
                fac.AntdButton(
                    '查看结果',
                    type='primary',
                    size='large',
                    id='AntdButton_end',
                    style={
                        'width': '150%',
                        'backgroundColor': 'rgba(235, 110 , 50, 0.9)',
                    }
                ),

            ],
            align='center',
            direction='vertical',
            style={
                'padding': '10px 100px',  # 上下页边距10像素px，左右100
                'width': '100%'
                # 'height': '200px',
            }
        ),
        # fac.AntdDivider(isDashed=True),  # 放置水平分割线  isDashed=True虚线，False实线
        fac.AntdSpace(
            [
                fac.AntdTitle(
                    ' ',
                    id="end_1",
                    level=3,
                    type='warning',  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                    mark=False,  # 默认为False,用于设置是否以高亮标黄模式渲染文字内容
                    strong=True  # 是否加粗
                ),
                fac.AntdText(
                    ' ',
                    id='end_2',
                    type=None,  # 颜色 'secondary'灰色、'success'绿色、'warning'黄色及'danger'红色，
                    strong=False  # 是否加粗
                )
            ],
            direction='vertical',
            style={
                'padding': '10px 100px',  # 上下页边距10像素px，左右100
                'width': '100%'
            }
        )
    ],
    style={
        'padding': '50px 100px'  # 上下页边距50像素px，左右100
    }

)


# 最终按钮的回调
@app.callback(
    Output('end_1', 'children'),
    Output('end_2', 'children'),
    Input('AntdButton_end','nClicks'),
    prevent_initial_call=True
)
def button_click_end(nClicks):
    global data_1, data_2, data_3, data_4, data_5, end
    global str_1, str_2, str_3, str_4
    end[0] = data_1[0]  # 学年
    end[1] = data_1[1]  # 年龄
    end[2] = data_1[2]  # 性别

    # 计算bmi
    if data_1[3] == 0:
        bmi = data_1[4] / 1
    else:
        bmi = data_1[4] / (data_1[3] * data_1[3])
    if bmi < 18.5:
        end[3] = 0  # Underweight
    elif bmi < 25:
        end[3] = 1  # Normal
    elif bmi < 30:
        end[3] = 2  # Overweight
    elif bmi < 35:
        end[3] = 3  # Class I Obesity
    elif bmi < 40:
        end[3] = 4  # Class II Obesity
    else:
        end[3] = 5  # Class III Obesity

    # 抑郁情况
    phq = 0
    for i in data_2:
        phq = phq + i
    if phq < 5:
        end[4] = 0  # 可能没有抑郁症状None - minimal
    elif phq < 10:
        end[4] = 1  # 轻度抑郁Mild
    elif phq < 15:
        end[4] = 2  # 中度抑郁Moderate
    elif phq < 20:
        end[4] = 3  # 中重度抑郁Moderately
    else:
        end[4] = 4  # 重度抑郁Severe

    end[5] = data_4  # 自杀倾向
    # 焦虑情况
    gad = 0
    for i in data_3:
        gad = gad + i
    if gad < 5:
        end[6] = 0  # 无焦虑症状
    elif gad < 10:
        end[6] = 1  # 轻度焦虑
    elif gad < 15:
        end[6] = 2  # 中度焦虑
    else:
        end[6] = 3  # 重度焦虑

    end[7] = data_5  # 嗜睡倾向

    # 加载保存的决策树模型
    loaded_model = joblib.load('tree.pkl')
    array = np.array([end])
    end = np.zeros(8)

    predictions = loaded_model.predict(array)  # 进行预测

    if predictions[0] == 0:  # 无焦虑倾向无抑郁倾向
        str_consequence = str_1
        str_end = str_0 + '————无焦虑倾向、无抑郁倾向'
        return [str_end, str_consequence]
    elif predictions[0] == 1:  # 有焦虑倾向无抑郁倾向
        str_consequence = str_2
        str_end = str_0 + '————有焦虑倾向、无抑郁倾向'
        return [str_end, str_consequence]
    elif predictions[0] == 2:  # 无焦虑倾向有抑郁倾向
        str_consequence = str_3
        str_end = str_0 + '————无焦虑倾向、有抑郁倾向'
        return [str_end, str_consequence]
    elif predictions[0] == 3:  # 有焦虑倾向有抑郁倾向
        str_consequence = str_4
        str_end = str_0 + '————有焦虑倾向、有抑郁倾向'
        return [str_end, str_consequence]


# 问题1-1的回调
@app.callback(
    Input("AntdRadioGroup_1_1", 'value'),
    prevent_initial_call=True
)
def button_click_1_1(value):
    global data_1
    if str(value) == str(list_grade[0]):  # 大一
        data_1[0] = 1
    elif str(value) == str(list_grade[1]):  # 大二
        data_1[0] = 2
    elif str(value) == str(list_grade[2]):  # 大三
        data_1[0] = 3
    elif str(value) == str(list_grade[3]):  # 大四
        data_1[0] = 4


# 问题1-2的回调
@app.callback(
    Input('AntdInputNumber_1_2', 'value')
)
def input_number_2(value):
    global data_1
    data_1[1] = value


# 问题1-3的回调
@app.callback(
    Input("AntdRadioGroup_1_3", 'value'),
    prevent_initial_call=True
)
def button_click_1_3(value):
    global data_1
    if str(value) == str(list_gender[0]):
        data_1[2] = 0
    elif str(value) == str(list_gender[1]):
        data_1[2] = 1


# 问题1-4的回调
@app.callback(
    Input('AntdInputNumber_1_4', 'value')
)
def input_number_4(value):
    global data_1
    data_1[3] = value


# 问题1-5的回调
@app.callback(
    Input('AntdInputNumber_1_5', 'value')
)
def input_number_5(value):
    global data_1
    data_1[4] = value


# 问题2-1的回调
@app.callback(
    Input("AntdRadioGroup_2_1", 'value'),
    prevent_initial_call=True
)
def button_click_2_1(value):
    global data_2
    if str(value) == str(list_phq[0]):
        data_2[0] = 0
    elif str(value) == str(list_phq[1]):
        data_2[0] = 1
    elif str(value) == str(list_phq[2]):
        data_2[0] = 2
    elif str(value) == str(list_phq[3]):
        data_2[0] = 3


# 问题2-2的回调
@app.callback(
    Input("AntdRadioGroup_2_2", 'value'),
    prevent_initial_call=True
)
def button_click_2_2(value):
    global data_2
    if str(value) == str(list_phq[0]):
        data_2[1] = 0
    elif str(value) == str(list_phq[1]):
        data_2[1] = 1
    elif str(value) == str(list_phq[2]):
        data_2[1] = 2
    elif str(value) == str(list_phq[3]):
        data_2[1] = 3


# 问题2-3的回调
@app.callback(
    Input("AntdRadioGroup_2_3", 'value'),
    prevent_initial_call=True
)
def button_click_2_3(value):
    global data_2
    global data_5
    if str(value) == str(list_phq[0]):
        data_2[2] = 0
        data_5 = 0
    elif str(value) == str(list_phq[1]):
        data_2[2] = 1
        data_5 = 0
    elif str(value) == str(list_phq[2]):
        data_2[2] = 2
        data_5 = 1
    elif str(value) == str(list_phq[3]):
        data_2[2] = 3
        data_5 = 1


# 问题2-4的回调
@app.callback(
    Input("AntdRadioGroup_2_4", 'value'),
    prevent_initial_call=True
)
def button_click_2_4(value):
    global data_2
    if str(value) == str(list_phq[0]):
        data_2[3] = 0
    elif str(value) == str(list_phq[1]):
        data_2[3] = 1
    elif str(value) == str(list_phq[2]):
        data_2[3] = 2
    elif str(value) == str(list_phq[3]):
        data_2[3] = 3


# 问题2-5的回调
@app.callback(
    Input("AntdRadioGroup_2_5", 'value'),
    prevent_initial_call=True
)
def button_click_2_5(value):
    global data_2
    if str(value) == str(list_phq[0]):
        data_2[4] = 0
    elif str(value) == str(list_phq[1]):
        data_2[4] = 1
    elif str(value) == str(list_phq[2]):
        data_2[4] = 2
    elif str(value) == str(list_phq[3]):
        data_2[4] = 3


# 问题2-6的回调
@app.callback(
    Input("AntdRadioGroup_2_6", 'value'),
    prevent_initial_call=True
)
def button_click_2_6(value):
    global data_2
    if str(value) == str(list_phq[0]):
        data_2[5] = 0
    elif str(value) == str(list_phq[1]):
        data_2[5] = 1
    elif str(value) == str(list_phq[2]):
        data_2[5] = 2
    elif str(value) == str(list_phq[3]):
        data_2[5] = 3


# 问题2-7的回调
@app.callback(
    Input("AntdRadioGroup_2_7", 'value'),
    prevent_initial_call=True
)
def button_click_2_7(value):
    global data_2
    if str(value) == str(list_phq[0]):
        data_2[6] = 0
    elif str(value) == str(list_phq[1]):
        data_2[6] = 1
    elif str(value) == str(list_phq[2]):
        data_2[6] = 2
    elif str(value) == str(list_phq[3]):
        data_2[6] = 3


# 问题2-8的回调
@app.callback(
    Input("AntdRadioGroup_2_8", 'value'),
    prevent_initial_call=True
)
def button_click_2_8(value):
    global data_2
    if str(value) == str(list_phq[0]):
        data_2[7] = 0
    elif str(value) == str(list_phq[1]):
        data_2[7] = 1
    elif str(value) == str(list_phq[2]):
        data_2[7] = 2
    elif str(value) == str(list_phq[3]):
        data_2[7] = 3


# 问题2-9的回调
@app.callback(
    Input("AntdRadioGroup_2_9", 'value'),
    prevent_initial_call=True
)
def button_click_2_9(value):
    global data_2
    global data_4
    if str(value) == str(list_phq[0]):
        data_2[8] = 0
        data_4 = 0
    elif str(value) == str(list_phq[1]):
        data_2[8] = 1
        data_4 = 0
    elif str(value) == str(list_phq[2]):
        data_2[8] = 2
        data_4 = 1
    elif str(value) == str(list_phq[3]):
        data_2[8] = 3
        data_4 = 1


# 问题3-1的回调
@app.callback(
    Input("AntdRadioGroup_3_1", 'value'),
    prevent_initial_call=True
)
def button_click_3_1(value):
    global data_3
    if str(value) == str(list_gad[0]):
        data_3[0] = 0
    elif str(value) == str(list_gad[1]):
        data_3[0] = 1
    elif str(value) == str(list_gad[2]):
        data_3[0] = 2
    elif str(value) == str(list_gad[3]):
        data_3[0] = 3


# 问题3-2的回调
@app.callback(
    Input("AntdRadioGroup_3_2", 'value'),
    prevent_initial_call=True
)
def button_click_3_2(value):
    global data_3
    if str(value) == str(list_gad[0]):
        data_3[1] = 0
    elif str(value) == str(list_gad[1]):
        data_3[1] = 1
    elif str(value) == str(list_gad[2]):
        data_3[1] = 2
    elif str(value) == str(list_gad[3]):
        data_3[1] = 3


# 问题3-3的回调
@app.callback(
    Input("AntdRadioGroup_3_3", 'value'),
    prevent_initial_call=True
)
def button_click_3_3(value):
    global data_3
    if str(value) == str(list_gad[0]):
        data_3[2] = 0
    elif str(value) == str(list_gad[1]):
        data_3[2] = 1
    elif str(value) == str(list_gad[2]):
        data_3[2] = 2
    elif str(value) == str(list_gad[3]):
        data_3[2] = 3


# 问题3-4的回调
@app.callback(
    Input("AntdRadioGroup_3_4", 'value'),
    prevent_initial_call=True
)
def button_click_3_4(value):
    global data_3
    if str(value) == str(list_gad[0]):
        data_3[3] = 0
    elif str(value) == str(list_gad[1]):
        data_3[3] = 1
    elif str(value) == str(list_gad[2]):
        data_3[3] = 2
    elif str(value) == str(list_gad[3]):
        data_3[3] = 3


# 问题3-5的回调
@app.callback(
    Input("AntdRadioGroup_3_5", 'value'),
    prevent_initial_call=True
)
def button_click_3_5(value):
    global data_3
    if str(value) == str(list_gad[0]):
        data_3[4] = 0
    elif str(value) == str(list_gad[1]):
        data_3[4] = 1
    elif str(value) == str(list_gad[2]):
        data_3[4] = 2
    elif str(value) == str(list_gad[3]):
        data_3[4] = 3


# 问题3-6的回调
@app.callback(
    Input("AntdRadioGroup_3_6", 'value'),
    prevent_initial_call=True
)
def button_click_3_6(value):
    global data_3
    if str(value) == str(list_gad[0]):
        data_3[5] = 0
    elif str(value) == str(list_gad[1]):
        data_3[5] = 1
    elif str(value) == str(list_gad[2]):
        data_3[5] = 2
    elif str(value) == str(list_gad[3]):
        data_3[5] = 3


# 问题3-7的回调
@app.callback(
    Input("AntdRadioGroup_3_7", 'value'),
    prevent_initial_call=True
)
def button_click_3_7(value):
    global data_3
    if str(value) == str(list_gad[0]):
        data_3[6] = 0
    elif str(value) == str(list_gad[1]):
        data_3[6] = 1
    elif str(value) == str(list_gad[2]):
        data_3[6] = 2
    elif str(value) == str(list_gad[3]):
        data_3[6] = 3


# server = pywsgi.WSGIServer(('127.0.0.1', 5000), app)
# server.serve_forever()

if __name__ == '__main__':
    app.run(debug=True)
