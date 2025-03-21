-- Create Products Table
CREATE TABLE IF NOT EXISTS Products (
    productId INTEGER PRIMARY KEY AUTOINCREMENT,
    productName TEXT NOT NULL UNIQUE,
    buyingPrice REAL NOT NULL,
    sellingPrice REAL NOT NULL,
    availableQuantity INTEGER NOT NULL,
    category TEXT NOT NULL
);

-- Create Users Table
CREATE TABLE IF NOT EXISTS Users (
    userId INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    emailId TEXT NOT NULL,
    password TEXT NOT NULL,
    superCoins INTEGER DEFAULT 100
);

-- Create Orders Table
CREATE TABLE IF NOT EXISTS Orders (
    orderId INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    productId INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    totalCost REAL NOT NULL,
    orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES Users(userId),
    FOREIGN KEY (productId) REFERENCES Products(productId)
);
