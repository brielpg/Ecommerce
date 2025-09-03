INSERT INTO tb_users (
    id, name, email, phone, birth_date, active, timestamp, password, role
) VALUES (
    '11111111-1111-1111-1111-111111111111',
    'Admin',
    'admin@email.com',
    '11999999999',
    '1980-01-01',
    true,
    CURRENT_DATE,
    '$2a$10$BXxxqMB9lb2Ucm0ifWB/XOIgS/EeeJkITelpzX/MgtxCSjC26AiMm', -- admin
    'ADMIN'
);

INSERT INTO tb_addresses (
    id, street_name, number, complement, neighborhood, city, state, zip_code, user_id
) VALUES (
    '33333333-3333-3333-3333-333333333333',
    'Rua Exemplo',
    '123',
    'Apto 45',
    'Centro',
    'São Paulo',
    'SP',
    '01000-000',
    '11111111-1111-1111-1111-111111111111'
);

INSERT INTO tb_carts (
    id, user_id, total_price, timestamp
) VALUES (
    '22222222-2222-2222-2222-222222222222',
    '11111111-1111-1111-1111-111111111111',
    0.00,
    CURRENT_DATE
);