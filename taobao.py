# 声明第三方库/头文件
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from pyquery import PyQuery as pq
import time
import pymysql
from datetime import datetime, timedelta
from fake_useragent import UserAgent
import requests                   

import random

# 设置多个User-Agent供随机选择
user_agents = [
    'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36',
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36',
    'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36',
    'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36',
    'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36',
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36',
    'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0',
    'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:94.0) Gecko/20100101 Firefox/94.0',
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:89.0) Gecko/20100101 Firefox/89.0',
    'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/91.0.864.59 Safari/537.36'
]


 
# 启动ChromeDriver服务
options = webdriver.ChromeOptions()
# 关闭自动测试状态显示 // 会导致浏览器报：请停用开发者模式

proxy = "http://202.117.115.6:80"  # 代理IP:端口
#options.add_argument(f'--proxy-server={proxy}')
options.add_experimental_option("excludeSwitches", ['enable-automation'])
# 随机选择一个User-Agent
options.add_argument(f'--user-agent={random.choice(user_agents)}')
# 把chrome设为selenium驱动的浏览器代理；
driver = webdriver.Chrome(options=options)

# 获取动态User-Agent
ua = UserAgent()
headers = {
    'User-Agent': ua.random,
    'Accept-Language': 'en-US,en;q=0.9',
}

# 将请求头传递给Selenium WebDriver
driver.execute_cdp_cmd('Network.setUserAgentOverride', {
    'userAgent': headers['User-Agent'],
    'acceptLanguage': headers['Accept-Language']
})
# 反爬机制
driver.execute_cdp_cmd("Page.addScriptToEvaluateOnNewDocument",
                       {"source": """Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"""})
driver.get('https://www.taobao.com')
# 窗口最大化
driver.maximize_window()
# wait是Selenium中的一个等待类，用于在特定条件满足之前等待一定的时间(这里是15秒)。
# 如果一直到等待时间都没满足则会捕获TimeoutException异常
wait = WebDriverWait(driver,10)
# 打开页面后会强制停止10秒，请在此时手动扫码登陆
# Spring Boot 后端的 URL，假设接口为 /notifyUpdate
spring_boot_url = "http://localhost:8082/api/python_remind"

def scroll_page():
    """模拟滚动页面，直到所有商品加载完毕"""
    last_height = driver.execute_script("return document.body.scrollHeight")  # 获取页面初始高度
    while True:
        wait.until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, '.mainPicWrapper--qRLTAeii img')))
        wait.until(EC.element_to_be_clickable(
            (By.CSS_SELECTOR, 'a.doubleCardWrapper--_6NpK_ey[data-has-appeared="true"]')
        ))
        # 向下滚动一段距离
        driver.execute_script("window.scrollBy(0, 600);")
        time.sleep(random.uniform(2, 5))  # 等待页面加载
        new_height = driver.execute_script("return document.body.scrollHeight")  # 获取新的页面高度
        if new_height == last_height:  # 如果高度没有变化，说明页面已加载完毕
            break
        last_height = new_height

def scroll_to_next_page_button():
    """模拟滚动到页面底部并确保‘下一页’按钮可见"""
    # 滚动到底部
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
    time.sleep(random.uniform(2, 5))
    
    # 向上滚动一段距离，确保“下一页”按钮出现在可见区域
    driver.execute_script("window.scrollBy(0, -300);")  # 向上滚动300px，确保按钮可见
    time.sleep(random.uniform(2, 5))  # 等待一下，确保按钮可见


def search_goods(KEYWORD, start_page, total_pages, max_retries=1):

            # 找到搜索“输入框”
            input = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "#q")))
            # 找到“搜索”按钮
            submit = wait.until(
                EC.element_to_be_clickable((By.CSS_SELECTOR, '#J_TSearchForm > div.search-button > button')))
            # 输入框写入“关键词KeyWord”
            input.send_keys(KEYWORD)
            # 点击“搜索”按键
            submit.click()
            # 搜索商品后会再强制停止5秒，如有滑块请手动操作
            time.sleep(random.uniform(2, 5))
            # 获取商品信息
            get_goods(start_page, KEYWORD)
 
            # 翻页操作
            for i in range(start_page + 1, total_pages + 1):
                page_turning(i)

            return  # 成功执行完毕后退出循环

 
