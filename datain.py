import pymysql
import json
from datetime import datetime
from decimal import Decimal

# 数据库连接配置
db_config = {
    'host': 'localhost',
    'user': 'bishaoshao',      # 替换为你的数据库用户名
    'password': '123456',      # 替换为你的数据库密码
    'database': 'bishaoshao',  # 替换为你的数据库名
    'charset': 'utf8mb4'
}

# 读取 JSON 文件
def load_json_data(filename='database_data.json'):
    with open(filename, 'r', encoding='utf-8') as file:
        return json.load(file)

# 将 JSON 中的 Decimal 和 datetime 类型转换为适当的格式
def convert_to_db_format(data):
    if isinstance(data, Decimal):
        return float(data)
    elif isinstance(data, datetime):
        return data.strftime('%Y-%m-%d %H:%M:%S')  # 转换为字符串
    return data

# 执行插入数据库操作
def insert_data(table, data, cursor):
    columns = ', '.join(data.keys())
    values = ', '.join(['%s'] * len(data))
    query = f"INSERT INTO {table} ({columns}) VALUES ({values})"
    
    # 转换数据类型
    data = {key: convert_to_db_format(value) for key, value in data.items()}
    
    cursor.execute(query, tuple(data.values()))

# 从 JSON 数据中提取数据并插入数据库
def insert_json_to_db(json_data, cursor):
    for table, rows in json_data.items():
        for row in rows:
            insert_data(table, row, cursor)

if __name__ == '__main__':
    try:
        # 建立数据库连接
        connection = pymysql.connect(**db_config)
        cursor = connection.cursor()

        # 从 JSON 文件加载数据
        json_data = load_json_data('database_data.json')

        # 将 JSON 数据插入数据库
        insert_json_to_db(json_data, cursor)

        # 提交事务
        connection.commit()

        print("数据已成功导入到数据库")

    except Exception as e:
        print("程序运行出错: ", e)
    finally:
        # 关闭数据库连接
        try:
            if cursor:
                cursor.close()
            if connection:
                connection.close()
            print("资源已释放")
        except Exception as e:
            print("释放资源时发生异常: ", e)
