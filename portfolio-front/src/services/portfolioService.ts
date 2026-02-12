import axios from 'axios'
import type { PortfolioDTO } from '../types/PortfolioDTO'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

export const getPortfolio = async (): Promise<PortfolioDTO> => {
  const response = await api.get<PortfolioDTO>('/portfolio')
  return response.data
}
export const downloadCV = async (fileName: string) => {
  const response = await axios.get(`http://localhost:8080/api/files/${fileName}`, {
    responseType: 'blob', // Crucial pour les fichiers binaires
  })
  return response.data
}