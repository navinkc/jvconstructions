# JV Constructions API Documentation

## Base URL
```
http://localhost:8082
```

## Authentication
The API uses JWT tokens for authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## Project Status Enum
- `UNDER_CONSTRUCTION`
- `COMPLETED`

## Project Roles
- `ADMIN` - Full access to all endpoints
- `CUSTOMER` - Limited access (mainly for enquiries)

---

## 1. Projects API

### 1.1 List Projects
**GET** `/api/v1/projects`

**Query Parameters:**
- `projectStatus` (optional): Filter by project status
- `city` (optional): Filter by city
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field and direction (e.g., `name,asc`)

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "code": "luxury-villa-001",
      "name": "Luxury Villa Project",
      "city": "Mumbai",
      "projectStatus": "UNDER_CONSTRUCTION",
      "heroImageUrl": "https://d1vy2v1dpaxgo7.cloudfront.net/projects/1/hero.jpg",
      "updatedAt": "2024-01-15T10:30:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

### 1.2 Get Project by Code
**GET** `/api/v1/projects/{code}`

**Path Parameters:**
- `code`: Project code (e.g., "luxury-villa-001")

**Response:**
```json
{
  "id": 1,
  "code": "luxury-villa-001",
  "name": "Luxury Villa Project",
  "description": "A modern luxury villa with premium amenities",
  "city": "Mumbai",
  "projectStatus": "UNDER_CONSTRUCTION",
  "startDate": "2024-01-01",
  "endDate": "2024-12-31",
  "images": [
    {
      "id": 1,
      "url": "https://d1vy2v1dpaxgo7.cloudfront.net/projects/1/hero.jpg",
      "mimeType": "image/jpeg",
      "width": 1920,
      "height": 1080,
      "sortOrder": 0,
      "hero": true
    }
  ]
}
```

### 1.3 Create Project
**POST** `/api/v1/projects`

**Authorization:** Required (ADMIN role)

**Request Body:**
```json
{
  "code": "luxury-villa-002",
  "name": "Premium Villa Project",
  "description": "A premium villa with modern design",
  "projectStatus": "UNDER_CONSTRUCTION",
  "city": "Delhi",
  "addressLine1": "123 Main Street",
  "addressLine2": "Sector 15",
  "pinCode": "110001",
  "startDate": "2024-02-01",
  "endDate": "2024-11-30",
  "tags": ["luxury", "villa", "modern"]
}
```

**Response:**
```json
{
  "id": 2,
  "code": "luxury-villa-002",
  "name": "Premium Villa Project",
  "description": "A premium villa with modern design",
  "city": "Delhi",
  "projectStatus": "UNDER_CONSTRUCTION",
  "startDate": "2024-02-01",
  "endDate": "2024-11-30",
  "images": []
}
```

### 1.4 Update Project
**PUT** `/api/v1/projects/{id}`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `id`: Project ID

**Request Body:**
```json
{
  "name": "Updated Villa Project",
  "description": "Updated description",
  "projectStatus": "COMPLETED",
  "city": "Delhi",
  "addressLine1": "456 Updated Street",
  "addressLine2": "Sector 20",
  "pinCode": "110002",
  "startDate": "2024-02-01",
  "endDate": "2024-10-31",
  "heroImageId": 5
}
```

**Response:**
```json
{
  "id": 2,
  "code": "luxury-villa-002",
  "name": "Updated Villa Project",
  "description": "Updated description",
  "city": "Delhi",
  "projectStatus": "COMPLETED",
  "startDate": "2024-02-01",
  "endDate": "2024-10-31",
  "images": []
}
```

### 1.5 Delete Project
**DELETE** `/api/v1/projects/{id}`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `id`: Project ID

**Response:**
```json
"Project with id 2 deleted successfully."
```

### 1.6 Get Presigned Upload URL
**POST** `/api/v1/projects/{id}/images/upload-url`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `id`: Project ID

**Request Body:**
```json
{
  "mimeType": "image/jpeg",
  "sizeBytes": 2048000
}
```

