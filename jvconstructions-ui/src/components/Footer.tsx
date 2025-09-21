import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { serviceApi } from '../services/api';
import { Service } from '../types';
import './Footer.css';

const Footer: React.FC = () => {
  const [services, setServices] = useState<Service[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchServices();
  }, []);

  const fetchServices = async () => {
    try {
      setLoading(true);
      const data = await serviceApi.getAllServices();
      setServices(data.content || []);
    } catch (error) {
      console.error('Error fetching services for footer:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-content">
          <div className="footer-section">
            <h3>JV Constructions</h3>
            <p>
              Building dreams into reality with premium construction services. 
              We deliver excellence in every project we undertake.
            </p>
          </div>
          
          <div className="footer-section">
            <h3>Quick Links</h3>
            <ul>
              <li><Link to="/">Home</Link></li>
              <li><Link to="/about">About Us</Link></li>
              <li><Link to="/projects">Projects</Link></li>
              <li><Link to="/contact">Contact</Link></li>
            </ul>
          </div>
          
          <div className="footer-section">
            <h3>Services</h3>
            <ul>
              {loading ? (
                <li>Loading services...</li>
              ) : services.length > 0 ? (
                services.map((service) => (
                  <li key={service.id}>
                    <Link to={`/service/${encodeURIComponent(service.name)}`}>
                      {service.name}
                    </Link>
                  </li>
                ))
              ) : (
                <li>No services available</li>
              )}
            </ul>
          </div>
          
          <div className="footer-section">
            <h3>Contact Info</h3>
            <p>📧 info@jvconstructions.com</p>
            <p>📞 +91-9876543210</p>
            <p>📍 Chennai, Tamil Nadu, India</p>
          </div>
        </div>
        
        <div className="footer-bottom">
          <p>&copy; 2025 JV Constructions. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
