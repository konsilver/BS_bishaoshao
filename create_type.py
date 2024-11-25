# 声明第三方库/头文件
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from pyquery import PyQuery as pq
import time
import pymysql
                        
 
# 启动ChromeDriver服务
options = webdriver.ChromeOptions()
# 关闭自动测试状态显示 // 会导致浏览器报：请停用开发者模式
options.add_experimental_option("excludeSwitches", ['enable-automation'])
# 把chrome设为selenium驱动的浏览器代理；
driver = webdriver.Chrome(options=options)
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

def crawer_type(cursor):
    print("开始爬取商品类别...")

    # 等待分类导航加载完成
    wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "ul")))

    # 获取所有父类的 li 元素
    main_categories = driver.find_elements(By.CSS_SELECTOR, "ul > li")

    for main_category in main_categories:
        try:
            # 鼠标悬停以展开子类别
            webdriver.ActionChains(driver).move_to_element(main_category).perform()
            time.sleep(1)  # 等待悬浮页面展开

            # 提取所有的 div:nth-child(n) 元素 (父类别和子类别)
            all_elements = main_category.find_elements(By.CSS_SELECTOR,
                "div.sec-cate--BSxnHXHu > div > div > div.cate-links-container--GbL3_3p2 > div")
            
            # 遍历每个 div:nth-child(n) 元素
            for i in range(0, len(all_elements), 2):  # 每次跳过两个元素，即每次处理一对父类别和子类别
                try:
                    # 获取当前父类别 (odd)
                    parent_element = all_elements[i]
                    parent_type = parent_element.find_element(By.CSS_SELECTOR, "a > div.sec-cate-txt-title--ZltlImBJ").text
                    
                    # 获取当前子类别 (even)
                    child_element = all_elements[i + 1]  # 紧接着的下一个是子类别
                    child_types = child_element.find_elements(By.CSS_SELECTOR, "a")

                    # 存入数据库
                    for child_type in child_types:
                        child_name = child_type.text
                        cursor.execute(
                            "INSERT INTO type (parent_type, child_type) VALUES (%s, %s)",
                            (parent_type, child_name)
                        )
                        print(f"插入成功: 父类别: {parent_type}, 子类别: {child_name}")
                        connection.commit()
                except Exception as e:
                    print(f"处理父类别和子类别时出错: {e}")
                    
        except Exception as e:
            print(f"爬取时出错: {e}")

    print("爬取完成！")






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
        # 初始化爬虫并开始爬取
        crawer_type(cursor)
    except Exception as e:
        print(f"发生错误：{e}")
    finally:
        # 无论程序是否发生异常，确保关闭数据库连接
        try:
            cursor.close()
            connection.close()
            print("数据库连接已关闭")
        except Exception as e:
            print("关闭数据库连接时发生异常：", e)