# 翻页函数
def page_turning(page_number):
    print('正在翻页: ', page_number)
    try:
        # 等待页面加载完成（强制等待，可根据具体情况调整时间）
        time.sleep(random.uniform(2, 5))
        scroll_to_next_page_button()  # 滚动到“下一页”按钮位置
        
        # 等待“下一页”按钮完全加载
        
        #wait.until(EC.presence_of_element_located((By.XPATH, '/html/body/div[4]/div[3]/div[1]/div/div[1]/div[3]/div[4]/div/div/button[2]')))
        
        # 判断“下一页”按钮是否可点击
        next_button = driver.find_element(By.CSS_SELECTOR, 
            '#search-content-leftWrap > div.leftContent--BdYLMbH8 > div.pgWrap--RTFKoWa6 > div > div > button.next-btn.next-medium.next-btn-normal.next-pagination-item.next-next')
        if 'disabled' in next_button.get_attribute('class'):
            print("最后一页，无法翻页")
            return  # 无法点击，结束函数

        # 如果可以点击，则点击“下一页”
        next_button.click()
        
        time.sleep(random.uniform(12, 20)) #模拟人类休息一下
        # 爬取当前页内容
        get_goods(page_number, KEYWORD)

    except TimeoutException:
        print(f"翻页时发生超时错误，重试: 第 {page_number} 页")
        page_turning(page_number)

    except Exception as e:
        print(f"翻页时发生其他错误: {e}")

 
def get_goods(page, KEYWORD):

    time.sleep(random.uniform(2, 5))
    scroll_page()
    # 等待商品加载完成

    time.sleep(random.uniform(2, 5))  # 增加缓冲时间确保图片资源加载完全


    html = driver.page_source
    doc = pq(html)

    # 提取所有商品的共同父元素的类选择器
    items = doc('div.contentInner--xICYBlag > a').items()

    for item in items:
        thing_url = item.attr('href')
        if thing_url:
            thing_url = 'https:' + thing_url if thing_url.startswith('//') else thing_url  # 补充完整链接
        # 定位商品标题
        title = item.find('.title--qJ7Xg_90 span').text()
        # 定位价格
        price_int = item.find('.priceInt--yqqZMJ5a').text()
        price_float = item.find('.priceFloat--XpixvyQ1').text()
        if price_int and price_float:
            price = float(f"{price_int}{price_float}")
        else:
            price = 0.0

        # 定位店名
        shop = item.find('.shopNameText--DmtlsDKm').text()
        # 定位商品图片 url
        img = item.find('.mainPicWrapper--qRLTAeii img')
        img_url = img.attr('src')
        crawl_date = datetime.now().date()
        source = '淘宝'
        child_type = KEYWORD

        # 插入商品的 SQL 语句（如果存在，更新）
        insert_query = """
            INSERT INTO card (title, price, shop, img_url, source, child_type, thing_url, crawl_date)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
        """

        # 插入历史记录的 SQL 语句
        history_insert_query = """
            INSERT INTO hitory (thing_id, crawl_date, price)
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
            
        response = requests.post(spring_boot_url)
        connection.commit()
    current_date = datetime.now()
    two_days_ago = current_date - timedelta(days=2)

    # 执行删除操作：删除crawl_date与当前日期相差两天及以上的记录
    delete_query = """
    DELETE FROM card 
    WHERE crawl_date <= %s;
    """

    cursor.execute(delete_query, (two_days_ago,))


 
def Crawer_main(KEYWORD, pageStart, pageEnd):
    try:
        print(KEYWORD)
        # 爬取从 pageStart 到 pageEnd 页的数据
        search_goods(KEYWORD, pageStart, pageEnd)
    except Exception as exc:
        print(f"Crawer_main函数报错: {exc}")
        # 进一步处理错误，例如记录日志、重试或退出

 
 
if __name__ == '__main__':
  
    # 数据库连接配置
    db_config = {
        'host': 'localhost',
        'user': 'bishaoshao',      # 替换为你的数据库用户名
        'password': '123456',  # 替换为你的数据库密码
        'database': 'bishaoshao',  # 替换为你的数据库名
        'charset': 'utf8mb4'
    }

    try:
        connection = pymysql.connect(**db_config)
        cursor = connection.cursor()
        print("数据库连接成功！")

        cursor.execute("SELECT child_type FROM type WHERE id>97")  # 只查询子类别
        child_types = cursor.fetchall()  # 获取所有子类别

        pageStart = 1     # 爬取起始页
        pageEnd = 1        # 爬取终止页,  请注意当真正用于生产开发时将这个参数调为最大值100，以获取所有的商品信息,因为反爬机制的存在和本地服务器性能限制，我们几乎不能把所有商品存储

        for child_type_tuple in child_types:
            KEYWORD = child_type_tuple[0]  # 获取子类别名称
            Crawer_main(KEYWORD,pageStart,pageEnd)
            driver.get('https://www.taobao.com')
            driver.maximize_window()
            time.sleep(random.uniform(15, 25))  #模拟人类行为，休息一下
       
    except Exception as e:
        print("数据库连接失败：", e)
        exit()
    finally:
        # 无论程序是否发生异常，确保关闭数据库连接
        try:
            cursor.close()
            connection.close()
            print("数据库连接已关闭")
        except Exception as e:
            print("关闭数据库连接时发生异常：", e)