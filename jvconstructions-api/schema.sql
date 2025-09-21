-- MySQL Schema for JV Constructions Application
-- Generated from JPA entities

-- Create database (uncomment if needed)
-- CREATE DATABASE jvconstructions;
-- USE jvconstructions;

-- Projects table
CREATE TABLE IF NOT EXISTS projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(40) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description LONGTEXT,
    project_status VARCHAR(24) NOT NULL,
    city VARCHAR(255),
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    pin_code VARCHAR(20),
    start_date DATE,
    end_date DATE,
    hero_image_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_projects_status (project_status),
    INDEX idx_projects_created_at (created_at),
    INDEX idx_projects_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Project images table
CREATE TABLE IF NOT EXISTS project_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    s3_key TEXT NOT NULL,
    mime_type VARCHAR(64) NOT NULL,
    width INT,
    height INT,
    size_bytes BIGINT,
    sort_order INT DEFAULT 0,
    hero BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    INDEX idx_project_images_project (project_id),
    INDEX idx_project_images_sort (project_id, sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Enquiries table
CREATE TABLE IF NOT EXISTS enquiries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL,
    phone VARCHAR(20),
    message LONGTEXT,
    status VARCHAR(24) NOT NULL DEFAULT 'NEW',
    assigned_to VARCHAR(255),
    utm_source VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE SET NULL,
    INDEX idx_enquiries_project (project_id),
    INDEX idx_enquiries_status (status),
    INDEX idx_enquiries_created_at (created_at),
    INDEX idx_enquiries_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Services table
CREATE TABLE IF NOT EXISTS services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_services_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add foreign key constraint for hero_image_id after project_images table is created
ALTER TABLE projects 
ADD CONSTRAINT fk_projects_hero_image 
FOREIGN KEY (hero_image_id) REFERENCES project_images(id) ON DELETE SET NULL;

-- Insert sample data for project status enum values
-- Note: The actual enum values are handled by the application layer
-- These are just for reference:
-- ProjectStatus: UNDER_CONSTRUCTION, COMPLETED
-- Role: ADMIN, USER
-- ProjectRole: ADMIN, CUSTOMER (constants)

-- Sample data (optional - uncomment if needed)
/*
-- Insert sample services
INSERT INTO services (name, description) VALUES 
('Residential Construction', 'Complete residential building construction services'),
('Commercial Construction', 'Commercial building and office construction'),
('Renovation', 'Home and office renovation services'),
('Interior Design', 'Complete interior design and decoration services');

-- Insert sample project
INSERT INTO projects (code, name, description, project_status, city, address_line1, pin_code) VALUES 
('PROJ001', 'Modern Villa Construction', 'A beautiful modern villa with contemporary design', 'UNDER_CONSTRUCTION', 'Mumbai', '123 Main Street', '400001');

-- Insert sample enquiry
INSERT INTO enquiries (project_id, name, email, phone, message, status) VALUES 
(1, 'John Doe', 'john.doe@example.com', '+91-9876543210', 'Interested in your villa construction project. Please provide more details.', 'NEW');
*/
