# 📝 How to Create a Task - Step by Step Guide

## Prerequisites
You need a JWT token to create tasks. Follow these steps:

---

## Step 1: Register a User (or Login)

### Option A: Register New User
1. In Swagger UI, find **`POST /api/auth/register`**
2. Click **"Try it out"**
3. Click the **"Example Value"** tab in the Request body section
4. Edit the JSON:
   ```json
   {
     "username": "testuser",
     "email": "test@example.com",
     "password": "password123"
   }
   ```
5. Click **"Execute"**
6. **Copy the `token`** from the response (it's a long string starting with `eyJ...`)

### Option B: Login (if you already registered)
1. In Swagger UI, find **`POST /api/auth/login`**
2. Click **"Try it out"**
3. Click the **"Example Value"** tab
4. Edit the JSON:
   ```json
   {
     "usernameOrEmail": "testuser",
     "password": "password123"
   }
   ```
5. Click **"Execute"**
6. **Copy the `token`** from the response

---

## Step 2: Authorize with Your Token

1. Look for the green **"Authorize"** button at the top right of Swagger UI
2. Click **"Authorize"**
3. In the popup, you'll see a field for "bearerAuth"
4. **Paste your token** (just the token, without "Bearer")
5. Click **"Authorize"**
6. Click **"Close"**

✅ Now you're authenticated! You'll see a lock icon 🔒 on protected endpoints.

---

## Step 3: Create a Task

1. Find **`POST /api/tasks`** in the task-controller section
2. Click **"Try it out"**
3. Click the **"Example Value"** tab in the Request body section
4. Edit the JSON with your task details:
   ```json
   {
     "title": "Complete project documentation",
     "description": "Write comprehensive README and API docs",
     "status": "TODO",
     "priority": "HIGH",
     "dueDate": "2024-12-31",
     "category": "Work"
   }
   ```

### Field Details:
- **title** (required): Task title
- **description** (optional): Task description
- **status** (optional): `TODO`, `IN_PROGRESS`, or `COMPLETED` (default: `TODO`)
- **priority** (optional): `LOW`, `MEDIUM`, or `HIGH` (default: `MEDIUM`)
- **dueDate** (optional): Date in format `YYYY-MM-DD` (e.g., `2024-12-31`)
- **category** (optional): Task category (e.g., `Work`, `Personal`, `Shopping`)

5. Click **"Execute"**
6. You should see a **201 Created** response with your new task details!

---

## Example Task Creation

### Minimal Task (only title required):
```json
{
  "title": "Buy groceries"
}
```

### Full Task with All Fields:
```json
{
  "title": "Complete API testing",
  "description": "Test all endpoints and document results",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2024-12-31",
  "category": "Development"
}
```

---

## Troubleshooting

### ❌ Getting 401 Unauthorized?
- Make sure you clicked "Authorize" and pasted your token
- Check that the token is still valid (tokens expire after 24 hours)
- Try logging in again to get a new token

### ❌ Getting 400 Bad Request?
- Check that "title" field is included (it's required)
- Verify JSON syntax is correct (no trailing commas)
- Make sure status/priority values are valid enums

### ❌ Can't see the Authorize button?
- Refresh the page (F5)
- Make sure the application is running
- Check that you're on the Swagger UI page

---

## Next Steps

After creating a task, you can:
- **Get all tasks**: `GET /api/tasks`
- **Get task by ID**: `GET /api/tasks/{id}` (use the ID from the create response)
- **Update task**: `PUT /api/tasks/{id}`
- **Delete task**: `DELETE /api/tasks/{id}`

Happy task managing! 🚀