**Response:**
```json
{
  "uploadUrl": "https://jvconstructions-media.s3.ap-south-1.amazonaws.com/projects/1/image-123.jpg?X-Amz-Algorithm=...",
  "method": "PUT",
  "headers": {
    "Content-Type": "image/jpeg"
  },
  "expiresIn": 300,
  "s3Key": "projects/1/image-123.jpg"
}
```

### 1.7 Confirm Image Upload
**POST** `/api/v1/projects/{id}/images/confirm`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `id`: Project ID

**Request Body:**
```json
{
  "s3Key": "projects/1/image-123.jpg",
  "mimeType": "image/jpeg",
  "width": 1920,
  "height": 1080,
  "sizeBytes": 2048000,
  "isHero": true,
  "sortOrder": 0
}
```

**Response:**
```json
{
  "id": 3,
  "url": "https://d1vy2v1dpaxgo7.cloudfront.net/projects/1/image-123.jpg",
  "mimeType": "image/jpeg",
  "width": 1920,
  "height": 1080,
  "sortOrder": 0,
  "hero": true
}
```

### 1.8 Upload Image Directly
**POST** `/api/v1/projects/{id}/images/upload`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `id`: Project ID

**Request:** Multipart form data
- `file`: Image file
- `isHero` (optional): Boolean, default false
- `sortOrder` (optional): Integer, default 0

**Response:**
```json
{
  "id": 4,
  "url": "https://d1vy2v1dpaxgo7.cloudfront.net/projects/1/direct-upload.jpg",
  "mimeType": "image/jpeg",
  "width": 1920,
  "height": 1080,
  "sortOrder": 0,
  "hero": false
}
```

### 1.9 Upload Multiple Images
**POST** `/api/v1/projects/{id}/images/upload-multiple`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `id`: Project ID

**Request:** Multipart form data
- `files`: Array of image files
- `isHero` (optional): Boolean, default false

**Response:**
```json
[
  {
    "id": 5,
    "url": "https://d1vy2v1dpaxgo7.cloudfront.net/projects/1/multi-1.jpg",
    "mimeType": "image/jpeg",
    "width": 1920,
    "height": 1080,
    "sortOrder": 0,
    "hero": false
  },
  {
    "id": 6,
    "url": "https://d1vy2v1dpaxgo7.cloudfront.net/projects/1/multi-2.jpg",
    "mimeType": "image/jpeg",
    "width": 1920,
    "height": 1080,
    "sortOrder": 1,
    "hero": false
  }
]
```

### 1.10 Delete Image
**DELETE** `/api/v1/projects/{projectId}/images/{imageId}`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `projectId`: Project ID
- `imageId`: Image ID

**Response:**
```json
"Image with id 4 deleted successfully."
```

### 1.11 Set Hero Image
**PUT** `/api/v1/projects/{projectId}/images/{imageId}/hero`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `projectId`: Project ID
- `imageId`: Image ID

**Response:**
```json
"Hero image set successfully for is 5"
```

---

## 2. Enquiries API

### 2.1 Create Enquiry
**POST** `/api/v1/enquiries`

**Request Body:**
```json
{
  "projectCode": "luxury-villa-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+91-9876543210",
  "message": "I am interested in this project. Please provide more details about pricing and availability."
}
```

