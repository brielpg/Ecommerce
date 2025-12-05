-- Insert additional users
-- password '123'
INSERT INTO tb_users (id, name, email, phone, birth_date, active, timestamp, password, role) VALUES
('11111111-1111-1111-1111-111111111111', 'Admin', 'admin@email.com', '11999999999', '1980-01-01', true, CURRENT_DATE, '$2a$10$6W.VPRrv25.21xYCxPCY9u693EvnPb1y2OaaIq09Uzhbc6IC8FySe', 'ADMIN'),
('22222222-2222-2222-2222-222222222222', 'John Doe', 'john.doe@email.com', '11999999991', '1990-05-15', true, CURRENT_DATE, '$2a$10$6W.VPRrv25.21xYCxPCY9u693EvnPb1y2OaaIq09Uzhbc6IC8FySe', 'CUSTOMER'),
('33333333-3333-3333-3333-333333333333', 'Jane Smith', 'jane.smith@email.com', '11999999992', '1985-10-20', true, CURRENT_DATE, '$2a$10$6W.VPRrv25.21xYCxPCY9u693EvnPb1y2OaaIq09Uzhbc6IC8FySe', 'CUSTOMER');

-- Insert addresses for new users
INSERT INTO tb_addresses (id, street_name, number, complement, neighborhood, city, state, zip_code, user_id) VALUES
('33333333-3333-3333-3333-333333333333', 'Rua Exemplo', '123', 'Apto 45','Centro', 'São Paulo', 'SP', '01000-000', '11111111-1111-1111-1111-111111111111'),
('44444444-4444-4444-4444-444444444444', 'Rua Nova', '456', 'Apt 101', 'Centro', 'Rio de Janeiro', 'RJ', '20000-000', '22222222-2222-2222-2222-222222222222'),
('55555555-5555-5555-5555-555555555555', 'Av. Paulista', '789', 'Sala 202', 'Bela Vista', 'São Paulo', 'SP', '01310-100', '33333333-3333-3333-3333-333333333333');

-- Insert carts for new users
INSERT INTO tb_carts (id, user_id, total_price, timestamp) VALUES
('66666666-6666-6666-6666-666666666666', '22222222-2222-2222-2222-222222222222', 0.00, CURRENT_DATE),
('77777777-7777-7777-7777-777777777777', '33333333-3333-3333-3333-333333333333', 0.00, CURRENT_DATE),
('22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 0.00, CURRENT_DATE);

-- Insert categories
INSERT INTO tb_categories (id, name, description, active, timestamp) VALUES
('88888888-8888-8888-8888-888888888888', 'Electronics', 'Electronic devices and gadgets', true, CURRENT_DATE),
('99999999-9999-9999-9999-999999999999', 'Clothing', 'Apparel and fashion items', true, CURRENT_DATE),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Books', 'Books and literature', true, CURRENT_DATE);

-- Insert products
INSERT INTO tb_products (id, name, description, price, stock, purchase_count, active, timestamp) VALUES
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Smartphone', 'Latest smartphone model', 2999.99, 50, 0, true, CURRENT_DATE),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Laptop', 'High-performance laptop', 4999.99, 30, 0, true, CURRENT_DATE),
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'T-Shirt', 'Cotton t-shirt', 49.99, 100, 0, true, CURRENT_DATE),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Jeans', 'Denim jeans', 99.99, 80, 0, true, CURRENT_DATE),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Novel', 'Bestselling fiction novel', 29.99, 200, 0, true, CURRENT_DATE);

-- Insert product-category relationships
INSERT INTO tb_product_category (product_id, category_id) VALUES
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '88888888-8888-8888-8888-888888888888'), -- Smartphone -> Electronics
('cccccccc-cccc-cccc-cccc-cccccccccccc', '88888888-8888-8888-8888-888888888888'), -- Laptop -> Electronics
('dddddddd-dddd-dddd-dddd-dddddddddddd', '99999999-9999-9999-9999-999999999999'), -- T-Shirt -> Clothing
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '99999999-9999-9999-9999-999999999999'), -- Jeans -> Clothing
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'); -- Novel -> Books

-- Insert user favorites
INSERT INTO tb_user_favorites (user_id, product_id) VALUES
('22222222-2222-2222-2222-222222222222', 'cccccccc-cccc-cccc-cccc-cccccccccccc'), -- John likes Laptop
('33333333-3333-3333-3333-333333333333', 'ffffffff-ffff-ffff-ffff-ffffffffffff'); -- Jane likes Novel