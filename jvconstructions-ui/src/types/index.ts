// Project Types
export interface Project {
  id: number;
  code: string;
  name: string;
  description?: string;
  city: string;
  projectStatus: 'UNDER_CONSTRUCTION' | 'COMPLETED';
  heroImageUrl?: string;
  startDate?: string;
  endDate?: string;
  updatedAt: string;
  images?: ProjectImage[];
}

export interface ProjectImage {
  id: number;
  url: string;
  mimeType: string;
  width: number;
  height: number;
  sortOrder: number;
  hero: boolean;
}

export interface ProjectResponse {
  content: Project[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      sorted: boolean;
    };
  };
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

// Service Types
export interface Service {
  id: number;
  name: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export interface ServiceResponse {
  content: Service[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      sorted: boolean;
    };
  };
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

// Enquiry Types
export interface Enquiry {
  id: number;
  projectCode: string;
  name: string;
  email: string;
  phone: string;
  message: string;
  status: string;
  assignedTo?: string;
  createdAt: string;
}

export interface CreateEnquiryRequest {
  projectCode?: string;
  name: string;
  email: string;
  phone: string;
  message: string;
}

// User Types
export interface User {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  enabled: boolean;
  emailVerified: boolean;
  createdTimestamp: number;
  attributes: {
    role: string[];
  };
}

// API Error Types
export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}
