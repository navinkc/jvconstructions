# JV Constructions - Frontend UI

A modern React TypeScript application for JV Constructions, providing an interactive user interface for showcasing construction projects, services, and handling customer enquiries.

## 🚀 Tech Stack

- **Framework**: React 18
- **Language**: TypeScript
- **Routing**: React Router DOM
- **HTTP Client**: Axios
- **Styling**: CSS3 with responsive design
- **Build Tool**: Create React App

## 📁 Project Structure

```
jvconstructions-ui/
├── public/
│   ├── index.html
│   └── manifest.json
├── src/
│   ├── components/
│   │   ├── Navbar.tsx & Navbar.css
│   │   ├── Footer.tsx & Footer.css
│   │   ├── Home.tsx & Home.css
│   │   ├── About.tsx & About.css
│   │   ├── Services.tsx & Services.css
│   │   ├── Projects.tsx & Projects.css
│   │   └── Contact.tsx & Contact.css
│   ├── services/
│   │   └── api.ts
│   ├── types/
│   │   └── index.ts
│   ├── styles/
│   ├── App.tsx & App.css
│   ├── index.tsx & index.css
│   └── App.test.tsx
├── package.json
├── tsconfig.json
├── API_DOCUMENTATION.md
└── README.md
```

## 🛠️ Features

### Core Pages
- **Home**: Hero section with company introduction and key features
- **About Us**: Company story, statistics, and team information
- **Services**: Dynamic service listing from backend API
- **Projects**: Project portfolio with images and status
- **Contact**: Contact form with enquiry submission

### Key Features
- ✅ Responsive design for mobile and desktop
- ✅ Modern UI with gradient backgrounds and animations
- ✅ API integration with backend services
- ✅ Form handling with validation
- ✅ Error handling and loading states
- ✅ Modular component architecture
- ✅ TypeScript for type safety
- ✅ CORS handling for backend compatibility

## 🔧 Installation & Setup

1. **Install Dependencies**
   ```bash
   npm install
   ```

2. **Start Development Server**
   ```bash
   npm start
   ```
   The application will open at `http://localhost:3000`

3. **Build for Production**
   ```bash
   npm run build
   ```

## 🌐 API Integration

The frontend integrates with the JV Constructions backend API running on `http://localhost:8082`. Key endpoints include:

- **Projects**: `/api/v1/projects` - Project listing and details
- **Services**: `/api/v1/services` - Service catalog
- **Enquiries**: `/api/v1/enquiries` - Customer enquiry submission
- **Users**: `/api/v1/users` - User management (admin only)

### API Configuration
- Base URL: `http://localhost:8082/api/v1`
- Authentication: JWT tokens (stored in localStorage)
- CORS: Configured for frontend domain
- Error Handling: Comprehensive error management

## 🎨 Design Reference

The UI design is inspired by modern construction company websites with:
- Professional color scheme (blues and gradients)
- Clean typography and spacing
- Interactive elements and hover effects
- Mobile-first responsive design
- Hero sections with compelling visuals

## 📱 Responsive Design

- **Desktop**: Full navigation with sidebar layout
- **Tablet**: Optimized grid layouts and touch-friendly buttons
- **Mobile**: Collapsible navigation and stacked content

## 🔒 Security Features

- JWT token management
- Input validation and sanitization
- CORS error handling
- Secure API communication

## 🚀 Deployment

The application is designed to be deployed on AWS EC2 as a static website:

1. Build the production bundle: `npm run build`
2. Serve the `build` folder using a web server (nginx, Apache, etc.)
3. Configure reverse proxy for API calls
4. Set up SSL certificates for HTTPS

## 📋 Available Scripts

- `npm start` - Start development server
- `npm run build` - Build for production
- `npm test` - Run test suite
- `npm run eject` - Eject from Create React App

## 🤝 Backend Compatibility

This frontend is designed to work seamlessly with the JV Constructions backend API located at:
`/Users/navinkc/Desktop/Arcadia/Navin/jv/jvconstructions-api/`

## 📞 Support

For technical support or questions about the frontend application, please refer to the API documentation or contact the development team.

---

**Built with ❤️ for JV Constructions**
