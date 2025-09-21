import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { serviceApi } from '../services/api';
import { Service } from '../types';
import './ServicesDropdown.css';

const ServicesDropdown: React.FC = () => {
  const [services, setServices] = useState<Service[]>([]);
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    fetchServices();
  }, []);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const fetchServices = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await serviceApi.getAllServices();
      setServices(data.content || []);
    } catch (err: any) {
      setError(err.message || 'Failed to fetch services');
      console.error('Error fetching services:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleMouseEnter = () => {
    setIsOpen(true);
  };

  const handleMouseLeave = () => {
    setIsOpen(false);
  };

  const handleServiceClick = () => {
    setIsOpen(false);
  };

  return (
    <div 
      className="services-dropdown" 
      ref={dropdownRef}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      <span className="services-trigger">
        Services
      </span>
      
      {isOpen && (
        <div className="services-menu">
          {loading && (
            <div className="dropdown-loading">Loading services...</div>
          )}
          
          {error && (
            <div className="dropdown-error">
              Sorry for the inconvenience. Internal server error. Try after sometime!
            </div>
          )}
          
          {!loading && !error && services.length === 0 && (
            <div className="dropdown-empty">No services available</div>
          )}
          
          {!loading && !error && services.length > 0 && (
            <div className="services-list">
              {services.map((service) => (
                <Link
                  key={service.id}
                  to={`/service/${encodeURIComponent(service.name)}`}
                  className="service-item"
                  onClick={handleServiceClick}
                >
                  {service.name}
                </Link>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default ServicesDropdown;
