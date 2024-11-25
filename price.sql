use bishaoshao;

CREATE TABLE history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    thing_id INT,
    crawl_date DATETIME,
    price DECIMAL(10, 2)
);