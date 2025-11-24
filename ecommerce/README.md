# Endpoints

## 🛡️ Authentication

| Method | Endpoint           | Description                                    | Permission |
|--------|--------------------|------------------------------------------------|------------|
| POST   | /api/auth/login    | Authenticates the user and returns a JWT token | Public     |
| POST   | /api/auth/register | Registers a new user                           | Public     |


## 🛒 Cart

| Method | Endpoint                  | Description                        | Permission              |
|--------|---------------------------|------------------------------------|-------------------------|
| GET    | /api/carts/{userId}       | Fetches the user's cart            | Resource owner or Admin |
| DELETE | /api/carts/{userId}       | Clears the user's cart             | Resource owner or Admin |
| POST   | /api/carts/{userId}/items | Adds items to the user's cart      | Resource owner or Admin |
| DELETE | /api/carts/{userId}/items | Removes items from the user's cart | Resource owner or Admin |


## 🗂️ Category

| Method | Endpoint                     | Description                     | Permission    |
|--------|------------------------------|---------------------------------|---------------|
| POST   | /api/categories              | Creates a new category          | Admin         |
| GET    | /api/categories              | Lists all active categories     | Authenticated |
| GET    | /api/categories/{id}         | Fetches a category by its ID    | Authenticated |
| PUT    | /api/categories              | Updates an existing category    | Admin         |
| POST   | /api/categories/{id}/restore | Restores a deactivated category | Admin         |
| DELETE | /api/categories/{id}         | Deactivates a category          | Admin         |


## 📦 Order

| Method | Endpoint                  | Description                | Permission              |
|--------|---------------------------|----------------------------|-------------------------|
| POST   | /api/orders               | Creates a new order        | Resource owner or Admin |
| GET    | /api/orders/user/{userId} | Lists orders for a user    | Resource owner or Admin |
| GET    | /api/orders/{id}          | Fetches an order by its ID | Resource owner or Admin |


## 🛍️ Product

| Method | Endpoint                                   | Description                       | Permission    |
|--------|--------------------------------------------|-----------------------------------|---------------|
| POST   | /api/products                              | Creates a new product             | Admin         |
| GET    | /api/products                              | Lists all active products         | Authenticated |
| GET    | /api/products/{id}                         | Fetches a product by its ID       | Authenticated |
| PUT    | /api/products                              | Updates an existing product       | Admin         |
| POST   | /api/products/{id}/restore                 | Restores a deactivated product    | Admin         |
| DELETE | /api/products/{id}                         | Deactivates a product             | Admin         |
| POST   | /api/products/{id}/categories              | Adds categories to a product      | Admin         |
| DELETE | /api/products/{id}/categories/{categoryId} | Removes a category from a product | Admin         |


## 👤 User

| Method | Endpoint                              | Description                                         | Permission              |
|--------|---------------------------------------|-----------------------------------------------------|-------------------------|
| GET    | /api/users                            | Lists all active users                              | Admin                   |
| GET    | /api/users/{id}                       | Fetches a user by its ID                            | Resource owner or Admin |
| PUT    | /api/users                            | Updates a user's data                               | Resource owner or Admin |
| POST   | /api/users/{id}/restore               | Restores a deactivated user                         | Admin                   |
| DELETE | /api/users/{id}                       | Deactivates a user                                  | Resource owner or Admin |
| GET    | /api/users/{id}/favorites             | List all products the user has added to favorites   | Resource owner or Admin |
| POST   | /api/users/{id}/favorites/{productId} | Adds a product to the user's list of favorites      | Resource owner or Admin |
| DELETE | /api/users/{id}/favorites/{productId} | Removes a product from the user's list of favorites | Resource owner or Admin |