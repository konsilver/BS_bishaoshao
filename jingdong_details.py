# 声明第三方库/头文件
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from pyquery import PyQuery as pq
import time
import pymysql
from datetime import datetime
from fake_useragent import UserAgent
                   
import json
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
# 禁用重定向
options.add_argument("--disable-redirects")
proxy = "http://202.117.115.6:80"  # 代理IP:端口
#options.add_argument(f'--proxy-server={proxy}')
options.add_experimental_option("excludeSwitches", ['enable-automation'])
# 随机选择一个User-Agent
options.add_argument(f'--user-agent={random.choice(user_agents)}')
#options.add_argument("--disable-javascript")
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
driver.get('https://www.jd.com')
# 窗口最大化
driver.maximize_window()
# wait是Selenium中的一个等待类，用于在特定条件满足之前等待一定的时间(这里是15秒)。
# 如果一直到等待时间都没满足则会捕获TimeoutException异常
wait = WebDriverWait(driver,20)

def scroll_page():
    driver.execute_script("window.scrollBy(0, 500);")  # 每次滚动1000像素
    time.sleep(random.uniform(1, 2))

# 登录后保存 cookies
def save_cookies():
    cookies = driver.get_cookies()  # 获取所有 cookies
    with open("cookies.json", "w") as f:
        json.dump(cookies, f)

# 登录功能
def login():
    log = wait.until(EC.element_to_be_clickable((By.CSS_SELECTOR, '#ttbar-login > a.link-login')))
    log.click()
    time.sleep(20)  # 等待登录完成
    save_cookies()  # 登录成功后保存 cookies


def load_cookies():
    try:
        with open("cookies.json", "r") as f:
            cookies = json.load(f)
            for cookie in cookies:
                driver.add_cookie(cookie)  # 将每个 cookie 添加到浏览器
        print("Cookies loaded successfully.")
    except Exception as e:
        print(f"Error loading cookies: {e}")
    


# 获取商品参数的函数
def fetch_thing_params(thing_url,count):
    try:
        load_cookies()
        driver.get(thing_url)
        print(count)
        #scroll_page()
        time.sleep(random.uniform(12, 15))

        params_container = driver.find_element(By.CSS_SELECTOR, "div.p-parameter > ul.parameter2.p-parameter-list")
        # 解析参数信息
        params_elements = params_container.find_elements(By.TAG_NAME, "li")
        params = {}
        for element in params_elements:
            try:
                key = element.text.split('：')[0]  # 提取参数名称
                value = element.get_attribute('title')  # 获取参数值
                if key and value:
                    params[key] = value
            except Exception as e:
                print(f"解析参数时出错: {e}")
                continue
        return params
    except Exception as e:
        print(f"抓取失败: {thing_url}，原因: {e}")
        return None



if __name__ == '__main__':
    # 数据库连接配置
    db_config = {
        'host': 'localhost',
        'user': 'bishaoshao',      # 替换为你的数据库用户名
        'password': '123456',      # 替换为你的数据库密码
        'database': 'bishaoshao',  # 替换为你的数据库名
        'charset': 'utf8mb4'
    }

    try:
        # 建立数据库连接
        connection = pymysql.connect(**db_config)
        cursor = connection.cursor()

        # 查询 card 表中的所有数据
        cursor.execute("SELECT id, thing_url FROM card WHERE source='京东' && id>10591")
        cards = cursor.fetchall()
        count=0
        login()
        for card_id, thing_url in cards:

            # 获取商品参数
            params = fetch_thing_params(thing_url,count)
            if params:
                # 存储到 thing 表中
                params_json = json.dumps(params, ensure_ascii=False)
                try:
                    cursor.execute(
                        "INSERT INTO thing (id, params) VALUES (%s, %s)",
                        (card_id, params_json)
                    )
                    print(f"成功存储卡片 ID: {card_id}")
                    count=count+1
                except Exception as e:
                    print(f"存储失败，卡片 ID: {card_id}, 原因: {e}")

        # 提交事务
            connection.commit()
            time.sleep(random.uniform(12, 15))

    except Exception as e:
        print("程序运行出错: ", e)
    finally:
        # 关闭数据库连接和 WebDriver
        try:
            if cursor:
                cursor.close()
            if connection:
                connection.close()
            driver.quit()
            print("资源已释放")
        except Exception as e:
            print("释放资源时发生异常: ", e)