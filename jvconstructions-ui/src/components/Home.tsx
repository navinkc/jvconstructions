import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { projectApi, enquiryApi } from '../services/api';
import { Project, CreateEnquiryRequest } from '../types';
import './Home.css';

const Home: React.FC = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState<CreateEnquiryRequest>({
    name: '',
    email: '',
    phone: '',
    message: '',
    projectCode: ''
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitStatus, setSubmitStatus] = useState<'idle' | 'success' | 'error'>('idle');

  useEffect(() => {
    fetchProjects();
  }, []);

  useEffect(() => {
    if (projects.length > 0) {
      const interval = setInterval(() => {
        setCurrentImageIndex((prevIndex) => 
          (prevIndex + 1) % projects.length
        );
      }, 15000); // Change image every 15 seconds

      return () => clearInterval(interval);
    }
  }, [projects]);

  const fetchProjects = async () => {
    try {
      setLoading(true);
      const data = await projectApi.getAllProjects();
      setProjects(data.content || []);
    } catch (error) {
      console.error('Error fetching projects:', error);
    } finally {
      setLoading(false);
    }
  };

  const getCurrentBackgroundImage = () => {
    if (projects.length === 0) return null;
    
    const currentProject = projects[currentImageIndex];
    if (currentProject.heroImageUrl) {
      return currentProject.heroImageUrl;
    }
    
    // If current project has no hero image, find one that does
    const projectWithImage = projects.find(p => p.heroImageUrl);
    return projectWithImage?.heroImageUrl || null;
  };

  const handleFormChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setSubmitStatus('idle');

    try {
      await enquiryApi.createEnquiry(formData);
      setSubmitStatus('success');
      setFormData({
        name: '',
        email: '',
        phone: '',
        message: '',
        projectCode: ''
      });
    } catch (error: any) {
      setSubmitStatus('error');
      console.error('Error submitting enquiry:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const backgroundImage = getCurrentBackgroundImage();

  // Pass the background image to the navbar via a custom event
  useEffect(() => {
    const event = new CustomEvent('backgroundImageChange', { 
      detail: { backgroundImage, currentImageIndex } 
    });
    window.dispatchEvent(event);
  }, [backgroundImage, currentImageIndex]);

  return (
    <div className="home">
      {/* Hero Section */}
      <section 
        className="hero"
        style={{
          backgroundImage: backgroundImage 
            ? `linear-gradient(rgba(102, 126, 234, 0.7), rgba(118, 75, 162, 0.7)), url(${backgroundImage})`
            : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          backgroundSize: 'cover',
          backgroundPosition: 'center top',
          backgroundRepeat: 'no-repeat',
          backgroundAttachment: 'fixed'
        }}
      >
        <div className="container">
          <div className="hero-content">
            <div className="hero-text">
              <h1>Building Dreams Into Reality</h1>
              <p>
                We are JV Constructions, a premier construction company delivering 
                exceptional residential and commercial projects with uncompromising 
                quality and attention to detail.
              </p>
              <div className="hero-buttons">
                <Link to="/projects" className="btn btn-primary">
                  View Our Projects
                </Link>
                <Link to="/contact" className="btn btn-secondary">
                  Get Quote
                </Link>
              </div>
              {loading && (
                <div className="loading-indicator">
                  <p>Loading our latest projects...</p>
                </div>
              )}
            </div>
            
            <div className="hero-form">
              <div className="form-container">
                <form onSubmit={handleFormSubmit}>
                  <div className="form-group">
                    <input
                      type="text"
                      name="name"
                      placeholder="Your Name *"
                      value={formData.name}
                      onChange={handleFormChange}
                      required
                    />
                  </div>
                  
                  <div className="form-group">
                    <input
                      type="email"
                      name="email"
                      placeholder="Your Email *"
                      value={formData.email}
                      onChange={handleFormChange}
                      required
                    />
                  </div>
                  
                  <div className="form-group">
                    <input
                      type="tel"
                      name="phone"
                      placeholder="Your Phone *"
                      value={formData.phone}
                      onChange={handleFormChange}
                      required
                    />
                  </div>
                  
                  <div className="form-group">
                    <input
                      type="text"
                      name="projectCode"
                      placeholder="Project Code (Optional)"
                      value={formData.projectCode}
                      onChange={handleFormChange}
                    />
                  </div>
                  
                  <div className="form-group">
                    <textarea
                      name="message"
                      placeholder="Your Message *"
                      rows={4}
                      value={formData.message}
                      onChange={handleFormChange}
                      required
                    />
                  </div>
                  
                  <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
                    {isSubmitting ? 'Sending...' : 'Send Message'}
                  </button>
                  
                  {submitStatus === 'success' && (
                    <div className="success-message">
                      Thank you! Your message has been sent successfully.
                    </div>
                  )}
                  
                  {submitStatus === 'error' && (
                    <div className="error-message">
                      Sorry for the inconvenience. Internal server error. Try after sometime!
                    </div>
                  )}
                </form>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features">
        <div className="container">
          <h2>Why Choose JV Constructions?</h2>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">🏗️</div>
              <h3>Expert Craftsmanship</h3>
              <p>Our skilled team brings years of experience and attention to detail to every project.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">⏰</div>
              <h3>On-Time Delivery</h3>
              <p>We understand the importance of deadlines and deliver projects on schedule.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">💰</div>
              <h3>Competitive Pricing</h3>
              <p>Get the best value for your investment with our transparent and fair pricing.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">🛡️</div>
              <h3>Quality Assurance</h3>
              <p>Every project undergoes rigorous quality checks to ensure the highest standards.</p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta">
        <div className="container">
          <h2>Ready to Start Your Project?</h2>
          <p>Contact us today for a free consultation and quote.</p>
          <Link to="/contact" className="btn btn-primary">
            Get Started
          </Link>
        </div>
      </section>
    </div>
  );
};

export default Home;
