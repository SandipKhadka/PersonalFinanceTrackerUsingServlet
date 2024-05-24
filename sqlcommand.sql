CREATE TABLE spending_limit
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    category_id int,
    user_id     int,
    amount      int,
    FOREIGN KEY (category_id) REFERENCES expenses_category (category_id),
    FOREIGN KEY (user_id) REFERENCES user_details (user_id)
);

CREATE TABLE user_details
(
    user_id    INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(99),
    last_name  VARCHAR(99),
    user_name  VARCHAR(99),
    password   INT
);


CREATE TABLE income_category
(
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(99)
);

CREATE TABLE expenses_category
(
    category_id   INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(99)
);

CREATE TABLE income
(
    income_id       INT PRIMARY KEY AUTO_INCREMENT,
    income_amount   int,
    income_category INT,
    user_id         INT,
    remarks         varchar(99),
    date            DATE,
    time            time(0),
    FOREIGN KEY (user_id) REFERENCES user_details (user_id),
    FOREIGN KEY (income_category) REFERENCES income_category (category_id)
);

CREATE TABLE expenses
(
    expenses_id       INT PRIMARY KEY AUTO_INCREMENT,
    expenses_amount   INT,
    expenses_category INT,
    user_id           INT,
    remarks           VARCHAR(99),
    date              DATE,
    time              TIME(0),
    FOREIGN KEY (user_id) REFERENCES user_details (user_id),
    FOREIGN KEY (expenses_category) REFERENCES expenses_category (category_id)
)

SELECT income_amount, category_name, remarks
FROM income
         INNER JOIN income_category ON income.income_category = income_category.category_id
         INNER JOIN user_details ON income.user_id = user_details.user_id
WHERE user_name = 'sandip';
