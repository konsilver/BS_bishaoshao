import pymysql
import random
from datetime import datetime, timedelta
from decimal import Decimal

# 数据库连接配置
db_config = {
    'host': 'localhost',
    'user': 'bishaoshao',      # 替换为你的数据库用户名
    'password': '123456',      # 替换为你的数据库密码
    'database': 'bishaoshao',  # 替换为你的数据库名
    'charset': 'utf8mb4'
}

def generate_price_variation(base_price):
    base_price = Decimal(base_price)
    # 随机生成价格波动，范围是 base_price 的 0.5 ~ 2 倍
    return round(base_price * Decimal(random.uniform(0.8, 1.2)), 2)

def generate_history_data(crawl_date, base_price):
    # 生成过去 3 个月的历史数据，间隔 7 天，共 12 条数据
    history_data = []
    for i in range(12):
        price = generate_price_variation(base_price)
        # 计算每次的日期，crawl_date 向前推 7 天
        date = crawl_date - timedelta(weeks=i)
        # 只保留年月日格式
        date_str = date.strftime('%Y-%m-%d')
        history_data.append((price, date_str))
    return history_data

if __name__ == '__main__':
    try:
        # 建立数据库连接
        connection = pymysql.connect(**db_config)
        cursor = connection.cursor()

        # 查询 card 表中的所有数据
        cursor.execute("SELECT id, price, crawl_date FROM card")
        cards = cursor.fetchall()

        # 遍历每一张卡片
        for card in cards:
            card_id, card_price, crawl_date = card
            # 将 crawl_date 转换为 datetime 对象
            crawl_date = crawl_date.date()

            # 生成该商品的历史价格波动数据
            history_data = generate_history_data(crawl_date, card_price)

            # 向 history 表插入数据
            for price, date in history_data:
                cursor.execute(
                    "INSERT INTO history (thing_id, crawl_date, price) VALUES (%s, %s, %s)",
                    (card_id, date, price)
                )
        
        # 提交事务
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
