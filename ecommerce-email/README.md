# 📧 Email Service

## 🚀 Getting Started

### 1. Configuration

To enable email sending, you must configure your Google Apps credentials. In the `.env` file, define the following variables:

```env
SMTP_USER=your-email@gmail.com
SMTP_PASSWORD=your-app-password
```

> **Note:** Use a **Google App Password**, not your regular Gmail password.

### 2. Managing Templates

Access the management interface at the `/templates` endpoint. Here you can:

* View all currently available email templates.
* Upload new `.html` files to define your email layouts.
* Ensure your templates use the correct placeholders (see [Available Variables](#-available-variables-in-received-data)).

---

## 📂 Available Event Types

* **`USER_WELCOME`**: Sent when a new user registers.

---

## 📊 Available Variables in Received Data

Below are the variables available for the templates to be rendered correctly:

| Variable        | Requirement      | Description                                                 |
|-----------------|------------------|-------------------------------------------------------------|
| `eventType`     | **Required**     | The unique identifier for the event (e.g., `USER_WELCOME`). |
| `data`          | **Required**     | Object containing the specific user and event information.  |
| `emailTo`       | **Required**     | The recipient's email address.                              |
| `userName`      | **Not Required** | The recipient's name.                                       |
| `userPhone`     | **Not Required** | The recipient's phone number.                               |
| `userBirthDate` | **Not Required** | The recipient's birth date.                                 |

### Template Example

In your `.html` files, use double curly braces to inject data:

```html
<h1>Hello, {{name}}!</h1>
<p>Thank you for joining our platform.</p>
```

---