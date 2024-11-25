use bishaoshao;



CREATE TABLE card (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2),
    shop VARCHAR(100),
    img_url TEXT,
    source VARCHAR(8),
    child_type VARCHAR(127),
    thing_url TEXT,
    crawl_date DATETIME
    UNIQUE (title, shop, source, thing_url, child_type)
);