**Response:**
```json
{
  "id": 1,
  "projectCode": "luxury-villa-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+91-9876543210",
  "message": "I am interested in this project. Please provide more details about pricing and availability.",
  "status": "NEW",
  "assignedTo": null,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### 2.2 List Enquiries
**GET** `/api/v1/enquiries`

**Authorization:** Required (ADMIN role)

**Query Parameters:**
- `status` (optional): Filter by status
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field and direction

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "projectCode": "luxury-villa-001",
      "name": "John Doe",
      "email": "john.doe@example.com",
      "phone": "+91-9876543210",
      "message": "I am interested in this project...",
      "status": "NEW",
      "assignedTo": null,
      "createdAt": "2024-01-15T10:30:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

### 2.3 Update Enquiry
**PUT** `/api/v1/enquiries/{id}`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `id`: Enquiry ID

**Request Body:**
```json
{
  "status": "CONTACTED",
  "assignedTo": "admin@jvconstructions.com"
}
```

**Response:**
```json
{
  "id": 1,
  "projectCode": "luxury-villa-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+91-9876543210",
  "message": "I am interested in this project...",
  "status": "CONTACTED",
  "assignedTo": "admin@jvconstructions.com",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

---

## 3. Services API

### 3.1 List All Services
**GET** `/api/v1/services`

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field and direction

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Residential Construction",
      "description": "Complete residential construction services",
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T10:30:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

### 3.2 Get Service by Name
**GET** `/api/v1/services/{name}`

**Path Parameters:**
- `name`: Service name (URL encoded)

**Response:**
```json
{
  "id": 1,
  "name": "Residential Construction",
  "description": "Complete residential construction services",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

### 3.3 Create Service
**POST** `/api/v1/services`

**Authorization:** Required (ADMIN role)

**Request Body:**
```json
{
  "name": "Commercial Construction",
  "description": "Commercial building construction services"
}
```

**Response:**
```json
{
  "id": 2,
  "name": "Commercial Construction",
  "description": "Commercial building construction services",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

### 3.4 Update Service
**PUT** `/api/v1/services/{id}`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `id`: Service ID

**Request Body:**
```json
{
  "name": "Updated Commercial Construction",
  "description": "Updated commercial building construction services"
}
```

**Response:**
```json
{
  "id": 2,
  "name": "Updated Commercial Construction",
  "description": "Updated commercial building construction services",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T11:00:00Z"
}
```

### 3.5 Delete Service
**DELETE** `/api/v1/services/{id}`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `id`: Service ID

**Response:**
```json
"Service with id 2 deleted successfully."
```

---

## 4. Users API (Keycloak Integration)

### 4.1 Get User by ID
**GET** `/api/v1/users/{userId}`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `userId`: Keycloak user ID

**Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "username": "john.doe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "enabled": true,
  "emailVerified": true,
  "createdTimestamp": 1642234567890,
  "attributes": {
    "role": ["CUSTOMER"]
  }
}
```

### 4.2 List All Users
**GET** `/api/v1/users`

**Authorization:** Required (ADMIN role)

**Response:**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "john.doe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "enabled": true,
    "emailVerified": true,
    "createdTimestamp": 1642234567890,
    "attributes": {
      "role": ["CUSTOMER"]
    }
  }
]
```

### 4.3 Create User
**POST** `/api/v1/users`

**Authorization:** Required (ADMIN role)

**Request Parameters:**
- `userName`: Username
- `email`: Email address
- `password`: Password
- `role`: User role (ADMIN or CUSTOMER)

**Response:**
```json
"User created successfully!"
```

### 4.4 Update User
**PUT** `/api/v1/users/{userId}`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `userId`: Keycloak user ID

**Request Parameters:**
- `email`: New email address

**Response:**
```json
"User updated successfully!"
```

### 4.5 Delete User
**DELETE** `/api/v1/users/{userId}`

**Authorization:** Required (ADMIN role)

**Path Parameters:**
- `userId`: Keycloak user ID

**Response:**
```json
"User deleted successfully!"
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/projects"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/v1/projects"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/v1/users"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Project not found with code: invalid-code",
  "path": "/api/v1/projects/invalid-code"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/v1/projects"
}
```

---

## Notes for Frontend Development

1. **Authentication**: Use JWT tokens from Keycloak for authenticated requests
2. **File Uploads**: For images, you can either use presigned URLs (recommended for large files) or direct upload
3. **Pagination**: All list endpoints support pagination with `page`, `size`, and `sort` parameters
4. **Image URLs**: All image URLs use CloudFront CDN for better performance
5. **Validation**: Request bodies are validated on the server side
6. **CORS**: Make sure to configure CORS for your frontend domain
7. **Error Handling**: Implement proper error handling for all HTTP status codes
8. **Loading States**: Show loading indicators for async operations
9. **Form Validation**: Implement client-side validation matching server-side constraints
10. **Responsive Design**: Ensure the UI works well on mobile and desktop devices
