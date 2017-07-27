# WanXiyou后台接口说明
***
## 一级URL
url：http://139.199.20.248:8080/WanXiyou

## 一、获取验证码
#### 请求方式：GET
#### URL：url/edu/GetScretImage
#### 请求参数：无
#### 返回参数：
- Response的header中使用Set-Cookie 取出cookie，后续登陆，请求数据需要使用该cookie
- 返回一张gif验证码

***
## 二、带验证码登陆

#### URL：url/edu/login

#### 请求方式：POST
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
xh | String |04151018|教务系统账号|
password | String|abc123456|密码
code | String | 1sd3|验证码|
cookie|String|ASP.NET_SessionId=w0kwjw55retyy3aqbsfib4vo;|获取验证码得到的Cookie

#### 返回参数
返回类型：json

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | String |1或0|成功返回1，失败返回0
error | String|获取用户信息失败，请稍后再试|当返回失败时，会有此参数，错误信息
data | json | 示例在下面|返回成功时，会有此参数，具体的信息
cookie|String|ASP.NET_SessionId=w0kwjw55retyy3aqbsfib4vo;|登录成功后返回登陆前使用的cookie
user_info|json|示例如下|登录成功后返回的具体用户信息

data参数说明
参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
user_info|json|示例如下|登录成功后返回的具体用户信息
name | String|刘智林|学生姓名
id|String|04151018|登录时的账号

user_info 参数说明

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
xh|String|04151018|教务系统中得到的学号
dqszj | String|2015|当前所在级
cc|String|本科|学历层次
xy|String|计算机学院|学院
xzb|String|计科1501|行政班
xz|String|4|学制
xm|String|刘智林|姓名
zy|String|计算机科学与技术|专业名称





#### 返回信息示例
```json
{
    "code": 1,
    "data": {
        "cookie": "ASP.NET_SessionId=iz5wmsnig3pbvsvzpphzfj45;",
        "user_info": {
            "xh": "04151018",
            "dqszj": "2015",
            "cc": "本科",
            "xy": "计算机学院",
            "xzb": "计科1501",
            "xz": "4",
            "xm": "刘智林",
            "zy": "计算机科学与技术"
        },
        "name": "刘智林",
        "id": "04151018"
    }
}
```



## 培养计划

URL: url/edu/getStudyPlan

#### 请求方式POST/GET

#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
xh | String |04151018|教务系统账号|
name | String|刘智林|姓名
cookie|String|ASP.NET_SessionId=w0kwjw55retyy3aqbsfib4vo;|登录成功码得到的cookie
device|String|iOS或Android|说明设备类型，类型不同返回数据的格式也不同


#### 返回参数
参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | String |1或0|成功返回1，失败返回0
error | String|获取用户信息失败，请稍后再试|当返回失败时，会有此参数，错误信息
data | jsonArray | 示例在下面|返回成功时，会有此参数，具体的信息

当device为iOS时
data返回的是一个jsonArray，array的大小为8，培养计划里面八个学期的数据，jsonArray里面还是一个array，这个array里面存的是每一学期的课程信息，是json格式，也可以将json转Map

- 当device为Android时获取返回信息需要使用Response的header中 result参数的值判断0、1判断成功失败
- 当result为1时,代表请求成功，请使用如下方法获取数据
```java

    ObjectInputStream inputStream = new ObjectInputStream(response.body().byteStream());
    List<List<Map<String,String>>> lists = (List<List<Map<String,String>>>)inputStream.readObject();
```
该lists的大小为8，代表八个学期的课程，其中的元素为list，该list大小代表该学期课程数量，map中存储课程信息，map中的key同下返回信息示例
自行处理异常

-当result为0时，代表请求失败，需要在Response body中获取json数据，code为0，从error中取出错误信息

**Android设备请求时也可以使用iOS参数，获取json串来解析数据**

