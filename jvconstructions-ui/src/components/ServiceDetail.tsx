import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { serviceApi } from '../services/api';
import { Service } from '../types';
import './ServiceDetail.css';

const ServiceDetail: React.FC = () => {
  const { serviceName } = useParams<{ serviceName: string }>();
  const navigate = useNavigate();
  const [service, setService] = useState<Service | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (serviceName) {
      fetchService(serviceName);
    }
  }, [serviceName]);

  const fetchService = async (name: string) => {
    try {
      setLoading(true);
      setError(null);
      const data = await serviceApi.getServiceByName(decodeURIComponent(name));
      setService(data);
    } catch (err: any) {
      setError(err.message || 'Failed to fetch service details');
      console.error('Error fetching service:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleBackToServices = () => {
    navigate('/');
  };

  if (loading) {
    return (
      <div className="service-detail">
        <div className="container">
          <div className="loading">Loading service details...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="service-detail">
        <div className="container">
          <div className="error-message">
            <h2>Service Not Available</h2>
            <p>Sorry for the inconvenience. Internal server error. Try after sometime!</p>
            <button onClick={handleBackToServices} className="btn btn-primary">
              Back to Home
            </button>
          </div>
        </div>
      </div>
    );
  }

  if (!service) {
    return (
      <div className="service-detail">
        <div className="container">
          <div className="error-message">
            <h2>Service Not Found</h2>
            <p>The requested service could not be found.</p>
            <button onClick={handleBackToServices} className="btn btn-primary">
              Back to Home
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="service-detail">
      <div className="service-hero">
        <div className="container">
          <button onClick={handleBackToServices} className="back-button">
            ← Back to Services
          </button>
          <h1>{service.name}</h1>
          <p className="service-intro">
            Discover our comprehensive {service.name.toLowerCase()} solutions designed to meet your specific needs.
          </p>
        </div>
      </div>

      <div className="service-content">
        <div className="container">
          <div className="service-description">
            <h2>About This Service</h2>
            <p>{service.description}</p>
          </div>

          <div className="service-features">
            <h2>Why Choose Our {service.name}?</h2>
            <div className="features-grid">
              <div className="feature-item">
                <div className="feature-icon">🏗️</div>
                <h3>Expert Craftsmanship</h3>
                <p>Our skilled professionals bring years of experience to every project.</p>
              </div>
              <div className="feature-item">
                <div className="feature-icon">⏰</div>
                <h3>Timely Delivery</h3>
                <p>We understand deadlines and deliver projects on schedule.</p>
              </div>
              <div className="feature-item">
                <div className="feature-icon">💰</div>
                <h3>Competitive Pricing</h3>
                <p>Get the best value with our transparent and fair pricing.</p>
              </div>
              <div className="feature-item">
                <div className="feature-icon">🛡️</div>
                <h3>Quality Assurance</h3>
                <p>Every project undergoes rigorous quality checks.</p>
              </div>
            </div>
          </div>

          <div className="service-cta">
            <h2>Ready to Get Started?</h2>
            <p>Contact us today for a free consultation and quote for our {service.name.toLowerCase()} services.</p>
            <div className="cta-buttons">
              <button 
                onClick={() => navigate('/contact')} 
                className="btn btn-primary"
              >
                Get Quote
              </button>
              <button 
                onClick={() => navigate('/projects')} 
                className="btn btn-secondary"
              >
                View Projects
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ServiceDetail;
