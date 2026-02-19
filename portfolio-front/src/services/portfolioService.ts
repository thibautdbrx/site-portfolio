import axios from 'axios'
import type {
  PortfolioDTO,
  ProjectDTO,
  ExperienceDTO,
  EducationDTO,
  SkillDTO,
  ProfileDTO,
} from '../types/PortfolioDTO'
import router from '@/router'

// On centralise l'URL pour plus de flexibilité
const API_BASE_URL = 'http://localhost:8080/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      console.warn('Accès refusé ou session expirée. Redirection vers login.')

      document.cookie = ''

      localStorage.removeItem('token')

      // 3. Rediriger vers la page de login
      //router.push('/login')
    }
    return Promise.reject(error)
  },
)

// Exportation de l'objet service contenant toutes les méthodes
export const portfolioService = {
  // --- LECTURE GLOBALE ---
  getPortfolio: async (): Promise<PortfolioDTO> => {
    const response = await api.get<PortfolioDTO>('/portfolio')
    return response.data
  },

  // --- PROFIL ---
  getProfile: () => api.get('/profile').then((res) => res.data),
  updateProfile: (data: ProfileDTO) => api.put('/profile', data),

  // --- PROJETS ---
  addProject: (data: ProjectDTO) => api.post('/project', data),
  updateProject: (id: number, data: ProjectDTO) => api.put(`/project/${id}`, data),
  deleteProject: (id: number) => api.delete('/project', { params: { id } }),

  // --- EXPÉRIENCES ---
  addExperience: (data: ExperienceDTO) => api.post('/experience', data),
  updateExperience: (id: number, data: ExperienceDTO) => api.put(`/experience/${id}`, data),
  deleteExperience: (id: number) => api.delete('/experience', { params: { id } }),

  // --- FORMATIONS (EDUCATION) ---
  addEducation: (data: EducationDTO) => api.post('/education', data),
  updateEducation: (id: number, data: EducationDTO) => api.put(`/education/${id}`, data),
  deleteEducation: (id: number) => api.delete('/education', { params: { id } }),

  // --- COMPÉTENCES (SKILLS) ---
  addSkill: (data: SkillDTO) => api.post('/skill', data),
  updateSkill: (id: number, data: SkillDTO) => api.put(`/skill/${id}`, data),
  deleteSkill: (id: number) => api.delete('/skill', { params: { id } }),

  // --- FICHIERS (UPLOAD & DOWNLOAD) ---
  uploadFile: async (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    const response = await api.post('/files/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
    return response.data // Retourne généralement {"fileName": "..."}
  },

  downloadCV: async (fileName: string) => {
    const response = await api.get(`/files/${fileName}`, {
      responseType: 'blob',
    })
    return response.data
  },
}

export const getPortfolio = portfolioService.getPortfolio
export const downloadCV = portfolioService.downloadCV