#### 返回信息示例
```json
{
    "code": 1,
    "data": [
        [
            {
                "周学时": "3.0-0.0",
                "起始结束周": "01-20",
                "课程名称": "C语言程序设计Ⅰ",
                "课程性质": "必修课",
                "学分": "3.0",
                "课程代码": "JS100031",
                "考核方式": "校考"
            },
            {
                "周学时": "2.0-0.0",
                "起始结束周": "01-20",
                "课程名称": "计算机科学导论",
                "课程性质": "必修课",
                "学分": "2.0",
                "课程代码": "JS110100",
                "考核方式": "系考"
            },
            {
                "周学时": "6.0-0.0",
                "起始结束周": "01-20",
                "课程名称": "高等数学AI",
                "课程性质": "必修课",
                "学分": "6.0",
                "课程代码": "LX120111",
                "考核方式": "校考"
            },
            {
                "周学时": "3.0-0.0",
                "起始结束周": "01-20",
                "课程名称": "线性代数A",
                "课程性质": "必修课",
                "学分": "3.0",
                "课程代码": "LX120201",
                "考核方式": "校考"
            },
            {
                "周学时": "0.5-0.0",
                "起始结束周": "01-16",
                "课程名称": "形势与政策1",
                "课程性质": "必修课",
                "学分": "0.5",
                "课程代码": "RW100011",
                "考核方式": "院考"
            },
            {
                "周学时": "2.0-0.0",
                "起始结束周": "01-20",
                "课程名称": "思想道德修养与法律基础",
                "课程性质": "必修课",
                "学分": "3.0",
                "课程代码": "RW100020",
                "考核方式": "院考"
            },
            {
                "周学时": "2.0-0.0",
                "起始结束周": "01-20",
                "课程名称": "大学体育Ⅰ",
                "课程性质": "必修课",
                "学分": "1.0",
                "课程代码": "TY100010",
                "考核方式": "院考"
            },
            {
                "周学时": "4.0-0.0",
                "起始结束周": "01-20",
                "课程名称": "大学英语I",
                "课程性质": "必修课",
                "学分": "4.0",
                "课程代码": "WY100010",
                "考核方式": " "
            },
            {
                "周学时": "1.0-0.0",
                "起始结束周": "01-20",
                "课程名称": "军事理论",
                "课程性质": "必修课",
                "学分": "1.0",
                "课程代码": "WZ100010",
                "考核方式": "院考"
            },
            {
                "周学时": "+2",
                "起始结束周": "01-20",
                "课程名称": "军训",
                "课程性质": "必修课",
                "学分": "2.0",
                "课程代码": "WZ200010",
                "考核方式": " "
            }
        ],
        [
            {
                "周学时": "4.0-0.0",
                "起始结束周": "01-16",
                "课程名称": "C语言程序设计Ⅱ",
                "课程性质": "必修课",
                "学分": "4.0",
                "课程代码": "JS100032",
                "考核方式": "校考"
            },
            {
                "周学时": "4.0-0.0",
                "起始结束周": "01-16",
                "课程名称": "离散数学",
                "课程性质": "必修课",
                "学分": "4.0",
                "课程代码": "JS100330",
                "考核方式": "校考"
            },
            {
                "周学时": "+0.5",
                "起始结束周": "01-18",
                "课程名称": "认识实习",
                "课程性质": "必修课",
                "学分": "0.5",
                "课程代码": "JS200100",
                "考核方式": "院考"
            },
            {
                "周学时": "+1.5",
                "起始结束周": "01-18",
                "课程名称": "高级语言课程设计",
                "课程性质": "必修课",
                "学分": "1.5",
                "课程代码": "JS200110",
                "考核方式": "院考"
            },
            {
                "周学时": "6.0-0.0",
                "起始结束周": "01-16",
                "课程名称": "高等数学AII",
                "课程性质": "必修课",
                "学分": "6.0",
                "课程代码": "LX120121",
                "考核方式": "校考"
            },
            {
                "周学时": "3.0-0.0",
                "起始结束周": "01-16",
                "课程名称": "毛泽东思想和中国特色社会主义理论体系概论",
                "课程性质": "必修课",
                "学分": "6.0",
                "课程代码": "RW100050",
                "考核方式": "院考"
            },
            {
                "周学时": "1.0-0.0",
                "起始结束周": "01-16",
                "课程名称": "大学体育Ⅱ",
                "课程性质": "必修课",
                "学分": "1.0",
                "课程代码": "TY100020",
                "考核方式": "院考"
            },
            {
                "周学时": "4.0-0.0",
                "起始结束周": "01-16",
                "课程名称": "大学英语II",
                "课程性质": "必修课",
                "学分": "4.0",
                "课程代码": "WY100020",
                "考核方式": " "
            },
            {
                "周学时": "2.0-0.0",
                "起始结束周": "01-16",
                "课程名称": "工程制图与计算机制图A",
                "课程性质": "必修课",
                "学分": "2.0",
                "课程代码": "ZD101301",
                "考核方式": "院考"
            }
        ]
    ]
}

```

## 课程表

#### URL： url/edu/getLessonList

#### 请求方式POST/GET

#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
xh | String |04151018|教务系统账号|
name | String|刘智林|姓名
cookie|String|ASP.NET_SessionId=w0kwjw55retyy3aqbsfib4vo;|登录成功码得到的cookie
device|String|iOS或Android|说明设备类型，类型不同返回数据的格式也不同


