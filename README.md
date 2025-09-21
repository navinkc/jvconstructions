# JV Constructions - Full Stack Application

A comprehensive construction company management system built with Spring Boot backend and React TypeScript frontend, featuring project management, service catalog, customer enquiries, and media storage capabilities.

## 🏗️ Project Overview

JV Constructions is a modern web application designed to showcase construction projects, manage services, and handle customer enquiries. The system consists of two main components:

- **Backend API** (`jvconstructions-api`): Spring Boot REST API with MySQL database
- **Frontend UI** (`jvconstructions-ui`): React TypeScript application with responsive design

## 🚀 Tech Stack

### Backend (`jvconstructions-api`)
- **Framework**: Spring Boot 3.5.5
- **Language**: Java 17
- **Database**: MySQL 8.0+
- **Authentication**: Keycloak + JWT
- **File Storage**: AWS S3 + CloudFront CDN
- **Build Tool**: Gradle
- **Security**: Spring Security with OAuth2 Resource Server

### Frontend (`jvconstructions-ui`)
- **Framework**: React 18
- **Language**: TypeScript
- **Routing**: React Router DOM
- **HTTP Client**: Axios
- **Styling**: CSS3 with responsive design
- **Build Tool**: Create React App

## 📁 Project Structure

```
jvconstructions/
├── jvconstructions-api/          # Backend Spring Boot API
│   ├── src/main/java/com/backend/jvconstructions/
│   │   ├── config/              # Configuration classes
│   │   ├── controller/          # REST API controllers
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── entity/              # JPA entities
│   │   ├── enums/               # Enumerations
│   │   ├── exception/           # Exception handling
│   │   ├── repository/          # JPA repositories
│   │   ├── service/             # Business logic services
│   │   └── util/                # Utility classes
│   ├── src/main/resources/
│   │   └── application.yml      # Application configuration
│   └── build.gradle             # Gradle dependencies
├── jvconstructions-ui/           # Frontend React Application
│   ├── src/
│   │   ├── components/          # React components
│   │   ├── services/            # API service layer
│   │   ├── types/               # TypeScript type definitions
│   │   └── utils/               # Utility functions
│   ├── public/                  # Static assets
│   └── package.json             # NPM dependencies
└── README.md                    # This file
```

## 🛠️ Features

### Core Functionality
- **Project Management**: Create, update, and showcase construction projects
- **Service Catalog**: Dynamic service listing with detailed descriptions
- **Customer Enquiries**: Contact form with enquiry submission and management
- **Media Management**: Image upload and storage with AWS S3 integration
- **User Management**: Keycloak-based authentication and authorization
- **Responsive Design**: Mobile-first approach with modern UI/UX

### Key Features
- ✅ **Dynamic Background Images**: Project images cycle as homepage backgrounds
- ✅ **Real-time Synchronization**: Navbar and hero section background sync
- ✅ **Interactive Services Dropdown**: Hover-based navigation with API integration
- ✅ **Form Validation**: Client and server-side validation
- ✅ **Error Handling**: Comprehensive error management and user feedback
- ✅ **Loading States**: User-friendly loading indicators
- ✅ **CORS Support**: Proper cross-origin resource sharing configuration

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Node.js 18+ and npm
- MySQL 8.0+
- Keycloak server (for authentication)
- AWS Account (for S3 storage)

### Backend Setup

1. **Navigate to backend directory**
   ```bash
   cd jvconstructions-api
   ```

2. **Configure database**
   - Create MySQL database: `jv_constructions`
   - Update `src/main/resources/application.yml` with your database credentials

3. **Configure Keycloak**
   - Set up Keycloak realm and client
   - Update Keycloak configuration in `application.yml`

4. **Configure AWS S3**
   - Set up S3 bucket and CloudFront distribution
   - Update AWS credentials in `application.yml`

5. **Run the application**
   ```bash
   ./gradlew bootRun
   ```
   The API will be available at `http://localhost:8082`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd jvconstructions-ui
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start development server**
   ```bash
   npm start
   ```
   The application will open at `http://localhost:3000`

## 📚 API Documentation

### Base URL
```
http://localhost:8082/api/v1
```

### Main Endpoints

