import React, { useState, useEffect } from 'react';
import { projectApi } from '../services/api';
import { Project } from '../types';
import './Projects.css';

const Projects: React.FC = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await projectApi.getAllProjects();
      setProjects(data.content || []);
    } catch (err: any) {
      setError(err.message || 'Sorry for the inconvenience. Internal server error. Try after sometime!');
      console.error('Error fetching projects:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="projects">
        <div className="container">
          <div className="loading">Loading projects...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="projects">
        <div className="container">
          <div className="error">{error}</div>
        </div>
      </div>
    );
  }

  return (
    <div className="projects">
      <section className="projects-hero">
        <div className="container">
          <h1>Our Projects</h1>
          <p>Explore our portfolio of completed and ongoing projects</p>
        </div>
      </section>

      <section className="projects-content">
        <div className="container">
          <div className="projects-grid">
            {projects.map((project) => (
              <div key={project.id} className="project-card">
                {project.heroImageUrl && (
                  <div className="project-image">
                    <img src={project.heroImageUrl} alt={project.name} />
                  </div>
                )}
                <div className="project-info">
                  <h3>{project.name}</h3>
                  <p className="project-location">📍 {project.city}</p>
                  <p className="project-status">
                    Status: <span className={`status ${project.projectStatus.toLowerCase()}`}>
                      {project.projectStatus.replace('_', ' ')}
                    </span>
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
};

export default Projects;