#### 返回参数
参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | String |1或0|成功返回1，失败返回0
error | String|获取用户信息失败，请稍后再试|当返回失败时，会有此参数，错误信息
data | jsonArray | 示例在下面|返回成功时，会有此参数，具体的信息

- 当device为iOS时

使用code判断成功失败，失败时从error中获取错误信息，成功时使用data获得课表信息

data 是一个jsonArray，jsonArray的大小代表改，横向课表一天有多少节，比如jsonArray.get(0),代表这一周1-2节的课程，jsonArray中的每一个元素还是一个jsonArray，该array中存的是每一天在这一节课是哪一节课。

- 当device为Android
使用Response header中的 result参数判断是否请求成功，0失败，1成功
```java

    ObjectInputStream inputStream = new ObjectInputStream(response.body().byteStream());
    List<List<Map<String,String>>> lists = (List<List<Map<String,String>>>)inputStream.readObject();
```
该lists大小代表，横向课表一天有多少节，比如jsonArray.get(0),代表这一周1-2节的课程，里面的子list的每一个元素表带，每一天在这一节课是哪一节课。

**Android设备请求时也可以使用iOS参数，获取json串来解析数据**

- 共同课程参数说明（Android iOS通用）

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
empty|String|0或1|代表该节课是否为空，应先使用empty判断
lesson_place|String|Fz203|课程教室地点，有可能为NULL
lesson_name|String|透过电影看文化|课程名称
lesson_teacher|String|王法宁|任课老师
lesson_time|String|周一第1,2节{第1-16周}|上课时间

#### 返回示例
```json

{
    "code": 1,
    "data": [
        [
            {
                "lesson_place": "FZ203",
                "lesson_name": "透过电影看文化",
                "lesson_teacher": "王法宁",
                "lesson_time": "周一第1,2节{第1-16周}",
                "empty": "0"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "微机原理与接口技术E",
                "lesson_teacher": "刘军",
                "lesson_time": "周二第1,2节{第1-16周}",
                "empty": "0"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "面向对象与C++程序设计",
                "lesson_teacher": "张德慧",
                "lesson_time": "周三第1,2节{第1-14周}",
                "empty": "0"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "微机原理与接口技术E",
                "lesson_teacher": "刘军",
                "lesson_time": "周四第1,2节{第1-16周}",
                "empty": "0"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "面向对象与C++程序设计",
                "lesson_teacher": "张德慧",
                "lesson_time": "周五第1,2节{第1-16周}",
                "empty": "0"
            },
            {
                "empty": "1"
            },
            {
                "empty": "1"
            }
        ],
        [
            {
                "empty": "1"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "操作系统A",
                "lesson_teacher": "陈莉君",
                "lesson_time": "周二第3,4节{第1-16周}",
                "empty": "0"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "计算方法",
                "lesson_teacher": "吴荣军",
                "lesson_time": "周三第3,4节{第1-16周}",
                "empty": "0"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "操作系统A",
                "lesson_teacher": "陈莉君",
                "lesson_time": "周四第3,4节{第1-16周}",
                "empty": "0"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "MATLAB程序设计基础",
                "lesson_teacher": "高聪",
                "lesson_time": "周五第3,4节{第1-16周}",
                "empty": "0"
            },
            {
                "empty": "1"
            },
            {
                "empty": "1"
            }
        ],
        [
            {
                "empty": "1"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "通信概论A",
                "lesson_teacher": "张晓燕",
                "lesson_time": "周一第5,6节{第1-4周}",
                "empty": "0"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "面向对象与C++程序设计",
                "lesson_teacher": "张德慧",
                "lesson_time": "周二第5,6节{第16-16周}",
                "empty": "0"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "通信概论A",
                "lesson_teacher": "张晓燕",
                "lesson_time": "周三第5,6节{第1-3周|单周}",
                "empty": "0"
            },
            {
                "empty": "1"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "通信概论A",
                "lesson_teacher": "张晓燕",
                "lesson_time": "周五第5,6节{第5-5周}",
                "empty": "0"
            },
            {
                "empty": "1"
            }
        ],
        [
            {
                "empty": "1"
            },
            {
                "empty": "1"
            },
            {
                "lesson_place": "FF207",
                "lesson_name": "通信概论A",
                "lesson_teacher": "张晓燕",
                "lesson_time": "周五第7,8节{第5-5周}",
                "empty": "0"
            },
            {
                "empty": "1"
            },
            {
                "empty": "1"
            },
            {
                "empty": "1"
            },
            {
                "empty": "1"
            }
        ],
        [
            {
                "empty": "1"
            },
            {
                "empty": "1"
            },
            {
                "empty": "1"
            },
            {
                "empty": "1"
            },
            {
                "empty": "1"
            },
            {
                "empty": "1"
            },
            {
                "empty": "1"
            }
        ]
    ]
}
```

