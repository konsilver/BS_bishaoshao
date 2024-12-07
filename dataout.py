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

# 获取数据函数
def fetch_data(query, cursor):
    cursor.execute(query)
    return cursor.fetchall()

# 将 Decimal 和 datetime 类型转换为适合 JSON 的格式
def convert_decimal_and_datetime(obj):
    if isinstance(obj, Decimal):
        return float(obj)  # 转换为 float
    elif isinstance(obj, datetime):
        return obj.strftime('%Y-%m-%d %H:%M:%S')  # 转换为字符串
    return obj

# 递归遍历数据并转换 Decimal 和 datetime
def process_data(data):
    if isinstance(data, list):
        return [process_data(item) for item in data]
    elif isinstance(data, dict):
        return {key: process_data(value) for key, value in data.items()}
    else:
        return convert_decimal_and_datetime(data)

if __name__ == '__main__':
    try:
        # 建立数据库连接
        connection = pymysql.connect(**db_config)
        cursor = connection.cursor(pymysql.cursors.DictCursor)  # 使用DictCursor以便获取字典类型的返回结果

        # 表名列表
        tables = ['user', 'type', 'card', 'thing', 'history', 'subscribe']
        data = {}

        # 遍历每个表并获取数据
        for table in tables:
            query = f"SELECT * FROM {table};"
            table_data = fetch_data(query, cursor)
            data[table] = table_data

        # 处理数据，将 Decimal 和 datetime 转换为合适格式
        data = process_data(data)

        # 将数据保存为 JSON 文件
        with open('database_data.json', 'w', encoding='utf-8') as json_file:
            json.dump(data, json_file, ensure_ascii=False, indent=4)

        print("数据已导出为 database_data.json")

        # 提交事务（如果有写操作的话）
        connection.commit()

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
