import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import ServicesDropdown from './ServicesDropdown';
import './Navbar.css';

const Navbar: React.FC = () => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [isScrolled, setIsScrolled] = useState(false);
  const [backgroundImage, setBackgroundImage] = useState<string | null>(null);
  const location = useLocation();

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  useEffect(() => {
    const handleScroll = () => {
      // Only apply scroll logic when on homepage
      if (location.pathname === '/') {
        setIsScrolled(window.scrollY > 50);
      } else {
        setIsScrolled(false);
      }
    };

    // Set initial state
    handleScroll();

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [location.pathname]);

  useEffect(() => {
    const handleBackgroundImageChange = (event: CustomEvent) => {
      if (location.pathname === '/') {
        setBackgroundImage(event.detail.backgroundImage);
      }
    };

    window.addEventListener('backgroundImageChange', handleBackgroundImageChange as EventListener);
    
    return () => {
      window.removeEventListener('backgroundImageChange', handleBackgroundImageChange as EventListener);
    };
  }, [location.pathname]);

  useEffect(() => {
    // Reset background image when not on homepage
    if (location.pathname !== '/') {
      setBackgroundImage(null);
    }
  }, [location.pathname]);

  const navbarStyle = {
    backgroundImage: backgroundImage && !isScrolled 
      ? `linear-gradient(rgba(102, 126, 234, 0.7), rgba(118, 75, 162, 0.7)), url(${backgroundImage})`
      : undefined,
    backgroundSize: backgroundImage && !isScrolled ? 'cover' : undefined,
    backgroundPosition: backgroundImage && !isScrolled ? 'center top' : undefined,
    backgroundRepeat: backgroundImage && !isScrolled ? 'no-repeat' : undefined,
    backgroundAttachment: backgroundImage && !isScrolled ? 'fixed' : undefined,
  };

  return (
    <nav 
      className={`navbar ${isScrolled ? 'scrolled' : ''} ${backgroundImage && !isScrolled ? 'with-background' : ''}`}
      style={navbarStyle}
    >
      <div className="container">
        <div className="nav-container">
          <Link to="/" className="logo">
            JV Constructions
          </Link>
          
          <ul className={`nav-links ${isMobileMenuOpen ? 'mobile-open' : ''}`}>
            <li><Link to="/" onClick={() => setIsMobileMenuOpen(false)}>Home</Link></li>
            <li><Link to="/about" onClick={() => setIsMobileMenuOpen(false)}>About Us</Link></li>
            <li><ServicesDropdown /></li>
            <li><Link to="/projects" onClick={() => setIsMobileMenuOpen(false)}>Projects</Link></li>
            <li><Link to="/contact" onClick={() => setIsMobileMenuOpen(false)}>Contact</Link></li>
          </ul>
          
          <button 
            className="mobile-menu-btn"
            onClick={toggleMobileMenu}
            aria-label="Toggle mobile menu"
          >
            ☰
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
