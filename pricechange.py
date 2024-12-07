import pymysql
from datetime import timedelta
import requests       
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

spring_boot_url = "http://localhost:8082/api/subscribe/remind"


if __name__ == '__main__':
    try:
        # 建立数据库连接
        connection = pymysql.connect(**db_config)
        cursor = connection.cursor()

        # 查询 card 表中的所有数据
        cursor.execute("SELECT title, price, shop, img_url,source, child_type,thing_url FROM card WHERE id=3")
        cards = cursor.fetchall()

        #put your code here 
        for card in cards:
            # 遍历每一张卡片
            title, price, shop, img_url, source, child_type,thing_url=card
            price=round(price * Decimal(0.94), 2)
            crawl_date=datetime.now().date()
            # 插入商品的 SQL 语句（如果存在，更新）
            insert_query = """
                INSERT INTO card (title, price, shop, img_url, source, child_type, thing_url, crawl_date)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
            """

            # 插入历史记录的 SQL 语句
            history_insert_query = """
                INSERT INTO history (thing_id, crawl_date, price)
                VALUES (%s, %s, %s)
            """

            # 检查是否存在违反唯一性约束的记录
            check_existing_query = """
                SELECT id, price, crawl_date
                FROM card
                WHERE title = %s AND shop = %s AND source = %s AND child_type = %s AND thing_url=%s
            """
            cursor.execute(check_existing_query, (title, shop, source, child_type,thing_url))
            existing_record = cursor.fetchone()

            if existing_record:  # 记录已存在，违反唯一性约束
                # 获取现有记录的 ID, price 和 crawl_date
                existing_id, existing_price, existing_crawl_date = existing_record

                # 将旧的价格和爬取时间插入到历史记录表
                cursor.execute(history_insert_query, (existing_id, existing_crawl_date, existing_price))

                # 更新卡片信息
                cursor.execute("""
                    UPDATE card
                    SET price = %s, img_url = %s, crawl_date = %s
                    WHERE id = %s
                """, (price, img_url, crawl_date, existing_id))


            else:  # 记录不存在，直接插入新记录
                cursor.execute(insert_query, (
                    title, price, shop, img_url, source, child_type, thing_url, crawl_date
                ))

            connection.commit()
        
        response = requests.get(spring_boot_url)

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