## 获取成绩

#### URL：url/edu/getScoresList

#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
xh | String |04151018|教务系统账号|
name | String|刘智林|姓名
cookie|String|ASP.NET_SessionId=w0kwjw55retyy3aqbsfib4vo;|登录成功码得到的cookie
device|String|iOS或Android|说明设备类型，类型不同返回数据的格式也不同


#### 返回参数
参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | String |1或0|成功返回1，失败返回0
error | String|获取用户信息失败，请稍后再试|当返回失败时，会有此参数，错误信息
data | jsonArray | 示例在下面|返回成功时，会有此参数，具体的信息

- 当device为iOS时

使用code判断成功失败，失败时从error中获取错误信息，成功时使用data获得课表信息

data 是一个jsonArray，jsonArray大小为2，array[0]中存放着所有学期的名称，使用循环下表i将其取出（i的范围从0-array.length-1）,array[1]中存放着该学期的数据，使用学期名称将其取出

- 当device为Android
使用Response header中的 result参数判断请求是否成功，0失败，1成功

**Android设备请求时也可以使用iOS参数，获取json串来解析数据**

```java

    ObjectInputStream inputStream = new ObjectInputStream(response.body().byteStream());
    Object object = inputStream.readObject();
    Map<String,List<Map<String,String>>> map = (Map<String,List<Map<String,String>>>) object;
```
map中存储这 学期的名称和学期成绩列表，可以使用Entry同时取出key和value

成绩公用参数
参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
xn|String|2016-2017|学年
xq|String|2|学期
kcmc|String|数字电路|课程名称
kcxz|String|选修课|课程学制
kcdm|String|cx11111|课程代码
xf|String|4.0|学分
jd|String|2.3|绩点
cj|String|86|成绩
kkxy|String|电院|课程学院


#### 返回示例

![image](http://note.youdao.com/yws/api/personal/file/4618246C5663452A9723D0CB0272827D?method=download&shareKey=d6cd75d3729a711d419b474c79dd03bc)

![image](http://note.youdao.com/yws/api/personal/file/4104DD542C294E3EBFB7A3D5236BCCCD?method=download&shareKey=37dced2e6bc6010a46d4b22980a3d95e)


## 获取成绩统计

#### URL：url/edu/getScoresStatistics

#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
xh | String |04151018|教务系统账号|
name | String|刘智林|姓名
cookie|String|ASP.NET_SessionId=w0kwjw55retyy3aqbsfib4vo;|登录成功码得到的cookie
device|String|iOS或Android|说明设备类型，类型不同返回数据的格式也不同


#### 返回参数
参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | String |1或0|成功返回1，失败返回0
error | String|获取用户信息失败，请稍后再试|当返回失败时，会有此参数，错误信息
data | jsonArray | 示例在下面|返回成功时，会有此参数，具体的信息

- 当device为iOS时

使用code判断成功失败，失败时从error中获取错误信息，成功时使用data获得信息

data 是一个jsonArray，jsonArray大小为2，array[0]中存着 成绩统计信息总评，array[1]中data 中存着，每一类课程的具体情况

- 当device为Android
使用Response header中的 result参数判断请求是否成功，0失败，1成功

**Android设备请求时也可以使用iOS参数，获取json串来解析数据**

```java

    ObjectInputStream inputStream = new ObjectInputStream(response.body().byteStream());
     final List<List<String>> dataResponseData = (List<List<String>>)inputStream.readObject();
```
该dateResponseData的格式如同 json格式中  array[1]中data的格式。

对应着如下表格

![iamge](http://note.youdao.com/yws/api/personal/file/5A1B86DBDD3F40078E4016E03F8D2613?method=download&shareKey=67e5c044eb287439d6f12e579bd3d706)


#### 返回示例

```json

{
    "code": 1,
    "data": [
        {
            "code": 1,
            "data": "所选学分110.50；获得学分101；重修学分0；正考未通过学分 19.50。"
        },
        {
            "code": 1,
            "data": [
                [
                    "课程性质名称",
                    "学分要求",
                    "获得学分",
                    "未通过学分",
                    "还需学分"
                ],
                [
                    "必修课",
                    "89",
                    "81",
                    "4",
                    "8"
                ],
                [
                    "课外实践教学",
                    "8",
                    "0",
                    "0",
                    "8"
                ],
                [
                    "选修课",
                    "49",
                    "20",
                    "5.50",
                    "29"
                ]
            ]
        }
    ]
}
```