#### Projects
- `GET /projects` - List all projects
- `GET /projects/{code}` - Get project by code
- `POST /projects` - Create new project (Admin only)
- `PUT /projects/{id}` - Update project (Admin only)
- `DELETE /projects/{id}` - Delete project (Admin only)

#### Services
- `GET /services` - List all services
- `GET /services/{name}` - Get service by name
- `POST /services` - Create new service (Admin only)
- `PUT /services/{id}` - Update service (Admin only)
- `DELETE /services/{id}` - Delete service (Admin only)

#### Enquiries
- `POST /enquiries` - Submit customer enquiry
- `GET /enquiries` - List enquiries (Admin only)
- `PUT /enquiries/{id}` - Update enquiry (Admin only)

#### Users
- `GET /users` - List all users (Admin only)
- `GET /users/{userId}` - Get user by ID (Admin only)
- `POST /users` - Create user (Admin only)
- `PUT /users/{userId}` - Update user (Admin only)
- `DELETE /users/{userId}` - Delete user (Admin only)

For detailed API documentation, see:
- Backend: `jvconstructions-api/API_DOCUMENTATION.md`
- Frontend: `jvconstructions-ui/API_DOCUMENTATION.md`

## 🔧 Configuration

### Backend Configuration (`application.yml`)
```yaml
server:
  port: 8082

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jv_constructions?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password

keycloak:
  auth-server-url: http://localhost:8080
  realm: jv-constructions
  clientId: jv-constructions-api

media:
  s3:
    region: ap-south-1
    bucket: your-bucket-name
    cdnDomain: your-cloudfront-domain.cloudfront.net
    accessKey: your-access-key
    secretKey: your-secret-key
```

### Frontend Configuration
The frontend automatically connects to the backend API at `http://localhost:8082/api/v1`.

## 🎨 UI Components

### Pages
- **Home**: Hero section with dynamic project backgrounds and contact form
- **About Us**: Company information and statistics
- **Services**: Dynamic service listing with detailed pages
- **Projects**: Project portfolio with images and status
- **Contact**: Contact form with enquiry submission

### Key Components
- **Navbar**: Responsive navigation with services dropdown
- **Footer**: Dynamic services listing and company information
- **Hero Section**: Dynamic background images with contact form integration
- **Service Detail**: Individual service pages with features and CTA

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-based Access Control**: Admin and Customer roles
- **CORS Configuration**: Proper cross-origin resource sharing
- **Input Validation**: Server-side validation for all inputs
- **SQL Injection Protection**: JPA/Hibernate ORM protection
- **XSS Protection**: Content Security Policy headers

## 📱 Responsive Design

- **Mobile First**: Optimized for mobile devices
- **Tablet Support**: Responsive grid layouts
- **Desktop Enhancement**: Full-featured desktop experience
- **Touch Friendly**: Optimized for touch interactions

## 🚀 Deployment

### Backend Deployment
1. Build the JAR file: `./gradlew bootJar`
2. Deploy to your preferred cloud platform (AWS, Azure, GCP)
3. Configure environment variables for production
4. Set up database and Keycloak in production

### Frontend Deployment
1. Build the production bundle: `npm run build`
2. Deploy the `build` folder to a web server
3. Configure reverse proxy for API calls
4. Set up SSL certificates for HTTPS

## 🧪 Testing

### Backend Testing
```bash
cd jvconstructions-api
./gradlew test
```

### Frontend Testing
```bash
cd jvconstructions-ui
npm test
```

## 📋 Available Scripts

### Backend (Gradle)
- `./gradlew bootRun` - Run the application
- `./gradlew test` - Run tests
- `./gradlew bootJar` - Build JAR file
- `./gradlew clean` - Clean build artifacts

### Frontend (NPM)
- `npm start` - Start development server
- `npm run build` - Build for production
- `npm test` - Run test suite
- `npm run eject` - Eject from Create React App

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Commit your changes: `git commit -am 'Add new feature'`
4. Push to the branch: `git push origin feature/new-feature`
5. Submit a pull request

## 📞 Support

For technical support or questions:
- Check the API documentation in respective directories
- Review the troubleshooting guides
- Contact the development team

## 📄 License

This project is proprietary software developed for JV Constructions.

---

**Built with ❤️ for JV Constructions**

*Last Updated: January 2025*
