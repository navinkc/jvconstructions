import React from 'react';
import './About.css';

const About: React.FC = () => {
  return (
    <div className="about">
      <section className="about-hero">
        <div className="container">
          <h1>About JV Constructions</h1>
          <p>Building excellence since our inception</p>
        </div>
      </section>

      <section className="about-content">
        <div className="container">
          <div className="about-grid">
            <div className="about-text">
              <h2>Our Story</h2>
              <p>
                JV Constructions has been at the forefront of the construction industry, 
                delivering exceptional projects that stand the test of time. With years 
                of experience and a commitment to quality, we have built a reputation 
                for excellence in both residential and commercial construction.
              </p>
              <p>
                Our team of skilled professionals brings together expertise, innovation, 
                and dedication to every project. We believe in creating spaces that not 
                only meet your needs but exceed your expectations.
              </p>
            </div>
            <div className="about-stats">
              <div className="stat">
                <h3>100+</h3>
                <p>Projects Completed</p>
              </div>
              <div className="stat">
                <h3>10+</h3>
                <p>Years Experience</p>
              </div>
              <div className="stat">
                <h3>50+</h3>
                <p>Happy Clients</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default About;
