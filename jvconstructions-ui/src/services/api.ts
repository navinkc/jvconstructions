import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { 
  Project, 
  ProjectResponse, 
  Service, 
  ServiceResponse, 
  Enquiry, 
  CreateEnquiryRequest,
  ApiError 
} from '../types';

// Create axios instance with base configuration
const apiClient: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8082/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for adding auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Handle unauthorized access
      localStorage.removeItem('authToken');
      window.location.href = '/login';
    }
    
    // Handle network errors and server errors
    if (!error.response) {
      const networkError: ApiError = {
        timestamp: new Date().toISOString(),
        status: 0,
        error: 'Network Error',
        message: 'Sorry for the inconvenience. Internal server error. Try after sometime!',
        path: error.config?.url || '',
      };
      return Promise.reject(networkError);
    }
    
    const apiError: ApiError = {
      timestamp: new Date().toISOString(),
      status: error.response?.status || 500,
      error: error.response?.data?.error || 'Unknown Error',
      message: error.response?.status >= 500 
        ? 'Sorry for the inconvenience. Internal server error. Try after sometime!'
        : error.response?.data?.message || 'An unexpected error occurred',
      path: error.config?.url || '',
    };
    
    return Promise.reject(apiError);
  }
);

// Project API
export const projectApi = {
  getAllProjects: async (params?: {
    projectStatus?: string;
    city?: string;
    page?: number;
    size?: number;
    sort?: string;
  }): Promise<ProjectResponse> => {
    const response = await apiClient.get<ProjectResponse>('/projects', { params });
    return response.data;
  },

  getProjectByCode: async (code: string): Promise<Project> => {
    const response = await apiClient.get<Project>(`/projects/${code}`);
    return response.data;
  },

  createProject: async (projectData: Partial<Project>): Promise<Project> => {
    const response = await apiClient.post<Project>('/projects', projectData);
    return response.data;
  },

  updateProject: async (id: number, projectData: Partial<Project>): Promise<Project> => {
    const response = await apiClient.put<Project>(`/projects/${id}`, projectData);
    return response.data;
  },

  deleteProject: async (id: number): Promise<string> => {
    const response = await apiClient.delete<string>(`/projects/${id}`);
    return response.data;
  },
};

// Service API
export const serviceApi = {
  getAllServices: async (params?: {
    page?: number;
    size?: number;
    sort?: string;
  }): Promise<ServiceResponse> => {
    const response = await apiClient.get<ServiceResponse>('/services', { params });
    return response.data;
  },

  getServiceByName: async (name: string): Promise<Service> => {
    const response = await apiClient.get<Service>(`/services/${encodeURIComponent(name)}`);
    return response.data;
  },

  createService: async (serviceData: { name: string; description: string }): Promise<Service> => {
    const response = await apiClient.post<Service>('/services', serviceData);
    return response.data;
  },

  updateService: async (id: number, serviceData: { name: string; description: string }): Promise<Service> => {
    const response = await apiClient.put<Service>(`/services/${id}`, serviceData);
    return response.data;
  },

  deleteService: async (id: number): Promise<string> => {
    const response = await apiClient.delete<string>(`/services/${id}`);
    return response.data;
  },
};

// Enquiry API
export const enquiryApi = {
  createEnquiry: async (enquiryData: CreateEnquiryRequest): Promise<Enquiry> => {
    const response = await apiClient.post<Enquiry>('/enquiries', enquiryData);
    return response.data;
  },

  getAllEnquiries: async (params?: {
    status?: string;
    page?: number;
    size?: number;
    sort?: string;
  }): Promise<{ content: Enquiry[]; totalElements: number }> => {
    const response = await apiClient.get<{ content: Enquiry[]; totalElements: number }>('/enquiries', { params });
    return response.data;
  },

  updateEnquiry: async (id: number, enquiryData: { status?: string; assignedTo?: string }): Promise<Enquiry> => {
    const response = await apiClient.put<Enquiry>(`/enquiries/${id}`, enquiryData);
    return response.data;
  },
};

// User API
export const userApi = {
  getAllUsers: async (): Promise<any[]> => {
    const response = await apiClient.get<any[]>('/users');
    return response.data;
  },

  getUserById: async (userId: string): Promise<any> => {
    const response = await apiClient.get<any>(`/users/${userId}`);
    return response.data;
  },

  createUser: async (userData: {
    userName: string;
    email: string;
    password: string;
    role: string;
  }): Promise<string> => {
    const response = await apiClient.post<string>('/users', null, { params: userData });
    return response.data;
  },

  updateUser: async (userId: string, userData: { email: string }): Promise<string> => {
    const response = await apiClient.put<string>(`/users/${userId}`, null, { params: userData });
    return response.data;
  },

  deleteUser: async (userId: string): Promise<string> => {
    const response = await apiClient.delete<string>(`/users/${userId}`);
    return response.data;
  },
};

export default apiClient;
